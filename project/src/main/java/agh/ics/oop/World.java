package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.JungleMap;
import agh.ics.oop.model.Vector2d;

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

         JungleMap map1 = new JungleMap(10,10);
         map1.registerListener(new ConsoleMapDisplay());
         Animal cat = new Animal(new Vector2d(2,2),60);
         Animal tiger = new Animal(new Vector2d(2,2),60);

         map1.placeAnimal(cat);
         map1.placeAnimal(tiger);

         map1.reproduce();

         for (Animal animal : map1.getAnimals()){
             map1.move(animal);
         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);
         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);
         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);

         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);
         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);

         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);

         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);

         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);

         }
         map1.consumeGrass();
         for (Animal animal : map1.getAnimals()){
             map1.move(animal);

         }
         map1.consumeGrass();

    }
}
