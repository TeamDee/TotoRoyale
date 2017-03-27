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
import GameModel.Map.Tile.Tile;
public abstract class TileView{
    private int age;
    private boolean hasBeenSeen;
    protected ArrayList<TileableView> tileableViews;

    protected Tile myTile;

    BufferedImage myImage;
    BufferedImage drawMe;


    public TileView(Tile mine) {
        age = 0;
        hasBeenSeen = false;
        tileableViews = new ArrayList<TileableView>();
        myTile = mine;
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
        g.drawImage(myImage, 0,0, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, null);

        //tileables
        for(TileableView tv: tileableViews){
            g.drawImage(tv.getImage(), 0, 0, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, null);
        }

        switch (myTile.getLevel()){
            case 0:
                break;
            case 1:
                break;
            case 2:
                g.drawString("2", Constants.TILE_WIDTH/2,Constants.TILE_HEIGHT/4);
                break;
            case 3:
                g.drawString("3", Constants.TILE_WIDTH/2,Constants.TILE_HEIGHT/4);
                break;
            case 4:
                g.drawString("4", Constants.TILE_WIDTH/2,Constants.TILE_HEIGHT/4);
                break;
        }

        drawMe = combined;

        return drawMe;
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

    public BufferedImage getImage(){
        if(drawMe ==null)
            makeNewImage();
        return drawMe;
    }

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
