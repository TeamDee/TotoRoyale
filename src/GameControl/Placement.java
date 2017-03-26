package GameControl;
import GameModel.Map.BoardSpace;
import GameModel.Map.Tile.TerrainType;
import GameModel.Map.Tile.VolcanoTile;
import GameModel.Map.TriHexTile;
import GameModel.Map.Tile.HexTile;

import java.util.ArrayList;

/**
 * A "Placement" denotes a placement of a TriHexTile and is useful in calculating how useful a move is. Placements aren't always legal.
 */
public class Placement {
    int value = 0; //Used for AI purposes

    TriHexTile toBePlaced;

    //NOTE: newA is placed on oldA, newB is placed on oldB, and newC is placed on oldC
    BoardSpace oldA, oldB, oldC; //lower level
    HexTile newA, newB, newC; //higher level

    /*
     *
     */
    public Placement(BoardSpace oldBS1, BoardSpace oldBS2, BoardSpace oldBS3, HexTile new1, HexTile new2, HexTile new3){

        oldA = oldBS1;
        oldB = oldBS2;
        oldC = oldBS3;

        newA = new1;
        newB = new2;
        newC = new3;

        /*
        if(oldBS1.hasTile()) {
            value += numberOfEnemyMeeplesKilled(oldBS1.topTile(), oldBS2.topTile(), oldBS3.topTile());
        /*
            TODO other ideas:
                breaking 5+meeple chain so we can place another totoro
                increase value based on level (but only if we can expand into it)
                allows us to place totoro

        }
        */
    }

    public boolean isOverlapping(){
        return (oldA.topTile().getTriHexTile() == oldB.topTile().getTriHexTile() && oldA.topTile().getTriHexTile() == oldC.topTile().getTriHexTile());
    }

    public boolean tilesAreOfProperType(){
//        if(newA.ofSameType(oldA.topTile()) && newB.ofSameType(oldB.topTile()) && newC.ofSameType(oldC.topTile())){
//            return true;
//        }
        return false;
    }

    // return newA.terrainType() == TerrainType.VOLCANO || newB.terrainType() == TerrainType.VOLCANO || newC.terrainType() == TerrainType.VOLCANO
    public boolean volcanoMatch(){
        if(oldA.topTile().terrainType() == TerrainType.VOLCANO)
            return newA.terrainType() == TerrainType.VOLCANO;
        else if (oldB.topTile().terrainType() == TerrainType.VOLCANO)
            return newB.terrainType() == TerrainType.VOLCANO;
        else
            return newC.terrainType() == TerrainType.VOLCANO; // works if there's at least one volcano
    }

    //returns true iff each of the three board spaces are currently on the same level
    public boolean isLevelPlacement(){
        return !(oldA.getLevel() != oldB.getLevel() || oldB.getLevel() != oldC.getLevel());
    }

    public int numberOfEnemyMeeplesKilled(HexTile oldHex1,HexTile oldHex2,HexTile oldHex3){
        return oldHex1.getMeepleCount() + oldHex2.getMeepleCount() + oldHex3.getMeepleCount();
    }

    /*
     * executes the placement onto the gameBoard, does not check to see if the placement is legal
     */
    
    // Added it here to test the legal check , same as code in GameMap: 417
    public boolean isLegalPlacement(Placement p) {
        if(p.isLevelPlacement() && p.isOverlapping() && !p.volcanoMatch())
            return true;
        else
            return false;
    }

    //TODO do individual hextiles need to reference each other? Or is getBoardSpace().getNorth.getTopTile() sufficient?
    public void place(){
        oldA.addTile(newA);
        oldB.addTile(newB);
        oldC.addTile(newC);
    }

    public ArrayList<BoardSpace> getBoardSpaces(){
        ArrayList<BoardSpace> list = new ArrayList<BoardSpace>();
        list.add(oldA);
        list.add(oldB);
        list.add(oldC);
        return list;
    }


}