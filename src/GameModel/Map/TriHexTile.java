package GameModel.Map;

import GameModel.Map.Tile.*;

/**
 * This is the tile that has three hex tiles on it. You may only place one of these at a time and you can't place it on another TriHexTile
 */

public class TriHexTile extends Tile {
    private HexTile tileOne;
    private HexTile tileTwo;
    private VolcanoTile tileThree; //generally the volcano

    //TODO validate that a, b, and c are properly referencing each other

    // public HexTile a, b, c; note that order is important a->b->c->a is clockwise
    public TriHexTile(){

    }

    public TriHexTile(HexTile a, HexTile b, VolcanoTile c){
        tileOne = a;
        tileTwo = b;
        tileThree = c;
        tileOne.setTriHexTile(this);
        tileTwo.setTriHexTile(this);
        tileThree.setTriHexTile(this);
    }

    public Direction directionOfBRelativeToA = Direction.NORTH; //default value

    public boolean isLegalPlacement(HexTile oldA, HexTile oldB, HexTile oldC){
        if(oldA.getLevel() == oldB.getLevel() && oldB.getLevel() == oldC.getLevel()) { //all on same level
            if(oldC.toString().compareTo(tileThree.toString())==0)
                return true;
        }
        return false;
    }

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

    public HexTile getTileOne() {
        return tileOne; //exposes internals but arguably necessary
    }

    public HexTile getTileTwo() {
        return tileTwo; //exposes internals but arguably necessary
    }

    public VolcanoTile getTileThree() {
        return tileThree; //exposes internals but arguably necessary
    }

    public void setTileOne(HexTile hexTile) {
        tileOne = hexTile;
    }

    public void setTileTwo(HexTile hexTile) {
        tileTwo = hexTile;
    }

    public void setTileThree(VolcanoTile hexTile) {
        tileThree = hexTile;
    }
}
