package GameControl.Player;

import GameControl.Controller;
import GameEngine.GameLogicDirector;
import GameModel.Map.GameMap;
import GameControl.Placement;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.TriHexTile;
import GameModel.Map.Tile.VolcanoTile;
import GameView.Viewports.ActiveGameViewport;
import GameView.Viewports.Viewport;
import GameModel.Map.Tile.Tile;

import java.util.List;

/**
 * Created by jowens on 3/10/17.
 */
public class PlayerController extends Controller{
    GameMap visibleGameMap;
    Viewport viewport;
    Player myPlayer;

    private Tile currentlySelecteTile;
    //place tile
    //build (settle, expand, or place totoro)
    public PlayerController(Player myPlayer){
        this.myPlayer = myPlayer;
        visibleGameMap = GameLogicDirector.getInstance().getMap();
        viewport = new ActiveGameViewport(myPlayer);
    }

    public void placeTile(TriHexTile tht, Placement placement) {
        List<Placement> legalPlacements = visibleGameMap.getLegalPlacements(tht);
        if (legalPlacements.contains(placement)) {
            //TODO actually place the tile
        }
        else {
            //TODO this player has attempted an illegal action, make them lose and end the game
        }
    }

    public boolean canPlaceMeeples(HexTile hexTile) {
        return hexTile.getMeepleCount() == 0 && myPlayer.getMeepleCount() >= hexTile.getLevel() + 1 && !hexTile.ofSameType(new VolcanoTile());
    }

    public void placeMeeples(HexTile hexTile) {
        if (canPlaceMeeples(hexTile)) {
            hexTile.placeMeeples(myPlayer);
            myPlayer.removeMeeples(hexTile.getLevel() + 1);
            myPlayer.awardPoints((hexTile.getLevel() + 1) * (hexTile.getLevel() + 1));
        }
        else {
            //TODO this player has attempted an illegal place action, make them lose and end the game
        }
    }

    public void buildSettlement(HexTile hexTile) {
        if (hexTile.getLevel() == 0 && canPlaceMeeples(hexTile)) {
            placeMeeples(hexTile);
        }
        else {
            //TODO this player has attempted an illegal build action, make them lose and end the game
        }
    }

    public boolean canExpandSettlement(List<HexTile> settlement, TerrainTile terrainType) {
        int expansionMeepleCost = 0;
        HexTile expansionHexTile;

        for(HexTile settlementHexTile: settlement) {
            //TODO check each tile within settlement for expansion
        }

        if (expansionMeepleCost > 0 && expansionMeepleCost < myPlayer.getMeepleCount())
            return true;
        else
            return false;
    }

    public void expandSettlement(List<HexTile> settlement, TerrainTile terrainType) {
        if (canExpandSettlement(settlement, terrainType)) {
            //TODO expand settlement

        }
        else{

        }
            //TODO this player has attempted an illegal action, make them lose and end the game
    }

    public void canExpandSettlementToHexTile() {

    }


    public void buildTotoro(HexTile hexTile) {

    }

    public Viewport getViewport(){
        return viewport;
    }

}
