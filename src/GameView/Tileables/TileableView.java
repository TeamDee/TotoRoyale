package GameView.Tileables;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//Tileables are objects that can be placed on tiles
public interface TileableView {
    int getPriority();
    Image getImage();
    //void drawToGraphics(Graphics g, JPanel parent);
    void setLocation(Point p);
}