package lab6.main.java.server;

import lab6.main.java.collection.CollectionLoader;
import lab6.main.java.collection.CollectionManager;
import lab6.main.java.data.LabWork;
import lab6.main.java.exception.FileLoadingException;
import lab6.main.java.network.CommandRequest;
import lab6.main.java.network.CommandResponse;
import lab6.main.java.util.OutputManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Server {
    private final int port;
    private final CollectionManager collectionManager;
    private final OutputManager outputManager;
    private final CommandManager commandManager;
    private DatagramChannel channel;
    private Selector selector;
    private volatile boolean running;

    public Server(int port, String filePath) {
        this.port = port;
        this.outputManager = new OutputManager(System.out);
        this.collectionManager = new CollectionManager(filePath);
        this.commandManager = new CommandManager(collectionManager, outputManager);
        this.running = true;
        loadInitialCollection(filePath);
    }

    private void loadInitialCollection(String filePath) {
        CollectionLoader loader = new CollectionLoader(filePath, outputManager);
        try {
            List<LabWork> data = loader.loadCollection();
            collectionManager.loadData(data);
            outputManager.println("Loaded " + data.size() + " elements from " + filePath);
        } catch (FileLoadingException e) {
            outputManager.println("Failed to load collection: " + e.getMessage());
        }
    }

    public void start() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            outputManager.println("Server started on port " + port);

            // Запуск потока для обработки консольных команд
            Thread consoleThread = new Thread(this::handleConsoleCommands);
            consoleThread.start();

            while (running) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isReadable()) {
                        handleRequest(key);
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
            outputManager.println("Received command: " + request.getCommandName());

            CommandResponse response = commandManager.executeCommand(request);
            outputManager.println("Sending response: " + response.getMessage());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            oos.flush();

            ByteBuffer responseBuffer = ByteBuffer.wrap(baos.toByteArray());
            channel.send(responseBuffer, clientAddress);
        } catch (Exception e) {
            outputManager.println("Error handling request: " + e.getMessage());
        }
    }

    private void handleConsoleCommands() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            outputManager.print("Server> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s+", 2);
            String commandName = parts[0].toLowerCase();

            if (commandName.equals("server_save")) {
                commandManager.executeSaveCommand();
            } else {
                outputManager.println("Unknown server command: " + commandName);
            }
        }
        scanner.close();
    }

    public void stop() {
        try {
            running = false;
            commandManager.executeSaveCommand();
            outputManager.println("Collection saved on server shutdown.");
            if (channel != null) {
                channel.close();
            }
            if (selector != null) {
                selector.close();
            }
        } catch (Exception e) {
            outputManager.println("Error during server shutdown: " + e.getMessage());
        }
    }
}