package GameEngine;

import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.TriHexTile;

import javax.swing.SwingUtilities;

import GameModel.Map.Tile.*;
import GameModel.Map.Tile.VolcanoTile;

public class Main {
    public static void main(String[] args) {
        GameLogicDirector.getInstance().begin(); //runs 1 game
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
