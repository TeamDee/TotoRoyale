package GameModel.Map.Contiguous;

import GameControl.Player.Player;
import GameModel.Map.BoardSpace;
import GameModel.Map.Direction;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;

import java.util.ArrayList;
import java.util.Stack;

import static GameModel.Map.Tile.TerrainType.VOLCANO;

/**
 * Created by conor on 3/20/2017.
 */
public class Settlement{
    private Player owner;
    public ArrayList<TerrainTile> settlement;
    private boolean hasTotoro = false;
    private boolean hasTiger = false;

    public Settlement() {
        owner = null;
        settlement = new ArrayList<TerrainTile>();
    }

    public Settlement(Player owner) {
        this.owner = owner;
        settlement = new ArrayList<TerrainTile>();

    }

    public boolean hasTotoro() {
        return hasTotoro;
    }

    public boolean hasTiger() {
        return hasTiger;
    }

    public void placedTotoro() {
        hasTotoro = true;
    }

    public void placedTiger() {
        hasTiger = true;
    }

    public ArrayList<TerrainTile> getAdjacentTerrainTiles() {
        ArrayList<TerrainTile> adjacentTerrainTiles = new ArrayList<TerrainTile>();
        ArrayList<TerrainTile> potentialPlacements;
        for(TerrainTile tt: settlement)
        {
            potentialPlacements = getAdjacentTerrainTiles(tt);
            for(TerrainTile scan: potentialPlacements)
            {
                if(!adjacentTerrainTiles.contains(scan) && !settlement.contains(scan)) {
                    adjacentTerrainTiles.add(scan);
                }
            }
        }
        return adjacentTerrainTiles;
    }

    public ArrayList<TerrainTile> getEmptyAdjacentTerrainTiles() {
        ArrayList<TerrainTile> emptyAdjacentTerrainTiles = new ArrayList<TerrainTile>();
        for (TerrainTile tt: settlement) {
            ArrayList<TerrainTile> emptyAdjacentTerrainTilesAtCurrentTile = tt.getEmptyAdjacentTerrainTiles();
            emptyAdjacentTerrainTiles.addAll(emptyAdjacentTerrainTilesAtCurrentTile);
        }
        return emptyAdjacentTerrainTiles;
    }

    public ArrayList<TerrainTile> getLegalTotoroTiles()
    {
        if(getSize() <5) {
            System.out.println("calling getLegalTotoroTiles when you shouldn't be.");
            return new ArrayList<TerrainTile>();
        }
        ArrayList<TerrainTile> legalTotoroPlacements = getEmptyAdjacentTerrainTiles();
        return legalTotoroPlacements;
    }

    public boolean doubleCheckAdjacent(TerrainTile tt)
    {
        HexTile adjacentTile;
        for(Direction d: Direction.values()) {
            if (tt.hasNeighborInDirection(d)) {
                adjacentTile = tt.getNeighborInDirection(d);
                if (adjacentTile.terrainType() != VOLCANO) {
                    if (settlement.contains((TerrainTile) adjacentTile))
                        return true;
                }
            }
        }
        return false;
    }

    public ArrayList<TerrainTile> getLegalTigerTiles()
    {
        ArrayList<TerrainTile> emptyAdjacentTerrainTiles = getEmptyAdjacentTerrainTiles();
        ArrayList<TerrainTile> legalTigerPlacements = new ArrayList<TerrainTile>();
        for(TerrainTile tt: emptyAdjacentTerrainTiles){
            if(tt.getLevel() >= 3) {
                legalTigerPlacements.add(tt);
            }
        }
        return legalTigerPlacements;
    }

    public void createSettlement(TerrainTile starttile){
        addToSettlement(starttile);
    }

    public void addToSettlement(TerrainTile tile){
        settlement.add(tile);
        tile.isPartOfSettlement = true;
        for (TerrainTile settlementTile : settlement) {
            settlementTile.settlementSize = settlement.size(); //both used for AI purposes
        }
    }

    public void temporarilyAddToSettlement(TerrainTile tt) {
        settlement.add(tt);
    }

    public ArrayList<TerrainTile> getTiles()
    {
        return settlement;
    }

