package StepDefs;

import GameModel.Map.GameMap;
import GameModel.Map.TriHexTile;
import GameModel.Map.Tile.*;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;


/**
 * Created by jowens on 3/16/17.
 */
public class MapStepdefs {
    private GameMap map;
    private TriHexTile tht;

    @Given("^I have (\\d+) game map$")
    public void I_am_on_the_NEW_BOARD(int gameMaps) throws Throwable{
        map = new GameMap();
    }

    @And("^I place (\\d+) TriHexTile$")
    public void I_place_TriHexTile(int triHexTiles) throws Throwable{
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
    }

    @When("^I go to place TriHexTile$")
    public void I_go_to_place_triHexTile(){
        map.getAllLegalPlacements(tht);
        map.implementPlacement(map.getAllLegalPlacements(tht).get(0));

    }

    @Then("^the gameMap should have (\\d+) hextiles$")
    public void theGameMapShouldHaveHexTiles_on_MapHextiles(int hextiles) throws Throwable {
        int numberOfTriHextiles = map.getNumberOfTriHextiles();
        Assert.assertEquals(3, numberOfTriHextiles*3);
    }
}
