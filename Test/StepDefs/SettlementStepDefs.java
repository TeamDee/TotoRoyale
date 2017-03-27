package StepDefs;

import GameModel.Map.GameMap;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by eb on 3/26/2017.
 */
public class SettlementStepDefs {
    private GameMap map;

    @Given("^I have a game map initalized$")
        public void Game_map_is_init() {
        map = new GameMap();

    }

    @When("^I am on level 1$")
        public void check_level_one() {
    }

    @Then("^a tile must be placed on a non-volcano terrain$")
        public void check_terrain() {


    }

    @Given("^a game map is initalized$")
    public void a_game_map_is_initalized() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I have exsisting settlements$")
    public void i_have_exsisting_settlements() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^I cannot found a settlement on an exsisting settlement$")
    public void i_cannot_found_a_settlement_on_an_exsisting_settlement() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
