package GameModel.Map;

import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;



import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static GameModel.Map.Tile.TerrainType.VOLCANO;

/**
 * Created by conor on 3/20/2017.
 */
public class Settlement{
    public ArrayList<TerrainTile> settlement;
    public ArrayList<TerrainTile> exsettle;
    public Settlement() {

        settlement = new ArrayList<TerrainTile>();
        exsettle = new ArrayList<TerrainTile>();
    }

    public void createSettlement(TerrainTile starttile){settlement.add(starttile);}
    public void adToSettlement(TerrainTile tile){settlement.add(tile);}

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
        ArrayList<TerrainTile> adjacentHexTiles = getAdjacentHexTiles(hexTile);
        for (TerrainTile adjacentHexTile : adjacentHexTiles) {
            if (!exsettle.contains(adjacentHexTile) && isContiguous(adjacentHexTile) && adjacentHexTile.terrainType() == terrainType && adjacentHexTile.getMeepleCount() == 0 && !adjacentHexTile.hasTotoro()) {
                exsettle.add(adjacentHexTile);
                fill(adjacentHexTile, terrainType);
            }
        }
    }

    public ArrayList<TerrainTile> getAdjacentHexTiles(TerrainTile hexTile) {
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
}
