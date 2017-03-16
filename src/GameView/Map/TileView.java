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


public class TileView{
    private int age;
    private boolean hasBeenSeen;
    private ArrayList<TileableView> tileableViews;



    BufferedImage myImage;

    public TileView() {
        age = 0;
        hasBeenSeen = false;
        //tileableViews = new ArrayList<>();
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


    private void makeNewImage(){
        BufferedImage combined = new BufferedImage(Constants.TILE_HEIGHT, Constants.TILE_WIDTH, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();

        TileableView tv2[] = new TileableView[tileableViews.size()];
        tileableViews.toArray( tv2);


        for(TileableView tv: tv2){
            g.drawImage(tv.getImage(), 0, 0,
                    Constants.TILE_HEIGHT, Constants.TILE_WIDTH, null);
        }


        myImage = combined;
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
        return myImage;
    }
    public int getAge(){
        return age;
    }

    private void darken(){
        RescaleOp op = new RescaleOp(0.90f, 0, null);
        myImage = op.filter(myImage, myImage);
    }



}
