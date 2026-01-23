package agh.ics.oop.model;


public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    @Override
    public String toString(){
        return switch(this){
            case NORTH -> "North";
            case NORTHEAST -> "North-East";
            case EAST -> "East";
            case SOUTHEAST -> "South-East";
            case SOUTH -> "South";
            case SOUTHWEST -> "South-West";
            case WEST -> "West";
            case NORTHWEST -> "North-West";
        };
    }

    public Vector2d toVector(){
        return switch(this){
            case NORTH -> new Vector2d(0,1);
            case NORTHEAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTHEAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTHWEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTHWEST -> new Vector2d(-1,1);
        };
    }

    public MapDirection rotate(int n){
        int newIndex = this.ordinal();
        if (n>0) newIndex = (newIndex+n)% MapDirection.values().length;
        return MapDirection.values()[newIndex];
    }
}
