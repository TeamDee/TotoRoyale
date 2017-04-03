package GameView;

/**
 * Created by jowens on 3/13/17.
 */
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import GameView.Viewports.*;

@SuppressWarnings("serial")
public class View extends JPanel {

    public static View view = new View();
    private ViewportOrder viewportOrder = ViewportOrder.getInstance();

    public View() {
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
    }

    public void render() {}

    public static View getInstance() {
        return view;
    }

    public void addViewport(Viewport viewport) {
        // this seems like it should work. idk. visitor pattern knock off?
        this.add(viewport);
        viewport.visit(viewportOrder);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void stylize() {
        this.setBackground(Color.black);
    }

    public void addViewport(Viewport viewport, String borderLayout) {
        System.out.println("~~~~\nAdd viewport");
        this.add(viewport);
        viewport.visit(viewportOrder);
    }
}
