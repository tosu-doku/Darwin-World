package agh.ics.oop.model;

import agh.ics.oop.World;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class JungleMap implements WorldMap{
    private final Map<Vector2d, ArrayList<WorldElement>> map = new HashMap<>();
    private final UUID mapId = UUID.randomUUID();
    private final Boundary mapBoundary;
    private final Boundary jungleBoundary;
    private Map<Vector2d,  ArrayList<Animal>> animals = new HashMap<>();
    private final MapVisualizer visualizer = new MapVisualizer(this);
    private Map<Vector2d, Grass> grasses = new HashMap<>();
    private final int START_GRASS_AMOUNT = 500;
    private final int ENERGY_FROM_EATING_GRASS_AMOUNT = 10;
    private final int REPRODUCE_ENERGY_LIMIT = 51;
    private final int REPRODUCE_ENERGY_COST = 25;

    public JungleMap(int width, int height){
        this.mapBoundary = new Boundary(new Vector2d(0,0),new Vector2d(width-1,height-1));
        int offsetY = (int) (height*0.3);
        this.jungleBoundary = new Boundary(new Vector2d(0,offsetY),new Vector2d(width-1,height-offsetY-1));
        placeGrass(START_GRASS_AMOUNT);
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
            }
        }
    }

    public void animalTryToEatGrass(Animal animal) {
        if(grasses.containsKey(animal.getPosition())){
            animal.restoreEnergy(ENERGY_FROM_EATING_GRASS_AMOUNT);
            grasses.remove(animal.getPosition());
        }
    }

    public void reproduce(){
        Map<Vector2d, ArrayList<Animal>> multipleAnimals = animals
            .entrySet()
            .stream()
            .filter(elem -> elem.getValue().size() > 1)
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (ArrayList<Animal> parents : multipleAnimals.values()){
            for (int i=0; i<parents.size(); i++){
                for (int j=i+1; j<parents.size(); j++){
                    var p1 = parents.get(i);
                    var p2 = parents.get(j);
                    reproduceFinalize(p1, p2);
                }
            }
        }
    }

    private void reproduceFinalize(Animal p1, Animal p2) {
        if(p1.ableToReproduce(REPRODUCE_ENERGY_LIMIT) && p2.ableToReproduce(REPRODUCE_ENERGY_LIMIT)){
            p1.useEnergyToReproduce(REPRODUCE_ENERGY_COST);
            p2.useEnergyToReproduce(REPRODUCE_ENERGY_COST);
            placeAnimal(new Animal(
                p1.getPosition(),
                generateNewGenotype(p1.getGenotype(), p1.getEnergy(), p2.getGenotype(), p2.getEnergy()),
                2*REPRODUCE_ENERGY_COST
                ));
            System.out.printf("New animal born at: %s%n",p1.getPosition());
        }
    }

    private ArrayList<Gene> generateNewGenotype(ArrayList<Gene> gen1, int en1, ArrayList<Gene> gen2, int en2){
        ArrayList<Gene> childGenotype = new ArrayList<Gene>();
        int totalEnergy = en1 + en2;
        for (int i=0; i<gen1.size(); i++){
            if ((Math.random() * totalEnergy <= en1)) {
                childGenotype.add(gen1.get(i));
            } else {
                childGenotype.add(gen2.get(i));
            }
        }
        return childGenotype;
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
    public List<WorldElement> getElements() {
        ArrayList<WorldElement> elements = new ArrayList<>();
        for (Vector2d pos : map.keySet()){
            elements.addAll(map.get(pos));
        }
        return elements;
    }

    @Override
    public Boundary getMapBoundary() {
        return this.mapBoundary;
    }

    @Override
    public UUID getId() {
        return null;
    }
}
