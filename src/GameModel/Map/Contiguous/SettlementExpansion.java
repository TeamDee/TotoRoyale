package GameModel.Map.Contiguous;

import GameModel.Map.Tile.TerrainTile;

import java.util.ArrayList;

/**
 * Created by conor on 4/8/2017.
 */
public class SettlementExpansion {
    private ArrayList<TerrainTile> tiles;
    private Settlement settlement;
    private int value;
    private int meepleCost;

    public SettlementExpansion(ArrayList<TerrainTile> tiles, Settlement settlement) {
        this.tiles = tiles;
        this.settlement = settlement;
        value = 0;
        meepleCost = 0;
        if (tiles != null) {
            for (TerrainTile tt : tiles) {
                meepleCost += tt.getLevel();
            }
        }
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public ArrayList<TerrainTile> getTiles() {
        return tiles;
    }

    public Settlement getSettlement () {
        return settlement;
    }

    public int getMeepleCost() {
        return meepleCost;
    }
}
