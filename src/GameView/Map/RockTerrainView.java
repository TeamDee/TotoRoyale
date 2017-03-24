package GameView.Map;

import GameView.ImagePaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jowens on 3/23/17.
 */
public class RockTerrainView extends HexTileView{
    BufferedImage image;

    public RockTerrainView() {
        try {
            myImage = ImageIO.read(new File(ImagePaths.ROCK_TERRAIN));
        }
        catch (IOException e) {
            System.out.println("Jungle File not found");
        }
    }
    @Override
    protected void makeNewImage(){
        try {
            myImage = ImageIO.read(new File(ImagePaths.ROCK_TERRAIN));

        }
        catch (IOException ioe){
            System.out.println("derp2");
        }
    }

    @Override
    public BufferedImage getImage() { return myImage; }
}
