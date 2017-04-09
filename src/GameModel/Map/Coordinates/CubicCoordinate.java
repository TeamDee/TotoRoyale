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

    public OffsetCoordinate getOffsetCoordinate(){
        int xOffset = 0;
        int yOffset = 0;

        yOffset = (y - z) * -1;
        xOffset = x;

        OffsetCoordinate ac = new OffsetCoordinate(xOffset,yOffset);
        return ac;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }

    @Override
    public String toString(){ return "" + x + " " + y + " " + z; }
}
