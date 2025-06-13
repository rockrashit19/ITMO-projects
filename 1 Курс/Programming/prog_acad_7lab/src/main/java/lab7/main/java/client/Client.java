package lab7.main.java.client;

import lab7.main.java.command.ExecuteScriptCommand;
import lab7.main.java.data.LabWork;
import lab7.main.java.network.CommandRequest;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.InputManager;
import lab7.main.java.util.OutputManager;

import java.io.*;
import java.net.*;
import java.util.List;

public class Client {
    private final String host;
    private final int port;
    private final InputManager inputManager;
    private final OutputManager outputManager;
    private DatagramSocket socket;
    private String username;
    private String password;
    private static final int TIMEOUT_MS = 5000;

    public Client(String host, int port, InputManager inputManager, OutputManager outputManager) {
        this.host = host;
        this.port = port;
        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }

    public boolean authenticate() {
        try {
            outputManager.println("Do you want to (1) Login or (2) Register?");
            String choice = inputManager.getStringInput("Enter choice (1 or 2): ", false);
            username = inputManager.getStringInput("Enter username: ", false);
            password = inputManager.getStringInput("Enter password: ", false);

            CommandRequest request = new CommandRequest(choice.equals("1") ? "login" : "register", "", null, username, password);
            CommandResponse response = sendRequest(request);
            if (response != null && response.isSuccess()) {
                outputManager.println(response.getMessage());
                return true;
            } else {
                outputManager.println(response != null ? response.getMessage() : "Authentication failed");
                return false;
            }
        } catch (Exception e) {
            outputManager.println("Authentication error: " + e.getMessage());
            return false;
        }
    }

    public void start() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT_MS);
            outputManager.println("Client started. Type 'help' for commands.");

            // Authenticate user
            if (!authenticate()) {
                outputManager.println("Authentication required to proceed.");
                return;
            }

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                outputManager.print("> ");
                String line = consoleReader.readLine();
                if (line == null || line.trim().equals("exit")) {
                    break;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.trim().split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String argument = parts.length > 1 ? parts[1] : "";

                CommandRequest request = processCommand(commandName, argument);
                if (request == null) {
                    continue;
                }

                CommandResponse response = sendRequest(request);
                if (response != null) {
                    outputManager.println(response.getMessage());
                    List<Object> responseData = response.getData();
                    if (responseData != null && !responseData.isEmpty()) {
                        responseData.forEach(item -> {
                            if (item != null) {
                                outputManager.println(item.toString());
                            }
                        });
                    }
                }
            }
        } catch (IOException e) {
            outputManager.println("Client error: " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private CommandRequest processCommand(String commandName, String argument) {
        try {
            if (commandName.equals("add")) {
                LabWork labWork = inputManager.getLabWorkFromInput();
                if (labWork == null) {
                    outputManager.println("Failed to create LabWork.");
                    return null;
                }
                return new CommandRequest(commandName, "", labWork, username, password);
            } else if (commandName.equals("update")) {
                if (argument == null || argument.trim().isEmpty()) {
                    outputManager.println("Usage: update <id>");
                    return null;
                }
                long id;
                try {
                    id = Long.parseLong(argument);
                } catch (NumberFormatException e) {
                    outputManager.println("Invalid id format: " + argument);
                    return null;
                }

                CommandRequest showRequest = new CommandRequest("show", "", null, username, password);
                CommandResponse showResponse = sendRequest(showRequest);
                if (showResponse == null || !showResponse.isSuccess()) {
                    outputManager.println(showResponse != null ? showResponse.getMessage() : "Failed to retrieve collection.");
                    return null;
                }
                List<Object> labWorks = showResponse.getData();
                boolean idExists = false;
                if (labWorks != null) {
                    for (Object obj : labWorks) {
                        if (obj instanceof LabWork labWork && labWork.getId() == id) {
                            idExists = true;
                            break;
                        }
                    }
                }
                if (!idExists) {
                    outputManager.println("No LabWork with id " + id + " found.");
                    return null;
                }

                labWork = inputManager.getLabWorkFromInput();
                if (labWork == null) {
                    outputManager.println("Failed to create LabWork.");
                    return null;
                }
                return new CommandRequest(commandName, argument, labWork, username, password);
            } else if (commandName.equals("execute_script")) {
                ExecuteScriptCommand scriptCommand = new ExecuteScriptCommand(this, inputManager, outputManager);
                CommandResponse response = scriptCommand.execute(argument);
                outputManager.println(response.getMessage());
                List<Object> responseData = response.getData();
                if (responseData != null && !responseData.isEmpty()) {
                    responseData.forEach(item -> {
                        if (item != null) {
                            outputManager.println(item.toString());
                        }
                    });
                }
                return null;
            } else {
                return new CommandRequest(commandName, argument, null, username, password);
            }
        } catch (Exception e) {
            outputManager.println("Error processing command: " + e.getMessage());
            return null;
        }
    }

    public CommandResponse sendRequest(CommandRequest request) {
        try {
            // Serialize request
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();
            byte[] sendData = baos.toByteArray();

            // Send request
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
            socket.send(sendPacket);

            // Receive response
            byte[] receiveData = new byte[65535];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // Deserialize response
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData, 0, receivePacket.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (CommandResponse) ois.readObject();
        } catch (SocketTimeoutException e) {
            outputManager.println("Server response timeout.");
            return null;
        } catch (IOException | ClassNotFoundException e) {
            outputManager.println("Error communicating with server: " + e.getMessage());
            return null;
        }
    }
}