    public ArrayList<TerrainTile> getExpansionTiles(TerrainType terrainType) {
        Stack<TerrainTile> tilesToVisit = new Stack<TerrainTile>();
        ArrayList<TerrainTile> expansionTiles = new ArrayList<TerrainTile>();
        ArrayList<Settlement> adjacentFriendlySettlementsAfterExpansion = new ArrayList<Settlement>();
        if (terrainType == VOLCANO) {
            return expansionTiles;
        }
        for (TerrainTile tt : settlement) {
            tilesToVisit.push(tt);
        }
        TerrainTile currentTile;
        while (!tilesToVisit.isEmpty()) {
            currentTile = tilesToVisit.pop();
            for (Direction d : Direction.values()) {
                if (currentTile.hasNeighborInDirection(d)) {
                    if (currentTile.getNeighborInDirection(d).terrainType() == terrainType) {
                        TerrainTile neighbor = (TerrainTile) currentTile.getNeighborInDirection(d);
                        if (!neighbor.isOccupied()) {
                            if (!settlement.contains(neighbor) && !expansionTiles.contains(neighbor)) {
                                tilesToVisit.push(neighbor);
                                expansionTiles.add(neighbor);
                            }
                        } else if (currentTile.getOwner() == neighbor.getOwner()) {
                            Player owner = currentTile.getOwner();
                            adjacentFriendlySettlementsAfterExpansion.add(owner.getSettlementContaining(neighbor));
                        }
                    }
                }
            }
        }
        return expansionTiles;
    }

    public ArrayList<SettlementExpansion> getAllExpansions() {
        ArrayList<SettlementExpansion> allExpansions = new ArrayList<SettlementExpansion>();
        allExpansions.add(getExpansion(TerrainType.GRASS));
        allExpansions.add(getExpansion(TerrainType.JUNGLE));
        allExpansions.add(getExpansion(TerrainType.ROCK));
        allExpansions.add(getExpansion(TerrainType.LAKE));
        return allExpansions;
    }

    public SettlementExpansion getExpansion(TerrainType terrainType) {
        Stack<TerrainTile> tilesToVisit = new Stack<TerrainTile>();
        ArrayList<TerrainTile> expansionTiles = new ArrayList<TerrainTile>();
        ArrayList<Settlement> adjacentFriendlySettlementsAfterExpansion = new ArrayList<Settlement>();
        if (terrainType == VOLCANO) {
            return new SettlementExpansion(expansionTiles, this, adjacentFriendlySettlementsAfterExpansion);
        }
        for (TerrainTile tt : settlement) {
            tilesToVisit.push(tt);
        }
        TerrainTile currentTile;
        while (!tilesToVisit.isEmpty()) {
            currentTile = tilesToVisit.pop();
            for (Direction d : Direction.values()) {
                if (currentTile.hasNeighborInDirection(d)) {
                    if (currentTile.getNeighborInDirection(d).terrainType() == terrainType) {
                        TerrainTile neighbor = (TerrainTile) currentTile.getNeighborInDirection(d);
                        if (!neighbor.isOccupied()) {
                            if (!settlement.contains(neighbor) && !expansionTiles.contains(neighbor)) {
                                tilesToVisit.push(neighbor);
                                expansionTiles.add(neighbor);
                            }
                        } else if (!settlement.contains(neighbor) && currentTile.getOwner() == neighbor.getOwner()) {
                            Player owner = currentTile.getOwner();
                            adjacentFriendlySettlementsAfterExpansion.add(owner.getSettlementContaining(neighbor));
                        }
                    }
                }
            }
        }
        return new SettlementExpansion(expansionTiles, this, adjacentFriendlySettlementsAfterExpansion);
    }

    public ArrayList<Settlement> combineAdjacentSettlementsForMultTiles(ArrayList<TerrainTile> ExpandedTile, ArrayList<Settlement> PlayerSettlements, Settlement BeingEdit)
    {
        ArrayList<Settlement> ss = PlayerSettlements;
        for(TerrainTile t: ExpandedTile)
        {
            ss = combineAdjacentSettlementsForSingleTile(t,PlayerSettlements,BeingEdit);
        }
        return ss;
    }

