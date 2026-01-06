package Server;

import java.net.*;
import java.io.*;
import java.util.Set;

public class ChatClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Set<ChatClientHandler> clients;

    public ChatClientHandler(Socket socket, Set<ChatClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String msg;
            while ((msg = in.readLine()) != null) {
                broadcast(msg);
            }
        } catch (IOException e) {
            System.out.println("Client chat ngat ket noi");
        } finally {
            clients.remove(this);
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void broadcast(String message) {
        for (ChatClientHandler client : clients) {
            client.out.println(message);
        }
    }
}
