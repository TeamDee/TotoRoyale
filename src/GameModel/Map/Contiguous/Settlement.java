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
    private Player owner = null;
    public ArrayList<TerrainTile> settlement = new ArrayList<TerrainTile>();
    private boolean hasTotoro = false;
    private boolean hasTiger = false;

    public Settlement(Player owner) {
        this.owner = owner;
    }

    public Settlement(Player owner, TerrainTile startTile) {
        this.owner = owner;
        settlement.add(startTile);
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

    public ArrayList<TerrainTile> getEmptyAdjacentTerrainTiles() {
        ArrayList<TerrainTile> emptyAdjacentTerrainTiles = new ArrayList<TerrainTile>();
        for (TerrainTile tt: settlement) {
            ArrayList<TerrainTile> emptyAdjacentTerrainTilesAtCurrentTile = tt.getEmptyAdjacentTerrainTiles();
            emptyAdjacentTerrainTiles.addAll(emptyAdjacentTerrainTilesAtCurrentTile);
        }
        return emptyAdjacentTerrainTiles;
    }

    public ArrayList<TerrainTile> getLegalTotoroTiles() {
        if(getSize() <5) {
            return new ArrayList<TerrainTile>();
        }
        ArrayList<TerrainTile> legalTotoroPlacements = getEmptyAdjacentTerrainTiles();
        return legalTotoroPlacements;
    }

    public ArrayList<TerrainTile> getLegalTigerTiles() {
        ArrayList<TerrainTile> emptyAdjacentTerrainTiles = getEmptyAdjacentTerrainTiles();
        ArrayList<TerrainTile> legalTigerPlacements = new ArrayList<TerrainTile>();
        for(TerrainTile tt: emptyAdjacentTerrainTiles){
            if(tt.getLevel() >= 3) {
                legalTigerPlacements.add(tt);
            }
        }
        return legalTigerPlacements;
    }

    public void addToSettlement(TerrainTile tile) {
        if(settlement.contains(tile))
            return;
        settlement.add(tile);
        tile.isPartOfSettlement = true;
        for (TerrainTile settlementTile : settlement) {
            settlementTile.settlementSize = settlement.size();
        }
    }

    public ArrayList<TerrainTile> getTiles() {
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
        boolean isAdjacentToEmptyLevel3Tile = false;
        boolean isAdjacentToRaisableLevel2Tile = false;
        boolean hasTotoroAfterMerge = hasTotoro();
        boolean hasTigerAfterMerge = hasTiger();
        int finalSettlementSize = getSize();
        if (terrainType == VOLCANO) {
            return new SettlementExpansion(expansionTiles, this, TerrainType.VOLCANO, adjacentFriendlySettlementsAfterExpansion, false, false, false);
        }
        for (TerrainTile tt : settlement) {
            tilesToVisit.push(tt);
        }
        TerrainTile currentTile;
        while (!tilesToVisit.isEmpty()) {
            currentTile = tilesToVisit.pop();
            for (Direction d : Direction.values()) {
                if (currentTile.hasNeighborInDirection(d)) {
                    if (currentTile.getNeighborInDirection(d) instanceof TerrainTile) {
                        TerrainTile neighbor = (TerrainTile) currentTile.getNeighborInDirection(d);
                        if (!neighbor.isOccupied()) {
                            if (neighbor.terrainType() == terrainType) {
                                if (!settlement.contains(neighbor) && !expansionTiles.contains(neighbor)) {
                                    tilesToVisit.push(neighbor);
                                    expansionTiles.add(neighbor);
                                    finalSettlementSize++;
                                }
                            } else if (neighbor.getLevel() >= 3) {
                                isAdjacentToEmptyLevel3Tile = true;
                            } else if (neighbor.getLevel() == 2) {
                                if (neighbor.canPlaceTileOn()) {
                                    isAdjacentToRaisableLevel2Tile = true;
                                }
                            }
                        } else if (!settlement.contains(neighbor) && currentTile.getOwner() == neighbor.getOwner()) {
                            Player owner = currentTile.getOwner();
                            Settlement adjacentFriendlySettlementAfterExpansion = owner.getSettlementContaining(neighbor);
                            adjacentFriendlySettlementsAfterExpansion.add(adjacentFriendlySettlementAfterExpansion);
                            finalSettlementSize += adjacentFriendlySettlementAfterExpansion.getSize();
                            if (adjacentFriendlySettlementAfterExpansion.hasTotoro()) {
                                hasTotoroAfterMerge = true;
                            }
                            if (adjacentFriendlySettlementAfterExpansion.hasTiger()) {
                                hasTigerAfterMerge = true;
                            }
                        }
                    }
                }
            }
        }
        return new SettlementExpansion(expansionTiles, this, terrainType, adjacentFriendlySettlementsAfterExpansion,
                finalSettlementSize >= 5 && !hasTotoroAfterMerge, isAdjacentToEmptyLevel3Tile && !hasTigerAfterMerge,
                !hasTigerAfterMerge && isAdjacentToRaisableLevel2Tile);
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

    public int getSize()
    {
        return settlement.size();
    }

    public ArrayList<Settlement> getSplitSettlementsAfterNuke(ArrayList<TerrainTile> nukedTiles) {
        ArrayList<TerrainTile> ungroupedTiles = new ArrayList<TerrainTile>(settlement);
        for (TerrainTile nukedTile : nukedTiles) {
            ungroupedTiles.remove(nukedTile);
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
            if (tt.hasTotoro()) {
                splitSettlement.placedTotoro();
            }
            if (tt.hasTiger()) {
                splitSettlement.placedTiger();
            }
        }
        return splitSettlement;
    }

    public boolean contains(TerrainTile tt) {
        return settlement.contains(tt);
    }

    public Player getOwner() {
        return owner;
    }

    public void checkSettlementsLegality() {
        checkForNonAdjacentTiles();
        checkForRepeatedTiles();
        checkForNotTopLevelTiles();
    }

    public void removeNonTopLevelTiles(){
        for(int i=0; i!=settlement.size();++i){
            for(TerrainTile t: this.settlement){
                if(t.getLevel()!=t.getBoardSpace().topTile().getLevel()){
                    settlement.remove(t);
                }
            }
        }
    }

    public void checkForNotTopLevelTiles() {
        for(int i=0; i!=settlement.size();++i){
            for(TerrainTile t: this.settlement){
                if(t.getLevel()!=t.getBoardSpace().topTile().getLevel()){
                    System.out.println("TILE NOT AT TOP LEVEL BUT IN SETTLEMENT");
                    removeNonTopLevelTiles();
                }
            }
        }
    }

    public void checkForRepeatedTiles() {
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

    public void checkForNonAdjacentTiles() {
        for(TerrainTile tt: settlement){
            if(isAdjacent(tt) || settlement.size() == 1) {

            }
            else{
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

    private boolean isAdjacent(TerrainTile tt) {
        ArrayList<TerrainTile> adjacentTerrainTiles = tt.getAdjacentTerrainTiles();
        for (TerrainTile adjacentTerrainTile: adjacentTerrainTiles) {
            if (settlement.contains(adjacentTerrainTile)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String returnMe = "";
        for(TerrainTile tt: settlement){
            returnMe += tt + "\n";
        }
        return returnMe;
    }

    public void removeAnyUndergroundTiles() {
        for (TerrainTile tt: settlement) {
            if (tt.getLevel() < tt.getBoardSpace().getLevel()) {
                settlement.remove(tt);
                System.out.println("FOUND AND REMOVED UNDERGROUND TILE " + tt + "\nFROM SETTLEMENT\n" + this);
            }
        }
    }
}
