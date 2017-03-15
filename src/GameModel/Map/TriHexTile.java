package GameModel.Map;

import GameModel.Map.Tile.*;

/**
 * This is the tile that has three hex tiles on it. You may only place one of these at a time and you can't place it on another TriHexTile
 */

public class TriHexTile extends Tile {
    //TODO validate that a, b, and c are properly referencing each other
    public HexTile a, b, c; //note that order is important a->b->c->a is clockwise

    public boolean isLegalPlacement(HexTile oldA, HexTile oldB, HexTile oldC){
        if(oldA.getTriHexTile().areSameTriHexTileAsMe(oldA, oldB, oldC))
            return false;
        if(oldA.getLevel() == oldB.getLevel() && oldB.getLevel() == oldC.getLevel()) { //all on same level
            //TODO validate volcano placement
            return true;
        }
        return false;
    }

    //tests to see if three Hexes are in this TriHexTile
    public boolean areSameTriHexTileAsMe(HexTile first, HexTile second, HexTile third){
        if(first == a || first == b || first ==c){
            if(second == a || second == b || second ==c){
                if(third == a || third == b || third ==c){
                    return true;
                }
            }
        }
        return false;
    }

    public HexTile getTileOne(){
        return a; //exposes internals but arguably necessary
    }
    public HexTile getTileTwo(){
        return b; //exposes internals but arguably necessary
    }
    public HexTile getTileThree(){
        return c; //exposes internals but arguably necessary  TODO attempt to avoid
    }
}
