
package StepDefs;


import GameControl.Placement;
import GameControl.Player.Player;
import GameModel.Map.BoardSpace;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.*;
import GameModel.Map.TriHexTile;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.ArrayList;


/**
 * Created by eb on 3/23/2017.
 */
public class BoardSpaceStepDefs {

    private GameMap map;
    private TriHexTile tht;
    private Player player;
    private ArrayList<Placement> placement;
    private BoardSpace before;
    private int levelBefore;


    @Given("^I have a game map game map initialized$")
    public void init_gamemap() throws Throwable {
        map = new GameMap();
    }

    @And("^there are TriHexTiles already placed on the board$")
    public void exsisting_triHexTile() {
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
    }

    @When("^I place a  TriHexTile adjacent to an exisiting TriHexTile$")
    public void place_Tile() {
        player = new Player(map);
        placement = map.getAllLegalPlacements(tht);
        player.placeTile(map, placement.get(0));
    }

    @Then("^the user's placement is legal$")
    public void update_BoardSpace() {
        Assert.assertTrue(player.placementCheck());
    }


    @Given("^I have a game map initialized$")
    public void init_the_gamemap() throws Throwable {
        map = new GameMap();
    }

    @And("^TriHexTiles are already placed on the board$")
    public void there_are_Exsisting_triHexTile() {
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
    }

    @When("^I do not place a  TriHexTile adjacent to an existing TriHexTile$")
    public void illegaly_place_Tile() {
        player = new Player(map);
        placement = map.getAllLegalPlacements(tht);
    }

    @Then("^the user's placement is illegal$")
    public void cannot_update_BoardSpace() {
        Assert.assertFalse(player.placementCheck());
    }


    @Given("^I have a game running for a while$")
    public void simulatedGame() {
        map = new GameMap();
        player = new Player(map);
        Deck deck = new Deck();
        int index = 10;
        while (index-- > 0) {
            TriHexTile tht = deck.draw();
            placement = map.getAllLegalPlacements(tht);
            map.implementPlacement(placement.get(0));
        }
    }

    @When("^I nuke some hextiles$")
    public void nukeTiles(){
        HexTile tile = map.getVisibleAtAxialCoordinate(new OffsetCoordinate(0,0));
        before = tile.getBoardSpace();
        levelBefore = before.getLevel();
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        before.addTile(tht.getTileOne());

    }

    @Then("^The hextiles being nuked level up$")
    public void ensureThatLevelsUp() {
        int beforeLevelAfter = before.getLevel();
        Assert.assertNotEquals(levelBefore, beforeLevelAfter);
        Assert.assertEquals(beforeLevelAfter, tht.getTileOne().getLevel());
    }
}



