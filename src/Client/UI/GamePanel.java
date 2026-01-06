package Client.UI;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Font;

public class GamePanel extends JPanel{
    private JLayeredPane layers = new JLayeredPane();
    public GamePanel() {
        //System.out.println("current panel : "+getClass().getSimpleName());
        this.setSize(1280, 720);
        this.setLayout(null);
        layers.setSize(1280,720);
        this.add(layers);
    }
    public void setFontComps(Font font,Component... components) {
        for (Component c:components) {
            c.setFont(font);
        }
    }
    public void addComps(int layer,Component... components) {
        for (Component c:components) {
            layers.add(c,Integer.valueOf(layer));
        }
    }
}
