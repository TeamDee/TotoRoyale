package GameControl.Player;

import GameControl.Placement;
import GameEngine.GameLogicDirector;
import GameModel.Map.BoardSpace;
import GameModel.Map.Contiguous.Settlement;
import GameModel.Map.Contiguous.SettlementExpansion;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.Direction;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;
import GameModel.Map.TriHexTile;
import GameView.Map.Constants;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jowens on 3/8/17.
 */
public class Player {

    public GameLogicDirector myLogicDirector = null;
    String name;
    private int totoroCount;
    private int meepleCount;
    private int tigerCount;
    private String buildMessage;
    private ArrayList<Settlement> settlements;
    private Settlement activeSettlement; //settlement we're adding stuff too
    public Player enemyPlayer;

    private GameMap myMap;

    //scoring
    private int score;
    public boolean placeTileCheck = false; //added for testing

    //random
    Random random = new Random();
    public Player(GameMap thisPlayersMap, Player enemy){
        totoroCount = Constants.TOTORO_PER_PLAYER;
        meepleCount = Constants.MEEPLES_PER_PLAYER;
        tigerCount = Constants.TIGER_PER_PLAYER;
        score = 0;
        settlements = new ArrayList<Settlement>();
        myMap = thisPlayersMap;
        enemyPlayer = enemy;
    }

    public boolean noLegalBuildMoves(GameMap map){
        // no settlements, legal totoro, or expansions
        return !legalNewSettlementVisible(map) && !legalTotoroPlacementVisible() && !legalExpansionVisible();
    }

    private void executeExpansion(SettlementExpansion expansion) {
        Settlement settlementToExpand = expansion.getSettlementToExpand();
        ArrayList<TerrainTile> expansionTiles = expansion.getTiles();
        ArrayList<Settlement> friendlyAdjacentSettlementsAfterExpansion = expansion.getFriendlyAdjacentSettlementsAfterExpansion();
        for (TerrainTile expansionTile: expansionTiles) {
            placeMeeples(expansionTile);
            settlementToExpand.addToSettlement(expansionTile);
        }
        for (Settlement settlementToMergeWith: friendlyAdjacentSettlementsAfterExpansion) {
            ArrayList<TerrainTile> tilesToAdd = settlementToMergeWith.getTiles();
            for (TerrainTile tt: tilesToAdd) {
                settlementToExpand.addToSettlement(tt);
            }
            if (settlementToMergeWith.hasTotoro()) {
                settlementToExpand.placedTotoro();
            }
            if (settlementToMergeWith.hasTiger()) {
                settlementToExpand.placedTiger();
            }
            settlements.remove(friendlyAdjacentSettlementsAfterExpansion);
        }
        System.out.println("Expansion added to settlment\n" + settlementToExpand);
    }

    private boolean legalExpansionVisible(){
        if(settlements.size() == 0){
            return false;
        }
        return true;
    }

    //todo test this
    private boolean legalTotoroPlacementVisible(){
        for(Settlement s: settlements){
            if(this.canPlaceTotoro(s)){
                return true;
            }
        }
        return false;
    }

    private boolean legalNewSettlementVisible(GameMap map){
        ArrayList<HexTile> topLevel = map.getVisible();

        //
        for(HexTile ht: topLevel){
            if(ht.isOccupied())
                continue;
            else if (ht.terrainType() == TerrainType.VOLCANO)
                continue;
            return true;
        }
        return false;
    }

    public String placementPhase(GameMap gameMap, TriHexTile tile){
        ArrayList<Placement> placements = gameMap.getLegalMapPlacements(tile);
        ArrayList<Placement> placementsThatFixesBuild = new ArrayList<Placement>();
        Placement mapPlacementThatFixesNoBuildOptionsProblem = null;
        if(noLegalBuildMoves(gameMap)){
            //todo
            //check to see if our "map" placement will leave us with a legal build move so we don't forfeit
            for(Placement p: placements){
                myMap.temporaryPlacement(p);
                if(noLegalBuildMoves(gameMap)){
                    //not helpful move, ignore
                }
                else{
                    mapPlacementThatFixesNoBuildOptionsProblem = p;
                    placementsThatFixesBuild.add(p);
                }
                myMap.revokeLastPlacement();
            }
            if(mapPlacementThatFixesNoBuildOptionsProblem == null){
                placements = new ArrayList<Placement>();
            }
        }

        if(placements.size() == 0) {   //todo OR the seen placements aren't good enough
            placements.addAll(gameMap.getLegalTablePlacements(tile)); //note this only gets level 0 placements
            System.out.println("NO LEGAL LEVEL >1 PLACEMENTS");
        }
        //placementAI
        Placement stupidPlacement = placementAI(placements);
        //Placement stupidPlacement = placements.get(random.nextInt(placements.size())); //todo iterate through placements for the best option

        OffsetCoordinate volcanoLocation = stupidPlacement.getVolcanoLocation();

//        if(mapPlacementThatFixesNoBuildOptionsProblem != null){
//            placeTile(gameMap, mapPlacementThatFixesNoBuildOptionsProblem);
//        } else {
//            placeTile(gameMap, stupidPlacement);
//        }
        placeTile(gameMap, stupidPlacement);
        return volcanoLocation.getCubicCoordinate().toString() + " " + stupidPlacement.getOrientation();
    }

