package agh.ics.oop;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;


public class World {
     void main(){
         try {       // show UTF-8, which encodes unicode
             System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
         } catch (UnsupportedEncodingException e) {
             throw new InternalError("VM does not support mandatory encoding UTF-8");
         }
         System.out.println("Hello");

//         JungleMap map1 = new JungleMap(10,10);
//         Animal cat = new Animal(new Vector2d(2,2),60);
//         Animal tiger = new Animal(new Vector2d(2,2),60);
//
//         map1.placeAnimal(cat);
//         map1.placeAnimal(tiger);
//
//         map1.reproduce();
//
//         for (Animal animal : map1.getAnimals()){
//             map1.move(animal);
//             System.out.println(map1);
//         }
//         map1.consumeGrass();

         Simulation simulation = new Simulation(3,30,4,10,100,5,50,25,false,5,5,50);
         for(int i=0; i<20; i++) {
             simulation.run();
         }
    }
}
