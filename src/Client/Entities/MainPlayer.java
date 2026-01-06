package Client.Entities;

import java.awt.Color;
import java.awt.Graphics;

import java.util.HashMap;
import java.util.Random;


public class MainPlayer extends Player {
    private final int SPEED = 3;
    private boolean upPressed,downPressed,leftPressed,rightPressed;
    

    public MainPlayer(Color color, HashMap<String, Player> playerList) {
        super(color,playerList);
        setX(new Random().nextInt(900));
        setY(new Random().nextInt(500));    
    }

    @Override
    public void render(Graphics g) {
        updateKeyAction();
        super.render(g);
    }

    public boolean isUpPressed() {
        return upPressed;
    }
    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }
    public boolean isDownPressed() {
        return downPressed;
    }
    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }
    public boolean isLeftPressed() {
        return leftPressed;
    }
    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }
    public boolean isRightPressed() {
        return rightPressed;
    }
    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }
    
    @Override
    public double getAngle() {
        double Vx = getTargetX() - getCenterWidth();
        double Vy = getTargetY() - getCenterHeight();
        double degree = 0.0;
        try {
            degree = Math.atan(Vy / Vx);
        } catch (ArithmeticException ignored) {
        }
        super.setAngle((Vx < 0.0)? (-Math.PI + degree):degree);
        return super.getAngle();
    }

    public void updateKeyAction() {
        if (upPressed) {
            setY((getY()>0)? getY()-SPEED:getY());
        }
        if (downPressed) {
            setY((getY() + getHeight() < 690)? getY()+SPEED:getY());
        }
        if (leftPressed) {
            setX((getX()>0)? getX()-SPEED:getX());
        }
        if (rightPressed) {
            setX((getX() + getWidth() < 1270)? getX()+SPEED:getX());
        }
    }

    
    

}
