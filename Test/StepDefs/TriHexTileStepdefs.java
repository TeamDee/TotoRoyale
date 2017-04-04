package StepDefs;

import GameControl.Placement;
import GameControl.Player.Player;
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
 * Created by Z_K on 3/20/2017.
 */
public class TriHexTileStepdefs {

    private GameMap map;
    private ArrayList<Placement> placements;
    private static OffsetCoordinate location;
    private Player player;
    private TriHexTile tht;



//
//    /*Scenario: create a TriHexTile */
   @Given("^The game is up and initializing$")
    public void initialize(){
       map = new GameMap();

}

    @When("^A TriHexTile get created$")
    public void aTriHexTileGetCreated() {
        location = new OffsetCoordinate(0, 0);
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
    }

    @Then("^The TriHexTile is initialized with three Tiles and one has to be VolcanoTile$")
    public void theTriHexTileIsInitializedWithThreeTilesAndOneHasToBeVocanoTile() {
        VolcanoTile vt = new VolcanoTile();
        //((TerrainTile)tht.getTileOne()).isGrass();
       // ((TerrainTile)tht.getTileTwo()).isRock();

        Assert.assertTrue( tht.getTileThree().ofSameType(vt));
        Assert.assertTrue(((TerrainTile)tht.getTileOne()).isGrass());
        Assert.assertTrue(((TerrainTile)tht.getTileTwo()).isRock());



    }


    /* Scenario:TriHexTile level Placement */
    @Given("^The game is initliazed$")
    public void game_is_Initiliazed() {
        map = new GameMap();

    }

    @And("^TriHexTiles exist on the board$")
    public void game_is_running(){
        location = new OffsetCoordinate(0, 0);

    }

    @When("^a user places a TriHexTile with intentions to build a new level$")
    public void build_new_level(){
        map = new GameMap();
        player = new Player(map);
        placements = map.getLegalMapPlacements(tht);
        //map.implementPlacement( placements.get(0));
    }

    @Then("^one volcano must be atop another volcano$")
    public void check_Volcano() {
            for (Placement p: placements)
                Assert.assertTrue(p.volcanoMatch());

    }

}
