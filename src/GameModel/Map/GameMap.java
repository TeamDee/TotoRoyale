package GameModel.Map;

import GameControl.Placement;
import GameModel.Map.Coordinates.AxialCoordinate;
import GameModel.Map.Tile.HexTile;
import GameModel.Map.Tile.TerrainType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import GameModel.Map.Tile.*;

import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;

/**
 * Created by jowens on 3/8/17.
 */
public class GameMap {

    //todo decide between these two
    //private BoardSpace[][] gameBoard;
    private HashMap<AxialCoordinate, BoardSpace> gameBoard2;
    private int numberOfTriHextiles;

    private static int boardLength = 20, boardWidth = 20;

    private List<BoardSpace> activeSpaces;

    public GameMap() {
        initializeBoard();
    }

    //using axial coordinate system, initialize the board to prepare it for play
    public void initializeBoard() {
        gameBoard2 = new HashMap<AxialCoordinate, BoardSpace>();
        this.addBoardSpace(new AxialCoordinate(0,0));
        numberOfTriHextiles = 0;
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

    public void placeFirstTile(TriHexTile first) {
        System.out.println("placing first tile");
        //TODO we'll probably have to deal with orienting "north" when the opposing player starts.
        BoardSpace firstBS = gameBoard2.get(new AxialCoordinate(0,0));;
        addRadialBoardSpaces(2, firstBS);

        gameBoard2.get(firstBS.getLocation()).addTile(first.getTileOne());
        gameBoard2.get(firstBS.getLocation().getNorth()).addTile(first.getTileTwo());
        gameBoard2.get(firstBS.getLocation().getNorthEast()).addTile(first.getTileThree());

        //TODO does this work?
        addRadialBoardSpaces(2, firstBS);
        addRadialBoardSpaces(2, gameBoard2.get(firstBS.getLocation().getNorth()));
        addRadialBoardSpaces(2, gameBoard2.get(firstBS.getLocation().getNorthEast()));

        activateAdjacentBoardSpaces(gameBoard2.get(firstBS.getLocation()));
        activateAdjacentBoardSpaces(gameBoard2.get(firstBS.getLocation().getNorth()));
        activateAdjacentBoardSpaces(gameBoard2.get(firstBS.getLocation().getNorthEast()));

        numberOfTriHextiles++;
    }

    private void activateAdjacentBoardSpaces(BoardSpace center){
        gameBoard2.get( center.getLocation().getSouthWest()).activate(center, Direction.NORTHEAST);
        gameBoard2.get( center.getLocation().getSouthEast()).activate(center, Direction.NORTHWEST);
        gameBoard2.get( center.getLocation().getSouth()).activate(center, Direction.NORTH);
        gameBoard2.get( center.getLocation().getNorthWest()).activate(center, Direction.SOUTHEAST);
        gameBoard2.get( center.getLocation().getNorthEast()).activate(center, Direction.SOUTHWEST);
        gameBoard2.get( center.getLocation().getNorth()).activate(center, Direction.SOUTH);
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

            System.out.println("Searching queue");
            // iterate through all of the current Tile's neighbors
            for (Direction direction : Direction.values()) {
                BoardSpace neighbor = gameBoard2.get(current.getBoardSpace().getLocation().getByDirection(direction));
                // if the Tile is on the map and we haven't visited it already
                if(neighbor == null){
                    neighbor = new BoardSpace(current.getBoardSpace().getLocation().getByDirection(direction));
                    this.addBoardSpace(neighbor.getLocation());
                    this.connectTwoBoardSpaces(neighbor,current.getBoardSpace(), direction);
                }

                queue.add(new Distance(current.getDistance()+1,neighbor));
            }
            visited.add(current);
        }
    }

