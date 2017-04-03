package GameView.Map;

import GameView.ImagePaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import GameModel.Map.Tile.Lake;
/**
 * Created by jowens on 3/23/17.
 */
public class WaterTerrainView extends TerrainView{
    public WaterTerrainView(Lake waterTerrain) {
        super(waterTerrain);
        try {
            myImage = ImageIO.read(new File(ImagePaths.WATER_TERRAIN));
        }
        catch (IOException e) {
            System.out.println("Lake File not found");
        }
    }

}
