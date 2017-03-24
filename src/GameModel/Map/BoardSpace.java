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

    public BoardSpace(AxialCoordinate location){
        this.hasTile = false;
        tiles = new ArrayList<HexTile>();
        this.location = location;
    }

    public int getLevel(){
        if(topTile()==null)
            return 0;
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
        ht.setMyBoardSpace(this);
        //activateAdjacentBoardSpaces(); todo ensure this is being done in gameMap
    }
    public void activateAdjacentBoardSpaces(){
        north.activate(this,Direction.SOUTH);
        northeast.activate(this,Direction.SOUTHWEST);
        northwest.activate(this,Direction.SOUTHEAST);
        south.activate(this,Direction.NORTH);
        southeast.activate(this,Direction.NORTHWEST);
        southwest.activate(this,Direction.NORTHEAST);
    }

    public boolean isActive(){
        return isActive;
    }
    public void activate(BoardSpace callingBS, Direction callingBSDirection){
        Direction.getConverse(callingBSDirection);
        switch(callingBSDirection){
            case NORTH:
                north = callingBS;
            case NORTHEAST:
                northeast = callingBS;
            case NORTHWEST:
                northwest = callingBS;
            case SOUTH:
                south = callingBS;
            case SOUTHEAST:
                southeast = callingBS;
            case SOUTHWEST:
                southwest = callingBS;
        }
        isActive = true;
    }

    public AxialCoordinate getLocation(){
        return location;
    }
    public void setLocation(AxialCoordinate ac){
        this.location = ac;
    }

    public BoardSpace getBoardSpaceAtDirection(Direction d){
        switch(d){
            case NORTH:
                return north;
            case NORTHEAST:
                return northeast;
            case NORTHWEST:
                return northwest;
            case SOUTH:
                return south;
            case SOUTHEAST:
                return southeast;
            case SOUTHWEST:
                return southwest;
        }
        return null;
    }
    public BoardSpace getNorth(){
        if(north == null){
            System.out.println("Warning: returning a null reference to a boardspace");
        }
        return north;
    }
    public BoardSpace getNorthEast(){
        if(northeast == null){
            System.out.println("Warning: returning a null reference to a boardspace");
        }
        return northeast;
    }
    public BoardSpace getNorthWest(){
        if(northwest == null){
            System.out.println("Warning: returning a null reference to a boardspace");
        }
        return northwest;
    }
    public BoardSpace getSouth(){
        if(south == null){
            System.out.println("Warning: returning a null reference to a boardspace");
        }
        return south;}
    public BoardSpace getSouthEast(){
        if(southeast == null){
            System.out.println("Warning: returning a null reference to a boardspace");
        }
        return southeast;
    }
    public BoardSpace getSouthWest(){
        if(southwest == null){
            System.out.println("Warning: returning a null reference to a boardspace");
        }
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
