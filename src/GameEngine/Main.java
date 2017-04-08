package GameEngine;

import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.TriHexTile;

import javax.swing.SwingUtilities;

import GameModel.Map.Tile.*;
import GameModel.Map.Tile.VolcanoTile;

public class Main {
    public static void main(String[] args) {

//        map.placeFirstTile(tht);
//        map.printInfoAboutMap();

//        OffsetCoordinate oc = new OffsetCoordinate(1,-3);
//        System.out.println(oc);
//        System.out.println(oc.getCubicCoordinate());
//
//        oc = new OffsetCoordinate(2, -4);
//        System.out.println(oc);
//        System.out.println(oc.getCubicCoordinate());
//
//        oc = new OffsetCoordinate(0, 6);
//        System.out.println(oc);
//        System.out.println(oc.getCubicCoordinate());
//
//        oc = new OffsetCoordinate(-1, 5);
//        System.out.println(oc);
//        System.out.println(oc.getCubicCoordinate());

        GameLogicDirector.getInstance().begin();

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
