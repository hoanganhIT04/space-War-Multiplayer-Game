package Client.UI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class LosePanel extends GamePanel {
    ImageIcon iconBack = new ImageIcon("assets/backBtn.png");
    ImageIcon iconBg = new ImageIcon("assets/endBg.png");
    JButton backBtn = new JButton();
    JLabel  bg = new JLabel();

    public LosePanel(GameFrame gameFrame) {
        bg.setBounds(0,0,1280,720);
        bg.setIcon(iconBg);
        backBtn.setBounds(98, 517, 192, 94);
        backBtn.setIcon(iconBack);
        addComps(2, bg);
        addComps(3,backBtn);
        

        backBtn.addActionListener(e -> {
            GameFrame newGame = new GameFrame();
            newGame.setGameState("Menu");
            gameFrame.dispose();
        });
    }
    
}
