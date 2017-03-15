package GameControl.Player;

import GameControl.Controller;
import GameEngine.GameLogicDirector;
import GameModel.Map.GameMap;
import GameControl.Placement;
import GameModel.Map.TriHexTile;
import GameView.Viewports.ActiveGameViewport;
import GameView.Viewports.Viewport;
import GameModel.Map.Tile.Tile;

import java.util.List;

/**
 * Created by jowens on 3/10/17.
 */
public class PlayerController extends Controller{
    GameMap visibleGameMap;

    private Viewport viewport;

    private Player myPlayer = new Player(); //todo this is wrong

    private Tile currentlySelecteTile;
    //place tile
    //build (settle, expand, or place totoro)
    public PlayerController(){
        visibleGameMap = GameLogicDirector.getInstance().getMap();
        viewport = new ActiveGameViewport(myPlayer);
    }

    void placeTile(TriHexTile tht) {
        List<Placement> legalPlacements = visibleGameMap.getLegalPlacements(tht);
        //TODO actually place the tile

    }

    public Viewport getViewport(){
        return viewport;
    }

}
