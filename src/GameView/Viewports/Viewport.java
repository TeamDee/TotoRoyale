package GameView.Viewports;

import javax.swing.JPanel;

public abstract class Viewport extends JPanel {

    public abstract void visit(ViewportOrder viewportStack);
    // abstract static Viewport getInstance();
}
