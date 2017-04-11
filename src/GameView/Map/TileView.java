package GameView.Map;

/**
 * Created by jowens on 3/13/17.
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.List;

import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.Tile.Tile;
import GameView.Tileables.TigerView;
import GameView.Tileables.TileableView;
import GameView.Viewports.Viewport;
import com.sun.scenario.effect.Offset;

import javax.swing.*;
import java.awt.*;

public abstract class TileView{
    private int age;
    private boolean hasBeenSeen;
    protected ArrayList<TileableView> tileableViews;

    protected Tile myTile;

    BufferedImage myImage;
    Image drawMe;

    private JPanel myPanel;

    private Viewport myViewport;

    public TileView(Tile mine) {
        age = 0;
        hasBeenSeen = false;
        tileableViews = new ArrayList<TileableView>();
        myTile = mine;

    }

    public void setViewport(Viewport viewport){
        this.myViewport = viewport;
    }

    public boolean hasViewport(){
        return myViewport != null;
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


    protected Image makeNewImage(Point location){
        Image combined = new BufferedImage(Constants.TILE_WIDTH, Constants.TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();

        //terrain
        g.drawImage(myImage, 0,0, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, null);

        //tileables
        for(TileableView tv: tileableViews){

            g.drawImage(tv.getImage(), 0, 0, Constants.TILE_WIDTH, Constants.TILE_HEIGHT, myPanel);
            tv.setLocation(location);
            //tv.drawToGraphics(g, myViewport);
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

        if (Constants.SHOW_COORDINATES) {
            OffsetCoordinate offsetCoordinate = myTile.getLocation();
            String coordinates = offsetCoordinate.x + ", " + offsetCoordinate.y;
            int coordinatesWidth = g.getFontMetrics().stringWidth(coordinates);
            g.drawString(coordinates, (Constants.TILE_WIDTH - coordinatesWidth) / 2, Constants.TILE_HEIGHT * 9 / 10);
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

    public Image getImage(JPanel myPanel){
        this.myPanel = myPanel;
        if(drawMe ==null)
            makeNewImage(new Point(myTile.getLocation().x, myTile.getLocation().y));
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

    public void drawSelf(JPanel panel, Graphics g, int x, int y){
        g.drawImage(getImage(panel),x,y,panel);
        for(TileableView tv: tileableViews){
            g.drawImage(tv.getImage(),x,y,panel);
        }
    }
}
