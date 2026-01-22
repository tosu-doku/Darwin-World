package agh.ics.oop;

import agh.ics.oop.model.util.SimulationConfig;

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

         Simulation simulation = new Simulation(new SimulationConfig(5,5,4,20,8,15,50,100,5,50,25,false));
         for(int i=0; i<20; i++) {
             simulation.run();
         }
    }
}
