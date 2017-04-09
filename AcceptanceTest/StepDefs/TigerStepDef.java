package StepDefs;

import GameModel.Map.BoardSpace;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.*;
import GameModel.Map.TriHexTile;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.ArrayList;

/**
 * Created by eb on 4/8/2017.
 */
public class TigerStepDef {
    private GameMap map;
    private OffsetCoordinate location;
    private BoardSpace boardSpace;
    private TriHexTile tht;

    @Given("^it is my turn to make a legal move$")
    public void make_legal_moves() throws Throwable {
        map = new GameMap();
        location = new OffsetCoordinate(0, 0);
         boardSpace = new BoardSpace(location,map);
        Assert.assertFalse(boardSpace.hasTile());
        //Assert.assertTrue(boardSpace.getLocation().compare(location));
//        boardspace.getLevel();
    }

    @When("^I place a tiger$")
            public void place_tiger_legally() {
        tht = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
      //  map.getAllLegalPlacements(tht);
        ArrayList<HexTile> tiles = map.getVisible();
        for(HexTile ht :tiles) {
            boardSpace.addTile(ht);
            //map.implementPlacement(map.getAllLegalPlacements(tht).get(8));
        }
}

    @Then("^it must be on level (\\d+) or greater$")
        public void check_level(int exp){
        Assert.assertEquals(exp, boardSpace.getLevel());
            }

}

