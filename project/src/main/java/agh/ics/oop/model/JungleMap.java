package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;
import java.util.stream.Collectors;


public class JungleMap implements WorldMap{
    private Map<Vector2d,  ArrayList<Animal>> animals = new HashMap<>();
    private Map<Vector2d, Grass> grasses = new HashMap<>();
    private final List<MapChangeListener> listeners = new ArrayList<>();

    private final UUID mapId = UUID.randomUUID();
    private final Boundary mapBoundary;
    private final Boundary jungleBoundary;

    private final MapVisualizer visualizer = new MapVisualizer(this);

    private final int START_GRASS_AMOUNT = 20;
    private final int ENERGY_FROM_EATING_GRASS_AMOUNT = 10;
    private final int REPRODUCE_ENERGY_LIMIT = 51;


    public JungleMap(int width, int height){
        this.mapBoundary = new Boundary(new Vector2d(0,0),new Vector2d(width-1,height-1));
        int offsetY = (int) (height*0.3);
        this.jungleBoundary = new Boundary(new Vector2d(0,offsetY),new Vector2d(width-1,height-offsetY-1));
        placeGrass(START_GRASS_AMOUNT);
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
                this.mapChanged(String.format(
                        "Animal moved: (%d,%d) -> (%d,%d); Energy: %s",
                        pos.x(),
                        pos.y(),
                        animal.getPosition().x(),
                        animal.getPosition().y(),
                        animal.getEnergy()
                ));
            }
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) instanceof WorldElement;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if(animals.get(position)!=null){
            return animals.get(position)
                    .stream()
                    .max(Comparator.comparing(Animal::getEnergy))
                    .orElse(null);
        } else if(grasses.get(position)!=null){
            return grasses.get(position);
        }
        return null;
    }

    public ArrayList<Animal> getAnimals() {
        ArrayList<Animal> allAnimals = new ArrayList<>();
        for (Vector2d pos : animals.keySet()){
            allAnimals.addAll(animals.get(pos));
        }

        return allAnimals;
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
            first.increaseEnergy(ENERGY_FROM_EATING_GRASS_AMOUNT);
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

            if (firstParent.getEnergy()>REPRODUCE_ENERGY_LIMIT && secondParent.getEnergy()>REPRODUCE_ENERGY_LIMIT) {
                Animal child = firstParent.reproduce(secondParent);
                firstParent.increaseChildrenCount();
                secondParent.increaseChildrenCount();

                newborns.add(child);
            }
        }
        for (Animal child : newborns) {
            this.placeAnimal(child);
            this.mapChanged(String.format(
                    "New animal has been born on (%d,%d) facing %s",
                    child.getPosition().x(),
                    child.getPosition().y(),
                    child.getDirection()
            ));
        }
    }

    public void removeDead(){
        List<Vector2d> positionsToRemove = new ArrayList<>();

        for (Vector2d pos : animals.keySet()) {
            ArrayList<Animal> animalList = animals.get(pos);
            animalList.removeIf(animal -> animal.getEnergy() <= 0);
            if (animalList.isEmpty()) {
                positionsToRemove.add(pos);
            }
        }
        for (Vector2d pos : positionsToRemove) {
            animals.remove(pos);
        }
    }


    private void placeGrass(int amount){
        int highBoundX = mapBoundary.higherBoundary().x()+1;
        int highBoundY = mapBoundary.higherBoundary().y()+1;
        int lowBoundX = mapBoundary.lowerBoundary().x();
        int lowBoundY = mapBoundary.lowerBoundary().y();
        int totalMapSpace = (highBoundX-lowBoundX)*(highBoundY-lowBoundY);
        amount = Math.min(amount, totalMapSpace);
        boolean reverse = amount > totalMapSpace / 2;

        if(reverse){
            amount = totalMapSpace - amount;
            for (int i = lowBoundX; i < highBoundX; i++) {
                for (int j = lowBoundY; j < highBoundY; j++) {
                    grasses.put(new Vector2d(i,j),new Grass(new Vector2d(i,j)));
                }
            }
        }

        for (int i = 0; i < amount; i++) {
            placeSingularGrass(reverse);
        }
    }

    private void placeSingularGrass(boolean reverse){
        boolean willSpawnInJungle = reverse ? (Math.random()<0.2) : (Math.random()<0.8);
        int randomY;
        if(willSpawnInJungle){
            randomY = (int) Math.round(Math.random()*(jungleBoundary.higherBoundary().y() - jungleBoundary.lowerBoundary().y()) + jungleBoundary.lowerBoundary().y());
        } else {
            if(Math.random()<0.5){
                randomY = (int) Math.floor(Math.random()*(jungleBoundary.lowerBoundary().y()));
            } else {
                randomY = (int) Math.ceil(Math.random()*(jungleBoundary.lowerBoundary().y()) + jungleBoundary.higherBoundary().y());
            }
        }

        int randomX = (int) Math.round(Math.random()*mapBoundary.higherBoundary().x());

        if(grasses.containsKey(new Vector2d(randomX,randomY)) && reverse) {
            grasses.remove(new Vector2d(randomX, randomY));
        }
        else if(!grasses.containsKey(new Vector2d(randomX,randomY)) && !reverse) {
            grasses.put(new Vector2d(randomX, randomY), new Grass(new Vector2d(randomX, randomY)));
        } else {
            placeSingularGrass(reverse);
        }
    }

    public void registerListener(MapChangeListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(MapChangeListener listener){
        this.listeners.remove(listener);
    }

    private void mapChanged(String message){
        this.listeners.forEach(listener -> listener.mapChanged(this,message));
    }
}
