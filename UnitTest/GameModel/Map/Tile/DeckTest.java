package GameModel.Map.Tile;

import GameModel.Map.TriHexTile;
import org.junit.*;

/**
 * Created by Z_K on 3/26/2017.
 */
public class DeckTest {
    private static Deck deck;

    @BeforeClass
    public static void setUp(){
        deck = new Deck();
    }

    @Before
    public void setUpEachTest(){
        deck.newRandomDeck();
    }

    @Test
    public void testNewRandomDeck(){
        Deck exampleDeck = Deck.newExampleDeck();
        boolean different = false;
        while(!exampleDeck.deck.isEmpty()){
            if(!exampleDeck.draw().isTheSameAs(deck.draw())){
                different = true;
                break;
            }
        }
        Assert.assertTrue(different);
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
        Assert.assertEquals(48, numberDrawed);
    }
    @Ignore
    @Test
    public void uniquenessTest(){
        boolean sameTriHexTileAppeared = false;
        TriHexTile current = deck.draw();
        TriHexTile previous = current;
        current = deck.draw();
        while(current!=null){
            if(current.isTheSameAs(previous)){
                sameTriHexTileAppeared = true;
                break;
            }
            previous = current;
            current = deck.draw();
        }
        Assert.assertFalse(sameTriHexTileAppeared);
    }
}
