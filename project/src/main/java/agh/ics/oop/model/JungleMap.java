package agh.ics.oop.model;

import java.util.*;

public class JungleMap implements WorldMap{
    private final Map<Vector2d, ArrayList<WorldElement>> map = new HashMap<>();
    private final UUID mapId = UUID.randomUUID();
    private final Boundary mapBoundary;
    private final Boundary jungleBoundary;

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
        return "";
    }

    @Override
    public void place(Animal animal) {

    }

    @Override
    public void move(Animal animal) {

    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return false;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return null;
    }

    @Override
    public List<WorldElement> getElements() {
        return List.of();
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
