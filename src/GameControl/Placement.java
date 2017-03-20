package GameControl;
import GameModel.Map.BoardSpace;
import GameModel.Map.TriHexTile;
import GameModel.Map.Tile.HexTile;

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

        if(oldBS1.hasTile()) {
            value += numberOfEnemyMeeplesKilled(oldBS1.topTile(), oldBS2.topTile(), oldBS3.topTile());
        /*
            TODO other ideas:
                breaking 5+meeple chain so we can place another totoro
                increase value based on level (but only if we can expand into it)
                allows us to place totoro
         */
        }
    }

    public boolean tilesAreOfProperType(){
//        if(newA.ofSameType(oldA.topTile()) && newB.ofSameType(oldB.topTile()) && newC.ofSameType(oldC.topTile())){
//            return true;
//        }
        return false;
    }

    //returns true iff each of the three boardspaces are currently on the same level
    public boolean isLevelPlacement(){
        return !(oldA.getLevel() != oldB.getLevel() || oldB.getLevel() != oldC.getLevel());
    }


    public int numberOfEnemyMeeplesKilled(HexTile oldHex1,HexTile oldHex2,HexTile oldHex3){
        return oldHex1.getMeepleCount() + oldHex2.getMeepleCount() + oldHex3.getMeepleCount();
    }

    //returns true iff the THT can be legally placed
    public boolean isLegal(){
        if(oldA.isActive() || oldB.isActive() || oldC.isActive())
            return isLevelPlacement(); // TODO currently ignores boardspace legality
        else
            return false;
    }

    /*
     * executes the placement onto the gameBoard, does not check to see if the placement is legal
     */
    //TODO do individual hextiles need to reference each other? Or is getBoardSpace().getNorth.getTopTile() sufficient?
    public void place(){
        oldA.addTile(newA);
        oldB.addTile(newB);
        oldC.addTile(newC);
    }

}
