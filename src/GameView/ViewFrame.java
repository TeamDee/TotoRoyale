package GameView;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import GameView.Viewports.*;

/**
 * Created by jowens on 3/13/17.
 */
public class ViewFrame extends JFrame {
    static View view;

    static ViewFrame viewFrame = new ViewFrame();
    public ViewFrame() {

    }

    public void refresh() {
        this.remove(0);
        this.add(view);
    }

    public void addViewport(Viewport v) {
        view.addViewport(v);
    }

    public void addViewport(Viewport v, String BorderLayout) {
        view.addViewport(v, BorderLayout);
    }

    public static ViewFrame getInstance() { return viewFrame; }
    public void initialize() {
        // ViewportStack.add(MainMenuViewport.getInstance());
        // ViewportStack.add(SimpleStatsViewport.getInstance());
        ViewportOrder.add(ActiveGameViewport.getInstance());
        if (ActiveGameViewport.getInstance() == null) System.out.println("null");
        // ViewportStack.add(ExtendedStatsViewport.getInstance());
        // ViewportStack.add(PauseMenuViewport.getInstance());
        addViewport(ActiveGameViewport.getInstance());
    }

    public int getWidth() { return (int)this.getSize().getWidth(); }
    public int getHeight() { return (int)this.getSize().getHeight(); }
}
