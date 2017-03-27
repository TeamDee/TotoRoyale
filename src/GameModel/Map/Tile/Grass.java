package GameModel.Map.Tile;

import GameView.Map.GrassTerrainView;

/**
 * Created by jowens on 3/8/17.
 */
public class Grass extends TerrainTile {

    public Grass(){
        myView = new GrassTerrainView(this);
        myType = TerrainType.GRASS;
        me = this;
    }


    @Override
    public boolean isGrass(){
        return true;
    }

    @Override
    public String toString(){
        return "Grass";
    }




}
