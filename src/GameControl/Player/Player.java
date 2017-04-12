package GameControl.Player;

import GameControl.Placement;
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
import java.util.HashMap;
import java.util.Random;

/**
 * Created by jowens on 3/8/17.
 */
public class Player {
    //units
    String name;
    private int totoroCount;
    private int meepleCount;
    private int tigerCount;
    private String buildMessage;
    private HashMap<OffsetCoordinate, TerrainTile> ownedTiles;
    private ArrayList<Settlement> settlements;
    private Settlement activeSettlement; //settlement we're adding stuff too
    private AIPlayerController AI;
    private int expansionWorth;

    public Player enemyPlayer;

    private GameMap myMap;
    //private TerrainTile Tplacement;

    //scoring
    private int score;//, expansionWorth;
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
        expansionWorth = 0;
        ownedTiles = new HashMap<OffsetCoordinate, TerrainTile>();
    }

    public ArrayList<Settlement> getSettlements()
    {return settlements;}
    //returns true iff there are no legal build moves for the current player on the given map
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

    private void executeExpansion(ArrayList<TerrainTile> expansion, Settlement toBeAddedTo){
        for (TerrainTile add : expansion) {
            placeMeeples(add);
            toBeAddedTo.addToSettlement(add);
            System.out.println("Expansion added to settlment\n" + toBeAddedTo);
        }
        settlements = toBeAddedTo.combineAdjacentSettlementsForMultTiles(expansion, settlements, toBeAddedTo);
        buildMessage = "EXPAND SETTLEMENT AT " + toBeAddedTo.getTiles().get(0).getBoardSpace().getLocation().toString();
    }

    private boolean legalExpansionVisible(){
        if(settlements.size() == 0){
            return false;
        }
        /*
        int whichsettle = random.nextInt(settlements.size());
        Integer expansionWorth = new Integer(0);
        ArrayList<TerrainTile> expansion = getBestExpansionForSettlement(settlements.get(whichsettle));
        if (expansionWorth >= 50) {
            for (TerrainTile add : expansion) {
                placeMeeples(add);
                settlements.get(whichsettle).addToSettlement(add);
                awardPoints(add.getLevel() ^ 2);
                System.out.println("added");
            }
            return true;
        }
        return false;
        */
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
    }

    public void opponentNewSettlement(OffsetCoordinate location){
        TerrainTile toBeFoundSettlement = (TerrainTile) myMap.getBoardSpaceAt(location).topTile();
// Updated upstream
        buildSettlement(toBeFoundSettlement);
        //activeSettlement = settlements.get(settlements.size()-1);
        //settlements = activeSettlement.combineAdjacentSettlementsForSingleTile(toBeFoundSettlement,settlements,activeSettlement);
//======
//        buildSettlement(toBeFoundSettlement);
//        activeSettlement = settlements.get(settlements.size()-1);
//        settlements = activeSettlement.combineAdjacentSettlementsforSingleTile(toBeFoundSettlement,settlements,activeSettlement);
// Stashed changes
        System.out.println("Opponent action: New Settlement");
    }

    public void opponentExpand(OffsetCoordinate location, String terrainType){
        TerrainTile toBeExpanded = (TerrainTile) myMap.getBoardSpaceAt(location).topTile();
        ArrayList<TerrainTile> expansionTiles = null;
        Settlement targetSettlement = getSettlementContaining(toBeExpanded);

        if(terrainType.compareTo("GRASS") == 0){
            expansionTiles = targetSettlement.getExpansionTiles(TerrainType.GRASS);
        } else if(terrainType.compareTo("JUNGLE") == 0){
            expansionTiles = targetSettlement.getExpansionTiles(TerrainType.JUNGLE);
        } else if(terrainType.compareTo("LAKE") == 0){
            expansionTiles = targetSettlement.getExpansionTiles(TerrainType.LAKE);
        } else if(terrainType.compareTo("ROCK") == 0){
            expansionTiles = targetSettlement.getExpansionTiles(TerrainType.ROCK);
        }

// Updated upstream
        executeExpansion(expansionTiles, targetSettlement);
// =======
//        executeExpansion(expansionTiles, targetSettlement);
        System.out.println("Opponent action: Expanded on " + terrainType);
// Stashed changes
    }

    public void opponentNewTotoro(OffsetCoordinate location){
        TerrainTile toBeBuiltTotoro = (TerrainTile) myMap.getBoardSpaceAt(location).topTile();
// Updated upstream
        placeTotoro(toBeBuiltTotoro);
        for (Direction d : Direction.values()) {
            if (toBeBuiltTotoro.hasNeighborInDirection(d)) {
                if (toBeBuiltTotoro.getNeighborInDirection(d) instanceof TerrainTile) {
                    Settlement adjacentSettlement = getSettlementContaining((TerrainTile) toBeBuiltTotoro.getNeighborInDirection(d));
                    if (adjacentSettlement != null) {
                        if (!adjacentSettlement.hasTotoro() && adjacentSettlement.getSize() >= 5) {
                            adjacentSettlement.addToSettlement(toBeBuiltTotoro);
                            adjacentSettlement.placedTotoro();
                            enemyPlayer.setSettlements(adjacentSettlement.combineAdjacentSettlementsForSingleTile(toBeBuiltTotoro, enemyPlayer.getSettlements(), adjacentSettlement));

                            awardPoints(200);
                        }
                    }
                }
            }
        }
//=======
//        placeTotoro(toBeBuiltTotoro);
//        activeSettlement.addToSettlement(toBeBuiltTotoro);
//        activeSettlement.placedTotoro();
//        this.awardPoints(200);
//        settlements = activeSettlement.combineAdjacentSettlementsforSingleTile(toBeBuiltTotoro,settlements,activeSettlement);
        System.out.println("Opponent action: Placed Totoro");
//Stashed changes
    }

    public void opponentNewTiger(OffsetCoordinate location){
        TerrainTile toBeBuiltTiger = (TerrainTile) myMap.getBoardSpaceAt(location).topTile();
        placeTiger(toBeBuiltTiger);
// Updated upstream
        for (Direction d : Direction.values()) {
            if (toBeBuiltTiger.hasNeighborInDirection(d)) {
                if (toBeBuiltTiger.getNeighborInDirection(d) instanceof TerrainTile) {
                    Settlement adjacentSettlement = getSettlementContaining((TerrainTile) toBeBuiltTiger.getNeighborInDirection(d));
                    if (adjacentSettlement != null) {
                        if (!adjacentSettlement.hasTiger()) {
                            adjacentSettlement.addToSettlement(toBeBuiltTiger);
                            adjacentSettlement.placedTotoro();
                            enemyPlayer.awardPoints(75);
                            enemyPlayer.setSettlements(adjacentSettlement.combineAdjacentSettlementsForSingleTile(toBeBuiltTiger, enemyPlayer.getSettlements(), adjacentSettlement));

                        }
                    }
                }
            }
        }
//=======
//        activeSettlement.addToSettlement(toBeBuiltTiger);
//        activeSettlement.placedTiger();
//        settlements = activeSettlement.combineAdjacentSettlementsforSingleTile(toBeBuiltTiger,settlements,activeSettlement);
//        this.awardPoints(75);
        System.out.println("Opponent action: Placed Tiger");
// Stashed changes
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
            if (hex.getLevel() == 2) //Causes a tiger to be place-able, high priority
                score += 50;
            if (hex.getLevel() >= 3) //There is no rel purpose after level 3, so not much priority
                score += 10;
            //Enemy Occupant
            if (hex.topTile().isOccupied()) {
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

    public boolean hasAdjacentTotoro(HexTile hex){
        if(hex.getNorth() != null)
            return hex.getNorth().hasTotoro();
        if (hex.getNorthWest() != null)
            return hex.getNorthWest().hasTotoro();
        if (hex.getSouthWest() != null)
            return hex.getSouthWest().hasTotoro();
        if (hex.getSouth() != null)
            return hex.getSouth().hasTotoro();
        if (hex.getSouthWest() != null)
            return hex.getSouthWest().hasTotoro();
        if (hex.getNorthWest() != null)
            return hex.getNorthWest().hasTotoro();
        else
            return false;
    }

    public int scoreAdjacentBoardSpaces(Placement p,BoardSpace bs, Settlement s)
    {
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
        int value = 0;
        Placement returnMe = null;
        if(tigerCount == 0)
        {
            return returnMe;
        }
        else {
            for (Placement t : Placements) {
                ArrayList<BoardSpace> BSLoactions = t.getBoardSpaces();
                for (Settlement s : settlements) {
                    //BSLocations 0 is a volcano Tile
                    ArrayList<TerrainTile> currentSettlement = s.getTiles();
                    HexTile temp1 = null;
                    HexTile temp2 = null;
                    if(BSLoactions.get(1).getLevel() > 0) {
                        temp1 = BSLoactions.get(1).topTile();
                    }
                    else
                    {
                        value = scoreAdjacentBoardSpaces(t,BSLoactions.get(1),s);
                        returnMe = t;
                    }
                    if(BSLoactions.get(2).getLevel() > 0) {
                        temp2 = BSLoactions.get(2).topTile();
                    }
                    else
                    {
                        value = scoreAdjacentBoardSpaces(t,BSLoactions.get(2),s);
                        returnMe = t;
                    }
                    for (TerrainTile tt : currentSettlement) {
                        ArrayList<TerrainTile> AdjacentTiles = s.getAdjacentTerrainTiles(tt);
                        if(temp1 != null) {
                            if (temp1.terrainType() != TerrainType.VOLCANO) {
                                if (AdjacentTiles.contains((TerrainTile) temp1)) {
                                    if (value < scoreTilePlacement(t)) {
                                        value = 20 + scoreTilePlacement(t);
                                        returnMe = t;
                                        if (value >= 50) {
                                            //boolean place tiger
                                            return returnMe;
                                        }
                                    }
                                }
                            }
                        }else if(temp2 != null) {
                            if (temp2.terrainType() != TerrainType.VOLCANO) {
                                if (AdjacentTiles.contains((TerrainTile) temp2)) {
                                    if (value < scoreTilePlacement(t)) {
                                        value = 20 + scoreTilePlacement(t);
                                        returnMe = t;
                                        if (value >= 50) {
                                            //boolean place tiger
                                            return returnMe;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (value >= 30) {
                    return returnMe;
                }
                if(value < scoreAdjacentBoardSpacesNotNearSettlement(t,BSLoactions.get(1)))
                {
                    value = scoreAdjacentBoardSpacesNotNearSettlement(t,BSLoactions.get(1));
                    returnMe = t;
                }
                if(value < scoreAdjacentBoardSpacesNotNearSettlement(t,BSLoactions.get(2)))
                {
                    value = scoreAdjacentBoardSpacesNotNearSettlement(t,BSLoactions.get(2));
                    returnMe = t;
                }
            }
            if (value >= 30) {
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
            for(Placement p: Placements)
            {
                int temp = scoreTilePlacement(p);
                if(temp == 15)
                {
                    returnMe = p;
                    return returnMe;
                }
            }
        }
        if(returnMe == null)
        {
            returnMe =  Placements.get(random.nextInt(Placements.size()));
        }
        return returnMe;
    }

    public ArrayList<TerrainTile> getLegalNewSettlements(GameMap gameMap){
        ArrayList<HexTile> tiles = gameMap.getVisible();
        ArrayList<TerrainTile> legalTilesToSettle = new ArrayList<TerrainTile>();
        //choose the best available settlement
        //if we settle... currently the only option
        TerrainTile bestPlaceToSettle = null;
        int currentBest = 0;
        for(HexTile ht: tiles){
            if(ht.terrainType() != TerrainType.VOLCANO && ht != null){
                if(ht.isOccupied() || ht.getLevel() != 1)
                    continue;
                else {
                    legalTilesToSettle.add((TerrainTile)ht);
                }
            }
        }
        return legalTilesToSettle;

    }

    public int scoreAdjacenttoLevel1Tiles(TerrainTile tt)
    {
        int value = 0;
        BoardSpace bs = tt.getBoardSpace();
        if(bs.getNorth() != null)
        {
            BoardSpace temp = bs.getNorth();
            if(temp.getLevel() >= 3)
            {
                return 50;
            }
            else if(temp.getLevel() == 2)
            {
                return 30;
            }
        }
        if(bs.getNorthWest() != null)
        {
            BoardSpace temp = bs.getNorthWest();
            if(temp.getLevel() > 0)
            {
                if(temp.getLevel() >= 3)
                {
                    return 50;
                }
                else if(temp.getLevel() == 2)
                {
                    return 30;
                }
            }
        }
        if(bs.getNorthEast() != null)
        {
            BoardSpace temp = bs.getNorthEast();
            if(temp.getLevel() > 0)
            {
                if(temp.getLevel() >= 3)
                {
                    return 50;
                }
                else if(temp.getLevel() == 2)
                {
                    return 30;
                }
            }
        }
        if(bs.getSouth() != null)
        {
            BoardSpace temp = bs.getSouth();
            if(temp.getLevel() > 0)
            {
                if(temp.getLevel() >= 3)
                {
                    return 50;
                }
                else if(temp.getLevel() == 2)
                {
                    return 30;
                }
            }
        }
        if(bs.getSouthEast() != null)
        {
            BoardSpace temp = bs.getSouthEast();
            if(temp.getLevel() > 0)
            {
                if(temp.getLevel() >= 3)
                {
                    return 50;
                }
                else if(temp.getLevel() == 2)
                {
                    return 30;
                }
            }
        }
        if(bs.getSouthWest() != null)
        {
            BoardSpace temp = bs.getSouthWest();
            if(temp.getLevel() > 0)
            {
                if(temp.getLevel() >= 3)
                {
                    return 50;
                }
                else if(temp.getLevel() == 2)
                {
                    return 30;
                }
            }
        }
        return value;
    }

    private TerrainTile getBestNewSettlement(ArrayList<TerrainTile> settlements){
        TerrainTile bestPlaceToSettle = null;
        int currentBest = 0;
        for(TerrainTile ht: settlements){
            if(ht.terrainType() != TerrainType.VOLCANO && ht != null){
                if(ht.isOccupied() || ht.getLevel() != 1)
                    continue;
                else if(bestPlaceToSettle == null) {
                    bestPlaceToSettle = (TerrainTile) ht;
                    currentBest = howGoodIsSettlement((TerrainTile)ht, this);
                }
                else if(howGoodIsSettlement((TerrainTile)ht, this) >currentBest) {
                    currentBest = howGoodIsSettlement((TerrainTile)ht, this);
                    bestPlaceToSettle = (TerrainTile)ht;
                    break;
                }
            }
        }
        return bestPlaceToSettle;
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
            //System.out.println("Player Settlement Size Before: " + settlements.size());
            settlements = activeSettlement.combineAdjacentSettlementsForSingleTile(placeTotoroHere,settlements,activeSettlement);
            //System.out.println("Player Settlement Size After: " + settlements.size());
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
            if(canPlaceTiger(s)) //checking size and whether it has a totoro
            {
                TigerLegal.add(s);
            }
            else{
                //no can place totoro
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
            this.awardPoints(75);
            buildMessage = "BUILD TIGER PLAYGROUND AT " + placeTigerHere.getBoardSpace().getLocation().getCubicCoordinate().toString();
            return true;
        }
        return false;
    }

    private void expandSettlementToMaximizeMeepleUsage() {
        Integer tempValue = new Integer(0);
        for(int i = 0; i!= settlements.size();++i){
            ArrayList<TerrainTile> expansion = expandSettlementToMaximizeMeeplePlacement(settlements.get(i), tempValue);
            executeExpansion(expansion, settlements.get(i));
        }
    }

    public ArrayList<TerrainTile> expandSettlementToMaximizeMeeplePlacement(Settlement settlement, Integer value) {
        value = 0;
        int excheck = 0;
        ArrayList<ArrayList<TerrainTile>> allexpand = new ArrayList<ArrayList<TerrainTile>>();
        ArrayList<TerrainTile> expansion1 = settlement.getExpansionTiles(TerrainType.GRASS);
        ArrayList<TerrainTile> expansion2 = settlement.getExpansionTiles(TerrainType.JUNGLE);
        ArrayList<TerrainTile> expansion3 = settlement.getExpansionTiles(TerrainType.LAKE);
        ArrayList<TerrainTile> expansion4 = settlement.getExpansionTiles(TerrainType.ROCK);
        //System.out.println("Expand test: " + expansion1.size() + " " + expansion2.size() + " " + expansion3.size() + " " + expansion4.size());
        allexpand.add(expansion1);
        allexpand.add(expansion2);
        allexpand.add(expansion3);
        allexpand.add(expansion4);
        for(int i = 0; i < allexpand.size(); i ++)
        {
            int tmpValue = 0; //ten points per tile


            for(TerrainTile checkpoint: allexpand.get(i))
            {
                //temporarily place meeples TODO
            }
            for(TerrainTile checkpoint: allexpand.get(i))
            {
                //todo maybe use a neural net here -- ignore this one if you're not Jason
                //TODO FIX THIS TO USE TEMP MEEPLES
                tmpValue += checkpoint.getLevel(); //todo how much do we value high levels
            }

            for(TerrainTile checkpoint: allexpand.get(i)) {
                //remove all temporary meeples TODO
            }
            if(tmpValue == this.getMeepleCount()){
                ArrayList<TerrainTile> expansion = allexpand.get(excheck);
                return  expansion;
            }
            if(tmpValue > value)
            {
                value = tmpValue;
                excheck = i;
            }
        }
        ArrayList<TerrainTile> expansion = allexpand.get(excheck);
        return  expansion;
    }

    public boolean expandSettlement() {
        SettlementExpansion bestOverallExpansion = new SettlementExpansion(new ArrayList<TerrainTile>(), settlements.get(0), null, false, false, false);

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

            return true;
        }

        return false;
    }

    public SettlementExpansion getBestExpansionForSettlement(Settlement settlement) {
        SettlementExpansion bestExpansion = new SettlementExpansion(new ArrayList<TerrainTile>(), settlement , null, false, false, false);
        ArrayList<SettlementExpansion> allExpansions = settlement.getAllExpansions();
        for (SettlementExpansion currentExpansion: allExpansions) {
            currentExpansion.setValue(scoreSettlementExpansion(currentExpansion));
            if (currentExpansion.getValue() > bestExpansion.getValue()) {
                bestExpansion = currentExpansion;
            }
        }
        return bestExpansion;
    }

    public boolean buildSettlement(GameMap gameMap){
        //choose the best available settlement
        //if we settle... currently the only option
        ArrayList<TerrainTile> legalSettlements = getLegalNewSettlements(gameMap);
        TerrainTile bestPlaceToSettle = getBestNewSettlement(legalSettlements);
        if(bestPlaceToSettle == null) {
            return false;
        }
        buildSettlement(bestPlaceToSettle);
        System.out.println("BUILT SETTLEMENT AT " + bestPlaceToSettle.toString());
        //activeSettlement = settlements.get(settlements.size()-1);
        //settlements = activeSettlement.combineAdjacentSettlementsForSingleTile(bestPlaceToSettle,settlements,activeSettlement);
        buildMessage = "FOUND SETTLEMENT AT " + bestPlaceToSettle.getBoardSpace().getLocation().getCubicCoordinate().toString();
        return true; //todo should there be a false?
    }

    private boolean outOfTotoroOrTigers(){
        return (totoroCount == 0 || tigerCount == 0);
    }

    private void getRidOfMeeples(){
        if(meepleCount == 1){
            buildSettlement(this.myMap);
        }
        else if(expandSettlement()){
            //successfully expands settlement.
        }
        else{
            buildSettlement(this.myMap);
        }
    }

    private String buildPhase(GameMap gameMap){

        for(Settlement settlement: settlements){
            settlement.checkSettlementsLegality();
        }


        ArrayList<HexTile> tiles = gameMap.getVisible();
        String finalMessage = "";

        if(outOfTotoroOrTigers()){
            getRidOfMeeples();
            finalMessage = buildMessage;
            return finalMessage;
        }

        if(settlements.size() > 0) { //if current player has at least one settlement already
//            if (addTotoro()) {
//                finalMessage = buildMessage;
//            }
            if (addTiger()) { //can't add totoro, add tiger
                finalMessage = buildMessage;
            }
            else if (expandSettlement()) {
                finalMessage = buildMessage;
            }
            else if (buildSettlement(gameMap)) {
                finalMessage = buildMessage;
            }

        }
        else {
            buildSettlement(gameMap);
            finalMessage = buildMessage;
        }

        for (Settlement s: settlements) {
            s.removeAnyUndergroundTiles();
        }
        return finalMessage;
    }

    //TODO add AI logic
    public String takeTurn(GameMap gameMap, TriHexTile tile){
        String result = placementPhase(gameMap, tile);
        result += " " + buildPhase(gameMap);
        return result;
    }

    //TODO this
    private int howGoodIsSettlement(TerrainTile tt, Player p){
        int value = 1;
        if(value < scoreAdjacenttoLevel1Tiles(tt))
        {
            value = scoreAdjacenttoLevel1Tiles(tt);
        }
        return value; //TODO either add logic here or find a better place to do AI stuff
        //ideas
        //get contiguos terrains of a type to see if we could expand there next turn
        //devalue these terrains if the opponent is also adjacent to them, as they may steal them from us

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
        ArrayList<Settlement> adjacentFriendlySettlementsAfterExpansion = new ArrayList<Settlement>();
        Settlement originalSettlement = settlementExpansion.getSettlementToExpand();
        Settlement settlementAfterExpansion = new Settlement(this);
        int meepleCost = settlementExpansion.getMeepleCost();

        //check if we can afford expansion
        if (meepleCost > meepleCount) {
            return 0;
        }

        //see what settlement will look like after expansion
        for (TerrainTile tt : originalSettlement.getTiles()) {
            settlementAfterExpansion.temporarilyAddToSettlement(tt);
        }
        for (TerrainTile tt : expansionTiles) {
            settlementAfterExpansion.temporarilyAddToSettlement(tt);
        }
        if (originalSettlement.hasTotoro()) {
            settlementAfterExpansion.placedTotoro();
        }
        if (originalSettlement.hasTiger()) {
            settlementAfterExpansion.placedTiger();
        }

        if (outOfTotoroOrTigers()) {
            expansionValue += meepleCost * 30;
        }
        else {
            expansionValue -= meepleCost * 5;
            if (settlementExpansion.canPlaceTotoroAfterExpansion()) {
                expansionValue += 50;
            }
            if (settlementExpansion.canPlaceTigerAfterExpansion()) {
                expansionValue += 150;
            }
            if (settlementExpansion.canPlaceTigerAfterExpansionAndTilePlacement()) {
                expansionValue += 75;
            }
        }

        expansionValue += expansionTiles.size()*10; //ten points per tile

        return expansionValue;
    }

    public void buildSettlement(TerrainTile tt) {
        if(!tt.isOccupied()) {
            placeMeeples(tt);
            Settlement settlement = new Settlement(this);
            settlement.createSettlement(tt);
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
            //getBestTotoroPlacementTile(Tplacements);
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

    private int scoreTigerPlacement(TerrainTile t) {
        int numberOfAdjacentEnemyTiles = t.getEnemyAdjacentTerrainTiles().size();
        return numberOfAdjacentEnemyTiles;
        //return 1; // TODO add AI in this method
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
    }

    public TerrainTile getBestTigerPlacementTile(ArrayList<Settlement> legalTigerSettlments)
    {
        Settlement bestSettlement = null;
        TerrainTile tigerPlacementTile = null;
        int bestScore = 0;
        for(Settlement s: legalTigerSettlments) {
            ArrayList<TerrainTile> potentialPlacements =  s.getLegalTigerTiles();
            for(TerrainTile t: potentialPlacements) {
                /*if(scoreTigerPlacement(t) > bestScore) {
                    bestSettlement = s;
                    tigerPlacementTile = t;
                }*/
                bestSettlement = s;
                tigerPlacementTile = t;
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
        if(amount > meepleCount)
            return false;
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

    //added for testing
    public boolean addSettlement(Settlement settlement) {
        if (settlement.getOwner() != this)
            return false;
        else if (settlements.contains(settlement))
            return false;
        else {
            settlements.add(settlement);
            return true;
        }

    }

    public void setSettlements(ArrayList<Settlement> settlements) {
        this.settlements = settlements;
    }

    //added for testing
    public void clearSettlements() {
        settlements.clear();
    }
}