    public void placeOpponent(TriHexTile tht, OffsetCoordinate location, int orientation){
        BoardSpace toBePlacedOn = myMap.getBoardSpaceAt(location);
        BoardSpace north = myMap.getBoardSpaceAt(location.getNorth());
        BoardSpace northeast = myMap.getBoardSpaceAt(location.getNorthEast());
        BoardSpace northwest = myMap.getBoardSpaceAt(location.getNorthWest());
        BoardSpace south = myMap.getBoardSpaceAt(location.getSouth());
        BoardSpace southeast = myMap.getBoardSpaceAt(location.getSouthEast());
        BoardSpace southwest = myMap.getBoardSpaceAt(location.getSouthWest());
        HexTile tileOne = tht.getTileOne();
        HexTile tileTwo = tht.getTileTwo();
        HexTile tileThree = tht.getTileThree();
        BoardSpace bs1 = null, bs2 = null;
        switch (orientation){
            case 1:
                bs1 = north;  bs2 = northeast;
                break;
            case 2:
                bs1 = northeast; bs2 = southeast;
                break;
            case 3:
                bs1 = southeast; bs2 = south;
                break;
            case 4:
                bs1 = south; bs2 = southwest;
                break;
            case 5:
                bs1 = southwest; bs2 = northwest;
                break;
            case 6:
                bs1 = northwest; bs2 = north;
                break;
        }
        Placement placement = new Placement(bs1, bs2, toBePlacedOn, tileOne, tileTwo, tileThree, orientation);
        myMap.implementPlacement(placement);
        System.out.print("-------------PLACED OPPONENT--------------");
    }

    public void opponentNewSettlement(OffsetCoordinate location){
        TerrainTile toBeFoundSettlement = (TerrainTile) myMap.getBoardSpaceAt(location).topTile();
        buildSettlement(toBeFoundSettlement);
        System.out.println("Opponent action: New Settlement");
    }

    public void opponentExpand(OffsetCoordinate location, String terrainType){
        TerrainTile toBeExpanded = (TerrainTile) myMap.getHexTileAt(location);
        SettlementExpansion expansion = null;
        Settlement targetSettlement = getSettlementContaining(toBeExpanded);

        if(terrainType.compareTo("GRASS") == 0) {
            expansion = targetSettlement.getExpansion(TerrainType.GRASS);
        } else if(terrainType.compareTo("JUNGLE") == 0) {
            expansion = targetSettlement.getExpansion(TerrainType.JUNGLE);
        } else if(terrainType.compareTo("LAKE") == 0) {
            expansion = targetSettlement.getExpansion(TerrainType.LAKE);
        } else if(terrainType.compareTo("ROCK") == 0) {
            expansion = targetSettlement.getExpansion(TerrainType.ROCK);
        }

        executeExpansion(expansion);
        System.out.println("Opponent action: Expanded on " + terrainType);
    }

    public void opponentNewTotoro(OffsetCoordinate location){
        TerrainTile toBeBuiltTotoro = (TerrainTile) myMap.getBoardSpaceAt(location).topTile();
        placeTotoro(toBeBuiltTotoro);
        for (Direction d : Direction.values()) {
            if (toBeBuiltTotoro.hasNeighborInDirection(d)) {
                if (toBeBuiltTotoro.getNeighborInDirection(d) instanceof TerrainTile) {
                    Settlement adjacentSettlement = getSettlementContaining((TerrainTile) toBeBuiltTotoro.getNeighborInDirection(d));
                    if (adjacentSettlement != null) {
                        if (!adjacentSettlement.hasTotoro() && adjacentSettlement.getSize() >= 5) {
                            adjacentSettlement.addToSettlement(toBeBuiltTotoro);
                            adjacentSettlement.placedTotoro();
                            settlements = adjacentSettlement.combineAdjacentSettlementsForSingleTile(toBeBuiltTotoro, settlements, adjacentSettlement);
                        }
                    }
                }
            }
        }
        System.out.println("Opponent action: Placed Totoro");
    }

    public void opponentNewTiger(OffsetCoordinate location){
        TerrainTile toBeBuiltTiger = (TerrainTile) myMap.getBoardSpaceAt(location).topTile();
        placeTiger(toBeBuiltTiger);
        for (Direction d : Direction.values()) {
            if (toBeBuiltTiger.hasNeighborInDirection(d)) {
                if (toBeBuiltTiger.getNeighborInDirection(d) instanceof TerrainTile) {
                    Settlement adjacentSettlement = getSettlementContaining((TerrainTile) toBeBuiltTiger.getNeighborInDirection(d));
                    if (adjacentSettlement != null) {
                        adjacentSettlement.addToSettlement(toBeBuiltTiger);
                        adjacentSettlement.placedTiger();
                        settlements = adjacentSettlement.combineAdjacentSettlementsForSingleTile(toBeBuiltTiger, settlements, adjacentSettlement);
                    }
                }
            }
        }
        System.out.println("Opponent action: Placed Tiger");
    }