    public ArrayList<Settlement> combineAdjacentSettlementsForSingleTile(TerrainTile hexTile, ArrayList<Settlement> PlayerSettlements, Settlement BeingEdit)
    {
        BoardSpace bs = hexTile.getBoardSpace();
        BoardSpace temp = null;
        HexTile check = null;
        ArrayList<Settlement> tempSettlements = PlayerSettlements;
        Settlement tempsettlement = BeingEdit;
        ArrayList<TerrainTile> adjacentHexTiles = new ArrayList<TerrainTile>();
        if (bs.getNorth().getLevel() > 0) {
            temp = bs.getNorth();
            check = temp.topTile();
            for(int i = 0; i < tempSettlements.size();i++)
            {
                Settlement s = tempSettlements.get(i);
                ArrayList<TerrainTile> a = s.getTiles();
                if(a.contains(check) && PlayerSettlements.contains(s)) {
                    if(BeingEdit.hasTotoro() || s.hasTotoro())
                    {
                        BeingEdit.placedTotoro();
                    }
                    if(BeingEdit.hasTiger() || s.hasTiger())
                    {
                        BeingEdit.placedTiger();
                    }
                    PlayerSettlements.remove(s);
                    adjacentHexTiles.addAll(a);
                }
            }
        }
        if (bs.getNorthEast().getLevel() > 0) {
            temp = bs.getNorthEast();
            check = temp.topTile();
            for(int i = 0; i < tempSettlements.size();i++)
            {
                Settlement s = tempSettlements.get(i);
                ArrayList<TerrainTile> a = s.getTiles();
                if(a.contains(check) && PlayerSettlements.contains(s)) {
                    if(BeingEdit.hasTotoro() || s.hasTotoro())
                    {
                        BeingEdit.placedTotoro();
                    }
                    if(BeingEdit.hasTiger() || s.hasTiger())
                    {
                        BeingEdit.placedTiger();
                    }
                    PlayerSettlements.remove(s);
                    adjacentHexTiles.addAll(a);
                }
            }
        }
        if (bs.getSouthEast().getLevel() > 0) {
            temp = bs.getSouthEast();
            check = temp.topTile();
            for(int i = 0; i < tempSettlements.size();i++)
            {
                Settlement s = tempSettlements.get(i);
                ArrayList<TerrainTile> a = s.getTiles();
                if(a.contains(check) && PlayerSettlements.contains(s)) {
                    if(BeingEdit.hasTotoro() || s.hasTotoro())
                    {
                        BeingEdit.placedTotoro();
                    }
                    if(BeingEdit.hasTiger() || s.hasTiger())
                    {
                        BeingEdit.placedTiger();
                    }
                    PlayerSettlements.remove(s);
                    adjacentHexTiles.addAll(a);
                }
            }
        }
        if (bs.getSouth().getLevel() > 0) {
            temp = bs.getSouth();
            check = temp.topTile();
            for(int i = 0; i < tempSettlements.size();i++)
            {
                Settlement s = tempSettlements.get(i);
                ArrayList<TerrainTile> a = s.getTiles();
                if(a.contains(check) && PlayerSettlements.contains(s)) {
                    if(BeingEdit.hasTotoro() || s.hasTotoro())
                    {
                        BeingEdit.placedTotoro();
                    }
                    if(BeingEdit.hasTiger() || s.hasTiger())
                    {
                        BeingEdit.placedTiger();
                    }
                    PlayerSettlements.remove(s);
                    adjacentHexTiles.addAll(a);
                }
            }
        }
        if (bs.getSouthWest().getLevel() > 0) {
            temp = bs.getSouthWest();
            check = temp.topTile();
            for(int i = 0; i < tempSettlements.size();i++)
            {
                Settlement s = tempSettlements.get(i);
                ArrayList<TerrainTile> a = s.getTiles();
                if(a.contains(check) && PlayerSettlements.contains(s)) {
                    if(BeingEdit.hasTotoro() || s.hasTotoro())
                    {
                        BeingEdit.placedTotoro();
                    }
                    if(BeingEdit.hasTiger() || s.hasTiger())
                    {
                        BeingEdit.placedTiger();
                    }
                    PlayerSettlements.remove(s);
                    adjacentHexTiles.addAll(a);
                }
            }
        }
        if (bs.getNorthWest().getLevel() > 0) {
            temp = bs.getNorthWest();
            check = temp.topTile();
            for(int i = 0; i < tempSettlements.size();i++)
            {
                Settlement s = tempSettlements.get(i);
                ArrayList<TerrainTile> a = s.getTiles();
                if(a.contains(check) && PlayerSettlements.contains(s)) {
                    if(BeingEdit.hasTotoro() || s.hasTotoro())
                    {
                        BeingEdit.placedTotoro();
                    }
                    if(BeingEdit.hasTiger() || s.hasTiger())
                    {
                        BeingEdit.placedTiger();
                    }
                    PlayerSettlements.remove(s);
                    adjacentHexTiles.addAll(a);
                }
            }
        }
        for(TerrainTile t: adjacentHexTiles)
        {
            if(!BeingEdit.getTiles().contains(t))
            {
                BeingEdit.getTiles().add(t);
            }
        }
        if(PlayerSettlements.contains(tempsettlement)) {
            PlayerSettlements.remove(tempsettlement);
            System.out.println("Hola como estas");
        }
        PlayerSettlements.add(BeingEdit);
        return PlayerSettlements;
    }

    public ArrayList<TerrainTile> getAdjacentTerrainTiles(TerrainTile hexTile) {
        BoardSpace bs = hexTile.getBoardSpace();
        BoardSpace temp = null;
        TerrainTile tile = null;
        HexTile check = null;
        ArrayList<TerrainTile> adjacentHexTiles = new ArrayList<TerrainTile>();

        for(Direction d: Direction.values()) {
            BoardSpace bs2 = bs.getBoardSpaceAtDirection(d);
            if (bs2.getLevel() > 0) {
                temp = bs2;
                check = temp.topTile();
                if (check.terrainType() != TerrainType.VOLCANO) {
                    tile = (TerrainTile) check;
                    adjacentHexTiles.add(tile);
                }
            }
        }
        return adjacentHexTiles;
    }

