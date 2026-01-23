package agh.ics.oop;

import agh.ics.oop.model.JungleMap;
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
         System.out.println("Start");

         SimulationConfig config = new SimulationConfig(10, 10, 25, 20, 8, 20, 75, 100, 5, 50, 15, true, 3, 3,2,10,5);
         JungleMap map = new JungleMap(config);
//         map.registerListener(new ConsoleMapDisplay());

         try {
             Simulation simulation = new Simulation(map, config);
             simulation.run();
         }
         catch (IllegalArgumentException e){
             e.printStackTrace();
         }
    }
}
