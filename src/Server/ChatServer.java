package Server;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {

    private static final int PORT = 4005;
    private static Set<ChatClientHandler> clients = 
        Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Chat Server chay tai port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ChatClientHandler client = new ChatClientHandler(socket, clients);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
