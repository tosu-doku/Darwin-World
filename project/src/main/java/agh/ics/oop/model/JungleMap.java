package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.SimulationConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class JungleMap implements WorldMap{
    private final Map<Vector2d,  ArrayList<Animal>> animals = new ConcurrentHashMap<>();
    private final Map<Vector2d, Grass> grasses = new ConcurrentHashMap<>();
    private final List<MapChangeListener> listeners = new ArrayList<>();

    private final UUID mapId = UUID.randomUUID();
    private final Boundary mapBoundary;
    private final Boundary jungleBoundary;

    private final MapVisualizer visualizer = new MapVisualizer(this);

    private final SimulationConfig config;


    private int totalDeadCount = 0;
    private long totalDeadAgeSum = 0;

    public JungleMap(SimulationConfig config){
        this.config = config;

        this.mapBoundary = new Boundary(new Vector2d(0,0),new Vector2d(config.mapWidth()-1,config.mapHeight()-1));
        int offsetY = (int) (config.mapHeight()*0.4);
        this.jungleBoundary = new Boundary(new Vector2d(0,offsetY),new Vector2d(config.mapWidth()-1,config.mapHeight()-offsetY-1));
        placeGrass(config.START_GRASS_AMOUNT());
    }

    public int getTotalDeadCount() {
        return totalDeadCount;
    }

    public long getTotalDeadAgeSum() {
        return totalDeadAgeSum;
    }


    @Override
    public boolean isInBounds(Vector2d position){
        return position.follows(mapBoundary.lowerBoundary()) && position.precedes(mapBoundary.higherBoundary());
    }

    @Override
    public String toString(){
        return visualizer.draw(mapBoundary.lowerBoundary(),mapBoundary.higherBoundary());
    }

    @Override
    public void placeAnimal(Animal animal) {
        if (!animals.containsKey(animal.getPosition())) {
            animals.put(animal.getPosition(), new ArrayList<>(List.of(animal)));
        } else {
            animals.get(animal.getPosition()).add(animal);
        }
    }

    @Override
    public void move(Animal animal) {
        if (animal.getEnergy()<=0) return;
        Vector2d pos = animal.getPosition();
        if (animals.containsKey(pos)) {
            ArrayList<Animal> animalListAtPos = animals.get(pos);
            if(animalListAtPos.contains(animal)){
                animalListAtPos.remove(animal);
                if(animalListAtPos.isEmpty()){
                    animals.remove(pos);
                }
                animal.move(this);
                placeAnimal(animal);
            }
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position).isPresent();
    }

    @Override
    public synchronized Optional<WorldElement> objectAt(Vector2d pos) {
        ArrayList<Animal> animalsAtPos = animals.get(pos);
        if (animalsAtPos == null || animalsAtPos.isEmpty()) {
            Grass grass = grasses.get(pos);
            return Optional.ofNullable(grass);
        }

        Animal strongest = null;
        for (Animal a : animalsAtPos) {
            if (strongest == null || a.getEnergy() > strongest.getEnergy()) {
                strongest = a;
            }
        }
        return Optional.ofNullable(strongest);
    }

    public synchronized ArrayList<Animal> getAnimals() {

        ArrayList<Animal> allAnimals = new ArrayList<>();
        for (ArrayList<Animal> animalList : animals.values()){
            if (animalList != null) {
                allAnimals.addAll(animalList);
            }
        }

        return allAnimals;
    }

    public Map<Vector2d,Grass> getGrasses(){
        return this.grasses;
    }


    @Override
    public Boundary getMapBoundary() {
        return this.mapBoundary;
    }

    @Override
    public UUID getId() {
        return this.mapId;
    }

    public void consumeGrass() {
        for (ArrayList<Animal> candidates : animals.values()){
            if (!grasses.containsKey(candidates.getFirst().getPosition())) continue;
            candidates.sort(Animal.animalComparator);
            Animal first = candidates.getFirst();
            if(first.getEnergy()> config.ENERGY_LIMIT()) continue;
            if(first.isFomoActive()) break;
            first.increaseEnergy(config.ENERGY_FROM_EATING_GRASS_AMOUNT());
            grasses.remove(first.getPosition());
        }
    }

    public void reproduce(){
        List<Animal> newborns = new ArrayList<>();
        for (ArrayList<Animal> candidates : animals.values()){
            if (candidates.size()<2) continue;
            candidates.sort(Animal.animalComparator);
            Animal firstParent = candidates.get(0);
            Animal secondParent = candidates.get(1);
            if (firstParent.isFomoActive()) break;
            if (firstParent.getEnergy()>config.REPRODUCE_ENERGY_LIMIT() && secondParent.getEnergy()>config.REPRODUCE_ENERGY_LIMIT()) {
                Animal child = firstParent.reproduce(secondParent);
                firstParent.increaseChildrenCount();
                secondParent.increaseChildrenCount();

                newborns.add(child);
            }
        }
        for (Animal child : newborns) {
            this.placeAnimal(child);
        }
    }

    public void removeDead(){
        List<Vector2d> positionsToRemove = new ArrayList<>();

        for (Vector2d pos : animals.keySet()) {
            ArrayList<Animal> animalList = animals.get(pos);
            animalList.removeIf(animal -> {
                if (animal.getEnergy() <= 0) {
                    totalDeadCount+=1;
                    totalDeadAgeSum+=animal.getAge();
                    return true;
                }
                return false;
            });
            if (animalList.isEmpty()) {
                positionsToRemove.add(pos);
            }
        }
        for (Vector2d pos : positionsToRemove) {
            animals.remove(pos);
        }
    }


    public void placeGrass(int amount){
        int highBoundX = mapBoundary.higherBoundary().x()+1;
        int highBoundY = mapBoundary.higherBoundary().y()+1;
        int lowBoundX = mapBoundary.lowerBoundary().x();
        int lowBoundY = mapBoundary.lowerBoundary().y();

        List<Vector2d> emptyJungleSpaces = new ArrayList<>();
        List<Vector2d> emptyNonJungleSpaces = new ArrayList<>();

        for (int x = lowBoundX; x < highBoundX; x++ ) {
            for (int y = lowBoundY; y < highBoundY; y++){
                Vector2d pos = new Vector2d(x,y);
                if (!grasses.containsKey(pos)){
                    if (isInJungle(pos)){
                        emptyJungleSpaces.add(pos);
                    } else {
                        emptyNonJungleSpaces.add(pos);
                    }
                }
            }
        }

        for (int i = 0; i < amount; i++) {
            Vector2d res;
            if(!emptyJungleSpaces.isEmpty() && !emptyNonJungleSpaces.isEmpty()){
                if(Math.random()<0.8){
                    // spawn in jungle
                    res = swapAndPop(emptyJungleSpaces, (int) (Math.random() * emptyJungleSpaces.size()));
                } else {
                    // spawn outside
                    res = swapAndPop(emptyNonJungleSpaces, (int) (Math.random() * emptyNonJungleSpaces.size()));
                }
            } else if (!emptyJungleSpaces.isEmpty()) {
                // spawn in jungle
                res = swapAndPop(emptyJungleSpaces, (int) (Math.random() * emptyJungleSpaces.size()));
            } else if (!emptyNonJungleSpaces.isEmpty()) {
                //spawn outside
                res = swapAndPop(emptyNonJungleSpaces, (int) (Math.random() * emptyNonJungleSpaces.size()));
            } else {
                break;
            }
            grasses.put(res, new Grass(res));
        }
    }

    private Vector2d swapAndPop(List<Vector2d> positions, int idx){
        Vector2d val = positions.get(idx);
        positions.set(idx, positions.getLast());
        positions.removeLast();
        return val;
    }

    public boolean isInJungle(Vector2d position) {
        return position.follows(jungleBoundary.lowerBoundary()) && position.precedes(jungleBoundary.higherBoundary());
    }

    public void registerListener(MapChangeListener listener){
        this.listeners.add(listener);
    }



    public void mapChanged(String message){
        this.listeners.forEach(listener -> listener.mapChanged(this,message));
    }

}
