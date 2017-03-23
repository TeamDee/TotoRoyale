import GameModel.Map.Tile.Deck;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by Z_K on 3/23/2017.
 */
public class DeckStepdefs {
    Deck newDeck;
    @Given("^The game is initialized.$")
    public void gameIsInitialized() {

    }

    @When("^The deck is created.$")
    public void deckIsCreated(){
        newDeck = new Deck();
    }

    @Then("^I get a shuffled deck.$")
    public void getShuffledDeck(){
        
    }
}
