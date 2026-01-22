package agh.ics.oop.model;

import agh.ics.oop.model.util.SimulationConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype {
    private List<Gene> genotypeList = new ArrayList<>();
    private int currentGeneIndex = 0;
    private final int mutationsAmount;

    public Genotype(SimulationConfig config){
        this.mutationsAmount = config.MAX_MUTATION_AMOUNT();
        for (int i = 0; i < config.GENOTYPE_LENGTH(); i++) {
            genotypeList.add(Gene.getRandom());
        }
    }
    // for creating children
    public Genotype(SimulationConfig config, List<Gene> list){
        this.mutationsAmount = config.MAX_MUTATION_AMOUNT();
        this.genotypeList = list;
        Random r = new Random();
        this.currentGeneIndex = r.nextInt(genotypeList.size());
    }

    public MapDirection getNewDirection(MapDirection currentDirection) {
        int geneValue = genotypeList.get(currentGeneIndex).getGeneNumber();
        return currentDirection.rotate(geneValue);
    }

    public void incrementIndex(){
        this.currentGeneIndex = (this.currentGeneIndex + 1) % genotypeList.size();
    }

    public void mutate(){
        Random r = new Random();
        for(int i=0;i<mutationsAmount;i++) {
            int idx = r.nextInt(this.genotypeList.size());
            this.genotypeList.set(idx, Gene.getRandom());
        }
    }

    public List<Gene> getGenotype(){
        return genotypeList;
    }

}
