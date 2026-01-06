package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class ClientHandler implements Runnable {

    private HashMap<String,ClientHandler> clientHandlers = new HashMap<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientID;

    public ClientHandler(Socket socket, HashMap<String, ClientHandler> clientHandlers) {
        this.clientHandlers = clientHandlers;
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientID = bufferedReader.readLine();
            this.clientHandlers.put(clientID,this);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String dataFromClient;
        while (socket.isConnected()) {
//            System.out.print("[");
//            for (String id : clientHandlers.keySet()) {
//                System.out.print(id+", ");
//            }
//            System.out.println("]");
            try {
                dataFromClient = bufferedReader.readLine();
                if (dataFromClient!=null) {
                    if(dataFromClient.startsWith("name")){
                        String[] payload = dataFromClient.split(":");
                        if(payload[1].equals("clientDisconnect")){
                            broadcastMessage("name:playerDisconnected:"+clientID);
                            clientHandlers.remove(payload[2]);
                        }else {
                            broadcastMessage(dataFromClient);
                        }
                    }
                }
//                if(dataFromClient!=null){
//                    System.out.println(dataFromClient);
//                }
                
            } catch (Exception e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    // send to all client except sent client
    public void broadcastMessage(String payload) {
        for (String id : clientHandlers.keySet()) {
            ClientHandler c = clientHandlers.get(id);
            if(!id.equals(clientID)){
                try {
                    c.bufferedWriter.write(payload);
                    c.bufferedWriter.newLine();
                    c.bufferedWriter.flush();
                } catch (Exception e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(clientID);
        if(clientHandlers.size()!=0){
            broadcastMessage("name:playerDisconnected:"+clientID);
        }
    }
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter) {
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        removeClientHandler();
    }

}
