package GameModel.Map.Coordinates;

/**
 * Created by jowens on 3/8/17.
 */
public class AxialCoordinate {
    public int x,y;
    public AxialCoordinate(int x1, int y1){
        x=x1;
        y=y1;
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
    @Override
    public boolean equals(Object ac){
        return this.hashCode() == ac.hashCode(); //will give false positives in very edge cases, maybe
    }
    @Override
    public int hashCode(){
        return y*1000 + x; //TODO this is a temporary hashing function that will fail for large x or y
    }

    @Override
    public String toString(){
        String s ="";
        s += "x: " + x;
        s += " ";
        s += "y: " + y;
        return s;
    }
}
