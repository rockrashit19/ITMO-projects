package lab6.main.java.client;

import lab6.main.java.util.InputManager;
import lab6.main.java.util.OutputManager;

public class ClientMain {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;
        OutputManager outputManager = new OutputManager(System.out);
        InputManager inputManager = new InputManager(outputManager);
        Client client = new Client(host, port, inputManager, outputManager);
        client.start();
    }
}