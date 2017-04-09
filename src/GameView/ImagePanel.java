import GameView.ImagePaths;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

    Image image;

    public ImagePanel() {
        image = Toolkit.getDefaultToolkit().createImage(ImagePaths.TIGER_WHITE_G);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

}