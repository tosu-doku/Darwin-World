package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.JungleMap;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.SimulationConfig;

public class Simulation {
    private final SimulationConfig config;

    JungleMap map;

    public Simulation(SimulationConfig config)
    {
        this.config = config;
        this.map = new JungleMap(config);

        // place animals
        for(int i=0; i<config.START_ANIMAL_AMOUNT(); i++){
            Vector2d pos = new Vector2d((int) (Math.random() * config.mapWidth()),(int) (Math.random()*config.mapHeight()));
            this.map.placeAnimal(new Animal(config, pos,config.START_ANIMAL_ENERGY()));
        }
    }

    public void run(){
        for(Animal animal : map.getAnimals()){
            map.move(animal);
        }

        map.consumeGrass();
        map.reproduce();
        System.out.println(map);
    }
}
