package agh.ics.oop.model;

public interface MoveValidator {
    boolean isInBounds(Vector2d position);
    Boundary getMapBoundary();
}
