package Client.UI;

import Client.Entities.MainPlayer;
import Client.Entities.Player;
import Client.Handlers.ChatClient;
import Client.Handlers.KeyHandler;
import Client.Handlers.MouseHandler;
import Client.Handlers.ServerHandler;
import Client.Utility.Sound;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.net.InetAddress;

public class PlayPanel extends JPanel {
    private static final int FPS = 120;
    private BufferedImage /* shipImage, */ background, hpImage, barImage, dashImage, triImage, misImage, qImage,
            spImage,
            rClickImage, xImage, enemyBullImage, bullImage, missileImage;
    private MainPlayer player;
    private HashMap<String, Player> playerList = new HashMap<>();
    private int socketPort;
    private String socketIP;
    private ServerHandler client;
    private Font pixelFont;
    private GameFrame gameFrame;
    private SettingPanel settingPanel;
    private Sound bgm;
    private Sound chatNotifySound;

    private boolean isChatting = false;
    private String chatInput = "";
    private java.util.List<String> chatMessages = new ArrayList<>();
    private ChatClient chatClient;
    private Font chatFont;

    private boolean showChatUI = false;
    private static final int MAX_CHAT_LENGTH = 120;
    private String playerName;


    public PlayPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        loadImage();
        bgm = new Sound("assets/audio/spaceBgm.wav");
        // 🔊 chat sound
        chatNotifySound = new Sound("assets/audio/soundChat.wav");

        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/PixelFont.ttf")).deriveFont(30f)
                    .deriveFont(Font.BOLD);
        } catch (IOException | FontFormatException e) {
            pixelFont = new Font("Arial", Font.BOLD, 24); // fallback
        }

        // ✅ FONT RIÊNG CHO CHAT (VIẾT Ở ĐÂY)
        chatFont = new Font("Arial", Font.PLAIN, 14);

        this.setLayout(null);
    }

    public void startGameLoop() {

        bgm.loop();

        // 1️⃣ CREATE PLAYER
        player = new MainPlayer(
                new Color(
                        new Random().nextInt(255),
                        new Random().nextInt(255),
                        new Random().nextInt(255)),
                playerList);

        player.setBulletImage(bullImage);
        player.setMissileImage(missileImage);

        int intId = 10000000 + (int) (Math.random() * 90000000);
        player.setID(String.valueOf(intId));
        playerList.put(player.getID(), player);

        System.out.println("Your client ID : " + player.getID());

        // 2️⃣ START GAME LOOP
        frameUpdate();

        MouseHandler mouseHandler = new MouseHandler(this, player);
        this.addKeyListener(new KeyHandler(this, player));
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        this.grabFocus();

        // 3️⃣ CONNECT GAME SERVER
        Socket socket = null;
        try {
            socket = new Socket(socketIP, socketPort);
        } catch (IOException e) {
            System.out.println("Không kết nối được Game Server");
        }

        try {
            playerName = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            playerName = "Player";
        }

        client = new ServerHandler(socket, playerList, player.getID());
        client.setBulletImage(enemyBullImage);
        client.setMissileImage(missileImage);
        client.joinToServer();
        client.listenServer();

        // 4️⃣ INIT CHAT CLIENT (SAU CÙNG)
        try {
            chatClient = new ChatClient(
                    socketIP,
                    4005,
                    msg -> {
                        chatMessages.add(msg);
                        if (chatMessages.size() > 8) {
                            chatMessages.remove(0);
                        }

                        // 🔊 âm thanh chat (nhận)
                        if (chatNotifySound != null) {
                            chatNotifySound.reset();
                            chatNotifySound.start();
                        }
                    });
            System.out.println("Chat client connected!");
        } catch (Exception e) {
            System.out.println("Không kết nối được Chat Server");
        }

        // 5️⃣ UI
        settingPanel = new SettingPanel(this);
        JButton settingBtn = new JButton();
        settingBtn.setBounds(1240, 0, 40, 20);
        add(settingBtn);

        settingPanel.setVisible(false);
        settingPanel.setBounds(490, 210, 300, 160);
        add(settingPanel);

        settingBtn.addActionListener(e -> settingPanel.setVisible(true));
    }

    private void frameUpdate() {
        new Thread(() -> {
            double timePerFrame = 1000000000.0 / FPS;
            long lastFrame = System.nanoTime();
            long now;

            while (true) {
                now = System.nanoTime();
                if (now - lastFrame >= timePerFrame) {
                    this.repaint();
                    lastFrame = now;
                }
            }
        }).start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (player != null && client != null) {
            g.drawImage(background, 0, 0, null);
            for (String id : playerList.keySet()) {
                Player p = playerList.get(id);
                p.render(g);
            }
            drawUI(g);

            if (!this.hasFocus()) {
                player.setUpPressed(false);
                player.setDownPressed(false);
                player.setLeftPressed(false);
                player.setRightPressed(false);
            }

            client.sendData("name:updatePlayer:" + player.getData());
            if (player.getHp() <= 0) {
                client.sendData("name:clientDisconnect:" + player.getID());
                new Thread(() -> {
                    try {
                        Thread.sleep(400);
                        client.disconnect();
                    } catch (InterruptedException e) {
                        //
                    }
                }).start();
                gameFrame.setGameState("Lose");
            }
            if (player.isWinner()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(200);
                        client.sendData("name:clientDisconnect:" + player.getID());
                    } catch (InterruptedException e) {
                        //
                    }
                }).start();
                gameFrame.setGameState("Win");
            }
        }
        drawChat(g);
    }

    private void loadImage() {
        try {
            // shipImage = ImageIO.read(new File("assets/ship.png"));
            background = ImageIO.read(new File("assets/gameBg.png"));
            hpImage = ImageIO.read(new File("assets/HpFrame.png"));
            barImage = ImageIO.read(new File("assets/skillBar.png"));
            dashImage = ImageIO.read(new File("assets/skillDash.png"));
            triImage = ImageIO.read(new File("assets/skillTribull.png"));
            misImage = ImageIO.read(new File("assets/skillMissile.png"));
            qImage = ImageIO.read(new File("assets/qbtn.png"));
            spImage = ImageIO.read(new File("assets/spbtn.png"));
            rClickImage = ImageIO.read(new File("assets/Rclick.png"));
            xImage = ImageIO.read(new File("assets/xbtn.png"));
            enemyBullImage = ImageIO.read(new File("assets/enemybullet.png"));
            bullImage = ImageIO.read(new File("assets/Bullet.png"));
            missileImage = ImageIO.read(new File("assets/missile.png"));
        } catch (IOException ignored) {

        }
    }

    public HashMap<String, Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(HashMap<String, Player> playerList) {
        this.playerList = playerList;
    }

    public MainPlayer getPlayer() {
        return player;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    public String getSocketIP() {
        return socketIP;
    }

    public void setSocketIP(String socketIP) {
        this.socketIP = socketIP;
    }

    private void drawUI(Graphics g) {
        g.setFont(pixelFont);
        g.setColor(Color.white);

        // g.fillPolygon(new int[] {52,86,92,92,86,52},new int[] {32,32,38,57,64,64},6);
        // g.drawImage(shipImage,20,20,null);
        // g.setColor(Color.BLACK);
        // g.drawString(String.valueOf(player.getKillScore()),67,58);

        g.drawImage(hpImage, 20, 20, null);
        g.setColor(Color.white);
        g.fillRect(28, 44, 204, 34);
        Color health = (player.getHp() < 25) ? new Color(236, 78, 49)
                : (player.getHp() < 50) ? new Color(252, 188, 54) : new Color(141, 218, 102);
        g.setColor(health);
        g.fillRect(30, 46, player.getHp() * 2, 30);

        // draw skill image
        g.drawImage(barImage, 20, 580, null);
        g.drawImage(dashImage, 120, 580, null);
        g.drawImage(triImage, 220, 580, null);
        g.drawImage(misImage, 320, 580, null);
        g.setColor(Color.ORANGE);

        // draw skill cool down
        if (player.getBarrier().isUsing()) {
            g.setColor(new Color(95, 110, 252, 180));
            g.fillRect(20, 580, 81, 81);
            g.setColor(Color.white);
            g.drawString(String.valueOf(player.getBarrier().getUseTimeLeft()), 52, 632);
        } else {
            if (!player.getBarrier().isAvailable()) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(20, 580, 81, 81);
                g.setColor(Color.white);
                g.drawString(String.valueOf(player.getBarrier().getCdTimeLeft()), 52, 632);
            }
        }

        if (!player.getDash().isAvailable()) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(120, 580, 81, 81);
            g.setColor(Color.white);
            g.drawString(String.valueOf(player.getDash().getCdTimeLeft()), 152, 632);
        }

        if (player.getTriBullet().isUsing()) {
            g.setColor(new Color(76, 150, 252, 180));
            g.fillRect(220, 580, 81, 81);
            g.setColor(Color.white);
            g.drawString(String.valueOf(player.getTriBullet().getUseTimeLeft()),
                    (player.getTriBullet().getUseTimeLeft() >= 10) ? 244 : 252, 632);
        } else {
            if (!player.getTriBullet().isAvailable()) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(220, 580, 81, 81);
                g.setColor(Color.white);
                g.drawString(String.valueOf(player.getTriBullet().getCdTimeLeft()),
                        (player.getTriBullet().getCdTimeLeft() >= 10) ? 244 : 252, 632);
            }
        }

        if (player.getAimMissile().isUsing()) {
            g.setColor(new Color(252, 76, 76, 180));
            g.fillRect(320, 580, 81, 81);
        } else {
            if (!player.getAimMissile().isAvailable()) {
                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(320, 580, 81, 81);
                g.setColor(Color.white);
                g.drawString(String.valueOf(player.getAimMissile().getCdTimeLeft()),
                        (player.getAimMissile().getCdTimeLeft() >= 10) ? 344 : 352, 632);
            }
        }

        // draw skill cool down
        g.drawImage(rClickImage, 52, 652, null);
        g.drawImage(spImage, 152, 652, null);
        g.drawImage(qImage, 252, 652, null);
        g.drawImage(xImage, 352, 652, null);

    }

    public SettingPanel getSettingPanel() {
        return settingPanel;
    }

    public void setSettingPanel(SettingPanel settingPanel) {
        this.settingPanel = settingPanel;
    }

    public Sound getBgm() {
        return bgm;
    }

    public void setBgm(Sound bgm) {
        this.bgm = bgm;
    }

    public void startChat() {
        isChatting = true;
        showChatUI = true;

        this.requestFocusInWindow();
    }

    public boolean isChatting() {
        return isChatting;
    }

    public void submitChat() {
        if (chatClient != null && !chatInput.trim().isEmpty()) {
            chatClient.send(playerName + ": " + chatInput);

            // 🔊 âm thanh chat (gửi)
            if (chatNotifySound != null) {
                chatNotifySound.reset();
                chatNotifySound.start();
            }
        }
        chatInput = "";
        isChatting = false;
        showChatUI = false;
    }

    public void appendChatChar(char c) {
        if (chatInput.length() >= MAX_CHAT_LENGTH)
            return;
        chatInput += c;
    }

    private void drawChat(Graphics g) {
        if (chatFont == null)
            return;
        if (!showChatUI)
            return;

        int boxX = 20;
        int boxY = 380;
        int boxW = 420;
        int boxH = 160;
        int padding = 10;
        int lineHeight = 16;

        g.setFont(chatFont);

        // ===== VẼ KHUNG =====
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(boxX, boxY, boxW, boxH);

        g.setColor(Color.WHITE);
        g.drawRect(boxX, boxY, boxW, boxH);

        // ===== CLIP VÙNG CHAT (QUAN TRỌNG) =====
        Shape oldClip = g.getClip(); // lưu clip cũ
        g.setClip(
                boxX + padding,
                boxY + padding,
                boxW - padding * 2,
                boxH - padding * 2);

        FontMetrics fm = g.getFontMetrics();
        int y = boxY + padding + lineHeight;

        // ===== CHAT HISTORY =====
        for (String msg : chatMessages) {
            for (String line : wrapText(msg, fm, boxW - padding * 2)) {
                if (y > boxY + boxH - padding)
                    break;
                g.drawString(line, boxX + padding, y);
                y += lineHeight;
            }
        }

        // ===== INPUT LINE =====
        if (isChatting) {
            g.drawString("> " + chatInput, boxX + padding, boxY + boxH - padding);
        }

        // ===== RESTORE CLIP =====
        g.setClip(oldClip);

        // ===== CHAR COUNT (ngoài clip) =====
        int remain = MAX_CHAT_LENGTH - chatInput.length();
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(
                remain + " chars left",
                boxX + boxW - 110,
                boxY + boxH - 5);
    }

    private java.util.List<String> wrapText(String text, FontMetrics fm, int maxWidth) {
        java.util.List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            String testLine = currentLine + word + " ";

            if (fm.stringWidth(testLine) > maxWidth) {
                if (!currentLine.toString().isEmpty()) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word + " ");
            } else {
                currentLine.append(word).append(" ");
            }
        }

        if (!currentLine.toString().isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    public void cancelChat() {
        chatInput = "";
        isChatting = false;
        showChatUI = false;
    }

    public void removeLastChatChar() {
        if (!chatInput.isEmpty()) {
            chatInput = chatInput.substring(0, chatInput.length() - 1);
        }
    }

}
