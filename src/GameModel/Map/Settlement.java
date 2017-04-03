package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;

import java.util.ArrayList;

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
        return getAdjacentTerrainTiles();

    }
    public ArrayList<TerrainTile> getLegalTigerTiles()
    {
        ArrayList<TerrainTile> getTigerPlaceMent = getAdjacentTerrainTiles();
        ArrayList<TerrainTile> returnMe = new ArrayList<TerrainTile>();
        for(TerrainTile t: getTigerPlaceMent){
            if(t.getLevel() >= 3){
                returnMe.add(t);
            }
        }
        return returnMe;
    }

    public void createSettlement(TerrainTile starttile){settlement.add(starttile);}
    public void addToSettlement(TerrainTile tile){settlement.add(tile);}

    public ArrayList<TerrainTile> getSettlement()
    {
        return settlement;
    }
    public boolean isContiguous(TerrainTile tile) {
        for (TerrainTile contiguousTile : settlement) {
            if (AxialCoordinate.areAdjacent(contiguousTile.getLocation(), tile.getLocation()))
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
            if (!exsettle.contains(adjacentHexTile) && isContiguous(adjacentHexTile) && adjacentHexTile.terrainType() == terrainType && adjacentHexTile.getMeepleCount() == 0 && !adjacentHexTile.hasTotoro()) {
                exsettle.add(adjacentHexTile);
                fill(adjacentHexTile, terrainType);
            }
        }
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
}
