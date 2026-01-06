package Server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerWindow {
    JFrame jFrame = new JFrame();
    JPanel jPanel = new JPanel();
    JLabel jLabel = new JLabel();

    public static void main(String[] args) {
        new ServerWindow();
    }

    public ServerWindow (){
        jFrame.setSize(300,300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jLabel.setText("Server started");
        jLabel.setBounds(0,0,300,100);
        jPanel.setSize(300,300);
        jPanel.add(jLabel);
        jFrame.add(jPanel);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        try {
            //Broadcast Server
            BroadcastServer broadcastServer = new BroadcastServer();
            broadcastServer.start();

            //Socket Server
            int PORT = 4004;
            ServerSocket serverSocket = new ServerSocket(PORT);
            MainServer server = new MainServer(serverSocket);
            System.out.println("Server started... on port " + PORT);
            server.startServer();

        } catch (IOException ignore) {
            
        }
    }
}
