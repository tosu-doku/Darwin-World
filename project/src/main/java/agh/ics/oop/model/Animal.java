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
    private boolean alive = true;
    private final int ENERGY_LIMIT = 100;
    private final int MOVE_ENERGY_COST = 5;
    private int energy;

    public Animal(Vector2d position, ArrayList<Gene> genotype){
        this.genotype = genotype;
        this.direction = MapDirection.NORTH;
        this.position = position;
        this.energy = 60;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public int getEnergy() { return energy; }

    public ArrayList<Gene> getGenotype() {
        return genotype;
    }

    public boolean ableToReproduce(int energyRequired) {
        return this.energy > energyRequired;
    }

    public void useEnergyToReproduce(int energyRequired) {
        this.energy = this.energy - energyRequired;
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
        if(alive){
            MapDirection directionToMove = GeneParser.parse(this.genotype.get(this.currentIndex),this.direction);
            Vector2d posAfterMove = this.position.add(directionToMove.toVector());
            if (moveValidator.isInBounds(posAfterMove)){
                System.out.println(String.format("%s -> %s, (%s -> %s)",this.position,posAfterMove,this.direction,directionToMove));
                this.position = posAfterMove;
            } else {
                System.out.println(String.format("No movement, (%s -> %s)",this.direction,directionToMove));
            }
            this.direction = directionToMove;
            //zawijanie jak na kuli

            this.currentIndex = (this.currentIndex + 1)% genotype.size();
            this.energy -= MOVE_ENERGY_COST;
            if(this.energy <= 0){
                alive = false;
        }}
    }

    @Override
    public String toString(){
        if(alive){
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
