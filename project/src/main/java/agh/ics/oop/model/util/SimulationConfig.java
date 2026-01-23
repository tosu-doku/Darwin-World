package agh.ics.oop.model.util;



public record SimulationConfig(
        int mapWidth, int mapHeight,
        int START_ANIMAL_AMOUNT,
        int START_GRASS_AMOUNT,
        int GENOTYPE_LENGTH,
        int ENERGY_FROM_EATING_GRASS_AMOUNT,
        int START_ANIMAL_ENERGY,
        int ENERGY_LIMIT, int MOVE_ENERGY_COST,
        int REPRODUCE_ENERGY_LIMIT, int REPRODUCE_ENERGY_COST,
        boolean IS_MODIFIER_ACTIVE, int MAX_MUTATION_AMOUNT,
        int GRASS_REGROW_AMOUNT, int FOMO_RADIUS_INSIDE, int FOMO_RADIUS_OUTSIDE, int AMOUNT_TO_TRIGGER_FOMO) {


        public SimulationConfig{
            if(START_ANIMAL_AMOUNT<0 || START_GRASS_AMOUNT<0 || GENOTYPE_LENGTH<0 || ENERGY_FROM_EATING_GRASS_AMOUNT<0 || ENERGY_LIMIT<0 || MOVE_ENERGY_COST<0 || REPRODUCE_ENERGY_COST<0 || REPRODUCE_ENERGY_LIMIT<0 || mapWidth<0 || mapHeight<0 || START_ANIMAL_ENERGY<0 || MAX_MUTATION_AMOUNT<0 || GRASS_REGROW_AMOUNT<0 ||  FOMO_RADIUS_INSIDE<0 || FOMO_RADIUS_OUTSIDE<0 || AMOUNT_TO_TRIGGER_FOMO<0) {
                throw new IllegalArgumentException("negative values are not allowed");
            }
            if(!(mapWidth>=2 && mapHeight>=2)) {
                throw new IllegalArgumentException("please provide a larger map size (minimum 2x2)");
            }
            if(REPRODUCE_ENERGY_COST > REPRODUCE_ENERGY_LIMIT) {
                throw new IllegalArgumentException("reproduction not possible energy limit > energy cost");
            }
            if(IS_MODIFIER_ACTIVE && FOMO_RADIUS_OUTSIDE < FOMO_RADIUS_INSIDE){
                throw new IllegalArgumentException("current FOMO arguments will not cause the FOMO effect!");
            }
            if(IS_MODIFIER_ACTIVE && AMOUNT_TO_TRIGGER_FOMO < 2){
                throw new IllegalArgumentException("current FOMO arguments are not allowed!");
            }
    }
}
