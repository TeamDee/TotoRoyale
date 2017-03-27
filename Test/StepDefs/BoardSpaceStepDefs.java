
package StepDefs;



import GameControl.Placement;
import GameModel.Map.GameMap;
import GameControl.Player.Player;
import GameModel.Map.TriHexTile;
import GameModel.Map.Tile.*;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import GameModel.Map.BoardSpace;
import java.util.ArrayList;
import GameModel.Map.Tile.Deck;


/**
 * Created by eb on 3/23/2017.
 */
public class BoardSpaceStepDefs {

    private GameMap map;
    private TriHexTile tht;
    private Player player;
    private ArrayList<Placement>  placement;



    @Given("^I have a game map game map initialized$")
    public void init_gamemap() throws Throwable{
        map = new GameMap();
    }

    @And("^there are TriHexTiles already placed on the board$")
    public void exsisting_triHexTile(){
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());


    }

    @When("^I place a  TriHexTile adjacent to an exisiting TriHexTile$")
    public void place_Tile() {
        player = new Player();
        placement = map.getAllLegalPlacements(tht);
        player.placeTile(map,placement.get(0));

    }

    @Then("^the user's placement is legal$")
    public void update_BoardSpace() {
        Assert.assertTrue(player.placementCheck() );


    }



    @Given("^I have a game map initialized$")
    public void init_the_gamemap() throws Throwable{
        map = new GameMap();
    }

    @And("^TriHexTiles are already placed on the board$")
    public void there_are_Exsisting_triHexTile(){
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());


    }

    @When("^I do not place a TriHexTile adjacent to an exisiting TriHexTile$")
    public void illegaly_place_Tile() {
        player = new Player();
        placement = map.getAllLegalPlacements(tht);


    }

    @Then("^the user's placement is illegal$")
    public void cannot_update_BoardSpace() {
        Assert.assertFalse(player.placementCheck() );


    }


}


