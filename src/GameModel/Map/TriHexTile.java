package GameModel.Map;

import GameModel.Map.Tile.*;

/**
 * This is the tile that has three hex tiles on it. You may only place one of these at a time and you can't place it on another TriHexTile
 */

public class TriHexTile extends Tile {
    private TerrainTile tileOne;
    private TerrainTile tileTwo;
    private VolcanoTile tileThree; //generally the volcano

    //TODO validate that a, b, and c are properly referencing each other

    // public HexTile a, b, c; note that order is important a->b->c->a is clockwise
    public TriHexTile(){
        System.out.println("Warning: using incorrect TriHexTile constructor");
    }

    public TriHexTile(TerrainTile a, TerrainTile b, VolcanoTile c){
        tileOne = a;
        tileTwo = b;
        tileThree = c;
        tileOne.setTriHexTile(this);
        tileTwo.setTriHexTile(this);
        tileThree.setTriHexTile(this);
    }

    public Direction directionOfBRelativeToA = Direction.NORTH; //default value

    //TODO I don't think this currently supports tile rotation (i.e. a rotated THT won't appear to be the same as the unrotated one)
    //tests to see if three Hexes are in this TriHexTile
    public static boolean areSameTriHexTileAsMe(TriHexTile tile, HexTile first, HexTile second, HexTile third){
        if(first == tile.tileOne || first == tile.tileTwo || first == tile.tileThree){
            if(second == tile.tileOne || second == tile.tileTwo || second == tile.tileThree){
                if(third == tile.tileOne || third == tile.tileTwo || third == tile.tileThree){
                    return true;
                }
            }
        }
        return false;
    }

    public TerrainTile getTileOne() {
        return tileOne; //exposes internals but arguably necessary
    }

    public TerrainTile getTileTwo() {
        return tileTwo; //exposes internals but arguably necessary
    }

    public VolcanoTile getTileThree() {
        return tileThree; //exposes internals but arguably necessary
    }

    public TerrainTile getClockwiseNonVolcanoTile() {
        return tileOne;
    }

    public TerrainTile getCounterClockwiseNonVolcanoTile() {
        return tileTwo;
    }

    public VolcanoTile getVolcanoTile() {
        return tileThree;
    }
    public void setTileOne(TerrainTile hexTile) {
        tileOne = hexTile;
    }

    public void setTileTwo(TerrainTile hexTile) {
        tileTwo = hexTile;
    }

    public void setTileThree(VolcanoTile hexTile) {
        tileThree = hexTile;
    }

    public boolean isTheSameAs(TriHexTile other){
        if(this.tileOne.ofSameType(other.getTileOne())){
            if(this.tileTwo.ofSameType(other.getTileTwo())){
                if(this.tileThree.ofSameType(other.getTileThree()))
                    return true;
            }
        }
        return false;
    }
}
