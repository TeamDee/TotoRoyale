package GameModel.Map.Coordinates;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameView.Map.Constants;

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
        pc = new PixelCoordinate(ac.x* Constants.TILE_WIDTH /2, ac.y * Constants.TILE_HEIGHT / 2);
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
