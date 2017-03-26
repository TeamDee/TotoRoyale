package GameView.Tileables;

import GameView.ImagePaths;
import GameView.Map.Constants;
import GameView.Map.TileableView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import GameModel.Map.Tile.HexTile;

/**
 * Created by jowens on 3/26/17.
 */
public class MeepleView implements TileableView{
    public int numMeeples;
    public BufferedImage myImage;
    public BufferedImage singleMeeple;

    int width = Constants.TILE_WIDTH;
    int height = Constants.TILE_HEIGHT;
    public MeepleView(int numMeeples) {
        try {
            myImage = new BufferedImage(Constants.TILE_WIDTH, Constants.TILE_HEIGHT,BufferedImage.TYPE_INT_ARGB);
            singleMeeple = ImageIO.read(new File(ImagePaths.MEEPLE_WHITE));
            fragment(numMeeples);
        }
        catch (IOException e) {
            System.out.println("Meeple File not found");
        }
    }


    public int getPriority(){
        return 0;
    }
//    @Override
//    protected void makeNewImage(){
//        try {
//            myImage = ImageIO.read(new File(ImagePaths.JUNGLE_TERRAIN));
//
//        }
//        catch (IOException ioe){
//            System.out.println("derp2");
//        }
//    }

    private void fragment(int numMeeples){
        Graphics g = myImage.getGraphics();
        switch (numMeeples){
            case 0:
                break;
            case 1:
                g.drawImage(singleMeeple, width/4, height/4,
                        Constants.TILE_WIDTH*3/4, Constants.TILE_HEIGHT*3/4, null);
            case 2:
                g.drawImage(singleMeeple, width/4, height/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*3/4, height*3/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
            case 3:
                g.drawImage(singleMeeple, width/4, height/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*3/4, height*3/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width/4, height*3/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
            case 4:
                g.drawImage(singleMeeple, width/4, height/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*3/4, height*3/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width/4, height*3/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*3/4, height/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);

        }

    }

    @Override
    public BufferedImage getImage(){
        return myImage;
    }
}
