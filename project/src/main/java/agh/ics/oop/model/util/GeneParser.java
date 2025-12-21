package agh.ics.oop.model.util;

import agh.ics.oop.model.Gene;
import agh.ics.oop.model.MapDirection;


public class GeneParser {
    public static MapDirection parse(Gene currentGene, MapDirection currentDirection) {
        int geneValue = currentGene.getGeneNumber();
        return currentDirection.rotate(geneValue);
    }
}
