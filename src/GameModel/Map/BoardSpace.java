package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.Tile;

import java.util.ArrayList;

import GameModel.Map.Tile.*;

/**
 * Created by jowens on 3/10/17.
 */
public class BoardSpace {
    private boolean hasTile = false;
    private ArrayList<HexTile> tiles;
    private boolean isActive; //Whether you can add hextiles to this boardspace yet

    private AxialCoordinate location;

    private BoardSpace north, northeast, southeast, south, southwest, northwest;

    public BoardSpace(){
        this.hasTile = false;
        tiles = new ArrayList<HexTile>();
    }

    public int getLevel(){
        return topTile().getLevel();
    }

    public HexTile topTile(){
        if(tiles.size()>0)
            return tiles.get(tiles.size()-1);
        else
            return null;
    }
    public boolean hasTile(){
        return hasTile;
    }
    public boolean isEmpty(){
        return !hasTile;
    }
    //adding a tile to a board space activates it and all adjacent tiles
    public void addTile(HexTile ht){
        if(!hasTile){
            //activateAdjacentBoardSpaces(); TODO
        }
        hasTile = true;
        if(tiles == null){
            tiles = new ArrayList<HexTile>();
        }
        tiles.add(ht);
    }

    public boolean isActive(){
        return isActive;
    }
    public void activate(BoardSpace callingBS, Direction callingBSDirection){
        Direction.getConverse(callingBSDirection);
        isActive = true;
    }

    public AxialCoordinate getLocation(){
        return location;
    }
    public void setLocation(AxialCoordinate ac){
        this.location = ac;
    }

    public BoardSpace getNorth(){
        return north;
    }
    public BoardSpace getNorthEast(){
        return northeast;
    }
    public BoardSpace getNorthWest(){
        return northwest;
    }
    public BoardSpace getSouth(){return south;}
    public BoardSpace getSouthEast(){
        return southeast;
    }
    public BoardSpace getSouthWest(){
        return southwest;
    }

    public void setNorth(BoardSpace bs){
        north = bs;
    }
    public void setNorthEast(BoardSpace bs){
        northeast = bs;
    }
    public void setNorthWest(BoardSpace bs){
        northwest = bs;
    }
    public void setSouth(BoardSpace bs){
        south = bs;
    }
    public void setSouthEast(BoardSpace bs){
        southeast = bs;
    }
    public void setSouthWest(BoardSpace bs){
        southwest = bs;
    }
}
