package GameModel.Map.Contiguous;

import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;

import java.util.ArrayList;

/**
 * Created by conor on 4/8/2017.
 */
public class SettlementExpansion {
    private ArrayList<TerrainTile> tiles;
    private Settlement settlementToExpand;
    private TerrainType terrainToExpand;
    private ArrayList<Settlement> friendlyAdjacentSettlementsAfterExpansion = new ArrayList<Settlement>();
    boolean canPlaceTotoroAfterExpansion;
    boolean canPlaceTigerAfterExpansion;
    boolean canPlaceTigerAfterExpansionAndTilePlacement;
    private int value;
    private int meepleCost;

    public SettlementExpansion(ArrayList<TerrainTile> tiles, Settlement settlementToExpand, TerrainType terrainToExpand, ArrayList<Settlement> friendlyAdjacentSettlementsAfterExpansion,
                               boolean canPlaceTotoroAfterExpansion, boolean canPlaceTigerAfterExpansion,
                               boolean canPlaceTigerAfterExpansionAndTilePlacement) {
        this.tiles = tiles;
        this.settlementToExpand = settlementToExpand;
        this.terrainToExpand = terrainToExpand;
        this.friendlyAdjacentSettlementsAfterExpansion = friendlyAdjacentSettlementsAfterExpansion;
        this.canPlaceTotoroAfterExpansion = canPlaceTotoroAfterExpansion;
        this.canPlaceTigerAfterExpansion = canPlaceTigerAfterExpansion;
        this.canPlaceTigerAfterExpansionAndTilePlacement = canPlaceTigerAfterExpansionAndTilePlacement;
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

    public boolean canPlaceTotoroAfterExpansion() {
        return canPlaceTotoroAfterExpansion;
    }

    public boolean canPlaceTigerAfterExpansion() {
        return canPlaceTigerAfterExpansion;
    }

    public boolean canPlaceTigerAfterExpansionAndTilePlacement() {
        return canPlaceTigerAfterExpansionAndTilePlacement;
    }

    public TerrainType getTerrainToExpand() { return terrainToExpand; }
}
