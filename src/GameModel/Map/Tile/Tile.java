package GameModel.Map.Tile;

/**
 * Created by jowens on 3/8/17.
 */
public class Tile {
    int level = 0;

    public Tile(){
        //default constructor
    }

    public void setLevel(int level) {
        this. level = level;
    }

    public int getLevel(){
        return level;
    }
}
