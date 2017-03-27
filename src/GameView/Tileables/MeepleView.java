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
import GameControl.Player.*;

/**
 * Created by jowens on 3/26/17.
 */
public class MeepleView implements TileableView{
    public int numMeeples;
    public BufferedImage myImage;
    public BufferedImage singleMeeple;

    int width = Constants.TILE_WIDTH;
    int height = Constants.TILE_HEIGHT;
    public MeepleView(int numMeeples, Player player) {
        try {
            myImage = new BufferedImage(Constants.TILE_WIDTH, Constants.TILE_HEIGHT,BufferedImage.TYPE_INT_ARGB);
            if(player.isWhite())
                singleMeeple = ImageIO.read(new File(ImagePaths.MEEPLE_WHITE));
            else
                singleMeeple = ImageIO.read(new File(ImagePaths.MEEPLE_BLACK));
            this.numMeeples = numMeeples;
            fragment(numMeeples);
            System.out.println("Meeple File CREATED");
        }
        catch (IOException e) {

        }
    }


    public int getPriority(){
        return 0;
    }

    private void fragment(int numMeeples){
        Graphics g = myImage.getGraphics();
        System.out.println("Meeple count: " + numMeeples);
        switch (numMeeples){
            case 0:
                break;
            case 1:
                g.drawImage(singleMeeple, width/4, height/4,
                        Constants.TILE_WIDTH/2, Constants.TILE_HEIGHT/2, null);
                break;
            case 2:
                g.drawImage(singleMeeple, width/4, height/4,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width/2, height*3/8,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                break;
            case 3:
                g.drawImage(singleMeeple, width/8, height/8,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*3/8, height*3/8,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*5/8, height*5/8,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                break;
            case 4:
                g.drawImage(singleMeeple, width/8, height/8,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*3/8, height*3/8,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*5/8, height*5/8,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                g.drawImage(singleMeeple, width*5/8, height/8,
                        Constants.TILE_WIDTH/4, Constants.TILE_HEIGHT/4, null);
                break;

        }

    }

    public BufferedImage getImage(){
        return myImage;
    }
}
