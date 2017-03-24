package GameView.Map;

import GameView.ImagePaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jowens on 3/23/17.
 */
public class VolcanoView extends HexTileView{
    BufferedImage image;

    public VolcanoView() {
        try {
            myImage = ImageIO.read(new File(ImagePaths.VOLCANO_TERRAIN));
        }
        catch (IOException e) {
            System.out.println("Volcano File not found");
        }
    }
    @Override
    protected void makeNewImage(){
        try {
            myImage = ImageIO.read(new File(ImagePaths.VOLCANO_TERRAIN));

        }
        catch (IOException ioe){
            System.out.println("derp2");
        }
    }

    @Override
    public BufferedImage getImage() { return myImage; }
}
