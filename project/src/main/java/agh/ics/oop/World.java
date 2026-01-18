package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Gene;
import agh.ics.oop.model.JungleMap;
import agh.ics.oop.model.Vector2d;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class World {
     void main(){
         try {       // show UTF-8, which encodes unicode
             System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
         } catch (UnsupportedEncodingException e) {
             throw new InternalError("VM does not support mandatory encoding UTF-8");
         }
         System.out.println("Hello");

         JungleMap map1 = new JungleMap(10,10);
         Animal cat = new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(0),new Gene(1))));

         map1.place(cat);

         map1.move(cat);
         map1.animalTryToEatGrass(cat);
         cat.checkIfDied();
         map1.move(cat);
         map1.animalTryToEatGrass(cat);
         cat.checkIfDied();


         System.out.println(map1);
    }
}
