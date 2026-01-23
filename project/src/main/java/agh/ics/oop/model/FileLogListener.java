package agh.ics.oop.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileLogListener implements MapChangeListener{
    @Override
    public void mapChanged(WorldMap world_map, String message) {
        UUID id = world_map.getId();
        String file_name = String.format("log_%s.csv", id.toString());
        try (FileOutputStream fos = new FileOutputStream(file_name,true)) {
            fos.write(world_map.toString().getBytes());
            fos.write(message.getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
