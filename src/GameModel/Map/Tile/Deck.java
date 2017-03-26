package GameModel.Map.Tile;

import GameModel.Map.TriHexTile;

import java.util.Random;
import java.util.Stack;
/**
 * stores the cards being used in a particular game
 * Created by jowens on 3/13/17.
 */
public class Deck {
    public Stack<TriHexTile> deck;

    public Deck() {
        deck = new Stack<TriHexTile>();
    }

    public TriHexTile draw() {
        if (deck.empty())
            return null;
        else
            return deck.pop();
    }

    public void newDeckGivenByServer(Stack<TriHexTile> newDeck) {
        deck = newDeck;
    }

    public void addCardToTopOfDeck(TriHexTile t) {
        deck.add(t);
    }

    public void newRandomDeck() {
        if(deck.size()!=0)
            deck = new Stack<TriHexTile>();
        TriHexTile[] exampleDeck = exampleFullDeck();
        exampleDeck = shuffleDeck(exampleDeck);
        prepareDeckStack(exampleDeck);
    }

    public static Deck newExampleDeck() {
        TriHexTile[] exampleDeck = exampleFullDeck();
        Deck returnMe = new Deck();
        for (TriHexTile tht : exampleDeck) {
            returnMe.addCardToTopOfDeck(tht);
        }
        return returnMe;
    }

    public static TriHexTile[] exampleFullDeck() {
        TriHexTile[] newDeck = new TriHexTile[48];
        newDeck[0] = new TriHexTile(new Grass(), new Grass(), new VolcanoTile());
        newDeck[1] = new TriHexTile(new Grass(), new Jungle(), new VolcanoTile());
        newDeck[2] = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        newDeck[3] = new TriHexTile(new Grass(), new Lake(), new VolcanoTile());

        newDeck[4] = new TriHexTile(new Jungle(), new Grass(), new VolcanoTile());
        newDeck[5] = new TriHexTile(new Jungle(), new Jungle(), new VolcanoTile());
        newDeck[6] = new TriHexTile(new Jungle(), new Rock(), new VolcanoTile());
        newDeck[7] = new TriHexTile(new Jungle(), new Lake(), new VolcanoTile());

        newDeck[8] = new TriHexTile(new Rock(), new Grass(), new VolcanoTile());
        newDeck[9] = new TriHexTile(new Rock(), new Jungle(), new VolcanoTile());
        newDeck[10] = new TriHexTile(new Rock(), new Rock(), new VolcanoTile());
        newDeck[11] = new TriHexTile(new Rock(), new Lake(), new VolcanoTile());

        newDeck[12] = new TriHexTile(new Lake(), new Grass(), new VolcanoTile());
        newDeck[13] = new TriHexTile(new Lake(), new Jungle(), new VolcanoTile());
        newDeck[14] = new TriHexTile(new Lake(), new Rock(), new VolcanoTile());
        newDeck[15] = new TriHexTile(new Lake(), new Lake(), new VolcanoTile());

        newDeck[16] = new TriHexTile(new Grass(), new Grass(), new VolcanoTile());
        newDeck[17] = new TriHexTile(new Grass(), new Jungle(), new VolcanoTile());
        newDeck[18] = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        newDeck[19] = new TriHexTile(new Grass(), new Lake(), new VolcanoTile());

        newDeck[20] = new TriHexTile(new Jungle(), new Grass(), new VolcanoTile());
        newDeck[21] = new TriHexTile(new Jungle(), new Jungle(), new VolcanoTile());
        newDeck[22] = new TriHexTile(new Jungle(), new Rock(), new VolcanoTile());
        newDeck[23] = new TriHexTile(new Jungle(), new Lake(), new VolcanoTile());

        newDeck[24] = new TriHexTile(new Rock(), new Grass(), new VolcanoTile());
        newDeck[25] = new TriHexTile(new Rock(), new Jungle(), new VolcanoTile());
        newDeck[26] = new TriHexTile(new Rock(), new Rock(), new VolcanoTile());
        newDeck[27] = new TriHexTile(new Rock(), new Lake(), new VolcanoTile());

        newDeck[28] = new TriHexTile(new Lake(), new Grass(), new VolcanoTile());
        newDeck[29] = new TriHexTile(new Lake(), new Jungle(), new VolcanoTile());
        newDeck[30] = new TriHexTile(new Lake(), new Rock(), new VolcanoTile());
        newDeck[31] = new TriHexTile(new Lake(), new Lake(), new VolcanoTile());

        newDeck[32] = new TriHexTile(new Grass(), new Grass(), new VolcanoTile());
        newDeck[33] = new TriHexTile(new Grass(), new Jungle(), new VolcanoTile());
        newDeck[34] = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        newDeck[35] = new TriHexTile(new Grass(), new Lake(), new VolcanoTile());

        newDeck[36] = new TriHexTile(new Jungle(), new Grass(), new VolcanoTile());
        newDeck[37] = new TriHexTile(new Jungle(), new Jungle(), new VolcanoTile());
        newDeck[38] = new TriHexTile(new Jungle(), new Rock(), new VolcanoTile());
        newDeck[39] = new TriHexTile(new Jungle(), new Lake(), new VolcanoTile());

        newDeck[40] = new TriHexTile(new Rock(), new Grass(), new VolcanoTile());
        newDeck[41] = new TriHexTile(new Rock(), new Jungle(), new VolcanoTile());
        newDeck[42] = new TriHexTile(new Rock(), new Rock(), new VolcanoTile());
        newDeck[43] = new TriHexTile(new Rock(), new Lake(), new VolcanoTile());

        newDeck[44] = new TriHexTile(new Lake(), new Grass(), new VolcanoTile());
        newDeck[45] = new TriHexTile(new Lake(), new Jungle(), new VolcanoTile());
        newDeck[46] = new TriHexTile(new Lake(), new Rock(), new VolcanoTile());
        newDeck[47] = new TriHexTile(new Lake(), new Lake(), new VolcanoTile());

        return newDeck;
    }

    private void prepareDeckStack(TriHexTile[] deckList){
        for(TriHexTile tht : deckList){
            deck.add(tht);
        }
    }

    public TriHexTile[] shuffleDeck(TriHexTile[] deck) {
        int size = deck.length;
        Random randomGenerator = new Random();
        int change;
        for(int i=0; i<size; i++){
            change = i + randomGenerator.nextInt(size - i);
            swap(deck, i, change);
        }
        return deck;
    }

    public int cardsLeft() {
        return deck.size();
    }

    private void swap(TriHexTile[] array, int positionA, int positionB) {
        TriHexTile temp = array[positionA];
        array[positionA] = array[positionB];
        array[positionB] = temp;
    }
}
