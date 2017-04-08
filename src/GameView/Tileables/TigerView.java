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

    int width = Constants.TILE_WIDTH;
    int height = Constants.TILE_HEIGHT;

    private Locale myLocation;

    public TigerView( Player player) {
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
        }
        catch (IOException e) {
            System.out.println("TigerView File NOT FOUND");
        }
    }


    public int getPriority(){
        return 0;
    }

    public BufferedImage getImage(){
        return myImage;
    }



    public void drawToGraphics(Graphics g, JPanel parent){
        Frame frame = new Frame();

        ImageIcon loading = new ImageIcon(ImagePaths.TIGER_WHITE_G);
        //frame.add(loading);
        frame.add(new JLabel("loading... ", loading, JLabel.CENTER));

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
        frame.setVisible(true);
        frame.setLocation();
        //parent.add(frame);
    }
    public ImageIcon getGif(){
        return null; //todo
    }
}
