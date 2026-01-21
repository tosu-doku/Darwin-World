package agh.ics.oop.model.util;

import agh.ics.oop.model.Gene;

import java.util.ArrayList;

public class GenerateNewGenotype {
    public static ArrayList<Gene> generateNewGenotype(ArrayList<Gene> gen1, int en1, ArrayList<Gene> gen2, int en2){
        ArrayList<Gene> childGenotype = new ArrayList<Gene>();
        int totalEnergy = en1 + en2;
        for (int i=0; i<gen1.size(); i++){
            if ((Math.random() * totalEnergy <= en1)) {
                childGenotype.add(gen1.get(i));
            } else {
                childGenotype.add(gen2.get(i));
            }
        }
        return childGenotype;
    }
}
