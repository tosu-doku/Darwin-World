package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JungleMap {
    private final Map<Vector2d,Animal> map = new HashMap<>();
    private final UUID mapId = UUID.randomUUID();


}
