package lab7.main.java.client;

import lab7.main.java.client.Client;
import lab7.main.java.util.InputManager;
import lab7.main.java.util.OutputManager;

public class ClientMain {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;
        OutputManager outputManager = new OutputManager(System.out);
        InputManager inputManager = new InputManager(outputManager);
        lab7.main.java.client.Client client = new Client(host, port, inputManager, outputManager);
        client.start();
    }
}