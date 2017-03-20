package GameModel.Map.Tile;

import GameControl.Player.Player;
import GameModel.Map.BoardSpace;
import GameModel.Map.Coordinates.*;
import GameModel.Map.TriHexTile;
import GameView.Map.HexTileView;
import GameView.Map.TileView;

/**
 * Created by jowens on 3/8/17.
 * current implementation retains old tile underneath new one, it would probably be more efficient to delete it but we can debate that later
 */
public abstract class HexTile extends Tile {
    private HexCoordinate location; //axial coordinates TODO add more coordinate system support

    public TerrainType myType;

    private HexTile north, northEast, southEast, south, southWest, northWest;

    private HexTileView myView = new HexTileView(); //todo check that you can initialize objects outside of constructor

    protected TriHexTile triHexTile;

    private BoardSpace myBoardSpace;

    //private TerrainTile terrainTile = null; //null implies this is an empty board space

    private Player owner = null;
    private int meepleCount;
    private boolean hasTotoro;

    public HexTile(){
        //you shouldn't be calling this in the context of this game, since you need a TriHexTile compositor
    }

    public HexTile(TriHexTile compositor){
        triHexTile = compositor;
    }

    public void placeMeeples(Player player) {
        owner = player;
        meepleCount = level + 1;
    }

    public int numMeeplesOnTile(){
        return meepleCount;
    }
    public boolean hasTotoro() {
        return hasTotoro;
    }


    public TerrainType terrainType(){
        return myType;
    }

    public boolean ofSameType(HexTile hexTile){
        return myType == hexTile.terrainType();
    }

    //not really exposing externals because it's an upward reference to a compositor class imo - Jason
    public TriHexTile getTriHexTile(){
        return triHexTile;
    }

    //places this hexTile above a previously existing HexTile and takes all of its neigbors and assigns itself as a neighbor to them
    public void placeOnHexTile(HexTile old){
        //taking references from replaced tile and
        this.north = old.north;
        north.setSouth(this);

        this.northEast = old.northEast;
        northEast.setSouthWest(this);

        this.southEast = old.southEast;
        southEast.setNorthWest(this);

        this.south = old.south;
        south.setNorth(this);

        this.southWest = old.southWest;
        southWest.setNorthEast(this);

        this.northWest = old.northWest;
        northWest.setSouthEast(this);

        this.level = old.level + 1;
    }

    public int getMeepleCount(){
        return meepleCount;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public String toString(){
        String returnMe = "HexTile";
        return returnMe;
    }


    public HexTile getNorth(){
        return north;
    }
    public HexTile getNorthEast(){
        return northEast;
    }
    public HexTile getNorthWest(){
        return northWest;
    }
    public HexTile getSouth(){
        return south;
    }
    public HexTile getSouthEast(){
        return southEast;
    }
    public HexTile getSouthWest(){
        return southWest;
    }
    //Setting references to adjacent tiles
    public void setNorth(HexTile newNorth){
        this.north = newNorth;
    }
    public void setNorthEast(HexTile newNorthEast){
        this.northEast = newNorthEast;
    }
    public void setSouthEast(HexTile newSouthEast){
        this.southEast = newSouthEast;
    }
    public void setSouth(HexTile newSouth){
        this.south = newSouth;
    }
    public void setSouthWest(HexTile newSouthWest){
        this.southWest = newSouthWest;
    }
    public void setNorthWest(HexTile newNorthWest){
        this.northWest = newNorthWest;
    }

    public BoardSpace getBoardSpace(){
        return myBoardSpace;
    }

    public HexCoordinate getLocation(){
        return location;
    }
    public TileView getTileView(){
        return myView;
    }
}
