package Client.Handlers;

import java.net.*;
import java.io.*;
import java.util.function.Consumer;

public class ChatClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatClient(String ip, int port, Consumer<String> onMessage)
            throws IOException {

        socket = new Socket(ip, port);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    onMessage.accept(msg);
                }
            } catch (IOException e) {
                System.out.println("Mất kết nối chat server");
            }
        }).start();
    }

    public void send(String msg) {
        out.println(msg);
    }
}
