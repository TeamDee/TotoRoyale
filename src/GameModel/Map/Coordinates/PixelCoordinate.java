package GameModel.Map.Coordinates;

import java.awt.Point;
/**
 * Created by jowens on 3/12/17.
 */
public class PixelCoordinate {
    int x, y;

    public PixelCoordinate(){
        x = 0;
        y = 0;
    }
    public PixelCoordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y=y;
    }


    public Point getPoint(){
        return new Point(x,y);
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public PixelCoordinate meRelativeToNewOrigin(PixelCoordinate origin){
        int tmpX = x - origin.x;
        int tmpY = y - origin.y;
        PixelCoordinate returnThis = new PixelCoordinate(tmpX,tmpY);
        return returnThis;

    }

}
