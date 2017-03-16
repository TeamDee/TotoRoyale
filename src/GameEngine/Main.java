package GameEngine;

import GameModel.Map.GameMap;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.TriHexTile;

import javax.swing.SwingUtilities;

import GameModel.Map.Tile.*;
import GameModel.Map.VolcanoTile;

public class Main {
    public static void main(String[] args) {
        //Main game = new Main();
        //game.RunGame();
        GameMap map = new GameMap();
        TriHexTile tht = new TriHexTile();
        tht.setTileOne(new Grass());
        tht.setTileTwo(new VolcanoTile());
        tht.setTileThree(new Rock());

        map.placeFirstTile(tht);
        map.printInfoAboutMap();
    }

    public void RunGame() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                createAndShowGui();
            }
        });
    }

    public static void createAndShowGui() {
        // ViewFrame vf = ViewFrame.getInstance();
        // vf.initialize();
        GameController gameController = GameController.getInstance();
        // gameController.swapViews(new GameController());
    }
}