    public int scoreTilePlacement(Placement placement) {
        int score = 0;
        for (int i = 0; i < 2; i++){
            BoardSpace hex = placement.getBoardSpaces().get(i); //Each individual hex tile at its top level
            //Level Consideration
            if (hex.getLevel() == 0) { //Empty space
                score += 15;
                return score;
            }
            if (hex.getLevel() == 1) //Nuke and potential for a tiger
                score += 20;
            if (hex.getLevel() >= 2) //Causes a tiger to be place-able, high priority
                score += 50;
            if (hex.getLevel() >= 3) //There is no rel purpose after level 3, so not much priority
                score += 10;
            //Enemy Occupant
            if (hex.topTile()!= null && hex.topTile().isOccupied()) {
                if (hex.getLevel() >= 1) {
                    if ((this.isWhite() && hex.topTile().isOwnedByBlack()) || (!this.isWhite() && hex.topTile().isOwnedByWhite())) { //Enemy owns place
                        if (hex.topTile().isPartOfSettlement && hex.topTile().settlementSize >= 5) {//Don't want to make it easier for the opponent
                            if (hasAdjacentTotoro(hex.topTile()))
                                return 0;
                            else
                                score -= 100;
                        } else if (hex.topTile().isPartOfSettlement && hex.topTile().settlementSize < 5)
                            score += 20;
                    }
                    //Own settlement (includes separation for having more totoros)
                    if ((this.isWhite() && hex.topTile().isOwnedByWhite()) || (!this.isWhite() && hex.topTile().isOwnedByBlack())) { //Friendly settlement
                        if (hex.topTile().isPartOfSettlement && hex.topTile().settlementSize >= 5) {
                            if (hasAdjacentTotoro(hex.topTile())) //Best point to separate a settlement at
                                score += 100;
                            else
                                score += 20;
                        }
                        if (hex.topTile().isPartOfSettlement && hex.topTile().settlementSize < 5) {
                            if (hasAdjacentTotoro(hex.topTile())) //Still important, but requires more work to have enough for a totoro
                                score += 50;
                            else
                                score += 10;
                        }
                    }
                }
            }
        }
        return score;
    }

    public boolean hasAdjacentTotoro(HexTile hexTile){
        ArrayList<TerrainTile> adjacentTerrainTiles = hexTile.getAdjacentTerrainTiles();
        for (TerrainTile adjacentTerrainTile: adjacentTerrainTiles) {
            if (adjacentTerrainTile.hasTotoro()) {
                return true;
            }
        }
        return false;
    }

    public int scoreAdjacentBoardSpaces(Placement p,BoardSpace bs, Settlement s) {
        int value = 0;
        if(bs.getNorth() != null)
        {
            BoardSpace temp = bs.getNorth();
            if(temp.getLevel() > 0)
            {
                ArrayList<TerrainTile> ts = s.getTiles();
                if(ts.contains(temp.topTile()))
                {
                    value = 20 + scoreTilePlacement(p);
                    return value;
                }
            }
        }
        if(bs.getNorthWest() != null)
        {
            BoardSpace temp = bs.getNorthWest();
            if(temp.getLevel() > 0)
            {
                ArrayList<TerrainTile> ts = s.getTiles();
                if(ts.contains(temp.topTile()))
                {
                    value = 20 + scoreTilePlacement(p);
                    return value;
                }
            }
        }
        if(bs.getNorthEast() != null)
        {
            BoardSpace temp = bs.getNorthEast();
            if(temp.getLevel() > 0)
            {
                ArrayList<TerrainTile> ts = s.getTiles();
                if(ts.contains(temp.topTile()))
                {
                    value = 20 + scoreTilePlacement(p);
                    return value;
                }
            }
        }
        if(bs.getSouth() != null)
        {
            BoardSpace temp = bs.getSouth();
            if(temp.getLevel() > 0)
            {
                ArrayList<TerrainTile> ts = s.getTiles();
                if(ts.contains(temp.topTile()))
                {
                    value = 20 + scoreTilePlacement(p);
                    return value;
                }
            }
        }
        if(bs.getSouthEast() != null)
        {
            BoardSpace temp = bs.getSouthEast();
            if(temp.getLevel() > 0)
            {
                ArrayList<TerrainTile> ts = s.getTiles();
                if(ts.contains(temp.topTile()))
                {
                    value = 20 + scoreTilePlacement(p);
                    return value;
                }
            }
        }
        if(bs.getSouthWest() != null)
        {
            BoardSpace temp = bs.getSouthWest();
            if(temp.getLevel() > 0)
            {
                ArrayList<TerrainTile> ts = s.getTiles();
                if(ts.contains(temp.topTile()))
                {
                    value = 20 + scoreTilePlacement(p);
                    return value;
                }
            }
        }
        return value;
    }

