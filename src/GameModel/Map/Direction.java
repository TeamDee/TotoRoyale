package GameModel.Map;

/**
 * Created by jowens on 3/16/17.
 */
public enum Direction {
    NORTH, NORTHEAST, SOUTHEAST, SOUTH, SOUTHWEST,NORTHWEST;
    public static Direction getClockwise(Direction d){
        switch(d){
            case NORTH:
                return NORTHEAST;
            case NORTHEAST:
                return SOUTHEAST;
            case SOUTHEAST:
                return SOUTH;
            case SOUTH:
                return SOUTHWEST;
            case SOUTHWEST:
                return NORTHWEST;
            case NORTHWEST:
                return NORTH;
        }
        return null; // this shouldn't happen but whatever
    }
}
