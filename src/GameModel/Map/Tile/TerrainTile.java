package GameModel.Map.Tile;
import GameControl.Player.BlackPlayer;
import GameControl.Player.Player;
import GameControl.Player.WhitePlayer;
import GameModel.Map.TriHexTile;
import GameView.Map.TerrainView;
import GameView.Tileables.MeepleView;

import java.awt.image.BufferedImage;

/**
 * Terrain tiles can hold meeples and totoros.
 * Created by jowens on 3/8/17.
 */


public abstract class TerrainTile extends HexTile {

    protected TerrainView myView;
    protected TerrainTile me;

    public TerrainTile(TriHexTile compositor){

        this.triHexTile = compositor;
    }
    public TerrainTile(){

        //System.out.println("warning: building a terrainTile without supplying a compositor");
    }



    public boolean placeMeeple(Player p){
        if(getMeepleCount() != 0) //terrain already has meeples
            return false;
        else {
            if(p.removeMeeples(getLevel())) {  //if the player has enough meeples, remove them and award points
                p.awardPoints(getLevel() * getLevel());
                meepleCount = getLevel();
                myView.addToList(new MeepleView(getLevel(),p));
                this.owner = p;
                myView.visit(this);
                owner = p;
            }
            else
                return false;
        }
        return true;
    }

    public boolean placeTotoro(Player owner) {
        if (isOccupied()) {
            return false;
        }
        else {
            this.owner = owner;
            hasTotoro = true;
            return true;
        }
    }

    public boolean placeTiger(Player owner) {
        if (isOccupied()) {
            return false;
        }
        else {
            this.owner = owner;
            hasTiger = true;
            return true;
        }
    }



    public boolean ofSameType(VolcanoTile vt){
        return false;
    }
    public boolean ofSameType(TerrainTile tt){
        return myType == tt.getTerrain();
    }

    public TerrainType getTerrain(){
        return myType;
    }

    //this is the OOPy way of determining terrain type, imo
    public boolean isGrass(){
        return false;
    }
    public boolean isLake(){
        return false;
    }
    public boolean isRock(){
        return false;
    }
    public boolean isJungle(){
        return false;
    }

    @Override
    public TerrainView getTileView(){
        return myView;
    }
}

