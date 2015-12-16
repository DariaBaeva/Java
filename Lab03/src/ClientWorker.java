import java.net.*;

public class ClientWorker implements Runnable {
    private static final int TIMEOUT = 5000;

    private String host;
    private int port;
    private String requestPrefix;
    private int threadNumber;
    private int requestsInThread;

    public ClientWorker(String host, int port, String requestPrefix, int threadNumber, int requestsInThread) {
        this.host = host;
        this.port = port;
        this.requestPrefix = requestPrefix;
        this.threadNumber = threadNumber;
        this.requestsInThread = requestsInThread;
    }

    @Override
    public void run() {
        InetAddress hostAddress;
        try {
            hostAddress = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Host not found");
        }
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);
            byte[] incomingBuffer = new byte[socket.getReceiveBufferSize()];
            DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, incomingBuffer.length);

            for (int i = 0; i < requestsInThread; i++) {
                String requestText = requestPrefix + threadNumber + "_" + i;
                byte[] outcomingBuffer = requestText.getBytes();
                DatagramPacket outcomingPacket = new DatagramPacket(outcomingBuffer, outcomingBuffer.length,
                        hostAddress, port);
                while (true) {
                    try {
                        socket.send(outcomingPacket);
                        socket.receive(incomingPacket);
                        String receivedText = new String(incomingPacket.getData(), incomingPacket.getOffset(),
                                incomingPacket.getLength());
                        System.out.println("Request from thread " + threadNumber + ": " + requestText);
                        System.out.println("Received in thread " + threadNumber + ": " + receivedText);
                        if (receivedText.equals("Hello, " + requestText)) {
                            break;
                        }
                    } catch (SocketTimeoutException e) {
                    }
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
