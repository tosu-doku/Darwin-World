package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

public class Genotype {
    List<Gene> genotype = new ArrayList<>();
    int currentGeneIndex;

    public Genotype(int length){

    }

    public MapDirection getNewDirection(MapDirection currentDirection) {
        int geneValue = genotype.get(currentGeneIndex).getGeneNumber();
        return currentDirection.rotate(geneValue);
    }

    public void incrementIndex(){
        this.currentGeneIndex = (this.currentGeneIndex + 1) % genotype.size();
    }

}
