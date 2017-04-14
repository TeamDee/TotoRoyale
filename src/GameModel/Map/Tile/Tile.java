package GameModel.Map.Tile;

import GameModel.Map.Coordinates.OffsetCoordinate;

/**
 * Created by jowens on 3/8/17.
 */
public abstract class Tile {
    int level = 0;


    public Tile(){
        //default constructor
    }

    public void setLevel(int level) {
        this. level = level;
    }



    public abstract OffsetCoordinate getLocation();
}
