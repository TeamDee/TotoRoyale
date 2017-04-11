package GameModel.Map.Coordinates;

import GameModel.Map.Direction;

/**
 * Created by jowens on 3/8/17.
 */
public class OffsetCoordinate {
    public int x,y;
    public OffsetCoordinate(int x1, int y1){
        x=x1;
        y=y1;
    }


    public OffsetCoordinate getByDirection(Direction directionRelativeToMe){
        switch(directionRelativeToMe){
            case NORTH:
                return getNorth();
            case NORTHEAST:
                return getNorthEast();
            case NORTHWEST:
                return getNorthWest();
            case SOUTH:
                return getSouth();
            case SOUTHEAST:
                return getSouthEast();
            case SOUTHWEST:
                return getSouthWest();
        }
        System.out.println("getByDirection in OffsetCoordinate fed a non-Direction, somehow ERROR ERROR ERROR ");
        return null;
    }
    public OffsetCoordinate getNorth(){
        return new OffsetCoordinate(x,y-2);
    }
    public OffsetCoordinate getNorthEast(){
        return new OffsetCoordinate(x+1,y-1);
    }
    public OffsetCoordinate getSouthEast(){
        return new OffsetCoordinate(x+1,y+1);
    }
    public OffsetCoordinate getSouth(){
        return new OffsetCoordinate(x,y+2);
    }
    public OffsetCoordinate getSouthWest(){
        return new OffsetCoordinate(x-1,y+1);
    }
    public OffsetCoordinate getNorthWest(){
        return new OffsetCoordinate(x-1,y-1);
    }



    public static boolean areAdjacent(OffsetCoordinate ac1, OffsetCoordinate ac2) {
        if(ac1.getNorth().equals(ac2))
            return true;
        if(ac1.getNorthEast().equals(ac2))
            return true;
        if(ac1.getNorthWest().equals(ac2))
            return true;
        if(ac1.getSouth().equals(ac2))
            return true;
        if(ac1.getSouthEast().equals( ac2))
            return true;
        if(ac1.getSouthWest().equals(ac2))
            return true;
        return false;
        //return Math.abs(ac1.x - ac2.x + ac1.y - ac2.y) == 2;
    }

    @Override
    public boolean equals(Object ac){
        return this.hashCode() == ac.hashCode(); //will give false positives in very edge cases, maybe
    }
    @Override
    public int hashCode(){
        return y*10000 + 10*x+1; //TODO this is a temporary hashing function that will fail for (ridiculously) large x or y
    }

    @Override
    public String toString(){
        return "x: " + x + " y: " + y;
    }

    public boolean compare(OffsetCoordinate other){
        boolean result = false;
        if(other.x == this.x && other.y == this.y){
            result = true;
        }
        return result;
    }

    public CubicCoordinate getCubicCoordinate(){
        int offsetY = this.y;
        int offsetX = this.x;

        int x = offsetX;
        int y = (-x - offsetY)/2;
        int z = -y-x;

        return new CubicCoordinate(x,y,z);
    }

}
