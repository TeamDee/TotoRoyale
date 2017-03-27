package GameModel.Map.Tile;

import GameView.Map.HexTileView;
import GameView.Map.VolcanoView;

/**
 * Volcano tiles can only be placed on other volcano tiles
 * Created by jowens on 3/13/17.
 */
public class VolcanoTile extends HexTile {
    public VolcanoTile(){
        myType = TerrainType.VOLCANO;
        myView = new VolcanoView();

    }

    //TODO test this
    public boolean ofSameType(VolcanoTile vt){
        return true;
    }
    public boolean ofSameType(TerrainTile tt){
        return false;
    }
    @Override
    public String toString(){
        return "Volcano";
    }

    @Override
    public HexTileView getTileView(){
        return myView;
    }


}
