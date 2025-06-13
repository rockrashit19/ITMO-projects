package lab7.main.java.server;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.data.Difficulty;
import lab7.main.java.data.LabWork;
import lab7.main.java.network.CommandRequest;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.database.DatabaseManager;
import lab7.main.java.util.OutputManager;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    private final int port;
    private final CollectionManager collectionManager;
    private final OutputManager outputManager;
    private final DatabaseManager dbManager;
    private DatagramChannel channel;
    private Selector selector;
    private volatile boolean running;
    private final ReentrantLock lock = new ReentrantLock();

    public Server(int port, DatabaseManager dbManager) throws SQLException {
        this.port = port;
        this.outputManager = new OutputManager(System.out);
        this.dbManager = dbManager;
        this.collectionManager = new CollectionManager(dbManager);
        this.running = true;
    }

    public void start() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            outputManager.println("Server started on port " + port);

            // Start console thread for server commands
            Thread consoleThread = new Thread(this::handleConsoleCommands);
            consoleThread.start();

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

            // Authenticate user
            if (!dbManager.authenticateUser(request.getUsername(), request.getPassword())) {
                sendResponse(clientAddress, new CommandResponse(false, "Authentication failed", null));
                return;
            }

            outputManager.println("Received command: " + request.getCommandName() + " from " + request.getUsername());

            // Process command in ForkJoinPool
            ForkJoinPool pool = ForkJoinPool.commonPool();
            CommandResponse response = pool.invoke(new CommandTask(request, collectionManager, dbManager));

            // Send response in a new thread
            new Thread(() -> sendResponse(clientAddress, response)).start();

        } catch (Exception e) {
            outputManager.println("Error handling request: " + e.getMessage());
        }
    }

    private void sendResponse(InetSocketAddress clientAddress, CommandResponse response) {
        lock.lock();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            oos.flush();

            ByteBuffer responseBuffer = ByteBuffer.wrap(baos.toByteArray());
            channel.send(responseBuffer, clientAddress);
            outputManager.println("Sent response: " + response.getMessage());
        } catch (IOException e) {
            outputManager.println("Error sending response: " + e.getMessage());
        } finally {
            lock.unlock();
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

            if (commandName.equals("server_exit")) {
                stop();
            } else {
                outputManager.println("Unknown server command: " + commandName);
            }
        }
        scanner.close();
    }

    public void stop() {
        try {
            running = false;
            outputManager.println("Server shutting down.");
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

    private static class CommandTask extends java.util.concurrent.RecursiveTask<CommandResponse> {
        private final CommandRequest request;
        private final CollectionManager collectionManager;
        private final DatabaseManager dbManager;

        public CommandTask(CommandRequest request, CollectionManager collectionManager, DatabaseManager dbManager) {
            this.request = request;
            this.collectionManager = collectionManager;
            this.dbManager = dbManager;
        }

        @Override
        protected CommandResponse compute() {
            try {
                String command = request.getCommandName().toLowerCase();
                String argument = request.getArgument();
                Object data = request.getData();
                String username = request.getUsername();

                switch (command) {
                    case "add":
                        collectionManager.add((LabWork) data, username);
                        return new CommandResponse(true, "LabWork added successfully", null);
                    case "update":
                        long id = Long.parseLong(argument);
                        if (collectionManager.update(id, (LabWork) data, username)) {
                            return new CommandResponse(true, "LabWork updated", null);
                        } else {
                            return new CommandResponse(false, "Update failed: Not owner or not found", null);
                        }
                    case "remove_by_id":
                        if (collectionManager.removeById(Long.parseLong(argument), username)) {
                            return new CommandResponse(true, "LabWork removed", null);
                        } else {
                            return new CommandResponse(false, "Remove failed: Not owner or not found", null);
                        }
                    case "clear":
                        collectionManager.clear(username);
                        return new CommandResponse(true, "Collection cleared for user", null);
                    case "show":
                        return new CommandResponse(true, "Collection retrieved", collectionManager.getAll());
                    case "info":
                        return new CommandResponse(true, "Collection info: Type=" + collectionManager.getCollectionType() +
                                ", Size=" + collectionManager.getSize() + ", InitDate=" + collectionManager.getInitializationDate(), null);
                    case "shuffle":
                        collectionManager.shuffle();
                        return new CommandResponse(true, "Collection shuffled", null);
                    case "reorder":
                        collectionManager.reorder();
                        return new CommandResponse(true, "Collection reordered", null);
                    case "sort":
                        collectionManager.sort();
                        return new CommandResponse(true, "Collection sorted", null);
                    case "remove_all_by_difficulty":
                        if (collectionManager.removeAllByDifficulty(Difficulty.valueOf(argument), username)) {
                            return new CommandResponse(true, "LabWorks with difficulty removed", null);
                        } else {
                            return new CommandResponse(false, "No LabWorks found with difficulty", null);
                        }
                    case "remove_any_by_difficulty":
                        if (collectionManager.removeAnyByDifficulty(Difficulty.valueOf(argument), username)) {
                            return new CommandResponse(true, "One LabWork with difficulty removed", null);
                        } else {
                            return new CommandResponse(false, "No LabWork found with difficulty", null);
                        }
                    case "register":
                        if (dbManager.registerUser(username, request.getPassword())) {
                            return new CommandResponse(true, "User registered successfully", null);
                        } else {
                            return new CommandResponse(false, "Registration failed: Username exists", null);
                        }
                    default:
                        return new CommandResponse(false, "Unknown command", null);
                }
            } catch (SQLException e) {
                return new CommandResponse(false, "Database error: " + e.getMessage(), null);
            } catch (Exception e) {
                return new CommandResponse(false, "Error: " + e.getMessage(), null);
            }
        }
    }
}