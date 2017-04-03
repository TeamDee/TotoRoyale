package GameView.Map;

import GameView.ImagePaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import GameModel.Map.Tile.Rock;
/**
 * Created by jowens on 3/23/17.
 */
public class RockTerrainView extends TerrainView{

    public RockTerrainView(Rock rock) {
        super(rock);
        try {
            myImage = ImageIO.read(new File(ImagePaths.ROCK_TERRAIN));
        }
        catch (IOException e) {
            System.out.println("Rock File not found");
        }
    }


}
