package lab7.main.java.client;

import lab7.main.java.command.ExecuteScriptCommand;
import lab7.main.java.data.LabWork;
import lab7.main.java.data.User;
import lab7.main.java.network.CommandRequest;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.InputManager;
import lab7.main.java.util.OutputManager;

import java.io.*;
import java.net.*;

public class Client {
    private final String host;
    private final int port;
    private final InputManager inputManager;
    private final OutputManager outputManager;
    private DatagramSocket socket;
    private String username;
    private String password;
    private User currentUser;
    private static final int TIMEOUT_MS = 5000;

    public Client(String host, int port, InputManager inputManager, OutputManager outputManager) {
        this.host = host;
        this.port = port;
        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void start() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT_MS);
            outputManager.println("Client started. Use 'login' or 'register' to begin.");

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                outputManager.print("> ");
                String line = consoleReader.readLine();
                if (line == null || line.trim().equals("exit")) break;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.trim().split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String argument = parts.length > 1 ? parts[1] : "";

                CommandRequest request = processCommand(commandName, argument);
                if (request == null) continue;

                CommandResponse response = sendRequest(request);
                if (response != null) {
                    outputManager.println(response.getMessage());
                    if (response.getData() != null) response.getData().forEach(obj -> outputManager.println(obj.toString()));
                }
            }
        } catch (IOException e) {
            outputManager.println("Client error: " + e.getMessage());
        } finally {
            if (socket != null) socket.close();
        }
    }

    private CommandRequest processCommand(String commandName, String argument) {
        if (commandName.equals("login")) {
            if (argument.isEmpty()) {
                outputManager.println("Usage: login <username>");
                return null;
            }
            username = argument;
            password = inputManager.getStringInput("Enter password: ", false);
            CommandRequest testRequest = new CommandRequest("help", "", null, username, password);
            CommandResponse response = sendRequest(testRequest);
            if (response != null && response.isSuccess()) {
                outputManager.println("Login successful.");
                currentUser = new User(1, username); // Adjust based on your User class
            } else {
                outputManager.println("Login failed: " + (response != null ? response.getMessage() : "No response"));
                username = null;
                password = null;
                currentUser = null;
            }
            return null;
        } else if (commandName.equals("register")) {
            if (argument.isEmpty()) {
                outputManager.println("Usage: register <username>");
                return null;
            }
            String regPassword = inputManager.getStringInput("Enter password: ", false);
            return new CommandRequest("register", argument, null, argument, regPassword);
        } else if (username == null || password == null) {
            outputManager.println("Please login first.");
            return null;
        }

        if (commandName.equals("add") || commandName.equals("update")) {
            LabWork labWork = commandName.equals("add") ? inputManager.getLabWorkFromInput(currentUser) :
                    (argument.isEmpty() ? null : inputManager.getLabWorkFromInput(currentUser));
            if (labWork == null) {
                outputManager.println("Failed to create LabWork.");
                return null;
            }
            return new CommandRequest(commandName, argument, labWork, username, password);
        } else if (commandName.equals("execute_script")) {
            ExecuteScriptCommand scriptCommand = new ExecuteScriptCommand(this, inputManager, outputManager);
            scriptCommand.execute(argument, currentUser);
            return null;
        }
        return new CommandRequest(commandName, argument, null, username, password);
    }

    public CommandResponse sendRequest(CommandRequest request) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();
            byte[] sendData = baos.toByteArray();

            InetAddress address = InetAddress.getByName(host);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            socket.send(sendPacket);

            byte[] receiveData = new byte[65535];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData, 0, receivePacket.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (CommandResponse) ois.readObject();
        } catch (Exception e) {
            outputManager.println("Error communicating with server: " + e.getMessage());
            return null;
        }
    }
}