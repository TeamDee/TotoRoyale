package GameView.Tileables;

import GameView.ImagePaths;
import GameView.Map.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import GameControl.Player.*;

/**
 * Created by jowens on 3/26/17.
 */
public class TotoroView implements TileableView{
    public int numMeeples;
    public BufferedImage myImage;
    public BufferedImage totoro;

    int width = Constants.TILE_WIDTH;
    int height = Constants.TILE_HEIGHT;

    private Point location;

    public TotoroView( Player player) {
        try {
            myImage = new BufferedImage(Constants.TILE_WIDTH, Constants.TILE_HEIGHT,BufferedImage.TYPE_INT_ARGB);
            if(player.isWhite())
                totoro = ImageIO.read(new File(ImagePaths.TOTORO_WHITE));
            else
                totoro = ImageIO.read(new File(ImagePaths.TOTORO_BLACK));
            Graphics graphics = myImage.getGraphics();
            graphics.drawImage(totoro, width/4, height/4,
                    Constants.TILE_WIDTH/2, Constants.TILE_HEIGHT/2, null);
            System.out.println("Totoro File CREATED");
        }
        catch (IOException e) {
            System.out.println("Totoro File NOT FOUND");
        }
    }


    public int getPriority(){
        return 0;
    }

    public BufferedImage getImage(){
        return myImage;
    }

    public void drawToGraphics(Graphics g, JPanel parent) {
        //g.drawImage(getImage())
    }

    public void setLocation(Point p){
        location = p;
    }
}
