package GameModel.Map.Coordinates;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameView.Map.Constants;
//import com.sun.tools.internal.jxc.ap.Const;

/**
 * Created by jowens on 3/12/17.
 */
public class HexCoordinate {
    private AxialCoordinate ac;
    private PixelCoordinate pc;
    private CubicCoordinate cc; // gunna ignore this for now

    /* how to redirect default constructor to another constructor? Should this not be done?
    public HexCoordinate(){
        return HexCoordinate(new AxialCoordinate(0,0));
    }
    */
    public HexCoordinate(AxialCoordinate ac){
        this.ac = ac;
        double x = Constants.TILE_WIDTH * Math.sqrt(3) * (getAxialCoordinate().x + getAxialCoordinate().y/2);
        double y = Constants.TILE_HEIGHT * 3/2 * getAxialCoordinate().y/2;
        int x1 = (int)x;
        int y1 = (int)y;
        //pc = new PixelCoordinate(x1, y1);

        pc = new PixelCoordinate(ac.x* Constants.TILE_WIDTH * 3/4, ac.y * Constants.TILE_HEIGHT/2);
        //cc = new CubicCoordinate();
    }

    public AxialCoordinate getAxialCoordinate(){
        return ac;
    }
    public PixelCoordinate getPixelCoordinate(){
        return pc;
    }
    public CubicCoordinate getCubicCoordinates(){
        return cc;
    }
}