    public int scoreAdjacentBoardSpacesNotNearSettlement(Placement p,BoardSpace bs)
    {
        int value = 0;
        if(bs.getNorth() != null)
        {
            BoardSpace temp = bs.getNorth();
            if(temp.getLevel() > 1 && temp.getLevel() < 4)
            {
                value = 20 + scoreTilePlacement(p);
                return value;

            }
        }
        if(bs.getNorthWest() != null)
        {
            BoardSpace temp = bs.getNorthWest();
            if(temp.getLevel() > 1 && temp.getLevel() < 4)
            {

                value = 20 + scoreTilePlacement(p);
                return value;
            }
        }
        if(bs.getNorthEast() != null)
        {
            BoardSpace temp = bs.getNorthEast();
            if(temp.getLevel() > 1 && temp.getLevel() < 4)
            {
                value = 20 + scoreTilePlacement(p);
                return value;
            }
        }
        if(bs.getSouth() != null)
        {
            BoardSpace temp = bs.getSouth();
            if(temp.getLevel() > 1 && temp.getLevel() < 4)
            {
                value = 20 + scoreTilePlacement(p);
                return value;
            }
        }
        if(bs.getSouthEast() != null)
        {
            BoardSpace temp = bs.getSouthEast();
            if(temp.getLevel() > 1 && temp.getLevel() < 4)
            {
                value = 20 + scoreTilePlacement(p);
                return value;
            }
        }
        if(bs.getSouthWest() != null)
        {
            BoardSpace temp = bs.getSouthWest();
            if(temp.getLevel() > 1 && temp.getLevel() < 4)
            {

                value = 20 + scoreTilePlacement(p);
                return value;

            }
        }
        return value;
    }

