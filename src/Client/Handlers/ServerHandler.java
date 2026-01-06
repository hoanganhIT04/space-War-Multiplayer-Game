package Client.Handlers;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.awt.Color;
import java.awt.image.BufferedImage;

import Client.Entities.Player;

public class ServerHandler {
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private HashMap<String, Player> playerList = new HashMap<>();
    private Socket socket;
    private String clientId;
    private BufferedImage bulletImage,missileImage;


    public ServerHandler(Socket socket, HashMap<String, Player> playerList, String clientId) {
        if (socket != null) {
            try {
                this.socket = socket;
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.playerList = playerList;
                this.clientId = clientId;
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }

    }

    public void joinToServer() {
        try {
            bufferedWriter.write(clientId);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendData(String payload) {
        try {
            bufferedWriter.write(payload);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    
    public void listenServer() {
        new Thread(() -> {
            String receiveData;
            while (socket.isConnected()) {
                try {
                    receiveData = bufferedReader.readLine();
                    if (receiveData != null &&receiveData.startsWith("name")) {
                        //System.out.println(receiveData);
                        String[] payload = receiveData.split(":");
                        switch (payload[1]) {
                            case "updatePlayer":
                                onUpdatePlayer(payload[2]);
                                break;
                            case "playerDisconnected":
                                playerList.remove(payload[2]);
                                break;
                            default:
                                break;
                        }
                    }

                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();

    }
    public void disconnect() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            //
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            //
        }
    }

    public void disconnect(int num) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            //
        }
    }

    private void onUpdatePlayer(String payload){
        String[] data = payload.split(",");
        if (playerList.containsKey(data[0])) {
            Player p = playerList.get(data[0]);
            p.setID(data[0]);
            p.setX(Integer.parseInt(data[1]));
            p.setY(Integer.parseInt(data[2]));
            p.setAngle(Double.parseDouble(data[3]));
            p.setBarPressed(Boolean.parseBoolean(data[4]));
            p.setDashPressed(Boolean.parseBoolean(data[5]));
            p.setTriPressed(Boolean.parseBoolean(data[6]));
            p.setMissilePressed(Boolean.parseBoolean(data[7]));
            p.setMissileTargetID(data[8]);
            p.setLeftClick(Boolean.parseBoolean(data[9]));
            p.setRightClick(Boolean.parseBoolean(data[10]));
            p.setHp(Integer.parseInt(data[14]));
        } else {
            Player p = new Player(new Color(Integer.parseInt(data[11]),Integer.parseInt(data[12]),Integer.parseInt(data[13])), playerList);
            p.setBulletImage(bulletImage);
            p.setMissileImage(missileImage);
            playerList.put(data[0], p);
        }
    }

    public void setBulletImage(BufferedImage bulletImage) {
        this.bulletImage = bulletImage;
    }

    public void setMissileImage(BufferedImage missileImage) {
        this.missileImage = missileImage;
    }
    
}
