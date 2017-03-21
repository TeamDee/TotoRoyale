package GameControl.Player;

import GameControl.Placement;
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
        int score;
        score = (int) (100 * Math.random());
        //TODO score placements nonrandomly
        return score;
    }

    public void takeBuildAction() {

    }
}
