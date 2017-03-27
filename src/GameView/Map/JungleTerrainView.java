package GameView.Map;

import GameModel.Map.Tile.Jungle;
import GameView.ImagePaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jowens on 3/23/17.
 */
public class JungleTerrainView extends TerrainView{

    public JungleTerrainView(Jungle fever) {
        super(fever);
        try {
            myImage = ImageIO.read(new File(ImagePaths.JUNGLE_TERRAIN));
        }
        catch (IOException e) {
            System.out.println("Jungle File not found");
        }
    }


}
