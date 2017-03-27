package GameModel.Map.Tile;

import GameView.Map.RockTerrainView;

/**
 * Created by jowens on 3/13/17.
 */
public class Rock extends TerrainTile{
    public Rock(){
        myType = TerrainType.ROCK;
        myView = new RockTerrainView(this);
        me = this;
    }

    @Override
    public boolean isRock(){
        return true;
    }
    @Override
    public String toString(){
        return "Rock";
    }



}
