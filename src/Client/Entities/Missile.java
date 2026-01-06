package Client.Entities;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Missile extends Entity{    
    private final int VELOCITY = 2;
    private final int DAMAGE = 10;
    private String shooterID;

    public Missile(BufferedImage image,HashMap<String, Player> playerList,String shooterID) {
        super(32, 1, true,playerList);
        this.shooterID = shooterID;
        setImage(image);
    }

    @Override
    public void render(Graphics g) {
        updateCollision();
        setX((int) (getX()+Math.cos(getAngle())*VELOCITY));
        setY((int) (getY()+Math.sin(getAngle())*VELOCITY));
        super.render(g);
    }

    @Override
    public Polygon getCollision() {
        int[] px = new int[] { getX(), getX()+getWidth(), getX() + getWidth(), getX()};
        int[] py = new int[] { getY(), getY() +getHeight(), getY() + getHeight(), getY()};
        return new Polygon(px, py, 4);
    }

    private void updateCollision(){
        for (String playerId : getPlayerList().keySet()) {
            if (!playerId.equals(shooterID)) {
                Player p = getPlayerList().get(playerId);
                if (p.getCollision().intersects(getRectangle())) {
                    p.setHp(p.getHp()-DAMAGE);
                    if (p.getHp()<=0) {
                        getPlayerList().get(shooterID).setWinner(true);
                    }
                    // setCollide(true);
                    getPlayerList().get(shooterID).setMissile(null);
                }
            }
        }
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
    
    
    
}
