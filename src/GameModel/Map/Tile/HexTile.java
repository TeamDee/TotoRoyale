package GameModel.Map.Tile;

import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import GameModel.Map.BoardSpace;
import GameModel.Map.Contiguous.Settlement;
import GameModel.Map.Coordinates.*;
import GameModel.Map.Direction;
import GameModel.Map.TriHexTile;
import GameView.Map.HexTileView;
import GameView.Map.TileView;

import java.util.ArrayList;

/**
 * Created by jowens on 3/8/17.
 * current implementation retains old tile underneath new one, it would probably be more efficient to delete it but we can debate that later
 */
public abstract class HexTile extends Tile {
    private HexCoordinate location; //axial coordinates TODO add more coordinate system support

    public TerrainType myType;

    private HexTile north, northEast, southEast, south, southWest, northWest;

    protected HexTileView myView; //todo check that you can initialize objects outside of constructor

    protected TriHexTile triHexTile;

    private BoardSpace myBoardSpace;

    protected int meepleCount = 0;
    protected Player owner = null;
    protected boolean hasTotoro = false;
    protected boolean hasTiger = false;

    public boolean isPartOfSettlement;
    public int settlementSize;


    public HexTile() {
        //you shouldn't be calling this in the context of this game, since you need a TriHexTile compositor
    }

    public HexTile(TriHexTile compositor){
        triHexTile = compositor;
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

    public void setTriHexTile(TriHexTile tht) { this.triHexTile = tht; }


    //places this hexTile above a previously existing HexTile and takes all of its neighbors and assigns itself as a neighbor to them
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

    public boolean isOccupied() {
        return getMeepleCount()>0 || hasTotoro || hasTiger;
    }

    public int getMeepleCount(){
        return meepleCount;
    }


    public boolean isOwnedBy(Player p){
        return p == this.owner;
    }

    public boolean isOwnedByWhite() {
        if (!isOccupied()) {
            return false;
        }
        else {
            return owner instanceof WhitePlayer;
        }
    }

    public boolean isOwnedByBlack() {
        if (!isOccupied()) {
            return false;
        }
        else {
            return owner instanceof BlackPlayer;
        }
    }

    public Player getOwner(){
        return owner;
    }

    public void resetOwner(){
        this.owner = null;
    }

    public boolean hasTotoro() {
        return hasTotoro;
    }
    public boolean hasTiger() {
        return hasTiger;
    }

    @Override
    public String toString(){
        String returnMe = super.toString();
        returnMe += "\tlocation: " + this.getLocation() + "\tlevel: " + getLevel();
        return returnMe;
    }

    public HexTile getNorth(){
        return myBoardSpace.getNorth().topTile();
    }
    public HexTile getNorthEast(){
        return myBoardSpace.getNorthEast().topTile();
    }
    public HexTile getNorthWest(){
        return myBoardSpace.getNorthWest().topTile();
    }
    public HexTile getSouth(){
        return myBoardSpace.getSouth().topTile();
    }
    public HexTile getSouthEast(){
        return myBoardSpace.getSouthEast().topTile();
    }
    public HexTile getSouthWest(){
        return myBoardSpace.getSouthWest().topTile();
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

    public void setMyBoardSpace(BoardSpace bs){myBoardSpace =bs;}
    public BoardSpace getBoardSpace(){
        return myBoardSpace;
    }
    public void setBoardSpace(BoardSpace boardSpace) { this.myBoardSpace = boardSpace; }

    public OffsetCoordinate getLocation(){
        if(myBoardSpace != null)
            return myBoardSpace.getLocation();
        return new OffsetCoordinate(0,0);
    }
    public abstract TileView getTileView();

    public boolean hasNeighborInDirection(Direction d) {
        switch (d) {
            case NORTH      : return getNorth() != null;
            case NORTHEAST  : return getNorthEast() != null;
            case SOUTHEAST  : return getSouthEast() != null;
            case SOUTH      : return getSouth() != null;
            case SOUTHWEST  : return getSouthWest() != null;
            case NORTHWEST  : return getNorthWest() != null;
            default         : return false;
        }
    }

    public HexTile getNeighborInDirection(Direction d) {
        switch (d) {
            case NORTH      : return getNorth();
            case NORTHEAST  : return getNorthEast();
            case SOUTHEAST  : return getSouthEast();
            case SOUTH      : return getSouth();
            case SOUTHWEST  : return getSouthWest();
            case NORTHWEST  : return getNorthWest();
            default         : return null;
        }
    }

    public void nuke() {
        meepleCount = 0;
        hasTotoro = false;
        hasTiger = false;
        isPartOfSettlement = false;
        settlementSize = 0;
    }

    public int getLevel(){
        return this.myBoardSpace.getLevel();
    }

    public boolean hasNeighborBelongingToPlayer(Player player) {
        for (Direction d : Direction.values()) {
            if (hasNeighborInDirection(d)) {
                HexTile neighbor = getNeighborInDirection(d);
                if (neighbor.getOwner() == player)
                    return true;
            }
        }
        return false;
    }

    public boolean canPlaceTileOn() {
        for (Direction d: Direction.values()) {
            if (hasNeighborInDirection(d)) {
                HexTile neighbor = getNeighborInDirection(d);
                if (neighbor.getLevel() == getLevel()) {
                    if (neighbor.terrainType() == TerrainType.VOLCANO) {
                        if (hasNeighborInDirection(Direction.getClockwise(d))) {
                            if (getNeighborInDirection(Direction.getClockwise(d)).getLevel() == getLevel()) {
                                return true;
                            }
                        }
                        if (hasNeighborInDirection(Direction.getCounterClockwise(d))) {
                            if (getNeighborInDirection(Direction.getCounterClockwise(d)).getLevel() == getLevel()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<HexTile> getAdjacentHexTiles() {
        ArrayList<HexTile> adjacentHexTiles = new ArrayList<HexTile>();
        for (Direction d: Direction.values()) {
            if (hasNeighborInDirection(d)) {
                adjacentHexTiles.add(getNeighborInDirection(d));
            }
        }
        return adjacentHexTiles;
    }

    public ArrayList<TerrainTile> getAdjacentTerrainTiles() {
        ArrayList<TerrainTile> adjacentTerrainTiles = new ArrayList<TerrainTile>();
        ArrayList<HexTile> adjacentHexTiles = getAdjacentHexTiles();
        for (HexTile ht: adjacentHexTiles) {
            if (ht.terrainType() != TerrainType.VOLCANO) {
                adjacentTerrainTiles.add((TerrainTile) ht);
            }
        }
        return adjacentTerrainTiles;
    }

    public ArrayList<Settlement> getAdjacentSettlementsOwnedBy(Player player) {
        ArrayList<Settlement> adjacentSettlements = new ArrayList<Settlement>();
        ArrayList<TerrainTile> adjacentTerrainTiles = getAdjacentTerrainTiles();
        for (TerrainTile tt: adjacentTerrainTiles) {
            if (tt.isOwnedBy(player)) {
                adjacentSettlements.add(player.getSettlementContaining(tt));
            }
        }
        return adjacentSettlements;
    }
}
