package agh.ics.oop;

public record SimulationStats(
        int animalCount,
        int grassCount,
        int freeFieldCount,
        double avgEnergy,
        double avgLifespan
) {}