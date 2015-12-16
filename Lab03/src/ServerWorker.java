import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerWorker implements Runnable {

    private DatagramSocket socket;

    public ServerWorker(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            byte[] incomingBuffer = new byte[socket.getReceiveBufferSize()];
            DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, incomingBuffer.length);

            while (true) {
                try {
                    socket.receive(incomingPacket);
                    String receivedText = new String(incomingPacket.getData(), incomingPacket.getOffset(),
                            incomingPacket.getLength());
                    System.out.println(Thread.currentThread().getId() + ": " + receivedText);
                    String outcomingText = "Hello, " + receivedText;
                    byte[] outcomingBuffer = outcomingText.getBytes();
                    DatagramPacket outcomingPacket = new DatagramPacket(outcomingBuffer, outcomingBuffer.length,
                            incomingPacket.getAddress(), incomingPacket.getPort());
                    socket.send(outcomingPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
