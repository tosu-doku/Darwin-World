package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public class JungleMap implements WorldMap{
    private final Map<Vector2d, ArrayList<WorldElement>> map = new HashMap<>();
    private final UUID mapId = UUID.randomUUID();
    private final Boundary mapBoundary;
    private final Boundary jungleBoundary;
    private Map<Vector2d, Animal> animals = new HashMap<>();
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private Map<Vector2d, Grass> grasses = new HashMap<>();
    private final int START_GRASS_AMOUNT = 50;

    public JungleMap(int width, int height){
        this.mapBoundary = new Boundary(new Vector2d(0,0),new Vector2d(width-1,height-1));
        int offsetY = (int) (height*0.3);
        this.jungleBoundary = new Boundary(new Vector2d(0,offsetY),new Vector2d(width-1,height-offsetY-1));
        placeGrass(START_GRASS_AMOUNT);
    }

    private void placeGrass(int amount){
        int highBoundX = mapBoundary.higherBoundary().x()+1;
        int highBoundY = mapBoundary.higherBoundary().y()+1;
        int lowBoundX = mapBoundary.lowerBoundary().x()+1;
        int lowBoundY = mapBoundary.lowerBoundary().y()+1;
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
        boolean willSpawnInJungle = Math.random()<0.8;
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

    @Override
    public boolean isInBounds(Vector2d position){
        return position.follows(mapBoundary.lowerBoundary()) && position.precedes(mapBoundary.higherBoundary());
    }

    @Override
    public String toString(){
        return visualizer.draw(mapBoundary.lowerBoundary(),mapBoundary.higherBoundary());
    }

    @Override
    public void place(Animal animal) {
        if(!isOccupied(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
        }
    }

    @Override
    public void move(Animal animal) {
        if(this.animals.containsValue(animal)){
            animals.remove(animal.getPosition());
            animal.move(this);
            animals.put(animal.getPosition(),animal);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) instanceof WorldElement;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if(animals.get(position)!=null){
            return animals.get(position);
        } else {
            return grasses.get(position);
        }
    }

    @Override
    public List<WorldElement> getElements() {
        return new ArrayList<WorldElement>(animals.values());
    }

    @Override
    public Boundary getCurrentBounds() {
        return null;
    }

    @Override
    public UUID getId() {
        return null;
    }
}
