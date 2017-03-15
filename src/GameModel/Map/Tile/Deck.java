package GameModel.Map.Tile;

import GameModel.Map.TriHexTile;

import java.util.Stack;
/**
 * stores the rest of the cards
 * Created by jowens on 3/13/17.
 */
public class Deck {
    public Stack<TriHexTile> deck;
    public Deck(){
        deck = new Stack<TriHexTile>();
    }

    public TriHexTile draw() {
        if(deck.empty())
            return null;
        else
            return deck.pop();
    }

    public void newDeckGivenByServer(Stack<TriHexTile> newDeck){
        deck = newDeck;
    }
    public void addCardToTopOfDeck(TriHexTile t){
        deck.add(t);
    }
    public void newRandomDeck(){
        //todo populate 48 tiles with 16(types)x3(of each)
    }
    public void shuffleDeck(){
        //unnecessary rn
    }
 }
