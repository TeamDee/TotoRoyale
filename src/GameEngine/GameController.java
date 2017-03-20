package GameEngine;

import java.util.Stack;

import javax.swing.JFrame;

import GameControl.Controller;
import GameModel.Map.GameMap;
import GameView.Viewports.Viewport;
import GameControl.Player.PlayerController;
import GameControl.Player.*;
public class GameController {

    private Stack<Controller> stack;
    private Viewport activeViewport;
    private Controller activeController;
    private JFrame frame;

    public static GameController gameController = new GameController();

    public GameController() {

        stack = new Stack<Controller>();
    }

    public static GameController getInstance() {
        return gameController;
    }

    public void swapViews(Controller c) {
        this.frame.setVisible(false);
        Viewport v = c.getViewport();
        this.frame.remove(activeViewport);
        activeViewport = v;
        activeController = c;
        this.frame.add(activeViewport);
        this.frame.setVisible(true);
    }

    public void initViewControllerInteractions(Player player){
        this.frame = new JFrame();
        this.frame.setExtendedState(this.frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.activeController = new PlayerController(player);
        this.activeViewport = this.activeController.getViewport();
        this.frame.add(this.activeViewport);
        this.frame.setVisible(true);
    }

    public void addToStack(Controller controller) {
        this.stack.push(controller);
    }

    public Controller removeFromStack() {
        return this.stack.pop();
    }

    public Controller getActiveController() {
        return this.activeController;
    }
}