    public Placement TigerFocusAI(ArrayList<Placement> Placements)
    {
        int BestValuePlacement = 0;
        Placement returnMe = null;
        if(tigerCount == 0)
        {
            return returnMe;
        }
        else {
            for (Placement t : Placements) {
                ArrayList<BoardSpace> BSLocations = t.getBoardSpaces();
                for (Settlement s : settlements) {
                    //BSLocations 0 is a volcano Tile
                    ArrayList<TerrainTile> currentSettlement = s.getTiles();
                    HexTile temp1 = null;
                    HexTile temp2 = null;
                    if(BSLocations.get(1).getLevel() > 0) {
                        temp1 = BSLocations.get(1).topTile();
                    }
                    else
                    {
                        BestValuePlacement = scoreAdjacentBoardSpaces(t,BSLocations.get(1),s);
                        returnMe = t;
                    }
                    if(BSLocations.get(2).getLevel() > 0) {
                        temp2 = BSLocations.get(2).topTile();
                    }
                    else
                    {
                        BestValuePlacement = scoreAdjacentBoardSpaces(t,BSLocations.get(2),s);
                        returnMe = t;
                    }
                    for (TerrainTile tt : currentSettlement) {
                        ArrayList<TerrainTile> adjacentTerrainTiles = tt.getAdjacentTerrainTiles();
                        if(temp1 != null) {
                            if (temp1.terrainType() != TerrainType.VOLCANO) {
                                if (adjacentTerrainTiles.contains((TerrainTile) temp1)) {
                                    if (BestValuePlacement < scoreTilePlacement(t)) {
                                        BestValuePlacement = 20 + scoreTilePlacement(t);
                                        returnMe = t;
                                        if (BestValuePlacement >= 50) {
                                            //boolean place tiger
                                            return returnMe;
                                        }
                                    }
                                }
                            }
                        }else if(temp2 != null) {
                            if (temp2.terrainType() != TerrainType.VOLCANO) {
                                if (adjacentTerrainTiles.contains((TerrainTile) temp2)) {
                                    if (BestValuePlacement < scoreTilePlacement(t)) {
                                        BestValuePlacement = 20 + scoreTilePlacement(t);
                                        returnMe = t;
                                        if (BestValuePlacement >= 50) {
                                            //boolean place tiger
                                            return returnMe;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (BestValuePlacement >= 30) {
                    return returnMe;
                }
                if(BestValuePlacement < scoreAdjacentBoardSpacesNotNearSettlement(t,BSLocations.get(1)))
                {
                    BestValuePlacement = scoreAdjacentBoardSpacesNotNearSettlement(t,BSLocations.get(1));
                    returnMe = t;
                }
                if(BestValuePlacement < scoreAdjacentBoardSpacesNotNearSettlement(t,BSLocations.get(2)))
                {
                    BestValuePlacement = scoreAdjacentBoardSpacesNotNearSettlement(t,BSLocations.get(2));
                    returnMe = t;
                }
            }
            if (BestValuePlacement >= 30) {
                return returnMe;
            }
        }
        return returnMe;
    }

    public Placement placementAI(ArrayList<Placement> Placements)
    {
        Placement returnMe = null;
        int value = 0;
        returnMe = TigerFocusAI(Placements);
        if(returnMe != null) {
            return returnMe;
        }
        else
        {
            int maxScore = 0;
            Placement bestPlacement = null;
            for(Placement p: Placements)
            {
                int temp = scoreTilePlacement(p);
                if(temp > maxScore){
                    maxScore = temp;
                    bestPlacement = p;
                }
            }
            returnMe = bestPlacement;
        }
        if(returnMe == null)
        {
            returnMe =  Placements.get(random.nextInt(Placements.size()));
        }
        return returnMe;
    }

    private TerrainTile getBestTileToBuildSettlementOn(ArrayList<TerrainTile> legalTilesToBuildSettlementOn){
        TerrainTile bestTile = null;
        int bestTileValue = -1;
        for(TerrainTile tt: legalTilesToBuildSettlementOn){
            int currentTileValue = scoreNewSettlementTile(tt);
            if (currentTileValue > bestTileValue) {
                bestTile = tt;
                bestTileValue = currentTileValue;
            }
        }
        return bestTile;
    }

    public ArrayList<TerrainTile> getLegalTilesToBuildSettlementOn(GameMap gameMap){
        ArrayList<HexTile> visibleTiles = gameMap.getVisible();
        ArrayList<TerrainTile> legalTilesToBuildSettlementOn = new ArrayList<TerrainTile>();
        for(HexTile ht: visibleTiles){
            if(ht.terrainType() != TerrainType.VOLCANO){
                if(!ht.isOccupied() && ht.getLevel() == 1) {
                    legalTilesToBuildSettlementOn.add((TerrainTile)ht);
                }
            }
        }
        return legalTilesToBuildSettlementOn;
    }

    private int scoreNewSettlementTile(TerrainTile tt){
        int value = 0;
        ArrayList<TerrainTile> adjacentTerrainTiles = tt.getAdjacentTerrainTiles();
        for (TerrainTile adjacentTerrainTile: adjacentTerrainTiles) {
            if (!adjacentTerrainTile.isOccupied()) {
                if (adjacentTerrainTile.getLevel() >= 3) {
                    value += 50;
                    if (adjacentTerrainTile.hasNeighborBelongingToPlayer(enemyPlayer)) {
                        value -= 20;
                    }
                } else if (adjacentTerrainTile.getLevel() == 2) {
                    value += 20;
                    if (adjacentTerrainTile.canPlaceTileOn()) {
                        value += 30;
                        if (adjacentTerrainTile.hasNeighborBelongingToPlayer(enemyPlayer)) {
                            value -= 20;
                        }
                    }
                }
            }
        }
        return value;
    }

    private boolean addTotoro(){
        ArrayList<Settlement> TotoroLegal = new ArrayList<Settlement>();
        for(Settlement s: settlements)
        {
            if(canPlaceTotoro(s)) //checking size and whether it has a totoro
            {
                TotoroLegal.add(s);
            }
            else{
                //no can place totoro
            }
        }
        if(TotoroLegal.size() > 0) {
            TerrainTile placeTotoroHere = getBestTotoroPlacementTile(TotoroLegal);
            placeTotoro(placeTotoroHere);
            activeSettlement.addToSettlement(placeTotoroHere);
            activeSettlement.placedTotoro();
            System.out.println("ADDED TOTORO AT " + placeTotoroHere + "\nIN SETTLEMENT\n" + activeSettlement);
            settlements = activeSettlement.combineAdjacentSettlementsForSingleTile(placeTotoroHere,settlements,activeSettlement);
            buildMessage = "BUILD TOTORO SANCTUARY AT " + placeTotoroHere.getBoardSpace().getLocation().getCubicCoordinate().toString();
            return true;
        }
        return false;
    }

    private boolean addTiger() {
        if(this.getTigerCount() < 1)
            return false;
        ArrayList<Settlement> TigerLegal = new ArrayList<Settlement>();
        for(Settlement s: settlements)
        {
            if(canPlaceTiger(s)) {
                TigerLegal.add(s);
            }
            else {
                //no can place tiger
            }
        }
        if(TigerLegal.size() > 0) {
            TerrainTile placeTigerHere = getBestTigerPlacementTile(TigerLegal);
            placeTiger(placeTigerHere);
            activeSettlement.addToSettlement(placeTigerHere);
            activeSettlement.placedTiger();
            //System.out.println("Player Settlement Size Before: " + settlements.size());
            settlements = activeSettlement.combineAdjacentSettlementsForSingleTile(placeTigerHere,settlements,activeSettlement);
            //System.out.println("Player Settlement Size After: " + settlements.size());
            buildMessage = "BUILD TIGER PLAYGROUND AT " + placeTigerHere.getBoardSpace().getLocation().getCubicCoordinate().toString();
            return true;
        }
        return false;
    }

    public boolean expandSettlementToMaximizeMeepleUsage() {
        SettlementExpansion bestExpansion = null;
        int bestExpansionValue = 0;
        for (Settlement settlement: settlements) {
            ArrayList<SettlementExpansion> allExpansions = settlement.getAllExpansions();
            for (SettlementExpansion currentExpansion : allExpansions) {
                if (currentExpansion.getMeepleCost() > meepleCount) {
                    continue;
                } else {
                    int currentExpansionValue = currentExpansion.getMeepleCost();
                    if (currentExpansionValue > bestExpansionValue) {
                        bestExpansion = currentExpansion;
                        bestExpansionValue = currentExpansionValue;
                    }
                }
            }
        }
        if (bestExpansionValue > 0) {
            executeExpansion(bestExpansion);
            buildMessage = "EXPAND SETTLEMENT AT " + bestExpansion.getSettlementToExpand().getTiles().get(0).getBoardSpace().getLocation().getCubicCoordinate().toString();
            buildMessage += " " + bestExpansion.getTerrainToExpand();
            return true;
        } else {
            return false;
        }
    }

    public boolean expandSettlement() {

        SettlementExpansion bestOverallExpansion = new SettlementExpansion(new ArrayList<TerrainTile>(), settlements.get(0), TerrainType.GRASS, null, false, false, false);

        for(int i = 0; i!= settlements.size();++i){
            Settlement s = settlements.get(i);
            SettlementExpansion bestCurrentSettlementExpansion = getBestExpansionForSettlement(s);
            if (bestCurrentSettlementExpansion.getValue() > bestOverallExpansion.getValue()) {
                bestOverallExpansion = bestCurrentSettlementExpansion;
            }
        }

        if (bestOverallExpansion.getValue() >= 50) {
            executeExpansion(bestOverallExpansion);
            //System.out.println("Player Settlement Size Before: " + settlements.size());
            //settlements = bestOverallExpansion.getSettlementToExpand().combineAdjacentSettlementsForMultTiles(bestOverallExpansion.getTiles(), settlements, bestOverallExpansion.getSettlementToExpand());
            //System.out.println("Player Settlement Size After: " + settlements.size());

            buildMessage = "EXPAND SETTLEMENT AT " + bestOverallExpansion.getSettlementToExpand().getTiles().get(0).getBoardSpace().getLocation().getCubicCoordinate().toString();
            buildMessage += " " + bestOverallExpansion.getTerrainToExpand();

            return true;
        }

        return false;
    }

    public SettlementExpansion getBestExpansionForSettlement(Settlement settlement) {
        SettlementExpansion bestExpansion = new SettlementExpansion(new ArrayList<TerrainTile>(), settlement , TerrainType.GRASS, null, false, false, false);
        ArrayList<SettlementExpansion> allExpansions = settlement.getAllExpansions();
        for (SettlementExpansion currentExpansion: allExpansions) {
            currentExpansion.setValue(scoreSettlementExpansion(currentExpansion));
            if (currentExpansion.getValue() > bestExpansion.getValue()) {
                bestExpansion = currentExpansion;
            }
        }
        return bestExpansion;
    }

    public boolean buildSettlement(GameMap gameMap) {
        //choose the best available settlement
        //if we settle... currently the only option
        if(meepleCount < 1)
            return false;
        ArrayList<TerrainTile> legalTilesToBuildSettlementOn = getLegalTilesToBuildSettlementOn(gameMap);
        TerrainTile bestPlaceToSettle = getBestTileToBuildSettlementOn(legalTilesToBuildSettlementOn);
        buildSettlement(bestPlaceToSettle);
        System.out.println("BUILT SETTLEMENT AT " + bestPlaceToSettle.toString());
        //activeSettlement = settlements.get(settlements.size()-1);
        //settlements = activeSettlement.combineAdjacentSettlementsForSingleTile(bestPlaceToSettle,settlements,activeSettlement);
        buildMessage = "FOUND SETTLEMENT AT " + bestPlaceToSettle.getBoardSpace().getLocation().getCubicCoordinate().toString();
        return true; //todo should there be a false?
    }

    private boolean outOfTotoroOrTigers() {
        return (totoroCount == 0 || tigerCount == 0);
    }

    private void getRidOfMeeples() {
        if(meepleCount == 1){
            buildSettlement(this.myMap);
        }
        else if(expandSettlementToMaximizeMeepleUsage()) {
            //successfully expands settlement.
        }
        else {
            buildSettlement(this.myMap);
        }
    }

    private String buildPhase(GameMap gameMap) {

        for(Settlement settlement: settlements){
            settlement.checkSettlementsLegality();
        }

        String finalMessage = "";

        if(outOfTotoroOrTigers()){
            getRidOfMeeples();
            finalMessage = buildMessage;
            return finalMessage;
        }

        if(settlements.size() > 0) { //if current player has at least one settlement already
            if (addTotoro()) {
                finalMessage = buildMessage;
            }
            if (addTiger()) { //can't add totoro, add tiger
                finalMessage = buildMessage;
            }
            else if (expandSettlement()) {
                finalMessage = buildMessage;
            }
            else if (buildSettlement(gameMap)) {
                finalMessage = buildMessage;
            }
            else {
                System.out.println("Player " + this + "cannot legally move");
                if(myLogicDirector != null) //todo
                    myLogicDirector.setGameOver();
            }
        }
        else {
            buildSettlement(gameMap);
            finalMessage = buildMessage;
        }

        for (Settlement s: settlements) {
            s.removeAnyUndergroundTiles();
            s.removeNonTopLevelTiles();
        }
        return finalMessage;
    }

    //TODO add AI logic
    public String takeTurn(GameMap gameMap, TriHexTile tile) {
        String result = placementPhase(gameMap, tile);
        result += " " + buildPhase(gameMap);
        return result;
    }

    public void placeTile(GameMap gameMap, Placement placement) {
        gameMap.implementPlacement(placement);
        placeTileCheck = true; //added for testing
    }
    // Added for testing
    public boolean placementCheck () {
        return placeTileCheck;
    }
    //end testing
    public void placeMeeples(TerrainTile tt) {
        tt.placeMeeple(this);
        removeMeeples(tt.getLevel());
        awardPoints(tt.getLevel() * tt.getLevel());
    }

    public int scoreSettlementExpansion(SettlementExpansion settlementExpansion) {
        int expansionValue = 0;
        ArrayList<TerrainTile> expansionTiles = settlementExpansion.getTiles();
        int meepleCost = settlementExpansion.getMeepleCost();

        //check if we can afford expansion
        if (meepleCost > meepleCount) {
            return 0;
        }

        if(enemyPlayer.meepleCount > meepleCount){
            return 0;
        }

        if(expansionTiles.size() < 1){
            return 0;
        }

        if (outOfTotoroOrTigers()) {
            expansionValue += meepleCost * 30;
        }
        else {
            if(meepleCount>10)
                expansionValue -= meepleCost * 5;
            else if(meepleCount>5) {
                expansionValue -= meepleCost * 30;
            }
            else
                expansionValue -= meepleCost * 1000;

            if (settlementExpansion.canPlaceTotoroAfterExpansion()) {
                if(totoroCount == 1)
                    expansionValue += 10000;
                else if(totoroCount == 2)
                    expansionValue += 50;
            }
            if (settlementExpansion.canPlaceTigerAfterExpansion()) {
                if(tigerCount == 1)
                    expansionValue += 10000;
                else if( tigerCount == 2)
                    expansionValue += 125;
            }
            if (settlementExpansion.canPlaceTigerAfterExpansionAndTilePlacement()) {

                if(tigerCount == 1)
                    expansionValue += 100;
                else if(tigerCount == 2)
                    expansionValue += 50;
            }
        }

        expansionValue += expansionTiles.size()*10; //ten points per tile

        return expansionValue;
    }

    public void buildSettlement(TerrainTile tt) {
        if(!tt.isOccupied()) {
            placeMeeples(tt);
            Settlement settlement = new Settlement(this, tt);
            settlements.add(settlement);
            settlement.combineAdjacentSettlementsForSingleTile(tt,settlements,settlement);
        }
    }

    /*
        Calls settlement and contiguous UnoccuppiedTerrainTyesTiles but DOES NOT places meeples on legal tiles of same terrain
        TODO this is currently dumb AI
     */

    public void placeTotoro(TerrainTile tt) {
        tt.placeTotoro(this);
        removeTotoro(1);
        awardPoints(200);
    }

    public boolean canPlaceTotoro(Settlement settlement)
    {
        if(settlement.getSize()<5 || settlement.hasTotoro()) {
            return false;
        }
        ArrayList<TerrainTile> Tplacements = settlement.getLegalTotoroTiles();
        if(Tplacements.size() != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean canPlaceTiger(Settlement settlement){
        if(settlement.hasTiger()) {
            return false;
        }
        ArrayList<TerrainTile> Tplacements = settlement.getLegalTigerTiles();
        if(Tplacements.size() != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private int scoreTotoroPlacement(TerrainTile t) {

        int numberOfAdjacentEnemyTiles = t.getEnemyAdjacentTerrainTiles().size();
        return numberOfAdjacentEnemyTiles;
        //return 1; // TODO add AI in this method
    }

    private int scoreTigerPlacement(TerrainTile tt) {
        int score = 0;
        if (tt.hasNeighborBelongingToPlayer(enemyPlayer)) {
            score += 20;    //deny the enemy the tiger placement
        }
        return score;
    }

    public TerrainTile getBestTotoroPlacementTile(ArrayList<Settlement> legalTotoroSettlements)
    {
        //ArrayList<TerrainTile> potentialPlacements = new ArrayList<TerrainTile>();
        Settlement bestSettlement = null;
        TerrainTile totoroPlacementTile = null;
        int bestScore = 0;
        for(Settlement s: legalTotoroSettlements) {
            ArrayList<TerrainTile> potentialPlacements =  s.getLegalTotoroTiles();
            for(TerrainTile t: potentialPlacements) {
                if(scoreTotoroPlacement(t) > bestScore) {
                    bestSettlement = s;
                    totoroPlacementTile = t;
                }
            }
        }
        if(activeSettlement ==null || totoroPlacementTile ==null) {
            System.out.println("CHECK getBestTotoroPlacementTile() in PLayer.java");
        }
        activeSettlement = bestSettlement;
        return  totoroPlacementTile;
    }

//    public TerrainTile getBestTotoroPlacementTile(ArrayList<TerrainTile> legalTotoroTiles)
//    {
//        int stupidAIPlacement = random.nextInt(legalTotoroTiles.size());
//        TerrainTile totoroPlacementTile  = legalTotoroTiles.get(stupidAIPlacement);
//        return  totoroPlacementTile;
//        //TODO AI to pick which totoro placment
//    }

    public void placeTiger(TerrainTile tt) {
        tt.placeTiger(this);
        removeTiger(1);
        awardPoints(75);
    }

    public TerrainTile getBestTigerPlacementTile(ArrayList<Settlement> legalTigerSettlements)
    {
        Settlement bestSettlement = null;
        TerrainTile tigerPlacementTile = null;
        int bestScore = -1;
        for(Settlement s: legalTigerSettlements) {
            ArrayList<TerrainTile> potentialPlacements =  s.getLegalTigerTiles();
            for(TerrainTile tt: potentialPlacements) {
                int currentScore = scoreTigerPlacement(tt);
                if(currentScore > bestScore) {
                    bestSettlement = s;
                    tigerPlacementTile = tt;
                    bestScore = currentScore;
                }
            }
        }
        if(activeSettlement ==null || tigerPlacementTile ==null) {
            System.out.println("CHECK getBestTigerPlacementTile() in PLayer.java");
        }
        activeSettlement = bestSettlement;
        return  tigerPlacementTile;
    }

    public int getScore()
    {
        return score;
    }

    public boolean removeTotoro(int amount){
        if(amount > totoroCount)
            return false;
        totoroCount-=amount;
        checkGameOver();
        return true;
    }

    public boolean removeTiger(int amount){
        if(amount > tigerCount)
            return false;
        tigerCount-=amount;
        checkGameOver();
        return true;
    }

    public boolean removeMeeples(int amount){
        if(amount > meepleCount) {
            System.out.println("Attempting to remove more meeples than we have.");
            return false;

        }
        meepleCount-=amount;
        checkGameOver();
        return true;
    }

    private void checkGameOver(){
        boolean gameOver = false;
        if ((meepleCount==0 && totoroCount==0) || (meepleCount==0 && tigerCount==0) || (totoroCount==0 && tigerCount==0)) {
            gameOver = true;
        }
    }

    public boolean checkOnlyOneTypeTokenIsLeft(){
        boolean gameOver = false;
        if ((meepleCount==0 && totoroCount==0) || (meepleCount==0 && tigerCount==0) || (totoroCount==0 && tigerCount==0)) {
            gameOver = true;
        }
        return gameOver;
    }

    public void awardPoints(int amount){
        score += amount;
    }

    public int getMeepleCount() {
        return meepleCount;
    }

    public int getTigerCount() { return tigerCount; }

    public int getTotoroCount() {
        return totoroCount;
    }

    public boolean isWhite(){
        return false;
    }

    public void setEnemyPlayer(Player enemyPlayer) { this.enemyPlayer = enemyPlayer; }

    @Override
    public String toString(){
        String returnMe = "";
        returnMe += name +"\n\tscore: " + this.getScore() +"\n\tMeepleCount: " + this.getMeepleCount() +"\n\tTotoroCount: " + this.getTotoroCount() + "\n\tTigerCount: " + this.getTigerCount();
        return returnMe;
    }

    public void nukeSettlements(ArrayList<TerrainTile> nukedTiles) {
        Settlement settlementToNuke = getSettlementContaining(nukedTiles.get(0));
        System.out.println("NUKING " + name + " SETTLEMENT\n" + settlementToNuke.toString());
        settlements.remove(settlementToNuke);
        ArrayList<Settlement> newSettlements = settlementToNuke.getSplitSettlementsAfterNuke(nukedTiles);
        settlements.addAll(newSettlements);
        for (TerrainTile tt : nukedTiles) {
            tt.nuke();
            System.out.println("NUKED TILE " + tt.toString());
        }
        for (int i = 0; i < newSettlements.size(); ++i) {
            System.out.println("RESULTING SETTLEMENT " + i + "\n" + newSettlements.get(i).toString());
        }
    }

    public Settlement getSettlementContaining(TerrainTile tt) {
        for (Settlement s : settlements) {
            if (s.contains(tt))
                return s;
        }
        return null;
    }

    public void cleanup(GameMap myMap){
        score = 0;
        placeTileCheck = false;
        totoroCount = Constants.TOTORO_PER_PLAYER;
        meepleCount = Constants.MEEPLES_PER_PLAYER;
        tigerCount = Constants.TIGER_PER_PLAYER;
        buildMessage = "";
        settlements.clear();
        activeSettlement = null;
//        enemyPlayer = null;
        this.myMap = myMap;
    }
}
