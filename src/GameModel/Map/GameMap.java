package GameModel.Map;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainType;
import GameModel.Map.Tile.VolcanoTile;

import java.util.*;

/**
 * Created by jowens on 3/8/17.
 */
public class GameMap {

    private HashMap<AxialCoordinate, BoardSpace> gameBoard2;
    private ArrayList<TriHexTile> playedTriHexTiles = new ArrayList<TriHexTile>();
    private int numberOfTriHextiles;
    private boolean firstTurn = true;

    private List<BoardSpace> activeSpaces;

    public GameMap() {
        initializeBoard();
    }

    //using axial coordinate system, initialize the board to prepare it for play
    public void initializeBoard() {
        gameBoard2 = new HashMap<AxialCoordinate, BoardSpace>();
        AxialCoordinate originLocation = new AxialCoordinate(0,0);
        BoardSpace originBS = this.addBoardSpace(originLocation);

        this.addBoardSpace(originLocation);
        this.addRadialBoardSpaces(2,originBS);
        this.connectAdjacentBoardSpaces(originBS);
        this.activateAdjacentBoardSpaces(originBS);


        this.connectAdjacentBoardSpaces(originBS.getNorth());
        this.connectAdjacentBoardSpaces(originBS.getNorthEast());
        this.connectAdjacentBoardSpaces(originBS.getNorthWest());
        this.connectAdjacentBoardSpaces(originBS.getSouth());
        this.connectAdjacentBoardSpaces(originBS.getSouthWest());
        this.connectAdjacentBoardSpaces(originBS.getSouthEast());

        originBS.activate();
        numberOfTriHextiles = 0;

        for(BoardSpace bs:gameBoard2.values()){
            connectAdjacentBoardSpaces(bs);
        }
    }

    //call this whenever you place a hextile on an empty boardspace.
    public void addAdjacentBoardSpaces(AxialCoordinate initial){
        //given an axial coordinate
        //     check to see if adjacent boardspaces are active
        //          if they are not, add them to board map
        BoardSpace newBS;
        BoardSpace initialBS = gameBoard2.get(initial);

        if(gameBoard2.get(initial.getNorth()) == null){ //north
            this.addBoardSpace(initial.getNorth());
        }
        initialBS.setNorth(gameBoard2.get(initial.getNorth()));

        if(gameBoard2.get(initial.getNorthEast()) == null){
            this.addBoardSpace(initial.getNorthEast());
        }
        initialBS.setNorthEast(gameBoard2.get(initial.getNorthEast()));

        if(gameBoard2.get(initial.getNorthWest()) == null){
            this.addBoardSpace(initial.getNorthWest());
        }
        initialBS.setNorthWest(gameBoard2.get(initial.getNorthWest()));

        if(gameBoard2.get(initial.getSouth()) == null){
            this.addBoardSpace(initial.getSouth());
        }
        initialBS.setSouth(gameBoard2.get(initial.getSouth()));
        if(gameBoard2.get(initial.getSouthEast()) == null){
            this.addBoardSpace(initial.getSouthEast());
        }
        initialBS.setSouthEast(gameBoard2.get(initial.getSouthEast()));
        if(gameBoard2.get(initial.getSouthWest()) == null){
            this.addBoardSpace(initial.getSouthWest());
        }
        initialBS.setSouthWest(gameBoard2.get(initial.getSouthWest()));
    }


    private void connectTwoBoardSpaces(BoardSpace bs1, BoardSpace bs2, Direction bs1TObs2){
        switch (bs1TObs2){
            case NORTH:
                bs1.setNorth(bs2);
                bs2.setSouth(bs1);
                break;
            case NORTHEAST:
                bs1.setNorthEast(bs2);
                bs2.setSouthWest(bs1);
                break;
            case SOUTHEAST:
                bs1.setSouthEast(bs2);
                bs2.setNorthWest(bs1);
                break;
            case SOUTH:
                bs1.setSouth(bs2);
                bs2.setNorth(bs1);
                break;
            case SOUTHWEST:
                bs1.setSouthWest(bs2);
                bs2.setNorthEast(bs1);
                break;
            case NORTHWEST:
                bs1.setNorthWest(bs2);
                bs2.setSouthEast(bs1);
                break;
        }
    }

