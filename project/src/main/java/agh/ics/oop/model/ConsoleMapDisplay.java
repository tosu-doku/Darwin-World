package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    public void mapChanged(WorldMap world_map, String message){
        System.out.printf("Map id: %s\n", world_map.getId().toString());
        System.out.println(message);
        System.out.println(world_map);
        }
}
