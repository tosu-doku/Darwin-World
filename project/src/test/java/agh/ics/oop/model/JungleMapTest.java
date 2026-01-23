package agh.ics.oop.model;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JungleMapTest {
    // do przerobienia
    private static Stream<Arguments> provideAnimalsForReproduceTesting() {
        return Stream.of(
            Arguments.of( new JungleMap(10,10), new ArrayList<>(Arrays.asList(
                new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(0),new Gene(1)))),
                new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(4),new Gene(7))))
            )),3),
            // no energy to mate
            Arguments.of( new JungleMap(10,10), new ArrayList<>(Arrays.asList(
                new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(0),new Gene(1))),1),
                new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(4),new Gene(7))),1)
            )),2),
            // different spots
            Arguments.of( new JungleMap(10,10), new ArrayList<>(Arrays.asList(
                new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(0),new Gene(1)))),
                new Animal(new Vector2d(2,3),new ArrayList<Gene>(List.of(new Gene(4),new Gene(7))))
            )),2),
            // only animals with energy mate
            Arguments.of( new JungleMap(10,10), new ArrayList<>(Arrays.asList(
                new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(0),new Gene(1))),1),
                new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(4),new Gene(7)))),
                new Animal(new Vector2d(2,2),new ArrayList<Gene>(List.of(new Gene(1),new Gene(1))))
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