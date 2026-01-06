package Client.UI;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameFrame extends JFrame{
    private ImageIcon icon = new ImageIcon("assets/ship.png");
    private MenuPanel menuPanel = new MenuPanel(this);
    private WinPanel winPanel = new WinPanel(this);
    private PlayPanel playPanel = new PlayPanel(this);
    private LosePanel losePanel = new LosePanel(this);
    private String ip;
    private int port;
    public GameFrame(){
        this.setIconImage(icon.getImage());
        this.setSize(1280,720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setTitle("Space War");
        this.add(menuPanel);
        this.add(playPanel);

        
    }
    public void setGameState(String gameState) {
        switch (gameState) {
            case "Menu":
                menuPanel.setVisible(true);
                playPanel.setVisible(false);
                break;
            case "Win":
                menuPanel.setVisible(false);
                playPanel.setVisible(false);
                this.remove(playPanel);
                this.add(winPanel);
                break;
            case "Lose":
                menuPanel.setVisible(false);
                playPanel.setVisible(false);
                this.remove(playPanel);
                this.add(losePanel);
                break;
            case "Play":
                menuPanel.setVisible(false);
                this.remove(menuPanel);
                playPanel.setVisible(true);
                playPanel.setSocketIP(ip);
                playPanel.setSocketPort(port);
                playPanel.startGameLoop();
                break;

        }
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    
    
}
