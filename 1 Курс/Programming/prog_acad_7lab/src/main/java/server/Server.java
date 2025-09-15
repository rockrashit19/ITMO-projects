package server;

import collection.CollectionManager;
import data.Difficulty;
import data.LabWork;
import network.CommandRequest;
import network.CommandResponse;
import database.DatabaseManager;
import util.OutputManager;

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
    private final CommandManager commandManager;


    public Server(int port, DatabaseManager dbManager) throws SQLException {
        this.port = port;
        this.outputManager = new OutputManager(System.out);
        this.dbManager = dbManager;
        this.collectionManager = new CollectionManager(dbManager);
        this.running = true;
        this.commandManager = new CommandManager(collectionManager, outputManager);
    }

    public void start() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            outputManager.println("Server started on port " + port);

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
            String cmd = request.getCommandName();
            String u = request.getUsername();
            String p = request.getPassword();

            if ("register".equalsIgnoreCase(cmd)) {
                boolean ok = dbManager.registerUser(u, p);
                sendResponse(clientAddress, new CommandResponse(ok, ok ? "User registered successfully" : "Registration failed: Username exists", null));
                return;
            }
            if ("login".equalsIgnoreCase(cmd)) {
                boolean ok = dbManager.authenticateUser(u, p);
                sendResponse(clientAddress, new CommandResponse(ok, ok ? "Login successful" : "Authentication failed", null));
                return;
            }

            if (!dbManager.authenticateUser(u, p)) {
                sendResponse(clientAddress, new CommandResponse(false, "Authentication failed", null));
                return;
            }

            outputManager.println("Received command: " + request.getCommandName() + " from " + request.getUsername());

            ForkJoinPool pool = ForkJoinPool.commonPool();
            CommandResponse response = pool.submit(() -> commandManager.executeCommand(request)).join();

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

//    private static class CommandTask extends java.util.concurrent.RecursiveTask<CommandResponse> {
//        private final CommandRequest request;
//        private final CollectionManager collectionManager;
//        private final DatabaseManager dbManager;
//
//        public CommandTask(CommandRequest request, CollectionManager collectionManager, DatabaseManager dbManager) {
//            this.request = request;
//            this.collectionManager = collectionManager;
//            this.dbManager = dbManager;
//        }
//
//        @Override
//        protected CommandResponse compute() {
//            try {
//                String command = request.getCommandName().toLowerCase();
//                String argument = request.getArgument();
//                Object data = request.getData();
//                String username = request.getUsername();
//
//                switch (command) {
//                    case "add":
//                        collectionManager.add((LabWork) data, username);
//                        return new CommandResponse(true, "LabWork added successfully", null);
//                    case "update":
//                        long id = Long.parseLong(argument);
//                        if (collectionManager.update(id, (LabWork) data, username)) {
//                            return new CommandResponse(true, "LabWork updated", null);
//                        } else {
//                            return new CommandResponse(false, "Update failed: Not owner or not found", null);
//                        }
//                    case "remove_by_id":
//                        if (collectionManager.removeById(Long.parseLong(argument), username)) {
//                            return new CommandResponse(true, "LabWork removed", null);
//                        } else {
//                            return new CommandResponse(false, "Remove failed: Not owner or not found", null);
//                        }
//                    case "clear":
//                        collectionManager.clear(username);
//                        return new CommandResponse(true, "Collection cleared for user", null);
//                    case "show":
//                        return new CommandResponse(true, "Collection retrieved",
//                                new java.util.ArrayList<Object>(collectionManager.getAll()));
//                    case "info":
//                        return new CommandResponse(true, "Collection info: Type=" + collectionManager.getCollectionType() +
//                                ", Size=" + collectionManager.getSize() + ", InitDate=" + collectionManager.getInitializationDate(), null);
//                    case "shuffle":
//                        collectionManager.shuffle();
//                        return new CommandResponse(true, "Collection shuffled", null);
//                    case "reorder":
//                        collectionManager.reorder();
//                        return new CommandResponse(true, "Collection reordered", null);
//                    case "sort":
//                        collectionManager.sort();
//                        return new CommandResponse(true, "Collection sorted", null);
//                    case "remove_all_by_difficulty":
//                        if (collectionManager.removeAllByDifficulty(Difficulty.valueOf(argument), username)) {
//                            return new CommandResponse(true, "LabWorks with difficulty removed", null);
//                        } else {
//                            return new CommandResponse(false, "No LabWorks found with difficulty", null);
//                        }
//                    case "remove_any_by_difficulty":
//                        if (collectionManager.removeAnyByDifficulty(Difficulty.valueOf(argument), username)) {
//                            return new CommandResponse(true, "One LabWork with difficulty removed", null);
//                        } else {
//                            return new CommandResponse(false, "No LabWork found with difficulty", null);
//                        }
//                    case "register":
//                        if (dbManager.registerUser(username, request.getPassword())) {
//                            return new CommandResponse(true, "User registered successfully", null);
//                        } else {
//                            return new CommandResponse(false, "Registration failed: Username exists", null);
//                        }
//                    case "help": {
//                        String help = String.join("\n", new String[]{
//                                "help : display available commands",
//                                "info : display collection information",
//                                "show : display all elements in the collection",
//                                "add : add a new LabWork",
//                                "update <id> : update LabWork with specified id",
//                                "remove_by_id <id> : remove LabWork by id",
//                                "clear : clear the collection (only your items)",
//                                "shuffle : shuffle the collection",
//                                "reorder : reverse the collection order",
//                                "sort : sort the collection by id",
//                                "remove_all_by_difficulty <DIFF> : remove all with difficulty",
//                                "remove_any_by_difficulty <DIFF> : remove one with difficulty",
//                                "filter_greater_than_minimal_point <point> : show with minimalPoint > point",
//                                "register : create user",
//                                "login : authenticate user"
//                        });
//                        return new CommandResponse(true, help, null);
//                    }
//                    default:
//                        return new CommandResponse(false, "Unknown command", null);
//                }
//            } catch (SQLException e) {
//                return new CommandResponse(false, "Database error: " + e.getMessage(), null);
//            } catch (Exception e) {
//                return new CommandResponse(false, "Error: " + e.getMessage(), null);
//            }
//        }
//    }
}