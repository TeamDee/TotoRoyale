package GameView.Tileables;

import GameControl.Player.Player;
import GameView.ImagePaths;
import GameView.Map.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.swing.*;

/**
 * Created by jowens on 4/3/17.
 */
public class TigerView implements TileableView{
    public BufferedImage myImage;
    public BufferedImage tiger;

    Player player;
    public Image TigerGif;
    int width = Constants.TILE_WIDTH;
    int height = Constants.TILE_HEIGHT;

    private Point myLocation;

    public TigerView( Player player) {
        this.player = player;
        try {
            myImage = new BufferedImage(Constants.TILE_WIDTH, Constants.TILE_HEIGHT,BufferedImage.TYPE_INT_ARGB);
            if(player.isWhite())
                tiger = ImageIO.read(new File(ImagePaths.TIGER_WHITE));
            else
                tiger = ImageIO.read(new File(ImagePaths.TIGER_BLACK));
            Graphics graphics = myImage.getGraphics();
            graphics.drawImage(tiger,  0, 0,
                    Constants.TILE_WIDTH, Constants.TILE_HEIGHT, null);
            System.out.println("TigerView File CREATED");
            makeGif();
        }
        catch (IOException e) {
            System.out.println("TigerView File NOT FOUND");
        }
    }


    public int getPriority(){
        return 0;
    }

    public Image getImage(){
        return TigerGif;
    }

    public void makeGif(){
        if(player.isWhite())
            TigerGif = Toolkit.getDefaultToolkit().createImage(ImagePaths.TIGER_WHITE_G);
        else
            TigerGif = Toolkit.getDefaultToolkit().createImage(ImagePaths.TIGER_BLACK_G);
        TigerGif = TigerGif.getScaledInstance(Constants.TILE_WIDTH,Constants.TILE_HEIGHT, 5);
    }

    public void setLocation(Point p){
        this.myLocation = p;
    }

    public ImageIcon getGif(){
        return null; //todo
    }
}
