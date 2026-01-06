package Client.Entities;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Bullet extends Entity {
    private final double VELOCITY = 8;
    private final int DAMAGE = 2;
    private String shooterID;

    public Bullet(BufferedImage image, HashMap<String, Player> playerList, String shooterID) {
        super(16, 1, false, playerList);
        this.shooterID = shooterID;
        setID(String.valueOf(100000 + (int) (Math.random() * ((999999 - 100000) + 1))));
        setImage(image);
    }

    @Override
    public Polygon getCollision() {
        int[] px = new int[] { getX(), getX()+getWidth(), getX() + getWidth(), getX()};
        int[] py = new int[] { getY(), getY(), getY() + getHeight(), getY()+getHeight()};
        return new Polygon(px, py, 4);
    }

    @Override
    public void render(Graphics g) {
        updateCollision();
        setX((int) (getX() + (Math.cos(getAngle()) * VELOCITY)));
        setY((int) (getY() + (Math.sin(getAngle()) * VELOCITY)));
        super.render(g);
    }

    private void updateCollision() {
        for (String playerId : getPlayerList().keySet()) {
            if (!playerId.equals(shooterID)) {
                Player p = getPlayerList().get(playerId);
                if (p.getCollision().intersects(getRectangle())) {
                    setCollide(true);
                    if (!p.getBarrier().isUsing()) {
                        p.setHp(p.getHp() - DAMAGE);
                        if (p.getHp() <= 0) {
                            getPlayerList().get(shooterID).setWinner(true);
                        }
                    }
                }
            }
        }
        if (isOutSide()) {
            setCollide(true);
        }
    }

}
