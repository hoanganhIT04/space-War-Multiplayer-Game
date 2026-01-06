package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MainServer {
    private final ServerSocket serverSocket;
    private HashMap<String,ClientHandler> clientHandlers = new HashMap<>();
    public MainServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("new client");
                ClientHandler clientHandler = new ClientHandler(socket,clientHandlers);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            System.out.println("server shutdown");
        } catch (IOException ignored) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public HashMap<String, ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public void setClientHandlers(HashMap<String,ClientHandler> clientHandlers) {
        this.clientHandlers = clientHandlers;
    }
}

