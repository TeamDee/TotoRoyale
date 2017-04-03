package StepDefs;

import GameControl.Player.BlackPlayer;
import GameControl.Player.WhitePlayer;
import GameModel.Map.BoardSpace;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import GameControl.Player.Player;
import sun.security.provider.certpath.BuildStep;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by eb on 3/26/2017.
 */
public class SettlementStepDefs {
    private GameMap map;
    private BoardSpace boardspace;
    private WhitePlayer whitePlayer;
    private BlackPlayer blackPlayer;
    private static AxialCoordinate location;
//WHITE MEEPLE SETTLEMENT
    @Given("^I have a game map initalized$")
        public void Game_map_is_init() {
        map = new GameMap();
        location = new AxialCoordinate(0, 0);

    }

    @When("^I am on level 1$")
        public void check_level_one() {
        BoardSpace boardspace = new BoardSpace(location,map);
        boardspace.getLevel();

    }

    @Then("^a white player must be placed on a non-volcano terrain$")
        public void check_terrain() {
        map = new GameMap();
        whitePlayer = new WhitePlayer();
        ArrayList<HexTile> tiles = map.getVisible();
        for (HexTile ht : tiles) {
            if (ht.terrainType() == TerrainType.VOLCANO)
                // new WhitePlayer().buildSettlement((TerrainTile)ht);
                whitePlayer.buildSettlement((TerrainTile) ht);
            break;

        }
    }
        //BLACK MEEPLE SETTLEMENT



        @Given("^my game map  is initalized$")
        public void Game_map_init(){
            map = new GameMap();
            location = new AxialCoordinate(0, 0);

        }

        @When("^I am on the first level$")
        public void check_for_level_one() {
            BoardSpace boardspace = new BoardSpace(location,map);
            boardspace.getLevel();

        }

        @Then("^a black player must be placed on a non-volcano terrain$")
        public void check_terrain_for_placement() {
            map = new GameMap();
            blackPlayer = new BlackPlayer();
            ArrayList<HexTile> tiles = map.getVisible();
            for(HexTile ht :tiles){
                if(ht.terrainType() == TerrainType.VOLCANO)
                    // new WhitePlayer().buildSettlement((TerrainTile)ht);
                    blackPlayer.buildSettlement((TerrainTile)ht);
                break;

            }

    }
//
//    @Given("^a game map is initalized$")
//    public void a_game_map_is_initalized() throws Throwable {
//        // Write code here that turns the phrase above into concrete actions
//        throw new PendingException();
//    }
//
//    @When("^I have exsisting settlements$")
//    public void i_have_exsisting_settlements() throws Throwable {
//        // Write code here that turns the phrase above into concrete actions
//        throw new PendingException();
//    }
//
//    @Then("^I cannot found a settlement on an exsisting settlement$")
//    public void i_cannot_found_a_settlement_on_an_exsisting_settlement() throws Throwable {
//        // Write code here that turns the phrase above into concrete actions
//        throw new PendingException();
//    }
}
