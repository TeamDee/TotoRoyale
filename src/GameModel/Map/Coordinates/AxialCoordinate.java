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

    //todo fix this
    public AxialCoordinate getNorth(){
        if(y-2 > 0)
            return new AxialCoordinate(x,y-2);
        else
            return null;
    }
    public AxialCoordinate getNorthEast(){
        if(y-2 > 0)
            return new AxialCoordinate(x+1,y-1);
        else
            return null;
    }
    public AxialCoordinate getSouthEast(){
        if(y-2 > 0)
            return new AxialCoordinate(x+1,y+1);
        else
            return null;
    }
    public AxialCoordinate getSouth(){
        if(y-2 > 0)
            return new AxialCoordinate(x,y+2);
        else
            return null;
    }
    public AxialCoordinate getSouthWest(){
        if(y-2 > 0)
            return new AxialCoordinate(x-1,y+1);
        else
            return null;
    }
    public AxialCoordinate getNorthWest(){
        if(y-2 > 0)
            return new AxialCoordinate(x-1,y-1);
        else
            return null;
    }

    @Override
    public int hashCode(){
        return
    }

}
