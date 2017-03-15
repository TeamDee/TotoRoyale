package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.Tile;

import java.util.ArrayList;
import GameModel.Map.Tile.*;

/**
 * Created by jowens on 3/10/17.
 */
public class BoardSpace extends Tile {
    private boolean hasTile = false;
    private ArrayList<HexTile> tiles;
    private ArrayList<BoardSpace> adjacentSpaces;
    private boolean isActive; //Whether the boardspace is playable yet

    private AxialCoordinate location;

    private BoardSpace north, northeast, southeast, south, southwest, northwest;

    public BoardSpace(ArrayList<BoardSpace> adjacentBoardSpaces){
        this.hasTile = false;
       //make sure you call setAdjacentSpaces from somewhere. Currently being done in GameMap as of 3/10/17
    }


    public HexTile topTile(){
        return tiles.get(tiles.size()-1);
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
            activateAdjacentBoardSpaces();
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
    public void setActive(){
        isActive = true;
    }

    //lets map know that all tiles adjacent to this one are active/playable
    private void activateAdjacentBoardSpaces(){
        for(BoardSpace b: adjacentSpaces){
            b.setActive();
        }
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
