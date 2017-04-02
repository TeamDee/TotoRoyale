package GameControl.Player;

/**
 * Created by jowens on 3/10/17.
 */
public class WhitePlayer extends Player {

    public WhitePlayer(){
        this("WhitePlayer");
    }

    public WhitePlayer(String name){
        super();
        this.name = name;
    }

    public boolean isWhite(){
        return true;
    }
}
