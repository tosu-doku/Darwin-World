package agh.ics.oop.model;

import agh.ics.oop.World;

import java.util.List;
import java.util.UUID;


public interface WorldMap extends MoveValidator {

    void placeAnimal(Animal animal);

    void move(Animal animal);

    boolean isOccupied(Vector2d position);

    WorldElement objectAt(Vector2d position);

    Boundary getMapBoundary();

    UUID getId();

    List<Animal> getAnimals();

    void reproduce();

    void removeDead();

    void consumeGrass();

    void placeGrass(int amount);
}