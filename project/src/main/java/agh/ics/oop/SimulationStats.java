package agh.ics.oop;

public record SimulationStats(
        int day,
        int animalCount,
        int grassCount,
        int freeFieldCount,
        double avgEnergy,
        double avgLifespan,
        double avgChildren
) {}