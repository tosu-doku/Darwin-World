package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype {
    List<Gene> genotypeList = new ArrayList<>();
    int currentGeneIndex;

    public Genotype(int length){
        for (int i = 0; i < length; i++) {
            genotypeList.add(Gene.getRandom());
        }
    }

    public Genotype(List<Gene> list){
        this.genotypeList = list;
    }

    public MapDirection getNewDirection(MapDirection currentDirection) {
        int geneValue = genotypeList.get(currentGeneIndex).getGeneNumber();
        return currentDirection.rotate(geneValue);
    }

    public void incrementIndex(){
        this.currentGeneIndex = (this.currentGeneIndex + 1) % genotypeList.size();
    }

    public void mutate(){
        //dodac for loop zeby wykonalo sie tyle razy ile config mowi
        Random r = new Random();
        int idx = r.nextInt(this.genotypeList.size());
        this.genotypeList.set(idx,Gene.getRandom());
    }

    public List<Gene> getGenotype(){
        return genotypeList;
    }

}