    public void connectAdjacentBoardSpaces(BoardSpace initial){
        AxialCoordinate initialLocation = initial.getLocation();

        BoardSpace north = gameBoard2.get(initialLocation.getNorth());
        BoardSpace northEast = gameBoard2.get(initialLocation.getNorthEast());
        BoardSpace northWest = gameBoard2.get(initialLocation.getNorthWest());
        BoardSpace south = gameBoard2.get(initialLocation.getSouth());
        BoardSpace southEast = gameBoard2.get(initialLocation.getSouthEast());
        BoardSpace southWest = gameBoard2.get(initialLocation.getSouthWest());

        if(north!=null)
            connectTwoBoardSpaces(initial, north, Direction.NORTH);
        if(northEast!=null)
            connectTwoBoardSpaces(initial, northEast, Direction.NORTHEAST);
        if(northWest!=null)
            connectTwoBoardSpaces(initial, northWest, Direction.NORTHWEST);
        if(south!=null)
            connectTwoBoardSpaces(initial, south, Direction.SOUTH);
        if(southEast!=null)
            connectTwoBoardSpaces(initial, southEast, Direction.SOUTHEAST);
        if(southWest!=null)
            connectTwoBoardSpaces(initial, southWest, Direction.SOUTHWEST);
    }

//    public void placeFirstTile(TriHexTile first) {
//        System.out.println("placing first tile");
//        //TODO we'll probably have to deal with orienting "north" when the opposing player starts.
//        BoardSpace firstBS = gameBoard2.get(new AxialCoordinate(0,0));;
//        addRadialBoardSpaces(2, firstBS);
//
//        BoardSpace current = gameBoard2.get(firstBS.getLocation());
//        current.addTile(first.getTileOne());
//        first.getTileOne().setBoardSpace(current);
//        current = gameBoard2.get(firstBS.getLocation().getNorth());
//        current.addTile(first.getTileTwo());
//        first.getTileTwo().setBoardSpace(current);
//        current = gameBoard2.get(firstBS.getLocation().getNorthEast());
//        current.addTile(first.getTileThree());
//
//        //TODO does this work?
//        addRadialBoardSpaces(2, firstBS);
//        addRadialBoardSpaces(2, gameBoard2.get(firstBS.getLocation().getNorth()));
//        addRadialBoardSpaces(2, gameBoard2.get(firstBS.getLocation().getNorthEast()));
//
//        activateAdjacentBoardSpaces(gameBoard2.get(firstBS.getLocation()));
//        activateAdjacentBoardSpaces(gameBoard2.get(firstBS.getLocation().getNorth()));
//        activateAdjacentBoardSpaces(gameBoard2.get(firstBS.getLocation().getNorthEast()));
//
//        numberOfTriHextiles++;
//    }

    private void activateAdjacentBoardSpaces(BoardSpace center){
        BoardSpace north = gameBoard2.get( center.getLocation().getNorth());
        BoardSpace northEast = gameBoard2.get( center.getLocation().getNorthEast());
        BoardSpace northWest = gameBoard2.get( center.getLocation().getNorthWest());
        BoardSpace south = gameBoard2.get( center.getLocation().getSouth());
        BoardSpace southEast = gameBoard2.get( center.getLocation().getSouthEast());
        BoardSpace southWest = gameBoard2.get( center.getLocation().getSouthWest());

        southWest.activate(center, Direction.NORTHEAST);
        southEast.activate(center, Direction.NORTHWEST);
        south.activate(center, Direction.NORTH);
        northWest.activate(center, Direction.SOUTHEAST);
        northEast.activate(center, Direction.SOUTHWEST);
        north.activate(center, Direction.SOUTH);

//        connectAdjacentBoardSpaces(north);
//        connectAdjacentBoardSpaces(northEast);
//        connectAdjacentBoardSpaces(northWest);
//        connectAdjacentBoardSpaces(south);
//        connectAdjacentBoardSpaces(southWest);
//        connectAdjacentBoardSpaces(southEast);
    }

