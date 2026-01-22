package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.JungleMap;
import agh.ics.oop.model.Vector2d;

public class Simulation {
    int START_GRASS_AMOUNT;
    int GENOTYPE_LENGTH;
    int ENERGY_FROM_EATING_GRASS_AMOUNT;
    int ENERGY_LIMIT;
    int MOVE_ENERGY_COST;
    int REPRODUCE_ENERGY_LIMIT;
    int REPRODUCE_ENERGY_COST;
    boolean IS_MODIFIER_ACTIVE;
    JungleMap map;

    public Simulation(int startAnimalAmount, int START_GRASS_AMOUNT, int GENOTYPE_LENGTH, int ENERGY_FROM_EATING_GRASS_AMOUNT,
                      int ENERGY_LIMIT, int MOVE_ENERGY_COST, int REPRODUCE_ENERGY_LIMIT, int REPRODUCE_ENERGY_COST, boolean IS_MODIFIER_ACTIVE,
                      int width, int height, int startAnimalEnergy)
    {
        if(startAnimalAmount<0 || START_GRASS_AMOUNT<0 || GENOTYPE_LENGTH<0 || ENERGY_FROM_EATING_GRASS_AMOUNT<0 || ENERGY_LIMIT<0 || MOVE_ENERGY_COST<0 || REPRODUCE_ENERGY_COST<0 || REPRODUCE_ENERGY_LIMIT<0 || width<0 || height<0 || startAnimalEnergy<0){
            throw new IllegalArgumentException("negative values are not allowed");
        }
        this.START_GRASS_AMOUNT = START_GRASS_AMOUNT;
        this.GENOTYPE_LENGTH = GENOTYPE_LENGTH;
        this.ENERGY_FROM_EATING_GRASS_AMOUNT = ENERGY_FROM_EATING_GRASS_AMOUNT;
        this.ENERGY_LIMIT = ENERGY_LIMIT;
        this.MOVE_ENERGY_COST = MOVE_ENERGY_COST;
        this.REPRODUCE_ENERGY_LIMIT = REPRODUCE_ENERGY_LIMIT;
        this.REPRODUCE_ENERGY_COST = REPRODUCE_ENERGY_COST;
        this.IS_MODIFIER_ACTIVE = IS_MODIFIER_ACTIVE;

        if(!(width>=2 && height>=2)){
            throw new IllegalArgumentException("please provide a larger map size (minimum 2x2)");
        }
         this.map = new JungleMap(width,height);

        // place animals
        for(int i=0; i<startAnimalAmount; i++){
            Vector2d pos = new Vector2d((int) (Math.random() * width),(int) (Math.random()*height));
            this.map.placeAnimal(new Animal(pos,startAnimalEnergy));
        }
    }

    public void run(){
        for(Animal animal : map.getAnimals()){
            map.move(animal);
        }

        map.consumeGrass();
        map.reproduce();
        System.out.println(map);
    }


}
