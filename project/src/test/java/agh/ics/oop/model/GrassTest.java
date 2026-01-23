package agh.ics.oop.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GrassTest {

    private static Stream<Arguments> grassTests() {
        return Stream.of(
                Arguments.of(new Grass(new Vector2d(2,2)),new Vector2d(1,1), new Vector2d(2,2)));
    }

    @ParameterizedTest
    @MethodSource("grassTests")
    void grassGetPosition(Grass grass, Vector2d wrongPos, Vector2d actualPos) {
        assertEquals(actualPos,grass.getPosition());
    }

    @ParameterizedTest
    @MethodSource("grassTests")
    void grassIsAt(Grass grass, Vector2d wrongPos, Vector2d actualPos) {
        assertTrue(grass.isAt(actualPos));
        assertFalse(grass.isAt(wrongPos));
    }
}
