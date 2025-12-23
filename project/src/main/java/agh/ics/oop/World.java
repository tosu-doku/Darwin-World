package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Gene;
import agh.ics.oop.model.JungleMap;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;

public class World {
     void main(){
         System.out.println("Hello");

         JungleMap map1 = new JungleMap(3,4);

         map1.place(new Animal(new Vector2d(2,2), new ArrayList<Gene>()));

         System.out.println(map1);
    }
}
