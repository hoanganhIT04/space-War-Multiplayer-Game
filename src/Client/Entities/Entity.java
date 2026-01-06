package Client.Entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public abstract class Entity {
    
    private String ID;
    private int x, y,targetX,targetY,size;
    private double angle = 0,scale;
    private BufferedImage image;
    private boolean canRotate;
    private boolean collide = false;
    public static boolean showCollision = false;
    private HashMap<String, Player> playerList;
    



    public Entity(int size, double scale,boolean canRotate,HashMap<String, Player> playerList) {
        this.size = size;
        this.scale = scale;
        this.canRotate = canRotate;
        this.playerList = playerList;
    }
    
    public HashMap<String, Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(HashMap<String, Player> playerList) {
        this.playerList = playerList;
    }

    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }

    public boolean isOutSide(){
        return (x > 1240 || x < 20 || y > 690 || y < 20);
    }

    public boolean isCollide() {
        return collide;
    }

    public void setCollide(boolean collide) {
        this.collide = collide;
    }
    public int getCenterHeight(){
        return (int) (y + ((size * scale)/2));

    }
    public int getCenterWidth(){
        return (int) (x + ((size * scale)/2));

    }
    public int getHeight(){
        return (int) (size * scale);

    }
    public int getWidth(){
        return (int) (size * scale);

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public double getAngle() {
        return angle;
    }

    public double getScale() {
        return scale;
    }

    
    public abstract Polygon getCollision();

    
    public Rectangle getRectangle() {
        return new Rectangle(x,y,size,size);
    }

    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(canRotate){
            AffineTransform atf = AffineTransform.getTranslateInstance(x, y);
            atf.rotate(getAngle() + Math.toRadians(90), size * scale / 2, size * scale / 2);
            atf.scale(scale, scale);
            g2d.drawImage(image, atf, null);
        }else{
            g2d.drawImage(image,x,y,(int) (size * scale),(int) (size * scale),null);
        }

        if(showCollision){
            g2d.setColor(Color.GREEN);
            g2d.draw(getCollision());
        }
    }  

    

    

}
