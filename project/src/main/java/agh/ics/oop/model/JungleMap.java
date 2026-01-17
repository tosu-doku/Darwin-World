package agh.ics.oop.model;

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

    public JungleMap(int width, int height){
        this.mapBoundary = new Boundary(new Vector2d(0,0),new Vector2d(width-1,height-1));
        int offsetY = (int) (height*0.2);
        this.jungleBoundary = new Boundary(new Vector2d(0,offsetY),new Vector2d(width-1,height-offsetY-1));
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
//        if(!isOccupied(animal.getPosition())) {
            if (!animals.containsKey(animal.getPosition())) {
                animals.put(animal.getPosition(), new ArrayList<>());
            }
            animals.get(animal.getPosition()).add(animal);
//        }
    }

    @Override
    public void move(Animal animal) {
        Vector2d pos = animal.getPosition();
        if (animals.containsKey(pos)) {
            ArrayList<Animal> animalListAtPos = animals.get(pos);
            if(animalListAtPos.contains(animal)){
                animalListAtPos.remove(animal);
                if(animalListAtPos.size()==0){
                    animals.remove(pos);
                }
                animal.move(this);
                place(animal);
            }
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
                    if(p1.ableToReproduce() && p2.ableToReproduce()){
                        System.out.println("New animal born!");
                        place(new Animal(p1.getPosition(),generateNewGenotype(p1.getGenotype(),p1.getEnergy(),p2.getGenotype(),p2.getEnergy())));
                    }
                }
            }
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
        for (WorldElement elem : objectAt(position)){
            if(elem instanceof Animal){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Animal> objectAt(Vector2d position) {
        return animals.getOrDefault(position, new ArrayList<>());
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
    public Boundary getCurrentBounds() {
        return null;
    }

    @Override
    public UUID getId() {
        return null;
    }
}
