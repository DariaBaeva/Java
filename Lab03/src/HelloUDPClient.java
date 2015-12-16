import java.util.ArrayList;
import java.util.List;

public class HelloUDPClient {

    private static final String USAGE = "usage: HelloUDPClient <host> <port> <request prefix> <threads number> <requests in thread>";

    private String host;
    private int port;
    private String requestPrefix;
    private int threadsNumber;
    private int requestsInThread;

    public HelloUDPClient(String host, int port, String requestPrefix, int threadsNumber, int requestsInThread) {
        this.host = host;
        this.port = port;
        this.requestPrefix = requestPrefix;
        this.threadsNumber = threadsNumber;
        this.requestsInThread = requestsInThread;
    }

    public static void main(String[] args) {
        if (args.length == 5) {
            String host = args[0];
            int port;
            String requestPrefix = args[2];
            int threadsNumber;
            int requestsInThread;
            try {
                port = Integer.parseInt(args[1]);
                threadsNumber = Integer.parseInt(args[3]);
                requestsInThread = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(USAGE);
            }
            new HelloUDPClient(host, port, requestPrefix, threadsNumber, requestsInThread).run();
        } else {
            System.out.println(USAGE);
            System.exit(0);
        }
    }

    public void run() {
        System.out.println("Client started!");
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadsNumber; i++) {
            threads.add(new Thread(new ClientWorker(host, port, requestPrefix, i, requestsInThread)));
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

