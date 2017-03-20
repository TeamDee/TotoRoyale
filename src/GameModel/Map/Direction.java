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
    public static Direction getConverse(Direction d){
        switch(d){
            case NORTH:
                return SOUTH;
            case NORTHEAST:
                return SOUTHWEST;
            case SOUTHEAST:
                return NORTHWEST;
            case SOUTH:
                return NORTH;
            case SOUTHWEST:
                return NORTHEAST;
            case NORTHWEST:
                return SOUTHEAST;
        }
        return null; // this shouldn't happen but whatever
    }
}
