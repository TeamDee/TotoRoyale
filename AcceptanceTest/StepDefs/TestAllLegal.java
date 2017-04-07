package StepDefs;

import GameControl.Placement;
import GameModel.Map.BoardSpace;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
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

    @Given("^a list of placements$")
    public void beforeItrytoplace(){
        map = new GameMap();
        location = new OffsetCoordinate(0, 0);
        BoardSpace boardSpace = new BoardSpace(location,map);
        Assert.assertFalse(boardSpace.hasTile());
        Assert.assertTrue(boardSpace.getLocation().compare(location));
    }

    @When("^all placements are legal$")
    public void makeMove(){

    }

    @Then("^no placements are illegal$")
    public void getaListOfLegalPlacements(){
        for(Placement p : placements)
        Assert.assertTrue(p.isLegalPlacement(p));
    }

}