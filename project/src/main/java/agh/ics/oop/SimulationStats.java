package agh.ics.oop;

import agh.ics.oop.model.WorldMap;

import java.io.*;
import java.util.UUID;

public record SimulationStats(
        int day,
        int animalCount,
        int grassCount,
        int freeFieldCount,
        double avgEnergy,
        double avgLifespan,
        double avgChildren
) {
    public void saveToCsv(UUID mapId){
        String fileName = String.format("log_%s.csv", mapId.toString());
        File file = new File(fileName);

        boolean isNew = !file.exists() || file.length() == 0;

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            if (isNew) {
                pw.println("Day,Animals,Grass,VacantCells,AvgEnergy,AvgLifespan,AvgChildren");
            }
            pw.printf(java.util.Locale.US, "%d,%d,%d,%d,%.2f,%.2f,%.2f%n",
                    this.day(),
                    this.animalCount(),
                    this.grassCount(),
                    this.freeFieldCount(),
                    this.avgEnergy(),
                    this.avgLifespan(),
                    this.avgChildren()
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}