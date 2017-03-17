package GameModel.Map;

import GameModel.Map.Tile.*;

/**
 * This is the tile that has three hex tiles on it. You may only place one of these at a time and you can't place it on another TriHexTile
 */

public class TriHexTile extends Tile {
    HexTile tileOne;
    HexTile tileTwo;
    HexTile tileThree;

    //TODO validate that a, b, and c are properly referencing each other

    //A is the "center" tile, b is clockwise to a
    public HexTile a, b, c; //note that order is important a->b->c->a is clockwise
    // public HexTile a, b, c; note that order is important a->b->c->a is clockwise
    public TriHexTile(){

    }
    public TriHexTile(HexTile a, HexTile b, HexTile c){
        HexTile tileOne = a;
        HexTile tileTwo = b;
        HexTile tileThree = c;
    }

    public void exampleMethod() {
        TriHexTile[] array = new TriHexTile[47];
        array[0] = new TriHexTile(new Grass(), new Grass(), new VolcanoTile());
        array[1] = new TriHexTile(new Grass(), new Jungle(), new VolcanoTile());
        array[2] = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        array[3] = new TriHexTile(new Grass(), new Lake(), new VolcanoTile());

        array[4] = new TriHexTile(new Jungle(), new Grass(), new VolcanoTile());
        array[5] = new TriHexTile(new Jungle(), new Jungle(), new VolcanoTile());
        array[6] = new TriHexTile(new Jungle(), new Rock(), new VolcanoTile());
        array[7] = new TriHexTile(new Jungle(), new Lake(), new VolcanoTile());

        array[8] = new TriHexTile(new Rock(), new Grass(), new VolcanoTile());
        array[9] = new TriHexTile(new Rock(), new Jungle(), new VolcanoTile());
        array[10] = new TriHexTile(new Rock(), new Rock(), new VolcanoTile());
        array[11] = new TriHexTile(new Rock(), new Lake(), new VolcanoTile());

        array[12] = new TriHexTile(new Lake(), new Grass(), new VolcanoTile());
        array[13] =new TriHexTile(new Lake(), new Jungle(), new VolcanoTile());
        array[14] = new TriHexTile(new Lake(), new Rock(), new VolcanoTile());
        array[15] =  new TriHexTile(new Lake(), new Lake(), new VolcanoTile());

        array[16] = new TriHexTile(new Grass(), new Grass(), new VolcanoTile());
        array[17] = new TriHexTile(new Grass(), new Jungle(), new VolcanoTile());
        array[18] = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        array[19] = new TriHexTile(new Grass(), new Lake(), new VolcanoTile());

        array[20] = new TriHexTile(new Jungle(), new Grass(), new VolcanoTile());
        array[21] = new TriHexTile(new Jungle(), new Jungle(), new VolcanoTile());
        array[22] = new TriHexTile(new Jungle(), new Rock(), new VolcanoTile());
        array[23] = new TriHexTile(new Jungle(), new Lake(), new VolcanoTile());

        array[24] = new TriHexTile(new Rock(), new Grass(), new VolcanoTile());
        array[25] = new TriHexTile(new Rock(), new Jungle(), new VolcanoTile());
        array[26] = new TriHexTile(new Rock(), new Rock(), new VolcanoTile());
        array[27] = new TriHexTile(new Rock(), new Lake(), new VolcanoTile());

        array[28] = new TriHexTile(new Lake(), new Grass(), new VolcanoTile());
        array[29] =new TriHexTile(new Lake(), new Jungle(), new VolcanoTile());
        array[30] = new TriHexTile(new Lake(), new Rock(), new VolcanoTile());
        array[31] =  new TriHexTile(new Lake(), new Lake(), new VolcanoTile());

        array[32] = new TriHexTile(new Grass(), new Grass(), new VolcanoTile());
        array[33] = new TriHexTile(new Grass(), new Jungle(), new VolcanoTile());
        array[34] = new TriHexTile(new Grass(), new Rock(), new VolcanoTile());
        array[35] = new TriHexTile(new Grass(), new Lake(), new VolcanoTile());

        array[36] = new TriHexTile(new Jungle(), new Grass(), new VolcanoTile());
        array[37] = new TriHexTile(new Jungle(), new Jungle(), new VolcanoTile());
        array[38] = new TriHexTile(new Jungle(), new Rock(), new VolcanoTile());
        array[39] = new TriHexTile(new Jungle(), new Lake(), new VolcanoTile());

        array[40] = new TriHexTile(new Rock(), new Grass(), new VolcanoTile());
        array[41] = new TriHexTile(new Rock(), new Jungle(), new VolcanoTile());
        array[42] = new TriHexTile(new Rock(), new Rock(), new VolcanoTile());
        array[43] = new TriHexTile(new Rock(), new Lake(), new VolcanoTile());

        array[44] = new TriHexTile(new Lake(), new Grass(), new VolcanoTile());
        array[45] =new TriHexTile(new Lake(), new Jungle(), new VolcanoTile());
        array[46] = new TriHexTile(new Lake(), new Rock(), new VolcanoTile());
        array[47] =  new TriHexTile(new Lake(), new Lake(), new VolcanoTile());
    }



    public Direction directionOfBRelativeToA = Direction.NORTH; //default value

    public boolean isLegalPlacement(HexTile oldA, HexTile oldB, HexTile oldC){
//        if(oldA.getTriHexTile().areSameTriHexTileAsMe(oldA, oldB, oldC))
//            return false;
        if(oldA.getLevel() == oldB.getLevel() && oldB.getLevel() == oldC.getLevel()) { //all on same level
            //TODO validate volcano placement
            return true;
        }
        return false;
    }

    //tests to see if three Hexes are in this TriHexTile
    public boolean areSameTriHexTileAsMe(TriHexTile tile, HexTile first, HexTile second, HexTile third){
        if(first == tile.tileOne || first == tile.tileTwo || first == tile.tileThree){
            if(second == tile.tileOne || second == tile.tileTwo || second == tile.tileThree){
                if(third == tile.tileOne || third == tile.tileTwo || third == tile.tileThree){
                    return true;
                }
            }
        }
        return false;
    }


    //rotates b and c about a
    public void rotateClockwise(){
        directionOfBRelativeToA = Direction.getClockwise(directionOfBRelativeToA);

    }

    //these basically make b or c the new a tile (a is the "center" tile, the one being placed on the desired boardspace)
    public void changeCenterTile(){
        HexTile tmp = a;
        a = c;
        c = b;
        b = tmp;
    }
    public void changeCenterTile2(){
        HexTile tmp = a;
        a = b;
        b = c;
        c = tmp;
    }

    public HexTile getTileOne() {
        return a; //exposes internals but arguably necessary
    }

    public HexTile getTileTwo() {
        return b; //exposes internals but arguably necessary
    }

    public HexTile getTileThree() {
        return c; //exposes internals but arguably necessary
    }

    public HexTile getTileOne(TriHexTile tile) {
        return tile.tileOne; //exposes internals but arguably necessary

    }

    public HexTile getTileTwo(TriHexTile tile) {
        return tile.tileTwo; //exposes internals but arguably necessary
    }

    public HexTile getTileThree(TriHexTile tile) {
        return tile.tileThree; //exposes internals but arguably necessary  TODO attempt to avoid
    }

    public void setTileOne(HexTile hexTile) {
        a = hexTile;
    }

    public void setTileTwo(HexTile hexTile) {
        b = hexTile;
    }

    public void setTileThree(HexTile hexTile) {
        c = hexTile;
    }
}