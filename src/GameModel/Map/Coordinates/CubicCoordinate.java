package GameModel.Map.Coordinates;

/**
 * Created by jowens on 3/12/17.
 */
public class CubicCoordinate {
    int x,y,z;
    public CubicCoordinate(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AxialCoordinate getAxialCoordinate(){
        int xAxial = x;
        int yAxial = z;
        AxialCoordinate ac = new AxialCoordinate(0,0);
        
    }
}
