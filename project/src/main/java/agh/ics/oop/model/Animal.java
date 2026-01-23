package agh.ics.oop.model;

import agh.ics.oop.model.util.SimulationConfig;

import java.util.*;

import static agh.ics.oop.model.MapDirection.*;


/**
 * An animal living in the {@link JungleMap},
 * movements of the Animal are based on their genotype
*/
public class Animal implements WorldElement {
    private final Genotype genotype;
    private MapDirection direction;
    private Vector2d position;
    private int energy;
    private int age = 0;
    private int childrenCount = 0;
    private boolean fomoActive = false;
    private MapDirection fomoDirectionOverride;

    private final SimulationConfig config;

    public static Comparator<Animal> animalComparator = Comparator
            .comparingInt(Animal::getEnergy)
            .thenComparingInt(Animal::getAge)
            .thenComparingInt(Animal::getEnergy)
            .reversed();


    public Animal(SimulationConfig config, Vector2d position, int energy){
        this(config, position,energy, new Genotype(config));
    }

    public Animal(SimulationConfig config, Vector2d position, int energy, Genotype genotype){
        this.config = config;
        this.direction = NORTH;
        this.position = position;
        this.energy = energy;
        this.genotype = genotype;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public int getEnergy() {
        return energy;
    }

    public List<Gene> getGenotype() {
        return genotype.getGenotype();
    }

    public int getAge(){
        return this.age;
    }

    public boolean isFomoActive(){
        return this.fomoActive;
    }

    public void  setFomo(boolean bool){
        this.fomoActive = bool;
    }

    public int getChildrenCount(){
        return this.childrenCount;
    }

    public void decreaseEnergy(int delta){
        this.energy = Math.max(0,this.energy-delta);
    }

    public void increaseEnergy(int delta){
        this.energy = Math.min(config.ENERGY_LIMIT(), this.energy+delta);
    }

    public void incrementAge(){
        this.age+=1;
    }

    public void increaseChildrenCount(){
        this.childrenCount+=1;
    }



    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public void move(MoveValidator moveValidator){
        if(this.energy>0){
            MapDirection directionToMove = genotype.getNewDirection(this.direction);
            if(fomoActive){
                directionToMove = fomoDirectionOverride;
            }
            Vector2d posAfterMove = this.position.add(directionToMove.toVector());
                Vector2d maxBound = moveValidator.getMapBoundary().higherBoundary();
                if(!moveValidator.isInBounds(this.position.add(new Vector2d(directionToMove.toVector().x(), 0)))){
                    if(directionToMove.toVector().x()<0){
                        posAfterMove = new Vector2d(maxBound.x(),posAfterMove.y());
                    }
                    else{
                        posAfterMove = new Vector2d(0,posAfterMove.y());
                    }
                }
                if(!moveValidator.isInBounds(this.position.add(new Vector2d(0,directionToMove.toVector().y())))){
                    posAfterMove = new Vector2d(maxBound.x()-posAfterMove.x(),this.position.y());
                    directionToMove = this.direction.rotate(4);
                }
            this.position = posAfterMove;
            this.direction = directionToMove;

            this.genotype.incrementIndex();

        }
    }


    @Override
    public String toString(){
        if(this.getEnergy()>0){
            return showDirection(this.direction);
        } else {
            return "X";
        }
    }

    public Animal reproduce(Animal otherParent){
        Random r = new Random();
        int totalEnergy = this.energy + otherParent.energy;
        Animal stronger;
        Animal weaker;
        if(this.energy>=otherParent.energy){
            stronger = this;
            weaker = otherParent;
        }
        else {
            stronger = otherParent;
            weaker = this;
        }
        boolean strongerGenesOnLeft = r.nextBoolean();
        int splitPoint = (int) Math.round((double) stronger.energy / totalEnergy * this.genotype.getGenotype().size());

        List<Gene> childGenes = new ArrayList<>();
        if (strongerGenesOnLeft) {
            childGenes.addAll(stronger.getGenotype().subList(0, splitPoint));
            childGenes.addAll(weaker.getGenotype().subList(splitPoint, weaker.getGenotype().size()));
        } else {
            int reversedSplitPoint = weaker.genotype.getGenotype().size() - splitPoint;
            childGenes.addAll(weaker.getGenotype().subList(0, reversedSplitPoint));
            childGenes.addAll(stronger.getGenotype().subList(reversedSplitPoint, stronger.getGenotype().size()));
        }
        Genotype childGenotype = new Genotype(config,childGenes);

        int mutationAmount = 0;
        if(config.MAX_MUTATION_AMOUNT()>0) {
            mutationAmount = r.nextInt(config.MAX_MUTATION_AMOUNT());
            for (int i = 0; i < mutationAmount; i++) {
                childGenotype.mutate();
            }
        }

        int childEnergy = 2*config.REPRODUCE_ENERGY_COST();
        this.decreaseEnergy(config.REPRODUCE_ENERGY_COST());
        otherParent.decreaseEnergy(config.REPRODUCE_ENERGY_COST());
        return new Animal(this.config, this.position, childEnergy, childGenotype);
    }

    public void triggerFOMO(Vector2d pos){
        this.fomoActive=true;
        int sum = 0;
        int directionX = (int) Math.signum(pos.x() - this.position.x());
        int directionY = (int) Math.signum(pos.y() - this.position.y());

        String key = directionX + ":" + directionY;

        this.fomoDirectionOverride = switch (key) {
            case "0:1"   -> NORTH;
            case "1:1"   -> NORTHEAST;
            case "1:0"   -> EAST;
            case "1:-1"  -> SOUTHEAST;
            case "0:-1"  -> SOUTH;
            case "-1:-1" -> SOUTHWEST;
            case "-1:0"  -> WEST;
            case "-1:1"  -> NORTHWEST;
            default -> throw new IllegalArgumentException("Unknown move direction");
        };
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
