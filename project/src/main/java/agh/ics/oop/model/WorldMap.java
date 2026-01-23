package agh.ics.oop.model;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public interface WorldMap extends MoveValidator {

    void placeAnimal(Animal animal);

    void move(Animal animal);

    boolean isOccupied(Vector2d position);

    Optional<WorldElement> objectAt(Vector2d position);

    Boundary getMapBoundary();

    UUID getId();

    List<Animal> getAnimals();

    Map<Vector2d,Grass> getGrasses();

    int getTotalDeadCount();

    long getTotalDeadAgeSum();

    void reproduce();

    void removeDead();

    void consumeGrass();

    void placeGrass(int amount);

    boolean isInJungle(Vector2d pos);

    void mapChanged(String message);

}