package GameModel.Map.Contiguous;

import GameModel.Map.Tile.TerrainTile;

import java.util.ArrayList;

/**
 * Created by conor on 4/8/2017.
 */
public class SettlementExpansion {
    private ArrayList<TerrainTile> tiles;
    private int value;
    private Settlement settlement;
    private int meepleCost;

    public SettlementExpansion(ArrayList<TerrainTile> tiles, int value, Settlement settlement) {
        this.tiles = tiles;
        this.value = value;
        this.settlement = settlement;
        meepleCost = 0;
        if (tiles != null) {
            for (TerrainTile tt : tiles) {
                meepleCost += tt.getLevel();
            }
        }
    }


    public ArrayList<TerrainTile> getTiles() {
        return tiles;
    }

    public int getValue() {
        return value;
    }

    public Settlement getSettlement () {
        return settlement;
    }

    public int getMeepleCost() {
        return meepleCost;
    }
}
