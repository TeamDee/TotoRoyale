package GameModel.Map.Tile;

import GameView.Map.WaterTerrainView;

/**
 * Created by jowens on 3/13/17.
 */
public class Lake extends TerrainTile{

    public Lake(){
        myType = TerrainType.LAKE;
        myView = new WaterTerrainView();
    }


    @Override
    public boolean isLake(){
        return true;
    }
    @Override
    public String toString(){
        return "Lake";
    }

}