    public int getSize()
    {
        return settlement.size();
    }

    public ArrayList<Settlement> getSplitSettlementsAfterNuke(ArrayList<TerrainTile> nukedTiles) {
        ArrayList<TerrainTile> ungroupedTiles = new ArrayList<TerrainTile>(settlement);
        for (TerrainTile tt : nukedTiles) {
            ungroupedTiles.remove(tt);
        }
        ArrayList<Settlement> splitSettlements = new ArrayList<Settlement>();

        while(!ungroupedTiles.isEmpty()) {
            splitSettlements.add(getNextSplitSettlement(ungroupedTiles));
        }
        return splitSettlements;
    }

    public Settlement getNextSplitSettlement(ArrayList<TerrainTile> ungroupedTiles) {
        ArrayList<TerrainTile> splitSettlementTiles = new ArrayList<TerrainTile>();
        Stack<TerrainTile> tilesToVisit = new Stack<TerrainTile>();
        tilesToVisit.push(ungroupedTiles.remove(0));
        TerrainTile currentTile;
        while(!tilesToVisit.isEmpty()) {
            currentTile = tilesToVisit.pop();
            splitSettlementTiles.add(currentTile);
            for(Direction d : Direction.values()) {
                if (currentTile.hasNeighborInDirection(d)) {
                    HexTile adjacentHexTile = currentTile.getNeighborInDirection(d);
                    if (adjacentHexTile instanceof TerrainTile) {
                        TerrainTile adjacentTerrainTile = (TerrainTile) adjacentHexTile;
                        if (ungroupedTiles.contains(adjacentTerrainTile)) {
                            ungroupedTiles.remove(adjacentTerrainTile);
                            tilesToVisit.push(adjacentTerrainTile);
                        }
                    }
                }
            }
        }

        Settlement splitSettlement = new Settlement(owner);
        for (TerrainTile tt : splitSettlementTiles) {
            splitSettlement.addToSettlement(tt);
        }
        return splitSettlement;
    }

    public boolean contains(TerrainTile tt) {
        return settlement.contains(tt);
    }

    public Player getOwner() {
        return settlement.get(0).getOwner();
    }

    public void checkSettlementsLegality(){
        checkForNonAdjacentTiles();
        checkForRepeatedTiles();
        checkForNotTopLevelTiles();
    }

    public void checkForNotTopLevelTiles(){
        for(int i=0; i!=settlement.size();++i){
            for(TerrainTile t: this.settlement){
                if(t.getLevel()!=t.getBoardSpace().topTile().getLevel()){
                    System.out.println("TILE NOT AT TOP LEVEL BUT IN SETTLEMENT");

                    try {
                        Thread.sleep(1000000);
                    }
                    catch(InterruptedException ie){

                    }
                }
            }
        }
    }

    public void checkForRepeatedTiles(){
        for(int i=0; i!=settlement.size();++i){
            for(int j = i+1; j!=settlement.size(); ++j){
                if(settlement.get(i) == settlement.get(j)){
                    System.out.println("REPEATED TILES IN SETTLEMENT");

                    try {
                        Thread.sleep(1000000);
                    }
                    catch(InterruptedException ie){

                    }
                }
            }
        }
    }

    public void checkForNonAdjacentTiles(){
        for(TerrainTile tt: this.settlement){
            if(this.doubleCheckAdjacent(tt) || settlement.size() == 1){

            }
            else{
                //settlement.remove(tt);
                System.out.println("SETTLEMENT EXISTS WITH NONADJACENT TILES");
                System.out.println("SETTLEMENT:\n" + this);
                for (TerrainTile tt1 : settlement) {
                    System.out.println("NEIGHBORS OF " + tt1.toString());
                    for (Direction d : Direction.values()) {
                        System.out.print(d.toString() + ":\t");
                        if (tt1.hasNeighborInDirection(d)) {
                            System.out.print(tt1.getNeighborInDirection(d).toString());
                        }
                        else {
                            System.out.print("NONE");
                        }
                        System.out.print("\n");
                    }
                }
                try {
                    Thread.sleep(1000000);
                }
                catch(InterruptedException ie){

                }
            }
        }
    }

    @Override
    public String toString(){
        String returnMe = "";
        for(TerrainTile tt: settlement){
            returnMe += tt + "\n";
        }
        return returnMe;
    }
}
