package StepDefs;

import GameControl.Placement;
import GameModel.Map.BoardSpace;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.Grass;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.Rock;
import GameModel.Map.Tile.VolcanoTile;
import GameModel.Map.TriHexTile;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.ArrayList;

/**
 * Created by eb on 3/29/2017.
 */
public class TestAllLegal {

    private GameMap map;
    private OffsetCoordinate location;
    private ArrayList<Placement> placements;
    private TriHexTile tht;
    private BoardSpace boardSpace;

    @Given("^a list of placements$")
    public void beforeItrytoplace(){
        map = new GameMap();
        location = new OffsetCoordinate(0, 0);
        boardSpace = new BoardSpace(location,map);
        Assert.assertFalse(boardSpace.hasTile());
        Assert.assertTrue(boardSpace.getLocation().compare(location));
    }

    @When("^all placements are legal$")
    public void makeMove() {
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        //map.getAllLegalPlacements(tht);
        // map.implementPlacement(map.getAllLegalPlacements(tht).get(0));
        ArrayList<HexTile> tiles = map.getVisible();
        for (HexTile ht : tiles) {
            boardSpace.addTile(ht);
        }
    }

    @Then("^no placements are illegal$")
    public void getaListOfLegalPlacements(){
        placements = new ArrayList<Placement>();
        for(Placement p : placements)
        Assert.assertTrue(p.isLegalPlacement(p));
    }

}