    public void addRadialBoardSpaces(int radius, BoardSpace origin){
        if (radius < 0)
            return;

        // spacesInRadius is the list of all bses being added, or found
        List<BoardSpace> spacesInRadius = new ArrayList<BoardSpace>();

        // queue will keep track of which BoardSpace to BFS out of next
        Queue<Distance> queue = new LinkedList<Distance>();

        // visited keeps track of which BoardSpaces we have already visited
        HashSet<Distance> visited = new HashSet<Distance>();

        // add the center BoardSpace to spacesInRadius and visited
        spacesInRadius.add(origin);
        //visited.add(origin);
        queue.add(new Distance(0, origin));

        while (!queue.isEmpty()) {
            Distance current = queue.remove();
            if (current.getDistance() >= radius || visited.contains(current)) //if we've reached the radius or we've already visited, skip
                continue;

            // iterate through all of the current Tile's neighbors
            for (Direction direction : Direction.values()) {
                BoardSpace bs = current.getBoardSpace();
                AxialCoordinate location = bs.getLocation();
                AxialCoordinate neighborLocation = location.getByDirection(direction);
                BoardSpace neighbor = gameBoard2.get(neighborLocation);
                // if the Tile is on the map and we haven't visited it already
                if(neighbor == null){
                    neighbor = addBoardSpace(neighborLocation);
                }

                connectAdjacentBoardSpaces(neighbor);
                queue.add(new Distance(current.getDistance()+1,neighbor));
            }
            visited.add(current);
        }
    }

