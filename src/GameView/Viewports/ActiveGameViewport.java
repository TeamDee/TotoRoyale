package GameView.Viewports;

import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import GameEngine.GameController;
import GameEngine.GameLogicDirector;
import GameModel.Map.Coordinates.HexCoordinate;
import GameModel.Map.Tile.Deck;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.TriHexTile;
import GameView.Map.Constants;
import GameView.Map.TileView;
import GameView.ViewFrame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Random;

public class ActiveGameViewport extends Viewport {

    TriHexTile tileToPlace;
    boolean scrolling = false;
    Player currentPlayer;
    Deck deck = GameLogicDirector.getInstance().deck;

    private static ActiveGameViewport activeGameViewport;// = new ActiveGameViewport(new WhitePlayer("WhitePlayer", null));

    public ActiveGameViewport(Player ownedBy) {
        this.setPreferredSize(ViewFrame.getInstance().getSize());
        this.setBackground(new Color(44,62,80));

        currentPlayer = ownedBy;
        tileToPlace = deck.draw();

        this.setFocusable(true);
    }

    private class PauseListener implements KeyListener {

        private Player player;

        public PauseListener(Player p) {
            this.player = p;
        }

        public void keyTyped(KeyEvent e) {
            //
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == e.VK_P) {
                GameController.getInstance().addToStack(GameController.getInstance().getActiveController());
            }
        }

        public void keyReleased(KeyEvent e) {
            //
        }
    }

    @Override
    public void visit(ViewportOrder viewportStack) {
        ViewportOrder.add(this);
    }

    public static ActiveGameViewport getInstance() {
        return activeGameViewport;
    }

    // --------------
    // THE ACTUAL PAINT FUNCTION
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        List<HexTile> tiles = GameLogicDirector.getInstance().getMap().getVisible();

        if(tiles!=null)
            if(tiles.size()> 0) {
                drawMap(g, tiles);
                System.out.println("drawMap called");
            }

        drawInfo(g);
    }
    // ----------------

    public void drawInfo(Graphics g){
        g.setColor(Color.WHITE);
        g.drawString(currentPlayer.toString(), 10,10);
        g.setColor(Color.RED);
        g.drawString(currentPlayer.enemyPlayer.toString(),10,30);
    }

    public void drawMap(Graphics g, List<HexTile> tiles) {
        g.clearRect(0,0,10000,10000);
        Random r = new Random();
        //Color c = new Color(Color.black);
        g.setColor(Color.black);
        g.fillRect(0,0,10000,10000);

        HexTile start = tiles.get(0);

        Point pixels = new HexCoordinate(start.getLocation()).getPixelCoordinate().getPoint();//law of demeter violation

        int dx=500;
        int dy=500;


        Point p;
        for (HexTile t : tiles) {
                p = new HexCoordinate(t.getLocation()).getPixelCoordinate().getPoint(); //law of demeter violation
                TileView tileView = t.getTileView();
                g.drawImage(tileView.getImage(), p.x + dx, p.y + dy, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, null);
        }
    }






}
