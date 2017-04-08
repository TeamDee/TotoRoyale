
package GameView.Map;
        import GameView.ImagePaths;
        import java.awt.image.BufferedImage;
        import java.awt.image.RescaleOp;
        import java.io.File;
        import java.io.IOException;

        import javax.imageio.ImageIO;
        import javax.swing.*;

        //import view.tools.ImagePaths;
import GameModel.Map.Tile.Grass;
public class GrassTerrainView extends TerrainView {

    public GrassTerrainView(Grass gt){
        super(gt);
        try {
            myImage = ImageIO.read(new File(ImagePaths.GRASS_TERRAIN));
        }
        catch (IOException e) {
            System.out.println("File not found");
        }
    }


}