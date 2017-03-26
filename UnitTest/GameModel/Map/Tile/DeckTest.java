package GameModel.Map.Tile;

import GameModel.Map.TriHexTile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Z_K on 3/26/2017.
 */
public class DeckTest {
    private static Deck deck;

    @BeforeClass
    public static void setUp(){
        deck = new Deck();
        deck.newRandomDeck();
    }

    @Test
    public void testDraw(){
        int numberDrawed=0;
        while(true){
            TriHexTile current = deck.draw();
            if(current==null)
                break;
            numberDrawed++;
        }
        Assert.assertEquals(numberDrawed, 48);
    }
}
