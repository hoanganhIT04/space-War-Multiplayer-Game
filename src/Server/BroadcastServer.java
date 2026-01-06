package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastServer extends Thread {
    DatagramSocket datagramSocket;
    @Override
    public void run() {
        try {
            datagramSocket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
            datagramSocket.setBroadcast(true);
            System.out.println(getClass().getName() + " Ready to receive broadcast packets!");
            while (true) {
                byte[] revBuf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(revBuf, revBuf.length);
                datagramSocket.receive(packet);
                //Packet received
                //System.out.println("from: " + packet.getAddress().getHostAddress());

                String message = new String(packet.getData()).trim();

                if (message.equals("Find Server")) {
                    byte[] sendData = "You found!".getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    datagramSocket.send(sendPacket);
                    //System.out.println(getClass().getName() + "Sent packet to: " + sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
