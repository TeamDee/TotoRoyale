package GameModel.Map;

import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.Tile.HexTile;

import java.util.ArrayList;

/**
 * Created by jowens on 3/10/17.
 */
public class BoardSpace {
    private boolean hasTile = false;
    private ArrayList<HexTile> tiles;
    private boolean isActive = false; //Whether you can add hextiles to this boardspace yet

    private OffsetCoordinate location;

    private BoardSpace north, northeast, southeast, south, southwest, northwest;
    GameMap map;
    public BoardSpace(OffsetCoordinate location, GameMap containingMap){
        this.hasTile = false;
        isActive = false;
        tiles = new ArrayList<HexTile>();
        this.location = location;
        containingMap.connectAdjacentBoardSpaces(this);
        map = containingMap;
    }

    public int getLevel(){
            return tiles.size();
    }

    public HexTile topTile(){
        if(hasTile)
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
            tiles = new ArrayList<HexTile>();
            hasTile = true;
        }

        tiles.add(ht);
        ht.setMyBoardSpace(this);
        ht.setLevel(tiles.size());

        if(north!=null)
            if (north.hasTile()) {
                ht.setNorth(north.topTile());
                north.topTile().setSouth(ht);
            }

        if(northeast != null)
            if (northeast.hasTile()) {
                ht.setNorthEast(northeast.topTile());
                northeast.topTile().setSouthWest(ht);
            }

        if(southeast != null)
            if (southeast.hasTile()) {
                ht.setSouthEast(southeast.topTile());
                southeast.topTile().setNorthWest(ht);
            }

        if(south != null)
            if (south.hasTile()) {
                ht.setSouth(south.topTile());
                south.topTile().setNorth(ht);
            }

        if(southwest!=null)
            if (southwest.hasTile()) {
                ht.setSouthWest(southwest.topTile());
                southwest.topTile().setNorthEast(ht);
            }

        if(northwest != null)
            if (northwest.hasTile()) {
                ht.setNorthWest(northwest.topTile());
                northwest.topTile().setSouthEast(ht);
            }
    }

    public boolean isActive(){
        return isActive;
    }

    public void activate(){
        isActive = true;
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

    public OffsetCoordinate getLocation(){
        return location;
    }
    public void setLocation(OffsetCoordinate ac){
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
    /*
    public BoardSpace getNorth(){
        if(north == null){
            //System.out.println("Warning: returning a null reference to a boardspace");
        }
        return north;
    }
    public BoardSpace getNorthEast(){
        if(northeast == null){
            //System.out.println("Warning: returning a null reference to a boardspace");
        }
        return northeast;
    }
    public BoardSpace getNorthWest(){
        if(northwest == null){
            //System.out.println("Warning: returning a null reference to a boardspace");
        }
        return northwest;
    }
    public BoardSpace getSouth(){
        if(south == null){
            //System.out.println("Warning: returning a null reference to a boardspace");
        }
        return south;}
    public BoardSpace getSouthEast(){
        if(southeast == null){
            //System.out.println("Warning: returning a null reference to a boardspace");
        }
        return southeast;
    }
    public BoardSpace getSouthWest(){
        if(southwest == null){
            //System.out.println("Warning: returning a null reference to a boardspace");
        }
        return southwest;
    }
    */

    public BoardSpace getNorth() {
        BoardSpace north = map.getBoardSpaceAt(location.getNorth());
        return north;
    }

    public BoardSpace getNorthEast() {
        BoardSpace northeast = map.getBoardSpaceAt(location.getNorthEast());
        return northeast;
    }

    public BoardSpace getSouthEast() {
        BoardSpace southeast = map.getBoardSpaceAt(location.getSouthEast());
        return southeast;
    }

    public BoardSpace getSouth() {
        BoardSpace south = map.getBoardSpaceAt(location.getSouth());
        return south;
    }

    public BoardSpace getSouthWest() {
        BoardSpace southwest = map.getBoardSpaceAt(location.getSouthWest());
        return southwest;
    }

    public BoardSpace getNorthWest() {
        BoardSpace northwest = map.getBoardSpaceAt(location.getNorthWest());
        return northwest;
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

    public boolean hasEmptyAdjacentLevel3(){
        ArrayList<BoardSpace> neighbors = new ArrayList<BoardSpace>();
        neighbors.add(getNorth());
        neighbors.add(getNorthEast());
        neighbors.add(getNorthWest());
        neighbors.add(getSouth());
        neighbors.add(getSouthEast());
        neighbors.add(getSouthWest());

        for(BoardSpace b: neighbors){
            if(b.getLevel() >=3 && !b.topTile().isOccupied()){ //if it is level 3 and is not occupied
             return true;
            }
        }
        return false;
    }

    public void removeTopTile(){
        tiles.remove(tiles.size() - 1);
    }

    public String toString() {
        if (hasTile()) {
            return topTile().toString();
        }
        else {
            return getLocation().toString() + "\tlevel: 0\tBoardspace";
        }
    }

    public void cleanUp(){
        for(HexTile ht: tiles){
            ht.cleanUp();
        }
        tiles = null;
        location =null;
        north = null;
        south =null;
        northeast = null;
        northwest = null;
        southeast = null;
        southwest = null;
    }
}
