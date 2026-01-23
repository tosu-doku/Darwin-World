package agh.ics.oop.model;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @ParameterizedTest
    @CsvSource({
        "NORTH, 1, NORTHEAST",
        "NORTHEAST, 1, EAST",
        "NORTH, 2, EAST",
        "NORTHWEST, 1, NORTH",
    })
    void rotateClockwiseIsCorrect(MapDirection start, int steps, MapDirection expected) {
        assertEquals(expected, start.rotate(steps));
    }

    @ParameterizedTest
    @CsvSource({
        "NORTH, 8",
        "SOUTHEAST, 16",
    })
    void rotateWholeShouldLeaveNoChange(MapDirection start, int steps) {
        assertEquals(start, start.rotate(steps));
    }

    @ParameterizedTest
    @CsvSource({
        "NORTH, -1",
    })
    void rotateCounterClockwiseNoResult(MapDirection start, int steps) {
        assertEquals(start, start.rotate(steps));
    }

}