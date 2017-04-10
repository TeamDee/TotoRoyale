package GameModel.Map.Tile;

import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import GameView.Map.HexTileView;
import GameView.Map.VolcanoView;

/**
 * Volcano tiles can only be placed on other volcano tiles
 * Created by jowens on 3/13/17.
 */
public class VolcanoTile extends HexTile {
    public VolcanoTile(){
        myType = TerrainType.VOLCANO;
        myView = new VolcanoView(this);

    }

    public boolean isOwnedByWhite() {
        return false;
    }

    public boolean isOwnedByBlack() {
        return false;
    }

    public Player getOwner(){
        return null;
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
