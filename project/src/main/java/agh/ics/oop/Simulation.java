package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.SimulationConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Simulation implements Runnable,StatsProvider,ThreadController {
    private final SimulationConfig config;
    private List<Animal> animalList = new ArrayList<>();
    private final WorldMap map;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    private int day = 0;
    public Simulation(WorldMap map, SimulationConfig config)
    {
        this.config = config;
        this.map = map;

        // place animals
        for(int i=0; i<config.START_ANIMAL_AMOUNT(); i++){
            Vector2d pos = new Vector2d((int) (Math.random() * config.mapWidth()),(int) (Math.random()*config.mapHeight()));
            Animal newAnimal = new Animal(config, pos,config.START_ANIMAL_ENERGY());
            this.map.placeAnimal(newAnimal);
            animalList.add(newAnimal);
        }
    }

    public List<Animal> getAnimals(){
        return this.animalList;
    }

    @Override
    public void run(){
        while (running&&!animalList.isEmpty()) {
            synchronized (pauseLock) {
                if (!running) {
                    break;
                }
                if (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
            }
            map.removeDead();
            this.animalList = new ArrayList<>(map.getAnimals().stream().filter(animal -> animal.getEnergy()>0).toList());
            for(Animal animal : animalList){
                map.move(animal);

            }

            map.consumeGrass();
            animalList.forEach(animal -> animal.decreaseEnergy(config.MOVE_ENERGY_COST()));
            map.reproduce();

            map.placeGrass(config.GRASS_REGROW_AMOUNT());

            for(Animal animal : animalList){
                animal.setFomo(false);
                animal.incrementAge();
            }

            if(config.IS_MODIFIER_ACTIVE())
                checkFOMO();

            map.mapChanged(String.format("Day: %d",this.day));
            this.day+=1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void checkFOMO(){
        for(int i=0; i<animalList.size(); i++){
            Vector2d animal1pos = animalList.get(i).getPosition();
            int countInsideFomo = 0;

            for(int j=0; j<animalList.size(); j++){
                Vector2d animal2pos = animalList.get(j).getPosition();

                if(Math.abs(animal1pos.x()-animal2pos.x())<config.FOMO_RADIUS_INSIDE()
                && Math.abs(animal1pos.y()-animal2pos.y())<config.FOMO_RADIUS_INSIDE()
                && animalList.get(i).getEnergy()>0
                && animalList.get(j).getEnergy()>0){
                    countInsideFomo += 1;
                    if(countInsideFomo >= config.AMOUNT_TO_TRIGGER_FOMO()){
                        triggerFOMO(animal1pos);
                        return;
                    }
                }
            }
        }
    }
    private void triggerFOMO(Vector2d pos){
        for (Animal animal : animalList){
            if(!(Math.abs(animal.getPosition().x()-pos.x())<config.FOMO_RADIUS_INSIDE()
                    && Math.abs(animal.getPosition().y()-pos.y())<config.FOMO_RADIUS_INSIDE()))
            {
                if(Math.abs(animal.getPosition().x()-pos.x())<config.FOMO_RADIUS_OUTSIDE()
                        && Math.abs(animal.getPosition().y()-pos.y())<config.FOMO_RADIUS_OUTSIDE())
                {
                    animal.triggerFOMO(pos);
                }
            }
        }
    }
    @Override
    public void stop() {
        running = false;
        resume();
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    @Override
    public synchronized SimulationStats getStats() {
        int childrenCount = 0;
        int sumEnergy = 0;
        int totalDead = map.getTotalDeadCount();
        long totalDeadAge = map.getTotalDeadAgeSum();
        Set<Vector2d> occupied = new HashSet<>(map.getGrasses().keySet());
        List<Animal> currentAnimals = this.getAnimals();
        for (Animal animal : currentAnimals){
            childrenCount+=animal.getChildrenCount();
            sumEnergy+=animal.getEnergy();
            occupied.add(animal.getPosition());
        }

        return new SimulationStats(
                day,
                currentAnimals.size(),
                map.getGrasses().size(),
                occupied.size(),
                !currentAnimals.isEmpty() ? (double) sumEnergy / currentAnimals.size(): 0,
                totalDead!=0 ? (double) totalDeadAge / totalDead : 0,
                !currentAnimals.isEmpty() ?(double)childrenCount / currentAnimals.size() : 0
        );

    }
}
