package GameView.Map;

/**
 * Created by jowens on 3/13/17.
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import GameView.ImagePaths;

public abstract class TileView{
    private int age;
    private boolean hasBeenSeen;
    protected ArrayList<TileableView> tileableViews;


    BufferedImage myImage;
    BufferedImage drawMe;

    public TileView() {
        age = 0;
        hasBeenSeen = false;
        tileableViews = new ArrayList<TileableView>();
    }

    public List<TileableView> getList() {
        return tileableViews;
    }

    public void accept(TileableView tileableView) {
        addToList(tileableView);
    }

    public void remove(TileableView tileableView) {
        tileableViews.remove(tileableView);
    }


    protected BufferedImage makeNewImage(){
        BufferedImage combined = new BufferedImage(Constants.TILE_WIDTH, Constants.TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();

        //terrain
        g.drawImage(getImage(), 0,0, Constants.TILE_HEIGHT, Constants.TILE_WIDTH, null);

        //tileables
        for(TileableView tv: tileableViews){
            g.drawImage(tv.getImage(), 0, 0,
                    Constants.TILE_HEIGHT, Constants.TILE_WIDTH, null);
        }
        drawMe = combined;
        return drawMe;
    }

    public void makeOriginalImage(){
        BufferedImage combined = new BufferedImage(Constants.TILE_WIDTH, Constants.TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();

        //terrain
        g.drawImage(getImage(), 0,0, Constants.TILE_HEIGHT, Constants.TILE_WIDTH, null);

        drawMe = combined;
    }

    public boolean hasBeenSeen(){
        return hasBeenSeen;
    }

    public void incrementAge(){
        age++;
        if(age % 5 == 0){
            darken();
        }

    }

    public void addToList(TileableView tv){
        TileableView current = tv;
        for(int i = 0; i< tileableViews.size(); ++i){
            if(current.getPriority() < tileableViews.get(i).getPriority()){
                TileableView tmp = tileableViews.get(i);
                tileableViews.set(i, current);
                current = tmp;
            }

        }
        tileableViews.add(current);
    }

    public abstract BufferedImage getImage();

    public int getAge(){
        return age;
    }

    private void darken(){
        RescaleOp op = new RescaleOp(0.90f, 0, null);
        myImage = op.filter(myImage, myImage);
    }

    protected void addTileableView(TileableView tv){
        if(tileableViews ==null)
            tileableViews = new ArrayList<TileableView>();
        tileableViews.add(tv);
    }

}
