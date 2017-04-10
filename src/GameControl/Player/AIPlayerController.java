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
        for (int i = 0; i < 2; i++){
            HexTile hex = placement.getBoardSpaces().get(i).topTile(); //Each individual hex tile at its top level
            //Level Consideration
            if (hex.getLevel() == 0) { //Empty space
                score += 15;
                return score;
            }
            if (hex.getLevel() == 1) //Nuke and potential for a tiger
                score += 20;
            if (hex.getLevel() == 2) //Causes a tiger to be place-able, high priority
                score += 50;
            if (hex.getLevel() >= 3) //There is no real purpose after level 3, so not much priority
                score += 10;
            //Enemy Occupant
            if (hex.isOccupied()) {
                if (hex.getLevel() >= 1) {
                    if ((myPlayer.isWhite() && hex.isOwnedByBlack()) || (!myPlayer.isWhite() && hex.isOwnedByWhite())) { //Enemy owns place
                        if (hex.isPartOfSettlement && hex.settlementSize >= 5) {//Don't want to make it easier for the opponent
                            if (hasAdjacentTotoro(hex)) {
                                return 0;
                            } else
                                score -= 100;
                            } else if (hex.isPartOfSettlement && hex.settlementSize < 5)
                                score += 20;
                    }
                    //Own settlement (includes separation for having more totoros)
                    if ((myPlayer.isWhite() && hex.isOwnedByWhite()) || (!myPlayer.isWhite() && hex.isOwnedByBlack())) { //Friendly settlement
                        if (hex.isPartOfSettlement && hex.settlementSize >= 5) {
                            if (hasAdjacentTotoro(hex)) //Best point to separate a settlement at
                                score += 100;
                            else
                                score += 20;
                        }
                        if (hex.isPartOfSettlement && hex.settlementSize < 5) {
                            if (hasAdjacentTotoro(hex)) //Still important, but requires more work to have enough for a totoro
                                score += 50;
                            else
                                score += 10;
                        }
                    }
                }
            }
        }
        return score;
    }

    public int scoreTotoroPlacement(TerrainTile t){
        int score = 0;

        return score;
    }

    public boolean hasAdjacentTotoro(HexTile hex){
        if(hex.getNorth() != null)
            return hex.getNorth().hasTotoro();
        if (hex.getNorthWest() != null)
            return hex.getNorthWest().hasTotoro();
        if (hex.getSouthWest() != null)
            return hex.getSouthWest().hasTotoro();
        if (hex.getSouth() != null)
            return hex.getSouth().hasTotoro();
        if (hex.getSouthWest() != null)
            return hex.getSouthWest().hasTotoro();
        if (hex.getNorthWest() != null)
            return hex.getNorthWest().hasTotoro();
        else
            return false;
    }

    public void takeBuildAction() {}
}
