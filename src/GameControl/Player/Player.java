package GameControl.Player;

import GameControl.Placement;
import GameModel.Map.Coordinates.OffsetCoordinate;
import GameModel.Map.GameMap;
import GameModel.Map.Contiguous.Settlement;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainType;
import GameModel.Map.Tile.TerrainTile;
import GameModel.Map.Tile.VolcanoTile;
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
    private List<OffsetCoordinate> meeplePlacements;
    private List<OffsetCoordinate> totoroPlacements;
    private List<Settlement> settlements;
    private Settlement activeSettlement; //settlement we're adding stuff too

    private GameMap myMap;
    //private TerrainTile Tplacement;

    //scoring
    private int score;//, expansionWorth;
    public boolean placeTileCheck = false; //added for testing

    //random
    Random random = new Random();
    public Player(GameMap thisPlayersMap){
        totoroCount = Constants.TOTORO_PER_PLAYER;
        meepleCount = Constants.MEEPLES_PER_PLAYER;
        tigerCount = Constants.TIGER_PER_PLAYER;
        score = 0;
        settlements = new ArrayList<Settlement>();
        myMap = thisPlayersMap;
    }

    //returns true iff there are no legal build moves for the current player on the given map
    public boolean noLegalBuildMoves(GameMap map){
        // no settlements, legal totoro, or expansions

        return legalNewSettlementVisible(map) && legalTotoroPlacementVisible() && legalExpansionVisible();
    }

    private void executeExpansion(ArrayList<TerrainTile> expansion, Settlement toBeAddedTo){
        for (TerrainTile add : expansion) {
            placeMeeples(add);
            toBeAddedTo.addToSettlement(add);
            awardPoints(add.getLevel() ^ 2);
            System.out.println("Expansion added to settlment " + toBeAddedTo);
        }
    }

    private boolean legalExpansionVisible(){
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


    public void placementPhase(GameMap gameMap, TriHexTile tile){
        ArrayList<Placement> placements = gameMap.getLegalMapPlacements(tile);
        if(noLegalBuildMoves(gameMap)){

        }
        if(placements.size() == 0) {   //todo OR the seen placements aren't good enough
            placements.addAll(gameMap.getLegalTablePlacements(tile)); //note this only gets level 0 placements
            System.out.println("NO LEGAL MAP PLACEMENTS");
        }

        Placement stupidPlacement = placements.get(random.nextInt(placements.size())); //todo iterate through placements for the best option

        placeTile(gameMap, stupidPlacement);
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
            return true;
        }
        return false;
    }

    private boolean addTiger() {
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
            this.awardPoints(75);
            return true;
        }
        return false;
    }

    public boolean expandSettlement() {
        Integer tempValue = new Integer(0);
        for(int i = 0; i!= settlements.size();++i){
            ArrayList<TerrainTile> expansion = expandSettlement(settlements.get(i), tempValue);
            if (tempValue >= 50) {
                executeExpansion(expansion, settlements.get(i));
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
        if(bestPlaceToSettle == null)
            return false;
        buildSettlement(bestPlaceToSettle);

        return true; //todo should there be a false?
    }


    //TODO add AI logic
    public void takeTurn(GameMap gameMap, TriHexTile tile){
        placementPhase(gameMap, tile);

        ArrayList<HexTile> tiles = gameMap.getVisible();

        if(settlements.size() > 0) { //if current player has at least one settlement already
            if (addTotoro()) {
                //logic is done in addTotoro()
            } else if (addTiger()) { //can't add totoro, add tiger
                //logic is done in addTiger()
            } else if (expandSettlement()) {

            } else if (buildSettlement(gameMap)) {

            }
        }
        else {
            buildSettlement(gameMap);
        }
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

    @Override
    public String toString(){
        return this.name;
    }


}
