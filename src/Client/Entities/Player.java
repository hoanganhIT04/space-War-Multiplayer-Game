package Client.Entities;

import java.util.HashMap;
import javax.imageio.ImageIO;

import Client.Utility.Skill;
import Client.Utility.Sound;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Player extends Entity {
    private final int DASH_SPEED = 200, FIRE_RATE = 200;
    private int oldX, oldY;
    private BufferedImage shipImage, barrierImage, bulletImage, missileImage;
    private BufferedImage[] shipFrames = new BufferedImage[3];
    private Skill barrier, triBullet, dash, aimMissile;
    private HashMap<String, Bullet> bulletList = new HashMap<>();
    private Missile missile = null;
    private Color color;
    private long lastBullet;
    private long lastFrame;
    private int frame = 0;
    private boolean leftClick, rightClick, barPressed, dashPressed, triPressed, missilePressed;
    private int hp = 100;
    private String missileTargetID = "noTarget";
    private boolean winner;
    private Sound shootSound,barrierSound,dashSound,missileSound;
    private float soundVolumeControl=-20f;

    public Player(Color color, HashMap<String, Player> playerList) {
        super(64, 1.5, true,playerList);
        this.color = color;
        setPlayerList(playerList);
        reColor();
        barrierSound = new Sound("assets/audio/barrier.wav");
        dashSound = new Sound("assets/audio/dash.wav");
        missileSound = new Sound("assets/audio/missile.wav");
        setImage(shipImage);
        barrier = new Skill(8);
        triBullet = new Skill(10);
        dash = new Skill(10);
        aimMissile = new Skill(20);
        lastFrame = System.currentTimeMillis();
        lastBullet = System.currentTimeMillis();
    }

    @Override
    public void render(Graphics g) {
        changeFrame();
        
        barrierSound.setVolume(soundVolumeControl);
        dashSound.setVolume(soundVolumeControl);
        missileSound.setVolume(soundVolumeControl);        

        updateBullets(g);
        updateMouseAction();
        updateSkillAction();
        updateMissile(g);

        heathBar(g);
        super.render(g);
    }

    private void reColor() {
        try {
            shipImage = ImageIO.read(new File("assets/ship.png"));
            barrierImage = ImageIO.read(new File("assets/shipWithBarrier.png"));
            shipFrames[0] = ImageIO.read(new File("assets/shipFrame_1.png"));
            shipFrames[1] = ImageIO.read(new File("assets/shipFrame_2.png"));
            shipFrames[2] = ImageIO.read(new File("assets/shipFrame_3.png"));
        } catch (IOException ignored) {
        }

        for (int y = 0; y < shipImage.getHeight(); y++) {
            for (int x = 0; x < shipImage.getWidth(); x++) {
                int pixel = shipImage.getRGB(x, y);
                Color c = new Color(pixel, true);
                if (c.getRed() > 100 && c.getRed() < 200 && c.getRed() != 163) {
                    int newColor = color.getRGB();
                    shipImage.setRGB(x, y, newColor);
                    barrierImage.setRGB(x, y, newColor);
                    shipFrames[0].setRGB(x, y, newColor);
                    shipFrames[1].setRGB(x, y, newColor);
                    shipFrames[2].setRGB(x, y, newColor);
                }
            }
        }
    }

    private void changeFrame() {
        if (System.currentTimeMillis() - lastFrame >= 200) {
            frame = (frame == 2) ? 0 : frame + 1;
            lastFrame = System.currentTimeMillis();
        }
    }

    private void updateBullets(Graphics g) {
        HashMap<String, Bullet> newBullets = new HashMap<>();
        for (String i : bulletList.keySet()) {
            Bullet bullet = bulletList.get(i);
            bullet.render(g);
            if (!bullet.isCollide() && !bullet.isOutSide()) {
                newBullets.put(i, bullet);
            }
        }
        bulletList = newBullets;
    }
    private void updateMissile(Graphics g) {
        if (missile != null) {
            if (getPlayerList().containsKey(missileTargetID)) {
                Player p = getPlayerList().get(missileTargetID);
                missile.setTargetX(p.getCenterWidth());
                missile.setTargetY(p.getCenterHeight());
                missile.render(g);
            }else{
                missile = null;
            }
        }
    }

    private void updateMouseAction() {
        if (leftClick) {
            if (System.currentTimeMillis() - lastBullet >= FIRE_RATE) {
                if (aimMissile.isUsing()) {
                    missileTracking();
                    missileSound.reset();
                    missileSound.start();
                    aimMissile.skillCooldown();
                }else if (triBullet.isUsing()) {
                    for (int i = -15; i <= 15; i += 15) {
                        Bullet bullet = new Bullet(bulletImage,getPlayerList(),getID());
                        bullet.setX(getX() + (getWidth() / 2) - (bullet.getWidth() / 2));
                        bullet.setY(getY() + (getHeight() / 2) - (bullet.getHeight() / 2));
                        bullet.setAngle(getAngle() + Math.toRadians(i));
                        bulletList.put(bullet.getID(), bullet);
                    }
                    triBullet.setUseTimeLeft(triBullet.getUseTimeLeft() - 1);
                    if (triBullet.getUseTimeLeft() == 0) {
                        triBullet.skillCooldown();
                    }
                    shootSound();
                } else {
                    Bullet bullet = new Bullet(bulletImage,getPlayerList(),getID());
                    bullet.setX(getX() + (getWidth() / 2) - (bullet.getWidth() / 2));
                    bullet.setY(getY() + (getHeight() / 2) - (bullet.getHeight() / 2));
                    bullet.setAngle(getAngle());
                    bulletList.put(bullet.getID(), bullet);
                    shootSound();
                }
                lastBullet = System.currentTimeMillis();
            }
        }
        barPressed = rightClick;
    }
    private void shootSound(){
        new Thread(() -> {
            while (true){
                shootSound = new Sound("assets/audio/shoot.wav");
                shootSound.setVolume(soundVolumeControl);
                shootSound.start();
                try {
                    Thread.sleep(shootSound.length()/1000);
                    break;
                } catch (InterruptedException ignore) {
                }
            }
        }).start();
    }

    private void missileTracking() {
        if (missile==null&&getPlayerList().size() > 1) {
            String targetID = null;
            double nearestTarget = Double.MAX_VALUE;
            for (String id : getPlayerList().keySet()) {
                if (id != getID()) {
                    Player p = getPlayerList().get(id);
                    double distance = Math.sqrt(Math.pow((p.getCenterWidth() - getTargetX()), 2)
                            + Math.pow((p.getCenterHeight() - getTargetY()), 2));
                    if (distance < nearestTarget) {
                        nearestTarget = distance;
                        targetID = id;
                    }
                }
            }
            if (targetID != null) {
                missileTargetID = targetID;
            }
        }
        missile = new Missile(missileImage,getPlayerList(),getID());
        missile.setX(getX() + (getWidth() / 2) - (missile.getWidth() / 2));
        missile.setY(getY() + (getHeight() / 2) - (missile.getHeight() / 2));
        
    }

    public void heathBar(Graphics g) {
        g.setColor(Color.white);
        // g.fillRect(getX() - 4, getY() - 10, 104, 10);
        // Color health = (hp < 25) ? new Color(236, 78, 49)
        //         : (hp < 50) ? new Color(252, 188, 54) : new Color(141, 218, 102);
        // g.setColor(health);
        // g.fillRect(getX() - 2, getY() - 8, hp, 6);
        g.drawString(String.valueOf(hp), getX()+36, getY()-10);
    }

    private void updateSkillAction() {
        if (barPressed && barrier.isAvailable()) {
            barrier.useSkill();
            barrierSound.reset();
            barrierSound.start();
            barrier.useCooldown(3);
        }
        if (dashPressed && dash.isAvailable()) {
            dashSound.reset();
            dashSound.start();
            dash.useSkill();
        }
        if (triPressed && triBullet.isAvailable()) {
            triBullet.useSkill();
            triBullet.setUseTimeLeft(10);
        }
        if (missilePressed && aimMissile.isAvailable()) {
            aimMissile.useSkill();
        }
        if (dash.isUsing()) {
            setX((int) (getX() + (Math.cos(getAngle()) * DASH_SPEED)));
            setY((int) (getY() + (Math.sin(getAngle()) * DASH_SPEED)));
            dash.skillCooldown();
        }
        if (barrier.isUsing()) {
            setImage(barrierImage);
        } else {
            if (oldX != getX() || oldY != getY()) {
                oldX = getX();
                oldY = getY();
                setImage(shipFrames[frame]);
            } else {
                setImage(shipImage);
            }
        }
    }

    @Override
    public Polygon getCollision() {
        int[] px = new int[] { getX() + 36, getX() + 60, getX() + 60, getX() + 92, getX() + 92, getX() + 4, getX() + 4,
                getX() + 36 };
        int[] py = new int[] { getY() + 4, getY() + 4, getY() + 36, getY() + 72, getY() + 88, getY() + 88, getY() + 72,
                getY() + 36 };
        double newAngle = getAngle() + Math.toRadians(90);
        for (int i = 0; i < 8; i++) {
            int temp = px[i];
            px[i] = (int) (((px[i] - getCenterWidth()) * Math.cos(newAngle))
                    - ((py[i] - getCenterHeight()) * Math.sin(newAngle)) + getCenterWidth());
            py[i] = (int) (((temp - getCenterWidth()) * Math.sin(newAngle))
                    + ((py[i] - getCenterHeight()) * Math.cos(newAngle)) + getCenterHeight());
        }
        return new Polygon(px, py, 8);
    }

    public String getData() {
        String data = getID() + "," + getX() + "," + getY() + "," + getAngle() + "," + barPressed + "," + dashPressed
                + "," + triPressed + "," + missilePressed + "," + missileTargetID
                + "," + leftClick + "," + rightClick + "," + color.getRed() + "," + color.getGreen() + ","
                + color.getBlue() + "," + hp + ",";
        return data;
    }

    public BufferedImage getBarrierImage() {
        return barrierImage;
    }

    public boolean isLeftClick() {
        return leftClick;
    }

    public void setLeftClick(boolean leftClick) {
        this.leftClick = leftClick;
    }

    public boolean isRightClick() {
        return rightClick;
    }

    public void setRightClick(boolean rightClick) {
        this.rightClick = rightClick;
    }

    public boolean isBarPressed() {
        return barPressed;
    }

    public void setBarPressed(boolean barPressed) {
        this.barPressed = barPressed;
    }

    public boolean isDashPressed() {
        return dashPressed;
    }

    public void setDashPressed(boolean dashPressed) {
        this.dashPressed = dashPressed;
    }

    public boolean isTriPressed() {
        return triPressed;
    }

    public void setTriPressed(boolean triPressed) {
        this.triPressed = triPressed;
    }

    public boolean isMissilePressed() {
        return missilePressed;
    }

    public void setMissilePressed(boolean missilePressed) {
        this.missilePressed = missilePressed;
    }

    public void setBulletImage(BufferedImage bulletImage) {
        this.bulletImage = bulletImage;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public BufferedImage getMissileImage() {
        return missileImage;
    }

    public void setMissileImage(BufferedImage missileImage) {
        this.missileImage = missileImage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Skill getDash() {
        return dash;
    }

    public Skill getAimMissile() {
        return aimMissile;
    }

    public HashMap<String, Bullet> getBulletList() {
        return bulletList;
    }

    public void setBulletList(HashMap<String, Bullet> bulletList) {
        this.bulletList = bulletList;
    }

    public Missile getMissile() {
        return missile;
    }

    public void setMissile(Missile missile) {
        this.missile = missile;
    }
    public Skill getBarrier() {
        return barrier;
    }

    public Skill getTriBullet() {
        return triBullet;
    }

    public String getMissileTargetID() {
        return missileTargetID;
    }

    public void setMissileTargetID(String missileTargetID) {
        this.missileTargetID = missileTargetID;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public float getSoundVolumeControl() {
        return soundVolumeControl;
    }

    public void setSoundVolumeControl(float soundVolumeControl) {
        this.soundVolumeControl = soundVolumeControl;
    }
    
    


}
