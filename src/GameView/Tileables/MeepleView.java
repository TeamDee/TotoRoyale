package GameView.Tileables;

import GameView.ImagePaths;
import GameView.Map.TileableView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jowens on 3/26/17.
 */
public class MeepleView implements TileableView{
    public int numMeeples;
    public BufferedImage myImage;
    public MeepleView(HexTile ht) {
        try {
            myImage = ImageIO.read(new File(ImagePaths.JUNGLE_TERRAIN));
        }
        catch (IOException e) {
            System.out.println("Jungle File not found");
        }
    }


    public int getPriority(){
        return 0;
    }
    @Override
    protected void makeNewImage(){
        try {
            myImage = ImageIO.read(new File(ImagePaths.JUNGLE_TERRAIN));

        }
        catch (IOException ioe){
            System.out.println("derp2");
        }
    }

    @Override
    public BufferedImage getImage() { return myImage; }
}
