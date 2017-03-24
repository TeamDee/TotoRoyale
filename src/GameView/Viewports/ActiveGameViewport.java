package GameView.Viewports;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import GameModel.Map.Coordinates.HexCoordinate;
import GameModel.Map.Coordinates.PixelCoordinate;
import GameModel.Map.Tile.*;

import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import GameEngine.GameController;

import GameEngine.GameLogicDirector;
import GameModel.Map.TriHexTile;
import GameView.*;
import GameView.Map.*;


public class ActiveGameViewport extends Viewport {

    TriHexTile tileToPlace;
    boolean scrolling = false;
    Player currentPlayer;
    Deck deck = GameLogicDirector.getInstance().deck;

    private static ActiveGameViewport activeGameViewport = new ActiveGameViewport(new Player());

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

        @Override
        public void keyTyped(KeyEvent e) {
            //
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == e.VK_P) {
                GameController.getInstance().addToStack(GameController.getInstance().getActiveController());
            }
        }

        @Override
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
            if(tiles.size()> 0)
                drawMap(g, tiles);
    }
    // ----------------

    public void drawMap(Graphics g, List<HexTile> tiles) {
        HexTile start = tiles.get(0);
        //Point pixels = start.getLocation().getPixelCoordinate().getPoint();//law of demeter violation

        int dx = 0;//this.getWidth() / 2 - pixels.x;
        int dy = 0;//this.getHeight() / 2 - pixels.y;

        Point p;
        for (HexTile t : tiles) {
            p = new HexCoordinate(t.getLocation()).getPixelCoordinate().getPoint(); //law of demeter violation
            TileView tileView = t.getTileView();
            if(tileView.hasBeenSeen()){
                g.drawImage(tileView.getImage(), p.x + dx, p.y + dy,
                        Constants.TILE_HEIGHT, Constants.TILE_WIDTH, null);
            }
        }
    }






}
