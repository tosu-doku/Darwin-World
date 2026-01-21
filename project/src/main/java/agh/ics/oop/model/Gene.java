package agh.ics.oop.model;

import java.util.Random;

public class Gene {
    int geneNumber;
    public Gene(int number){
        this.geneNumber = number;
    }

    public static Gene getRandom(){
        Random r = new Random();
        return new Gene(r.nextInt(8));
    }

    public int getGeneNumber() {
        return this.geneNumber;
    }

    @Override
    public String toString() {
        return String.valueOf(this.geneNumber);
    }
}
