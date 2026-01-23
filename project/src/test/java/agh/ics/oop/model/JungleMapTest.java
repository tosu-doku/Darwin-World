package agh.ics.oop.model;

import agh.ics.oop.model.util.SimulationConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static agh.ics.oop.model.MapDirection.*;
import static agh.ics.oop.model.MapDirection.NORTH;
import static agh.ics.oop.model.MapDirection.NORTHWEST;
import static org.junit.jupiter.api.Assertions.*;

class JungleMapTest {
    static SimulationConfig confPlace0grass = new SimulationConfig(10,10,0,0,0,0,0,0,0,0,0,false,0,0,0,0,0);
    static SimulationConfig confPlace5grass = new SimulationConfig(10,10,0,5,0,0,0,0,0,0,0,false,0,0,0,0,0);
    static SimulationConfig confReproduceCost10 = new SimulationConfig(10,10,0,0,2,0,0,0,0,10,10,false,0,0,0,0,0);

    private static Stream<Arguments> provideArgsForPlaceGrassCorrectAmountPlaced() {
        return Stream.of(
                Arguments.of(new JungleMap(confPlace0grass),0,5,5),
                Arguments.of(new JungleMap(confPlace0grass),0,confPlace0grass.mapHeight()*confPlace0grass.mapWidth()+2,confPlace0grass.mapHeight()*confPlace0grass.mapWidth()),
                Arguments.of(new JungleMap(confPlace5grass),5,0,5),
                Arguments.of(new JungleMap(confPlace5grass),5,5,10)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgsForPlaceGrassCorrectAmountPlaced")
    void placeGrassCorrectAmountPlaced(JungleMap map, int amountPlacedAtStart, int placeMore, int finalGrassCount) {
        assertEquals(amountPlacedAtStart, map.getGrasses().size());

        map.placeGrass(placeMore);

        assertEquals(finalGrassCount, map.getGrasses().size());
    }

    private static Stream<Arguments> provideAnimalsForReproduceTesting() {
        return Stream.of(
            Arguments.of( new JungleMap(confReproduceCost10), new ArrayList<>(Arrays.asList(
                new Animal(confReproduceCost10,new Vector2d(2,2),50),
                new Animal(confReproduceCost10,new Vector2d(2,2),50)
            )),3),
            // no energy to mate
                Arguments.of( new JungleMap(confReproduceCost10), new ArrayList<>(Arrays.asList(
                        new Animal(confReproduceCost10,new Vector2d(2,2),1),
                        new Animal(confReproduceCost10,new Vector2d(2,2),1)
                )),2),
            // different spots
                Arguments.of( new JungleMap(confReproduceCost10), new ArrayList<>(Arrays.asList(
                        new Animal(confReproduceCost10,new Vector2d(2,3),50),
                        new Animal(confReproduceCost10,new Vector2d(2,2),50)
                )),2),
            // only animals with energy mate
                Arguments.of( new JungleMap(confReproduceCost10), new ArrayList<>(Arrays.asList(
                        new Animal(confReproduceCost10,new Vector2d(2,2),50),
                        new Animal(confReproduceCost10,new Vector2d(2,2),50),
                        new Animal(confReproduceCost10,new Vector2d(2,2),0)
                )),4)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAnimalsForReproduceTesting")
    void reproduceIntegrationTests(JungleMap map, ArrayList<Animal> animalsForTest, int expectedAmountOfAnimals) {
        for(Animal animal : animalsForTest){
            map.placeAnimal(animal);
        }
        map.reproduce();
        assertEquals(expectedAmountOfAnimals,map.getAnimals().size());
    }
}