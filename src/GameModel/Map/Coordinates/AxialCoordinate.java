package GameModel.Map.Coordinates;

import GameModel.Map.Direction;

/**
 * Created by jowens on 3/8/17.
 */
public class AxialCoordinate {
    public int x,y;
    public AxialCoordinate(int x1, int y1){
        x=x1;
        y=y1;
    }


    public AxialCoordinate getByDirection(Direction directionRelativeToMe){
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
        System.out.println("getByDirection in AxialCoordinate fed a non-Direction, somehow ERROR ERROR ERROR ");
        return null;
    }
    public AxialCoordinate getNorth(){
        return new AxialCoordinate(x,y-2);
    }
    public AxialCoordinate getNorthEast(){
        return new AxialCoordinate(x+1,y-1);
    }
    public AxialCoordinate getSouthEast(){
        return new AxialCoordinate(x+1,y+1);
    }
    public AxialCoordinate getSouth(){
        return new AxialCoordinate(x,y+2);
    }
    public AxialCoordinate getSouthWest(){
        return new AxialCoordinate(x-1,y+1);
    }
    public AxialCoordinate getNorthWest(){
        return new AxialCoordinate(x-1,y-1);
    }



    public static boolean areAdjacent(AxialCoordinate ac1, AxialCoordinate ac2) {
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
        String s ="";
        s += "x: " + x;
        s += " ";
        s += "y: " + y;
        return s;
    }

    public boolean compare(AxialCoordinate other){
        boolean result = false;
        if(other.x == this.x && other.y == this.y){
            result = true;
        }
        return result;
    }
}
