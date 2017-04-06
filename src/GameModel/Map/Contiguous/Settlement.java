package GameModel.Map.Contiguous;

import GameModel.Map.BoardSpace;
import GameModel.Map.Coordinates.OffsetCoordinate;
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
    public ArrayList<TerrainTile> settlement;
    public ArrayList<TerrainTile> exsettle;
    //public ArrayList<TerrainTile> Tplacement;
    public boolean HasTotoro;
    public boolean HasTiger;
    public Settlement() {
        settlement = new ArrayList<TerrainTile>();
        exsettle = new ArrayList<TerrainTile>();
        HasTotoro = false;
        HasTiger = false;
    }

    public boolean hasTotoro()
    {
        return HasTotoro;
    }
    public boolean hasTiger()
    {
        return HasTiger;
    }
    public void placedTotoro()
    {
        HasTotoro = true;
    }
    public void placedTiger()
    {
        HasTiger = true;
    }

    public ArrayList<TerrainTile> getAdjacentTerrainTiles(){
        ArrayList<TerrainTile> adjacentTerrainTiles = new ArrayList<TerrainTile>();
        ArrayList<TerrainTile> potentialPlacements = new ArrayList<TerrainTile>();
        for(TerrainTile check: settlement)
        {
            potentialPlacements = getAdjacentTerrainTiles(check);
            for(TerrainTile scan: potentialPlacements)
            {
                if(!adjacentTerrainTiles.contains(scan)) {
                    adjacentTerrainTiles.add(scan);
                }
            }
        }
        return adjacentTerrainTiles;
    }
    public ArrayList<TerrainTile> getLegalTotoroTiles()
    {
        if(this.getSettlementSize() <5) {
            System.out.println("calling getLegalTotoroTiles when you shouldn't be.");
            return null;
        }
        ArrayList<TerrainTile> getTotoroPlaceMent = getAdjacentTerrainTiles();
        ArrayList<TerrainTile> returnMe = new ArrayList<TerrainTile>();
        for(TerrainTile t: getTotoroPlaceMent){
            if(!t.isOccupied()){
                returnMe.add(t);
            }
        }
        return returnMe;

    }
    public ArrayList<TerrainTile> getLegalTigerTiles()
    {
        ArrayList<TerrainTile> getTigerPlaceMent = getAdjacentTerrainTiles();
        ArrayList<TerrainTile> returnMe = new ArrayList<TerrainTile>();
        for(TerrainTile t: getTigerPlaceMent){
            if(t.getLevel() >= 3 && !t.isOccupied()){
                returnMe.add(t);
            }
        }
        return returnMe;
    }

    public void createSettlement(TerrainTile starttile){settlement.add(starttile);}
    public void addToSettlement(TerrainTile tile){
        settlement.add(tile);
        tile.getBoardSpace().topTile().isPartOfSettlement = true;
        tile.getBoardSpace().topTile().settlementSize = settlement.size(); //both used for AI purposes
    }

    public ArrayList<TerrainTile> getSettlement()
    {
        return settlement;
    }
    public boolean isContiguous(TerrainTile tile) {
        for (TerrainTile contiguousTile : settlement) {
            if (OffsetCoordinate.areAdjacent(contiguousTile.getLocation(), tile.getLocation()))
                return true;
        }
        return false;
    }
    public ArrayList<TerrainTile> getExpansionTiles(ArrayList<TerrainTile> ExpandSettlement,TerrainType terrainType)
    {
        ArrayList<TerrainTile> expand = new ArrayList<TerrainTile>();
        for(TerrainTile check: ExpandSettlement)
        {
            fill(check, terrainType);
            for(TerrainTile scan: exsettle)
            {
                if(!expand.contains(scan)) {
                    expand.add(scan);
                }
            }
        }
        exsettle.clear();
        return expand;
    }

    public void fill(TerrainTile hexTile, TerrainType terrainType) {
        ArrayList<TerrainTile> adjacentHexTiles = getAdjacentTerrainTiles(hexTile);
        for (TerrainTile adjacentHexTile : adjacentHexTiles) {
            if (!exsettle.contains(adjacentHexTile) && isContiguous(adjacentHexTile) && adjacentHexTile.terrainType() == terrainType && !adjacentHexTile.isOccupied()) {
                exsettle.add(adjacentHexTile);
                fill(adjacentHexTile, terrainType);
            }
        }
    }
    public ArrayList<Settlement> combineAdacentSettlementsforMultTiles(ArrayList<TerrainTile> ExpandedTile, ArrayList<Settlement> PlayerSettlements, Settlement BeingEdit)
    {
        ArrayList<Settlement> ss = PlayerSettlements;
        for(TerrainTile t: ExpandedTile)
        {
            ss = combineAdjacentSettlementsforSingleTile(t,PlayerSettlements,BeingEdit);
        }
        return ss;
    }

    public ArrayList<Settlement> combineAdjacentSettlementsforSingleTile(TerrainTile hexTile, ArrayList<Settlement> PlayerSettlements, Settlement BeingEdit)
    {
        BoardSpace bs = hexTile.getBoardSpace();
        BoardSpace temp = null;
        HexTile check = null;
        ArrayList<Settlement> tempSettlements = PlayerSettlements;
        ArrayList<TerrainTile> adjacentHexTiles = new ArrayList<TerrainTile>();
        if (bs.getNorth().getLevel() > 0) {
            temp = bs.getNorth();
            check = temp.topTile();
            for(int i = 0; i < tempSettlements.size();i++)
            {
                Settlement s = tempSettlements.get(i);
                ArrayList<TerrainTile> a = s.getSettlement();
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
                ArrayList<TerrainTile> a = s.getSettlement();
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
                ArrayList<TerrainTile> a = s.getSettlement();
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
                ArrayList<TerrainTile> a = s.getSettlement();
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
                ArrayList<TerrainTile> a = s.getSettlement();
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
                ArrayList<TerrainTile> a = s.getSettlement();
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
        BeingEdit.getSettlement().addAll(adjacentHexTiles);
        return PlayerSettlements;
    }

    public ArrayList<TerrainTile> getAdjacentTerrainTiles(TerrainTile hexTile) {
        BoardSpace bs = hexTile.getBoardSpace();
        BoardSpace temp = null;
        TerrainTile tile = null;
        HexTile check = null;
        ArrayList<TerrainTile> adjacentHexTiles = new ArrayList<TerrainTile>();
        if (bs.getNorth().getLevel() > 0) {
            temp = bs.getNorth();
            check = temp.topTile();
            if (check.terrainType() != TerrainType.VOLCANO)
            {
                tile = (TerrainTile)check;
                adjacentHexTiles.add(tile);
            }
        }
        if (bs.getNorthEast().getLevel() > 0) {
            temp = bs.getNorthEast();
            check = temp.topTile();
            if (check.terrainType() != VOLCANO) {
                tile = (TerrainTile)check;
                adjacentHexTiles.add(tile);
            }
        }
        if (bs.getSouthEast().getLevel() > 0) {
            temp = bs.getSouthEast();
            check = temp.topTile();
            if(check.terrainType() != VOLCANO) {
                //System.out.println("Test3");
                tile = (TerrainTile)check;
                adjacentHexTiles.add(tile);
            }
        }
        if (bs.getSouth().getLevel() > 0) {
            temp = bs.getSouth();
            check = temp.topTile();
            if(check.terrainType() != VOLCANO) {
                //System.out.println("Test4");
                tile = (TerrainTile)check;
                adjacentHexTiles.add(tile);
            }
        }
        if (bs.getSouthWest().getLevel() > 0) {
            temp = bs.getSouthWest();
            check = temp.topTile();
            if (check.terrainType() != VOLCANO) {
                //System.out.println("Test5");
                tile = (TerrainTile)check;
                adjacentHexTiles.add(tile);
            }
        }
        if (bs.getNorthWest().getLevel() > 0) {
            temp = bs.getNorthWest();
            check = temp.topTile();
            if(check.terrainType() != VOLCANO) {
                //System.out.println("Test6");
                tile = (TerrainTile)check;
                adjacentHexTiles.add(tile);
            }
        }
        return adjacentHexTiles;
    }

    public int getSettlementSize()
    {
        return settlement.size();
    }

    public ArrayList<Settlement> getSplitSettlementsAfterNuke(TerrainTile nukedTile) {
        ArrayList<TerrainTile> ungroupedTiles = (ArrayList<TerrainTile>) settlement.clone();
        ungroupedTiles.remove(nukedTile);
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

        Settlement splitSettlement = new Settlement();
        for (TerrainTile tt : splitSettlementTiles) {
            splitSettlement.addToSettlement(tt);
        }
        return splitSettlement;
    }

    public boolean contains(TerrainTile tt) {
        return settlement.contains(tt);
    }
}
