package Client.Handlers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.SwingUtilities;

import Client.Entities.MainPlayer;
import Client.UI.PlayPanel;

public class MouseHandler implements MouseListener,MouseMotionListener{
    private PlayPanel gamePanel;
    private MainPlayer player;

    public MouseHandler(PlayPanel playPanel, MainPlayer player) {
        this.gamePanel = playPanel;
        this.player = player;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMovement(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseMovement(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gamePanel.grabFocus();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseClicked(e,true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseClicked(e,false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    private void mouseClicked(MouseEvent e,boolean clicked){
        if(SwingUtilities.isRightMouseButton(e)){
            player.setRightClick(clicked);
        }
        else{
            player.setLeftClick(clicked);
        }
    }
    private void mouseMovement(MouseEvent e){
        player.setTargetX(e.getX());
        player.setTargetY(e.getY());
    }
    
}
