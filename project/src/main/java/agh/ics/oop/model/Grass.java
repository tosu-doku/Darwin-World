package agh.ics.oop.model;

import java.util.Objects;

public class Grass implements WorldElement{
    private final Vector2d position;

    public Grass (Vector2d position){
        this.position = position;
    }

    @Override
    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public String toString(){
        return "*";
    }
}