    /*
     CALL THIS INSTEAD OF DIRECTLY MODIFYING GAMEBOARD2
     */
    public void addBoardSpace(AxialCoordinate ac){
        BoardSpace newBS = new BoardSpace(ac);
        gameBoard2.put(ac, newBS);
        if(gameBoard2.get(ac.getNorth())!=null)
            connectTwoBoardSpaces(newBS, gameBoard2.get(ac.getNorth()), Direction.NORTH);
        if(gameBoard2.get(ac.getNorthEast())!=null)
            connectTwoBoardSpaces(newBS, gameBoard2.get(ac.getNorthEast()), Direction.NORTHEAST);
        if(gameBoard2.get(ac.getNorthWest())!=null)
            connectTwoBoardSpaces(newBS, gameBoard2.get(ac.getNorthWest()), Direction.NORTHWEST);
        if(gameBoard2.get(ac.getSouth())!=null)
            connectTwoBoardSpaces(newBS, gameBoard2.get(ac.getSouth()), Direction.SOUTH);
        if(gameBoard2.get(ac.getSouthEast())!=null)
            connectTwoBoardSpaces(newBS, gameBoard2.get(ac.getSouthEast()), Direction.SOUTHEAST);
        if(gameBoard2.get(ac.getSouthWest())!=null)
            connectTwoBoardSpaces(newBS, gameBoard2.get(ac.getSouthWest()), Direction.SOUTHWEST);
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

        for(BoardSpace bs: gameBoard2.values()){ //for each active board space (i.e. above a played tile, or adjacent to one)
            if(bs.isEmpty() && bs.isActive()){
                //get all placements that are possible given adjacent empty boardspaces

                //NORTH AND NORTHEAST
                boolean northLegal, northEastLegal, southEastLegal, southLegal, southWestLegal, northWestLegal;


                northLegal = legalLevel0(bs.getLocation().getNorth());
                northEastLegal = legalLevel0(bs.getLocation().getNorthEast());
                northWestLegal = legalLevel0(bs.getLocation().getNorthWest());
                southLegal = legalLevel0(bs.getLocation().getSouth());
                southEastLegal = legalLevel0(bs.getLocation().getSouthEast());
                southWestLegal = legalLevel0(bs.getLocation().getSouthWest());

                if(northLegal && northEastLegal) {
                    Placement p1 = new Placement(bs, bs.getNorth(), bs.getNorthEast(), ht1, ht2, ht3);
                    Placement p2 = new Placement(gameBoard2.get(bs.getLocation().getNorthEast()), bs, gameBoard2.get(bs.getLocation().getNorth()), ht1, ht2, ht3);
                    Placement p3 = new Placement(gameBoard2.get(bs.getLocation().getNorth()), gameBoard2.get(bs.getLocation().getNorthEast()), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(northEastLegal && southEastLegal) {
                    Placement p1 = new Placement(bs, bs.getNorthEast(), bs.getSouthEast(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getSouthEast(), bs, bs.getNorthEast(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getNorthEast(), bs.getSouthEast(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(southEastLegal && southLegal) {
                    Placement p1 = new Placement(bs, bs.getSouthEast(), bs.getSouth(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getSouth(), bs, bs.getSouthEast(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getSouthEast(), bs.getSouth(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(southLegal && southWestLegal) {
                    Placement p1 = new Placement(bs, bs.getSouth(), bs.getSouthWest(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getSouthWest(), bs, bs.getSouth(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getSouth(), bs.getSouthWest(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(southWestLegal && northWestLegal) {
                    Placement p1 = new Placement(bs, bs.getSouthWest(), bs.getNorthWest(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getNorthWest(), bs, bs.getSouthWest(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getSouthWest(), bs.getNorthWest(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                if(northWestLegal && northLegal) {
                    Placement p1 = new Placement(bs, bs.getNorthWest(), bs.getNorth(), ht1, ht2, ht3);
                    Placement p2 = new Placement(bs.getNorth(), bs, bs.getNorthWest(), ht1, ht2, ht3);
                    Placement p3 = new Placement(bs.getNorthWest(), bs.getNorth(), bs, ht1, ht2, ht3);
                    returnMe.add(p1);
                    returnMe.add(p2);
                    returnMe.add(p3);
                }
                //END NORTH AND NORTHEAST
            }
        }
        return returnMe;
    }


    /*
     * tests that
     * A. the board space exists in the gameBoard (at the give coordiate)
     * B. The Board Space is level 0
     */
    private boolean legalLevel0(AxialCoordinate ac) {
        BoardSpace BStoTest = gameBoard2.get(ac);
        boolean bsExists = !(BStoTest == null);
        boolean isBoardLevel = true;
        if(bsExists){
            if(BStoTest.getLevel() >0){
                isBoardLevel = false;
            }
        }
        if( BStoTest == null || isBoardLevel) { //tile is legal
            return true;
        }
        else {
            return false;
        }
    }
    /*  TODO
        for each of the three hextiles in the tri-hex, attempt to place them at the placeAt tile, and then check each rotation with that tile
        as center to see if such rotations are legal.
     */
    public ArrayList<Placement> getLegalPlacementsAtHexTile(TriHexTile toBePlaced, HexTile placeAt){
        ArrayList<Placement> returnMe = new ArrayList<Placement>();

        HexTile ht1,ht2,ht3;

        ht1 = toBePlaced.getTileOne();
        ht2 = toBePlaced.getTileTwo();
        ht3 = toBePlaced.getTileThree();

        if(placeAt.terrainType() == TerrainType.VOLCANO){ //bombs away
            BoardSpace mine = placeAt.getBoardSpace();// law of demeter violation
            BoardSpace north = mine.getNorth();
            BoardSpace northEast = mine.getNorthEast();
            BoardSpace northWest = mine.getNorthWest();
            BoardSpace south = mine.getSouth();
            BoardSpace southEast = mine.getSouthEast();
            BoardSpace southWest = mine.getSouthWest();


            if(north.getLevel() == mine.getLevel()){ //me, north, northeast
                if(northEast.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, north,northEast, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(northEast.getLevel() == mine.getLevel()){
                if(southEast.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, northEast,southEast, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(southEast.getLevel() == mine.getLevel()){
                if(south.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, southEast,south, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(south.getLevel() == mine.getLevel()){
                if(southWest.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, south,southWest, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(southWest.getLevel() == mine.getLevel()){
                if(northWest.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, southWest,northWest, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }
            if(northWest.getLevel() == mine.getLevel()){
                if(north.getLevel() == mine.getLevel())
                    returnMe.add(new Placement(mine, northWest,north, toBePlaced.getTileOne(), toBePlaced.getTileTwo(), toBePlaced.getTileThree()));
            }

        }
        else{ //only allow volcano tile placements on top of of other tiles
            return null;
        }

        return null;
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
    /*
     * returns all triplets of adjacent tiles that aren't all in the same tri-hex tile
     * TODO this would be useful for ignoring illegal placements
     */
//    public List<TriHexTile> sameLevelAdjacentHexesOfDifferentTriHexes() {
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
    }

    public boolean isLegalPlacement(Placement p) {
        if(p.isLevelPlacement() && p.tilesAreOfProperType()){
            return true;
        }
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
            if(!b.isEmpty())
                visible.add(b.topTile());
        }
        return visible;
    }

    public void printInfoAboutMap(){
        for(BoardSpace bs: gameBoard2.values()){
            if(bs.topTile() != null)
                System.out.println(bs.topTile() + " " + bs.getLocation());
        }
    }

    public int getNumberOfTriHextiles(){
        return numberOfTriHextiles;
    }

    /*
        given a hextile, do a BFS or DFS for all contiguos, connected tiles of the same type and return them as a list
     */
    public ArrayList<HexTile> getContiguousTerrainFromTile(TerrainTile tt){
        return null; //TODO
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
}