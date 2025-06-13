package lab7.main.java.server;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.network.CommandRequest;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.DatabaseManager;
import lab7.main.java.util.OutputManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class Server {
    private final int port;
    private final CollectionManager collectionManager;
    private final DatabaseManager dbManager;
    private final OutputManager outputManager;
    private final CommandManager commandManager;
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private DatagramChannel channel;
    private Selector selector;
    private volatile boolean running;

    public Server(int port, String dbUrl, String dbUser, String dbPassword) {
        this.port = port;
        this.outputManager = new OutputManager(System.out);
        this.dbManager = new DatabaseManager(dbUrl, dbUser, dbPassword);
        this.collectionManager = new CollectionManager();
        this.commandManager = new CommandManager(collectionManager, dbManager, outputManager);
        this.running = true;
        loadInitialCollection();
    }

    private void loadInitialCollection() {
        collectionManager.loadData(dbManager.loadLabWorks());
    }

    public void start() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            outputManager.println("Server started on port " + port);

            while (running) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isReadable()) {
                        new Thread(() -> handleRequest(key)).start();
                    }
                }
            }
        } catch (Exception e) {
            outputManager.println("Server error: " + e.getMessage());
            stop();
        } finally {
            stop();
        }
    }

    private void handleRequest(SelectionKey key) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(65536);
            InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            CommandRequest request = (CommandRequest) ois.readObject();

            CommandResponse response = forkJoinPool.invoke(
                    ForkJoinTask.adapt(() -> commandManager.executeCommand(request))
            );

            new Thread(() -> {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(response);
                    oos.flush();
                    ByteBuffer responseBuffer = ByteBuffer.wrap(baos.toByteArray());
                    channel.send(responseBuffer, clientAddress);
                } catch (IOException e) {
                    outputManager.println("Error sending response: " + e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            outputManager.println("Error handling request: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
        try {
            if (channel != null) channel.close();
            if (selector != null) selector.close();
        } catch (IOException e) {
            outputManager.println("Error during shutdown: " + e.getMessage());
        }
    }
}