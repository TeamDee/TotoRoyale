package GameModel.Map.Contiguous;

import GameModel.Map.Tile.TerrainTile;

import java.util.ArrayList;

/**
 * Created by conor on 4/8/2017.
 */
public class SettlementExpansion {
    private ArrayList<TerrainTile> tiles;
    private Settlement settlementToExpand;
    private ArrayList<Settlement> friendlyAdjacentSettlementsAfterExpansion = new ArrayList<Settlement>();
    private int value;
    private int meepleCost;

    public SettlementExpansion(ArrayList<TerrainTile> tiles, Settlement settlementToExpand, ArrayList<Settlement> friendlyAdjacentSettlementsAfterExpansion) {
        this.tiles = tiles;
        this.settlementToExpand = settlementToExpand;
        this.friendlyAdjacentSettlementsAfterExpansion = friendlyAdjacentSettlementsAfterExpansion;
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

    public ArrayList<Settlement> getFriendlyAdjacentSettlementsAfterExpansion() {
        return friendlyAdjacentSettlementsAfterExpansion;
    }

    public Settlement getSettlementToExpand() {
        return settlementToExpand;
    }

    public int getMeepleCost() {
        return meepleCost;
    }
}
