package GameControl;

import GameView.Viewports.MainMenuViewport;
import GameView.Viewports.Viewport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuController extends Controller {

    public Viewport view;

    public MainMenuController() {
        this.view = new MainMenuViewport(new NewGameListener(), new LoadGameListener(), new ExitGameListener());
    }

    public ActionListener getNewGameListener() {
        return new NewGameListener();
    }

    public Viewport getViewport() {
        return this.view;
    }

    public class NewGameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //System.out.println("NEW GAME");
            //GameController.getInstance().swapViews(new CharacterCreationController());
            //GameController.getInstance().swapViews(new CharacterCreationController());
        }
    }

    public class LoadGameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("LOAD GAME");
        }
    }

    public class ExitGameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
