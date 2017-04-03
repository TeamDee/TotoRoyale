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


    public OffsetCoordinate getAxialCoordinate(){
        int qAxial = x;
        int rAxial = z;
        OffsetCoordinate ac = new OffsetCoordinate(qAxial,rAxial);
        return ac;
    }
}
