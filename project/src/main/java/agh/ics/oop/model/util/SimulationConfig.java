package agh.ics.oop.model.util;

import agh.ics.oop.model.JungleMap;

public record SimulationConfig(
        int mapWidth, int mapHeight,
        int START_ANIMAL_AMOUNT,
        int START_GRASS_AMOUNT,
        int GENOTYPE_LENGTH,
        int ENERGY_FROM_EATING_GRASS_AMOUNT,
        int START_ANIMAL_ENERGY,
        int ENERGY_LIMIT,
        int MOVE_ENERGY_COST,
        int REPRODUCE_ENERGY_LIMIT,
        int REPRODUCE_ENERGY_COST,
        boolean IS_MODIFIER_ACTIVE,
        int MAX_MUTATION_AMOUNT,
        int GRASS_REGROW_AMOUNT) {


        public SimulationConfig{
            if(START_ANIMAL_AMOUNT<0 || START_GRASS_AMOUNT<0 || GENOTYPE_LENGTH<0 || ENERGY_FROM_EATING_GRASS_AMOUNT<0 || ENERGY_LIMIT<0 || MOVE_ENERGY_COST<0 || REPRODUCE_ENERGY_COST<0 || REPRODUCE_ENERGY_LIMIT<0 || mapWidth<0 || mapHeight<0 || START_ANIMAL_ENERGY<0 || GRASS_REGROW_AMOUNT<0) {
                throw new IllegalArgumentException("negative values are not allowed");
            }
            if(!(mapWidth>=2 && mapHeight>=2)) {
                throw new IllegalArgumentException("please provide a larger map size (minimum 2x2)");
            }
            if(REPRODUCE_ENERGY_COST > REPRODUCE_ENERGY_LIMIT) {
                throw new IllegalArgumentException("reproduction not possible energy limit > energy cost");
            }
    }
}
