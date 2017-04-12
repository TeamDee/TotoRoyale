package GameModel.Map.Tile;
import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import GameModel.Map.Contiguous.Settlement;
import GameModel.Map.Direction;
import GameModel.Map.TriHexTile;
import GameView.Map.TerrainView;
import GameView.Tileables.MeepleView;
import GameView.Tileables.TigerView;
import GameView.Tileables.TotoroView;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static GameModel.Map.Tile.TerrainType.VOLCANO;

/**
 * Terrain tiles can hold meeples and totoros.
 * Created by jowens on 3/8/17.
 */


public abstract class TerrainTile extends HexTile {

    protected TerrainView myView;
    protected TerrainTile me;

    public TerrainTile(TriHexTile compositor){

        this.triHexTile = compositor;
    }
    public TerrainTile(){

        //System.out.println("warning: building a terrainTile without supplying a compositor");
    }

    public void placeMeeple(Player p){
        owner = p;
        meepleCount = getLevel();
        myView.addToList(new MeepleView(getLevel(), p));
        myView.visit(this);
    }

    public boolean placeTotoro(Player owner) {
        if (isOccupied()) {
            return false;
        }
        else {
            this.owner = owner;
            hasTotoro = true;
            myView.addToList(new TotoroView(owner));
            return true;
        }
    }

    public boolean placeTiger(Player owner) {
        if (isOccupied()) {
            return false;
        }
        else {
            this.owner = owner;
            hasTiger = true;
            myView.addToList(new TigerView(owner));
            return true;
        }
    }



    public boolean ofSameType(VolcanoTile vt){
        return false;
    }
    public boolean ofSameType(TerrainTile tt){
        return myType == tt.getTerrain();
    }

    public TerrainType getTerrain(){
        return myType;
    }

    //this is the OOPy way of determining terrain type, imo
    public boolean isGrass(){
        return false;
    }
    public boolean isLake(){
        return false;
    }
    public boolean isRock(){
        return false;
    }
    public boolean isJungle(){
        return false;
    }

    @Override
    public TerrainView getTileView(){
        return myView;
    }

    public int getNumberOfFriendlyNeighbors() {
        int numberOfFriendlyNeighbors = 0;
        if (!isOccupied()) {
            return 0;
        }
        for (Direction d : Direction.values()) {
            if (hasNeighborInDirection(d)) {
                if (getNeighborInDirection(d).terrainType() != VOLCANO) {
                    TerrainTile neighbor = (TerrainTile) getNeighborInDirection(d);
                    if (neighbor.isOccupied() && neighbor.getOwner() == getOwner()) {
                        ++numberOfFriendlyNeighbors;
                    }
                }
            }
        }
        return numberOfFriendlyNeighbors;
    }

    public ArrayList<TerrainTile> getEmptyAdjacentTerrainTiles() {
        ArrayList<TerrainTile> emptyAdjacentTerrainTiles = new ArrayList<TerrainTile>();
        for (Direction d: Direction.values()) {
            if (hasNeighborInDirection(d)) {
                if (getNeighborInDirection(d).terrainType() != VOLCANO) {
                    TerrainTile adjacentTerrainTile = (TerrainTile) getNeighborInDirection(d);
                    if (!adjacentTerrainTile.isOccupied()) {
                        emptyAdjacentTerrainTiles.add(adjacentTerrainTile);
                    }
                }
            }
        }
        return emptyAdjacentTerrainTiles;
    }

    public ArrayList<TerrainTile> getFriendlyAdjacentTerrainTiles() {
        ArrayList<TerrainTile> friendlyAdjacentTerrainTiles = new ArrayList<TerrainTile>();
        for (Direction d: Direction.values()) {
            if (hasNeighborInDirection(d)) {
                if (getNeighborInDirection(d).terrainType() != VOLCANO) {
                    TerrainTile adjacentTerrainTile = (TerrainTile) getNeighborInDirection(d);
                    if (adjacentTerrainTile.isOccupied() && adjacentTerrainTile.getOwner() == owner) {
                        friendlyAdjacentTerrainTiles.add(adjacentTerrainTile);
                    }
                }
            }
        }
        return friendlyAdjacentTerrainTiles;
    }

    public boolean isOwnedBy(Player p) {
        return isOccupied() && owner == p;
    }
}

