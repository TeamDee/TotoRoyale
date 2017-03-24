
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


    /* @Given("^I have (\\d+) game map$")
     public void I_am_on_the_Board(int gameMaps) throws Throwable{
         map = new GameMap();
     }

     @When("^I place (\\d+) TriHexTile$")
     public void place_TriHexTile(int triHexTiles) throws Throwable{
         tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        tht.getLevel();
     }

     @And("^It is atop two or more (\\d+) TriHexTile$")

     @And("^the <volcano> Volcano tile lies atop an exsisting <volcano>$")
             public void check_Volcano(int VolcanoTile) throws Throwable{
     }
 */
    @Given("^I have a game map game map initialized$")
    public void Init_gamemap() throws Throwable{
        map = new GameMap();
    }

    @And("^there are TriHexTiles already placed on the board$")
    public void Exsisting_triHexTile(){
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        map.placeFirstTile(tht);

    }

    @When("^I place a  TriHexTile adjacent to an exisiting TriHexTile$")
    public void Place_Tile() {
        player = new Player();
        placement = map.getLegalTablePlacements(tht);
        player.placeTile(map,placement.get(0));

    }

    @Then("^the user's placement is legal$")
    public void Update_BoardSpace() {
        Assert.assertTrue(player.placementCheck() );


    }

}


