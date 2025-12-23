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

public class World {
     void main(){
         try {       // show UTF-8, which encodes unicode
             System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
         } catch (UnsupportedEncodingException e) {
             throw new InternalError("VM does not support mandatory encoding UTF-8");
         }
         System.out.println("Hello");

         JungleMap map1 = new JungleMap(3,4);
         Animal cat = new Animal(new Vector2d(2,2),new ArrayList<Gene>());

         Animal catDead = new Animal(new Vector2d(1,2),new ArrayList<Gene>());
         catDead.setDead(true);

         map1.place(cat);
         map1.place(catDead);

         System.out.println(map1);
    }
}