    /*
     CALL THIS INSTEAD OF DIRECTLY MODIFYING GAMEBOARD2
     */
    public BoardSpace addBoardSpace(AxialCoordinate ac){
        BoardSpace newBS = new BoardSpace(ac, this);
        gameBoard2.put(ac, newBS);
        return newBS;
    }
    //TODO this currently doesn't work
    /*
     * idea:
     *  1. get legal placements above level 1
     *  2. get legal board space placements
     */
    public ArrayList<Placement> getLegalTablePlacements(TriHexTile tht) {
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        HexTile ht1,ht2;
        VolcanoTile ht3;



        //hextiles contained in tri-hex to be placed
        ht1 = tht.getTileOne();
        ht2 = tht.getTileTwo();
        ht3 = tht.getTileThree();

        if(isFirstTurn()){
            firstTurn = false;
            BoardSpace bs = gameBoard2.get(new AxialCoordinate(0,0));

            AxialCoordinate location =  bs.getLocation();
            BoardSpace north = gameBoard2.get(location.getNorth());
            BoardSpace northeast = gameBoard2.get(location.getNorthEast());
            BoardSpace northwest = gameBoard2.get(location.getNorthWest());
            BoardSpace south = gameBoard2.get(location.getSouth());
            BoardSpace southeast = gameBoard2.get(location.getSouthEast());
            BoardSpace southwest = gameBoard2.get(location.getSouthWest());
            returnMe.add(new Placement(bs, north, northeast, ht1, ht2, ht3));
            returnMe.add(new Placement(bs, northeast, southeast, ht1, ht2, ht3));
            returnMe.add(new Placement(bs, southeast, south, ht1, ht2, ht3));
            returnMe.add(new Placement(bs, south, southwest, ht1, ht2, ht3));
            returnMe.add(new Placement(bs, southwest, northwest, ht1, ht2, ht3));
            returnMe.add(new Placement(bs, northwest, north, ht1, ht2, ht3));
            return returnMe;
        }
        for(BoardSpace bs: gameBoard2.values()){ //for each active board space (i.e. above a played tile, or adjacent to one)
            AxialCoordinate location =  bs.getLocation();
            BoardSpace north = gameBoard2.get(location.getNorth());
            BoardSpace northeast = gameBoard2.get(location.getNorthEast());
            BoardSpace northwest = gameBoard2.get(location.getNorthWest());
            BoardSpace south = gameBoard2.get(location.getSouth());
            BoardSpace southeast = gameBoard2.get(location.getSouthEast());
            BoardSpace southwest = gameBoard2.get(location.getSouthWest());

            if(bs.isEmpty() && bs.isActive() ){
                //get all placements that are possible given adjacent empty boardspaces

                //NORTH AND NORTHEAST
                boolean northLegal, northEastLegal, southEastLegal, southLegal, southWestLegal, northWestLegal;


                northLegal = legalLevel0(north.getLocation());
                northEastLegal = legalLevel0(northeast.getLocation());
                northWestLegal = legalLevel0(northwest.getLocation());
                southLegal = legalLevel0(south.getLocation());
                southEastLegal = legalLevel0(southeast.getLocation());
                southWestLegal = legalLevel0(southwest.getLocation());


                if(northLegal && northEastLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, north, northeast, ht1,ht2,ht3));
                }
                if(northEastLegal && southEastLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, northeast, southeast, ht1,ht2,ht3));
                }
                if(southEastLegal && southLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, southeast, south, ht1, ht2, ht3));
                }
                if(southLegal && southWestLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, south, southwest, ht1,ht2,ht3));
                }
                if(southWestLegal && northWestLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, southwest, northwest, ht1,ht2,ht3));
                }
                if(northWestLegal && northLegal) {
                    returnMe.addAll(getAllPlacementsAtThreeBoardSpaces(bs, northwest, northeast, ht1,ht2,ht3));
                }
            }
        }

        return returnMe;
    }

    public ArrayList<Placement> getAllPlacementsAtThreeBoardSpaces(BoardSpace b1, BoardSpace b2, BoardSpace b3, HexTile ht1, HexTile ht2, HexTile ht3){
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        Placement p1 = new Placement(b1, b2, b3, ht1, ht2, ht3);
        Placement p2 = new Placement(b3, b1, b2, ht1, ht2, ht3);
        Placement p3 = new Placement(b2,b3, b1, ht1, ht2, ht3);
        returnMe.add(p1);
        returnMe.add(p2);
        returnMe.add(p3);
        return returnMe;
    }

    protected boolean isFirstTurn(){
        return firstTurn;
    }

    /*
     * tests that
     * A. the board space exists in the gameBoard (at the give coordiate)
     * B. The Board Space is level 0
     */
    private boolean legalLevel0(AxialCoordinate ac) {
        BoardSpace BStoTest = gameBoard2.get(ac);
        return BStoTest.isEmpty();
    }


    /*  TODO
        for each of the three hextiles in the tri-hex, attempt to place them at the placeAt tile, and then check each rotation with that tile
        as center to see if such rotations are legal.
     */
    private ArrayList<Placement> getLegalPlacementsAtHexTile(TriHexTile toBePlaced, HexTile placeAt){
        ArrayList<Placement> returnMe = new ArrayList<Placement>();

        HexTile ht1,ht2,ht3;

        ht1 = toBePlaced.getTileOne();
        ht2 = toBePlaced.getTileTwo();
        ht3 = toBePlaced.getTileThree();

        if(placeAt.terrainType() == TerrainType.VOLCANO){ //bombs away
            BoardSpace mine = placeAt.getBoardSpace();
            BoardSpace north = mine.getNorth();
            BoardSpace northEast = mine.getNorthEast();
            BoardSpace southEast = mine.getSouthEast();
            BoardSpace south = mine.getSouth();
            BoardSpace southWest = mine.getSouthWest();
            BoardSpace northWest = mine.getNorthWest();

            if(north.hasTile() && northEast.hasTile()) {
                if(canPlaceOnHexTiles(placeAt, north.topTile(), northEast.topTile()))
                        returnMe.add(new Placement(mine, north, northEast, ht1, ht2, ht3));
            }
            if(northEast.hasTile() && southEast.hasTile()) {
                if(canPlaceOnHexTiles(placeAt, northEast.topTile(), southEast.topTile()))
                    returnMe.add(new Placement(mine, northEast, southEast, ht1, ht2, ht3));
            }
            if(southEast.hasTile() && south.topTile() != null) {
                if(canPlaceOnHexTiles(placeAt, southEast.topTile(), south.topTile()))
                    returnMe.add(new Placement(mine, southEast, south, ht1, ht2, ht3));
            }
            if(south.topTile() != null && southWest.topTile() != null) {
                if(canPlaceOnHexTiles(placeAt, south.topTile(), southWest.topTile()))
                    returnMe.add(new Placement(mine, south, southWest, ht1, ht2, ht3));
            }
            if(southEast.topTile() != null && northWest.topTile() != null) {
                if(canPlaceOnHexTiles(placeAt, southEast.topTile(), northWest.topTile()))
                    returnMe.add(new Placement(mine, southEast, southWest, ht1, ht2, ht3));
            }
            if(northWest.topTile() != null && north.topTile() != null) {
                if(canPlaceOnHexTiles(placeAt, northWest.topTile(), north.topTile()))
                    returnMe.add(new Placement(mine, northWest, north, ht1, ht2, ht3));
            }
        }
        else{ //only allow volcano tile placements on top of of other tiles
            return null;
        }

        return returnMe;
    }

    public boolean canPlaceOnHexTiles(HexTile ht1, HexTile ht2, HexTile ht3) {
        boolean areSameLevel = ht1.getLevel() == ht2.getLevel() && ht2.getLevel() == ht3.getLevel();
        boolean areNotInSameTriHexTile = !(ht1.getTriHexTile() == ht2.getTriHexTile() && ht2.getTriHexTile() == ht3.getTriHexTile());
        boolean doNotContainTotorosOrTigers = !ht1.hasTotoro() && ! ht1.hasTiger() && !ht2.hasTotoro() && !ht2.hasTiger() && !ht3.hasTotoro() && !ht3.hasTiger();
        boolean doNotContainSize1Settlements = !containsSize1Settlement(ht1) && !containsSize1Settlement(ht2) && !containsSize1Settlement(ht3);
        return areSameLevel && areNotInSameTriHexTile;//&& doNotContainSize1Settlements;// && doNotContainTotorosOrTigers && doNotContainSize1Settlements;
    }

    public boolean containsSize1Settlement(HexTile ht) {
        if (!ht.isOccupied()) {
            return false;
        }
        else {
            BoardSpace myBoardSpace = ht.getBoardSpace();
            BoardSpace[] neighborBoardSpaces = new BoardSpace[6];
            neighborBoardSpaces[0] = myBoardSpace.getNorth();
            neighborBoardSpaces[1] = myBoardSpace.getNorthEast();
            neighborBoardSpaces[2] = myBoardSpace.getSouthEast();
            neighborBoardSpaces[3] = myBoardSpace.getSouth();
            neighborBoardSpaces[4] = myBoardSpace.getSouthWest();
            neighborBoardSpaces[5] = myBoardSpace.getNorthWest();
            if (ht.isOwnedByWhite()) {
                for (BoardSpace neighborBoardSpace : neighborBoardSpaces) {
                    if (neighborBoardSpace.hasTile()) {
                        HexTile neighborHexTile = neighborBoardSpace.topTile();
                        if (neighborHexTile.isOwnedByWhite())
                            return false;
                    }
                }
            }
            else {//black
                for (BoardSpace neighborBoardSpace : neighborBoardSpaces) {
                    if (neighborBoardSpace.hasTile()) {
                        HexTile neighborHexTile = neighborBoardSpace.topTile();
                        if (neighborHexTile.isOwnedByBlack())
                            return false;
                    }
                }
            }
            return true;
        }
    }

    /*
     * All legal plaaements > 0
     */
    public ArrayList<Placement> getLegalMapPlacements(TriHexTile tht){
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        for(BoardSpace bs: gameBoard2.values()){
            HexTile thisTile = bs.topTile();
            if(thisTile != null){
                if(thisTile.terrainType() == TerrainType.VOLCANO)
                    returnMe.addAll(getLegalPlacementsAtHexTile(tht, thisTile));
            }
        }
        return returnMe;
    }
    /* returns all triplets of adjacent tiles that aren't all in the same tri-hex tile
     * TODO this would be useful for ignoring illegal placements
     */


