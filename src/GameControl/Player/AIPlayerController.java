package GameControl.Player;

import GameControl.Placement;
import GameModel.Map.BoardSpace;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;
import GameModel.Map.TriHexTile;

import java.util.List;

/**
 * Created by conor on 3/17/2017.
 */
public class AIPlayerController extends PlayerController{

    public AIPlayerController(Player myPlayer) {
        super(myPlayer);
    }

    public void takeTurn(TriHexTile tileToPlace) {
        takePlaceTileAction(tileToPlace);
        takeBuildAction();
    }


    public void takePlaceTileAction(TriHexTile tileToPlace) {
        List<Placement> legalPlacements = visibleGameMap.getAllLegalPlacements(tileToPlace);
        int currentPlacementScore;
        int maxPlacementScore = -1;
        Placement placementWithMaxScore = null;
        for (Placement currentPlacement: legalPlacements) {
            currentPlacementScore = scoreTilePlacement(currentPlacement);
            if (currentPlacementScore > maxPlacementScore) {
                maxPlacementScore = currentPlacementScore;
                placementWithMaxScore = currentPlacement;
            }
        }
        super.placeTile(tileToPlace, placementWithMaxScore);
    }

    public int scoreTilePlacement(Placement placement) {
        int score = 0;
        //score = (int) (100 * Math.random());
        //TODO score placements nonrandomly
        for (int i = 0; i < 2; i++){
            BoardSpace hex = placement.getBoardSpaces().get(i);
            //Level Consideration
            if (hex.topTile().getLevel() == 0) //Empty space
                score += 15;
            if (hex.topTile().getLevel() == 1) //Nuke and potential for a tiger
                score += 20;
            if (hex.topTile().getLevel() == 2) //Causes a tiger to be place-able, high priority
                score += 50;
            if (hex.topTile().getLevel() >= 3) //There is no rel purpose after level 3, so not much priority
                score += 10;
            //Enemy Occupant
            if (myPlayer.isWhite() && hex.topTile().isOwnedByBlack()) { //Enemy owns place
                if (hex.topTile().isPartOfSettlement && hex.topTile().settlementSize >= 5)
                    score += 0;
                else if (hex.topTile().isPartOfSettlement && hex.topTile().settlementSize < 5)
                    score += 20;
            }
        }
        return score;
    }

    public void takeBuildAction() {

    }
}
