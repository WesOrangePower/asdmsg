package agency.shitcoding;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Main {
    private static final int SERVER_PORT = 8080;

    // Limit of simultaneously working threads
    private static final int THREAD_LIMIT = 1;

    private static final int TIMEOUT_NON_AUTHORIZED = 1000;
    private static final int TIMEOUT = 5000;

    public static ServerSocket server;
    public static Boolean running = true;

    public static Logger LOGGER = new Logger();

    // Counter of all registered threads from the start
    private static Integer threadCounter = 0;

    private static final Database dataBase = new Database();

    public static void main(String[] args) {
        // List of threads
        HashMap<Integer, Thread> threadList = new HashMap<>(THREAD_LIMIT);

        try {
            server = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        while (running) {
            try (Socket socket = server.accept()) {

                if (THREAD_LIMIT == threadList.size()) {
                    continue;
                }

                socket.setSoTimeout(TIMEOUT);

                final Integer threadID = threadCounter;

                Runnable runnable = () -> {
                    try {
                        Client client = new Client(socket);
                        client.authorization();

                        synchronized (dataBase) {
                            // For working with the database
                        }

                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }

                    // At the end of thread, remove it from the thread list
                    synchronized (threadList) {
                        threadList.remove(threadID);
                        LOGGER.info("Thread " + threadID + " closed.");
                    }
                };
                Thread client_thread = new Thread(runnable);

                // Adding a thread to the thread list
                threadList.put(threadID, client_thread);
                client_thread.start();
                LOGGER.info("Thread " + threadID + " started.");

                threadCounter++;
            } catch (IOException e) {
                throw new RuntimeException("Failed to accept client: " + e);
            }
        }
    }
}