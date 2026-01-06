package Client.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MenuPanel extends GamePanel {
    private ImageIcon joinBtnImage =new ImageIcon("assets/joinBtn.png");
    private JButton joinBtn = new JButton();
    private JLabel background = new JLabel(new ImageIcon("assets/menuBg.png"));
    private JRadioButton lanBtn = new JRadioButton("LAN");
    private JRadioButton ipBtn = new JRadioButton("IP");
    private ButtonGroup btnGroup = new ButtonGroup();
    private JTextField textField = new JTextField("IP:PORT");
    private Font pixelFont;

    private String IP;
    private int PORT = 4004;
    private long lastTime = System.currentTimeMillis();
    private int time = 0;
    private DatagramSocket datagramSocket = null;
    private DatagramPacket sendPacket = null;
    

    public MenuPanel(GameFrame gameFrame) {
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/PixelFont.ttf")).deriveFont(18f).deriveFont(Font.BOLD);
        } catch (IOException | FontFormatException ignored) {
        }

        background.setSize(1280, 720);
        background.setForeground(Color.orange);
        background.setHorizontalTextPosition(JLabel.CENTER);
        background.setVerticalTextPosition(JLabel.TOP);
        background.setIconTextGap(-600);

        joinBtn.setBounds(555, 439, 192, 94);
        joinBtn.setIcon(joinBtnImage);

        btnGroup.add(lanBtn);
        btnGroup.add(ipBtn);

        ipBtn.setBounds(860,370,60,40);
        ipBtn.setForeground(Color.orange);
        ipBtn.setOpaque(false);
        lanBtn.setBounds(860,469,60,40);
        lanBtn.setForeground(Color.orange);
        lanBtn.setOpaque(false);
        lanBtn.setSelected(true);

        textField.setBounds(440, 370, 400, 34);
        textField.setBorder(BorderFactory.createCompoundBorder());

        this.addComps(0, background);
        this.addComps(1,joinBtn,lanBtn,ipBtn,textField);
        this.setFontComps(pixelFont, background,ipBtn,lanBtn,textField);
        
        joinBtn.addActionListener(action ->{
            if (lanBtn.isSelected()) {
                //gameFrame.setGameState("Win");
                background.setForeground(Color.orange);
                background.setText("finding a server...");
                new Thread(() -> {
                    while (true) {
                        findServer();
                        if (IP != null) {
                            System.out.println("found: "+IP);
                            gameFrame.setIp(IP);
                            gameFrame.setPort(PORT);
                            gameFrame.setGameState("Play");
                            break;
                        }
                        if (System.currentTimeMillis() - lastTime >= 1000) {
                            time++;
                            lastTime = System.currentTimeMillis();
                        }
                        if (time == 10) {
                            background.setForeground(Color.RED);
                            background.setText("server not found");
                            break;
                        }
                    }
                }).start();

                new Thread(() -> {
                    IP = getServerIP();
                }).start();
            }
            if (ipBtn.isSelected()) {
                System.out.println("input ip: "+textField.getText());
                String[] text = textField.getText().split(":");
                gameFrame.setIp(text[0]);
                try {
                    gameFrame.setPort(Integer.parseInt(text[1]));
                } catch (Exception ignored) {
                }
                gameFrame.setGameState("Play");
                
            }
        });
    }



    public String getServerIP(){
        String serverIP = null;
        //Wait for a response
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket revPacket = new DatagramPacket(receiveData, receiveData.length);
            datagramSocket.receive(revPacket);
            serverIP = revPacket.getAddress().getHostAddress();
            datagramSocket.close();
        } catch (IOException ignore) {
        }
        return serverIP;
    }
    public void findServer() {
        try {
            if (sendPacket==null) {
                byte[] sendData = "Find Server".getBytes();
                sendPacket= new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
            }else{
                datagramSocket.send(sendPacket);
            }
            //sent packet to: 255.255.255.255 (DEFAULT);
        } catch (SocketException ignore) {
        } catch (IOException ignore) {
        }
    }

   

}
