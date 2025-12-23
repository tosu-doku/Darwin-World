package agh.ics.oop.model;

import agh.ics.oop.model.util.GeneParser;

import java.util.ArrayList;
import java.util.Objects;


/**
 * An animal living in the {@link JungleMap},
 * movements of the Animal are based on their genotype
*/
public class Animal implements WorldElement {
    private final ArrayList<Gene> genotype;
    private int currentIndex = 0;
    private MapDirection direction;
    private Vector2d position;
    private boolean isDead = false;

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Animal(Vector2d position, ArrayList<Gene> genotype){
        this.genotype = genotype;
        this.direction = MapDirection.NORTH;
        this.position = position;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public boolean isAt(Vector2d position){
        return Objects.equals(this.position,position);
    }

    public void move(MoveValidator moveValidator){
        MapDirection directionToMove = GeneParser.parse(this.genotype.get(this.currentIndex),this.direction);
        Vector2d posAfterMove = this.position.add(directionToMove.toVector());
        if (moveValidator.isInBounds(posAfterMove)){
            this.direction = directionToMove;
            this.position = posAfterMove;
        }
        //zawijanie jak na kuli

        this.currentIndex = (this.currentIndex + 1)% genotype.size();
        //obnizenie energii
    }

    @Override
    public String toString(){
        if(!isDead){
            return showDirection(this.direction);
        } else {
            return "X";
        }
    }

    private String showDirection(MapDirection dir){
        return switch (dir){
            // Unicode arrows
            case NORTH -> "\u2191";
            case NORTHEAST -> "\u2197";
            case EAST -> "\u2192";
            case SOUTHEAST -> "\u2198";
            case SOUTH -> "\u2193";
            case SOUTHWEST -> "\u2199";
            case WEST -> "\u2190";
            case NORTHWEST -> "\u2196";
        };
    }
}
