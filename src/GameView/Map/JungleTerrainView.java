package GameView.Map;

import GameView.ImagePaths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jowens on 3/23/17.
 */
public class JungleTerrainView extends HexTileView{
    BufferedImage image;

    public JungleTerrainView() {
        try {
            image = ImageIO.read(new File(ImagePaths.GRASS_TERRAIN));
        }
        catch (IOException e) {
            System.out.println("Jungle File not found");
        }
    }

    @Override
    public BufferedImage getImage() { return image; }
}
