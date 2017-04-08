package GameControl.Player;

import GameControl.Placement;
import GameModel.Map.BoardSpace;
import GameModel.Map.Contiguous.Settlement;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.TerrainType;
import GameModel.Map.TriHexTile;
import GameView.Map.Constants;

import java.util.ArrayList;
import java.util.List;
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
    private List<OffsetCoordinate> meeplePlacements;
    private List<OffsetCoordinate> totoroPlacements;
    private ArrayList<Settlement> settlements;
    private Settlement activeSettlement; //settlement we're adding stuff too
    private AIPlayerController AI;

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
    }

    public ArrayList<Settlement> getPlayerSettlements()
    {return settlements;}
    //returns true iff there are no legal build moves for the current player on the given map
    public boolean noLegalBuildMoves(GameMap map){
        // no settlements, legal totoro, or expansions
        return !legalNewSettlementVisible(map) && !legalTotoroPlacementVisible() && !legalExpansionVisible();
    }

    private void executeExpansion(ArrayList<TerrainTile> expansion, Settlement toBeAddedTo){
        for (TerrainTile add : expansion) {
            placeMeeples(add);
            toBeAddedTo.addToSettlement(add);
            awardPoints(add.getLevel() ^ 2);
            System.out.println("Expansion added to settlment " + toBeAddedTo);
        }
        buildMessage = "EXPAND SETTLEMENT AT " + toBeAddedTo.getSettlement().get(0).getBoardSpace().getLocation().toString();
    }

    private boolean legalExpansionVisible(){
        if(settlements.size() == 0){
            return false;
        }
        int whichsettle = random.nextInt(settlements.size());
        Integer expansionWorth = new Integer(0);
        ArrayList<TerrainTile> expansion = expandSettlement(settlements.get(whichsettle), expansionWorth);
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

        OffsetCoordinate volcanolocation = stupidPlacement.getVolcanoLocation();

        if(mapPlacementThatFixesNoBuildOptionsProblem != null){
            placeTile(gameMap, mapPlacementThatFixesNoBuildOptionsProblem);
        }
        placeTile(gameMap, stupidPlacement);
        return volcanolocation.toString();
    }
    public int scoreTilePlacement(Placement placement) {
        int score = 0;
        for (int i = 0; i < 2; i++){
            BoardSpace hex = placement.getBoardSpaces().get(i); //Each individual hex tile at its top level
            //Level Consideration
            if (hex.getLevel() == 0) { //Empty space
                score += 15;
                //return score;
            }
            if (hex.getLevel() == 1) //Nuke and potential for a tiger
                score += 20;
            if (hex.getLevel() == 2) //Causes a tiger to be place-able, high priority
                score += 50;
            if (hex.getLevel() >= 3) //There is no rel purpose after level 3, so not much priority
                score += 10;
            //Enemy Occupant
            if (hex.getLevel() >= 1){
                if ((this.isWhite() && hex.topTile().isOwnedByBlack()) || (!this.isWhite() && hex.topTile().isOwnedByWhite())) { //Enemy owns place
                    if (hex.topTile().isPartOfSettlement && hex.topTile().settlementSize >= 5) {//Don't want to make it easier for the opponent
                        if (hasAdjacentTotoro(hex.topTile()))
                            return 0;
                        else
                            score -= 100;
                    }
                    else if (hex.topTile().isPartOfSettlement && hex.topTile().settlementSize < 5)
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
                ArrayList<TerrainTile> ts = s.getSettlement();
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
                ArrayList<TerrainTile> ts = s.getSettlement();
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
                ArrayList<TerrainTile> ts = s.getSettlement();
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
                ArrayList<TerrainTile> ts = s.getSettlement();
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
                ArrayList<TerrainTile> ts = s.getSettlement();
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
                ArrayList<TerrainTile> ts = s.getSettlement();
                if(ts.contains(temp.topTile()))
                {
                    value = 20 + scoreTilePlacement(p);
                    return value;
                }
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
                    ArrayList<TerrainTile> currentSettlement = s.getSettlement();
                    HexTile temp1 = null;
                    HexTile temp2 = null;
                    if(BSLoactions.get(1).getLevel() > 0) {
                        temp1 = BSLoactions.get(1).topTile();
                    }
                    else
                    {
                        value = scoreAdjacentBoardSpaces(t,BSLoactions.get(1),s);
                    }
                    if(BSLoactions.get(2).getLevel() > 0) {
                        temp2 = BSLoactions.get(2).topTile();
                    }
                    else
                    {
                        value = scoreAdjacentBoardSpaces(t,BSLoactions.get(2),s);
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
        ArrayList<Settlement> legalSettlements = new ArrayList<Settlement>();
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
                    Settlement s = new Settlement();
                    s.addToSettlement((TerrainTile)ht);
                    legalSettlements.add(s);
                }
            }
        }
        return legalTilesToSettle;

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
            this.awardPoints(200);
            //System.out.println("Player Settlement Size Before: " + settlements.size());
            settlements = activeSettlement.combineAdjacentSettlementsforSingleTile(placeTotoroHere,settlements,activeSettlement);
            //System.out.println("Player Settlement Size After: " + settlements.size());
            buildMessage = "BUILD TOTORO SANCTUARY AT " + placeTotoroHere.getBoardSpace().getLocation().toString();
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
            settlements = activeSettlement.combineAdjacentSettlementsforSingleTile(placeTigerHere,settlements,activeSettlement);
            //System.out.println("Player Settlement Size After: " + settlements.size());
            this.awardPoints(75);
            buildMessage = "BUILD TIGER PLAYGROUND AT " + placeTigerHere.getBoardSpace().getLocation().toString();
            return true;
        }
        return false;
    }

    private void expandSettlementToMaximizeMeepleUsage(){
        Integer tempValue = new Integer(0);
        for(int i = 0; i!= settlements.size();++i){
            ArrayList<TerrainTile> expansion = expandSettlementToMaximizeMeeplePlacement(settlements.get(i), tempValue);
            executeExpansion(expansion, settlements.get(i));

        }
    }
    public ArrayList<TerrainTile> expandSettlementToMaximizeMeeplePlacement(Settlement settlement1, Integer value) {
        value = 0;
        int excheck = 0;
        ArrayList<ArrayList<TerrainTile>> allexpand = new ArrayList<ArrayList<TerrainTile>>();
        ArrayList<TerrainTile> expansion1 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.GRASS);
        ArrayList<TerrainTile> expansion2 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.JUNGLE);
        ArrayList<TerrainTile> expansion3 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.LAKE);
        ArrayList<TerrainTile> expansion4 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.ROCK);
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
            tmpValue = 0;
        }
        ArrayList<TerrainTile> expansion = allexpand.get(excheck);
        return  expansion;
    }

    public boolean expandSettlement() {
        Integer tempValue = new Integer(0);
        for(int i = 0; i!= settlements.size();++i){
            Settlement s = settlements.get(i);
            ArrayList<TerrainTile> expansion = expandSettlement(s, tempValue);
            if (tempValue >= 50) {
                executeExpansion(expansion, settlements.get(i));
                //System.out.println("Player Settlement Size Before: " + settlements.size());
                settlements = s.combineAdacentSettlementsforMultTiles(expansion,settlements,s);
                //System.out.println("Player Settlement Size After: " + settlements.size());
                buildMessage = "EXPAND SETTLEMENT AT " + s.getSettlement().get(0).getBoardSpace().getLocation().toString();
                return true;
            }
        }
        return false;
    }

    public ArrayList<TerrainTile> expandSettlement(Settlement settlement1, Integer value) {
        value = 0;
        int excheck = 0;
        ArrayList<ArrayList<TerrainTile>> allexpand = new ArrayList<ArrayList<TerrainTile>>();
        ArrayList<TerrainTile> expansion1 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.GRASS);
        ArrayList<TerrainTile> expansion2 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.JUNGLE);
        ArrayList<TerrainTile> expansion3 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.LAKE);
        ArrayList<TerrainTile> expansion4 = settlement1.getExpansionTiles(settlement1.getSettlement(), TerrainType.ROCK);
        //System.out.println("Expand test: " + expansion1.size() + " " + expansion2.size() + " " + expansion3.size() + " " + expansion4.size());
        allexpand.add(expansion1);
        allexpand.add(expansion2);
        allexpand.add(expansion3);
        allexpand.add(expansion4);
        for(int i = 0; i < allexpand.size(); i ++)
        {
            int tmpValue = settlement1.getSettlementSize()*10; //ten points per tile


            for(TerrainTile checkpoint: allexpand.get(i))
            {
                //temporarily place meeples TODO
            }
            for(TerrainTile checkpoint: allexpand.get(i))
            {

                //todo maybe use a neural net here -- ignore this one if you're not Jason
                //TODO FIX THIS TO USE TEMP MEEPLES
                tmpValue += 10;
                tmpValue += checkpoint.getLevel()^2; //todo how much do we value high levels
                if(checkpoint.getBoardSpace().hasEmptyAdjacentLevel3()) {
                    tmpValue += 20; //todo make this number more accurately reflect the value of an adjacent level 3 when expanding
                }

            }
            if(allexpand.get(i).size() + settlement1.getSettlementSize() >= 5){
                tmpValue += 50;
            }
            if(allexpand.get(i).size() >= 2){
                tmpValue += 50;
            }

            for(TerrainTile checkpoint: allexpand.get(i)) {
                //remove all temporary meeples TODO
            }
            if(tmpValue > value)
            {
                value = tmpValue;
                excheck = i;
            }
            tmpValue = 0;
        }
        ArrayList<TerrainTile> expansion = allexpand.get(excheck);
        return  expansion;
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
        activeSettlement = settlements.get(settlements.size()-1);
        settlements = activeSettlement.combineAdjacentSettlementsforSingleTile(bestPlaceToSettle,settlements,activeSettlement);
        buildMessage = "FOUND SETTLEMENT AT " + bestPlaceToSettle.getBoardSpace().getLocation().toString();
        return true; //todo should there be a false?
    }

    private boolean outOfTotoroOrTigers(){
        return (totoroCount == 0 || tigerCount == 0);
    }

    private void getRidOfMeeples(){
        if(meepleCount == 1){
            buildSettlement(this.myMap);
        }
        else
            this.expandSettlementToMaximizeMeepleUsage();
    }

    private String buildPhase(GameMap gameMap){
        ArrayList<HexTile> tiles = gameMap.getVisible();
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
            else if (addTiger()) { //can't add totoro, add tiger
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
        return 1; //TODO either add logic here or find a better place to do AI stuff
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
    }

    public void buildSettlement(TerrainTile tt) {
        if(tt.getMeepleCount() == 0) {
            tt.placeMeeple(this);
            Settlement settlement = new Settlement();
            settlement.createSettlement(tt);
            settlements.add(settlement);
            awardPoints(1);
        }
    }


    /*
        Calls settlement and contiguous UnoccuppiedTerrainTyesTiles but DOES NOT places meeples on legal tiles of same terrain
        TODO this is currently dumb AI
     */

    public void placeTotoro(TerrainTile tt) {
        tt.placeTotoro(this);
        removeTotoro(1);
    }
    public boolean canPlaceTotoro(Settlement settlement)
    {
        if(settlement.getSettlementSize()<5 || settlement.hasTotoro()) {
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
        return 1; // TODO add AI in this method
    }

    private int scoreTigerPlacement(TerrainTile t) {
        return 1; // TODO add AI in this method
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
                if(scoreTigerPlacement(t) > bestScore) {
                    bestSettlement = s;
                    tigerPlacementTile = t;
                }
            }
        }
        if(activeSettlement ==null || tigerPlacementTile ==null) {
            System.out.println("CHECK getBestTotoroPlacementTile() in PLayer.java");
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

    public void nukeSettlements(TerrainTile nukedTile) {
        Settlement settlementToNuke = getSettlementContaining(nukedTile);
        ArrayList<Settlement> newSettlements = settlementToNuke.getSplitSettlementsAfterNuke(nukedTile);
        settlements.remove(settlementToNuke);
        settlements.addAll(newSettlements);
    }

    public Settlement getSettlementContaining(TerrainTile tt) {
        for (Settlement s : settlements) {
            if (s.contains(tt));
            return s;
        }
        return null;
    }
}
