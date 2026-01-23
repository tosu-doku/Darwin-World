package agh.ics.oop.model;

import agh.ics.oop.model.util.SimulationConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static agh.ics.oop.model.MapDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class GenotypeTest {
    private final static SimulationConfig simpleConf = new SimulationConfig(10,10,0,0,0,0,0,0,0,0,0,false,0,0,0,0,0);
    private final static SimulationConfig confGeneLen2 = new SimulationConfig(10,10,0,0,2,0,0,0,0,0,0,false,0,0,0,0,0);
    private final static SimulationConfig confGeneLen3 = new SimulationConfig(10,10,0,0,3,0,0,0,0,0,0,false,0,0,0,0,0);


    private static Stream<Arguments> provideGenotypeForNewDirection() {
        return Stream.of(
                Arguments.of(new Genotype(simpleConf, List.of(new Gene(0))), NORTH, NORTH),
                Arguments.of(new Genotype(simpleConf, List.of(new Gene(1))), NORTH, NORTHEAST),
                Arguments.of(new Genotype(simpleConf, List.of(new Gene(2))), NORTH, EAST),
                Arguments.of(new Genotype(simpleConf, List.of(new Gene(1))), NORTHWEST, NORTH),
                Arguments.of(new Genotype(simpleConf, List.of(new Gene(-1))), NORTH, NORTH)
                );
    }

    @ParameterizedTest
    @MethodSource("provideGenotypeForNewDirection")
    void getNewDirectionCorrectlyRotatesDirection(Genotype genotype, MapDirection start, MapDirection expected) {
        assertEquals(expected, genotype.getNewDirection(start));
    }


    @Test
    void mutate() {

    }
}