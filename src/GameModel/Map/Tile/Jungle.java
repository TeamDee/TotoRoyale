package GameModel.Map.Tile;
import GameView.Map.JungleTerrainView;
/**
 * Created by jowens on 3/13/17.
 */

public class Jungle extends TerrainTile{
    public Jungle(){
        myType = TerrainType.JUNGLE;
        this.myView = new JungleTerrainView(this);
        me = this;
    }

    @Override
    public boolean isJungle(){
        return true;
    }

    @Override
    public String toString() {
        String returnMe = super.toString();
        returnMe += "Jungle";
        return returnMe;
    }
}