//    public List<HexTile> sameLevelAdjacentHexesOfDifferentTriHexes() {
//        List<HexTile> returnMe;
//        for
//    }

    public ArrayList<Placement> getAllLegalPlacements(TriHexTile tht){
        ArrayList<Placement> returnMe = new ArrayList<Placement>();
        returnMe.addAll(getLegalMapPlacements(tht));
        returnMe.addAll(getLegalTablePlacements(tht));
        return returnMe;
    }



    /*
        takes a placement object and implements it's effects on the board
     */
    public void implementPlacement(Placement p){
        p.place();
        //System.out.println(p.isLegalPlacement(p));
        for(BoardSpace b: p.getBoardSpaces()){
            addRadialBoardSpaces(2,b);
            this.activateAdjacentBoardSpaces(b);
        }
        this.playedTriHexTiles.add(p.getBoardSpaces().get(0).topTile().getTriHexTile());

    }

    public boolean isLegalPlacement(Placement p) {
        if(p.isLevelPlacement() && p.isOverlapping() && !p.volcanoMatch())
            return true;
        else
            return false;
    }

    /*
        returns a list of all non-null, top-level hextiles on the board (i.e. tiles that have been placed)
     */
    public ArrayList<HexTile> getVisible() {
        ArrayList<HexTile> visible =new ArrayList<HexTile>();
        Collection<BoardSpace> bs = gameBoard2.values();
        for(BoardSpace b: bs){
            if(b.hasTile())
                visible.add(b.topTile());
        }
        return visible;
    }

    public HexTile getVisibleAtAxialCoordinate(AxialCoordinate axialCoordinate) {
        return gameBoard2.get(axialCoordinate).topTile();
    }

    public void printInfoAboutMap(){
        for(TriHexTile tht: this.playedTriHexTiles)
        {
            HexTile curr;
            curr = tht.getTileOne();
            if(curr.getLevel()>1) {
                System.out.println(curr + "\n\t Location: " + curr.getLocation() + "\n\t MeepleCount: " + curr.getMeepleCount() + "\n\t Level: " + curr.getLevel());
                curr = tht.getTileTwo();
                System.out.println(curr + "\n\t Location: " + curr.getLocation() + "\n\t MeepleCount: " + curr.getMeepleCount() + "\n\t Level: " + curr.getLevel());
                curr = tht.getTileThree();
                System.out.println(curr + "\n\t Location: " + curr.getLocation() + "\n\t MeepleCount: " + curr.getMeepleCount() + "\n\t Level: " + curr.getLevel());
            }
        }
    }

    public int getNumberOfTriHextiles(){
        return numberOfTriHextiles;
    }

    /*
        given a hextile, do a BFS or DFS for all contiguos, connected tiles of the same type and return them as a list
     */
    public ContiguousTerrainTypeTiles getContiguousTerrainFromTile(HexTile ht){
        return new ContiguousTerrainTypeTiles(ht);
    }

    public int getNumberOfBoardSpaces(){
        return gameBoard2.size();
    }
}



 class Distance{
    private BoardSpace mine;
    private int distance = 0;
    public Distance(int distance, BoardSpace me){
        this.distance = distance;
        mine = me;
    }
    public BoardSpace getBoardSpace(){
        return mine;
    }
    public int getDistance(){
        return distance;
    }

    @Override
     public int hashCode(){
        return mine.getLocation().hashCode();
    }
}