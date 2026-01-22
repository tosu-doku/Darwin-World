package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.SimulationConfig;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final SimulationConfig config;
    private List<Animal> animalList = new ArrayList<>();
    private final WorldMap map;
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

    public void run(int days){
        for (int i = 0; i <= days; i++) {
            for(Animal animal : animalList){
                map.move(animal);
            }

            map.consumeGrass();
            animalList.forEach(animal -> animal.decreaseEnergy(config.MOVE_ENERGY_COST()));
            map.reproduce();
            System.out.printf("Current day: %d%n",this.day);

            map.placeGrass(config.GRASS_REGROW_AMOUNT());
            map.removeDead();
            this.day += 1;

            this.animalList = new ArrayList<>(map.getAnimals().stream().filter(animal -> animal.getEnergy()>0).toList());
            System.out.println(map);
        }

    }
}
