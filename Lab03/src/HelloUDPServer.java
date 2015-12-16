import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class HelloUDPServer {
    private static final String USAGE = "usage: HelloUDPServer <port> <threads number>";

    private int port;
    private int threadsNumber;

    public HelloUDPServer(int port, int threadsNumber) {
        this.port = port;
        this.threadsNumber = threadsNumber;
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            int port;
            int threadsNumber;
            try {
                port = Integer.parseInt(args[0]);
                threadsNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(USAGE);
            }
            new HelloUDPServer(port, threadsNumber).run();
        } else {
            System.out.println(USAGE);
            System.exit(0);
        }
    }

    public void run() {
        System.out.println("Server started!");
        List<Thread> threads = new ArrayList<>();
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < threadsNumber; i++) {
            threads.add(new Thread(new ServerWorker(socket)));
            threads.get(i).start();
        }

        for (int i = 0; i < threadsNumber; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
