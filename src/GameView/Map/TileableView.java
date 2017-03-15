package GameView.Map;

import java.awt.image.BufferedImage;

//Tileables are objects that can be placed on tiles
public interface TileableView {
    int getPriority();
    BufferedImage getImage();
}