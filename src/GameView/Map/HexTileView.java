package GameView.Map;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

/**
 * Created by jowens on 3/8/17.
 */
public class HexTileView extends TileView{
    BufferedImage myImage;



    private void makeNewImage(){
        BufferedImage combined = new BufferedImage(Constants.TILE_HEIGHT, Constants.TILE_WIDTH, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();


        //g.drawImage(TerrainView.getImage());

//        TileableView tv2[] = new TileableView[tileableViews.size()];
//        tileableViews.toArray( tv2);


//        for(TileableView tv: tv2){
//            g.drawImage(tv.getImage(), 0, 0,
//                    Constants.TILE_HEIGHT, Constants.TILE_WIDTH, null);
//
//            //g.drawImage(tv.getImage(),0,0,null);
//        }


        myImage = combined;
    }
}
