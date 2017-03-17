import GameModel.Map.GameMap;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.TriHexTile;
import GameModel.Map.VolcanoTile;
import cucumber.api.PendingException;
import org.junit.experimental.theories.Theories;

import cucumber.api.java.en.*;
import cucumber.api.java.en.Then;

use_step_matcher("re");
/**
 * Created by jowens on 3/16/17.
 */
public class MapStepdefs {
    private GameMap map;

    @Given("^I have (\\d+) game map$")
    public void I_am_on_the_NEW_BOARD(int gameMaps) throws Throwable{
      map = new GameMap();
    }

    @And("^I place (\\d+) TriHexTile$")
    public void I_place_TriHexTile(int triHexTiles) throws Throwable{
        TriHexTile tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
    }

    @When("^I go to place TriHexTile$")
    public void I_go_to_place_triHexTile(){
        map.placeFirstTile(tht);
    }

    public MyStepdefs() {
        Then("^the gameMap should have <HexTiles on Map> HexTiles$", () -> {
            // Write code here that turns the phrase above into concrete actions
            if(map.getGameBoard().size != 3)
            throw new PendingException();
        });
    }

    @Then("^the gameMap should have (\\d+) hextiles$")
    public void theGameMapShouldHaveHexTiles_on_MapHextiles(int hextiles) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
