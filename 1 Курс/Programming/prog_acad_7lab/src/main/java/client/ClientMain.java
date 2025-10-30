package client;

import util.InputManager;
import util.OutputManager;

public class ClientMain {
    public static void main(String[] args) {
        String host = (args.length > 0 ? args[0] : null);
        String portStr = (args.length > 1 ? args[1] : null);

        if (host == null) host = System.getProperty("SERVER_HOST");
        if (portStr == null) portStr = System.getProperty("SERVER_PORT");

        if (host == null) host = System.getenv("SERVER_HOST");
        if (portStr == null) portStr = System.getenv("SERVER_PORT");

        if (host == null) host = "localhost";
        int port = 4242;
        try { if (portStr != null) port = Integer.parseInt(portStr); } catch (Exception ignored) {}

        OutputManager out = new OutputManager(System.out);
        InputManager in = new InputManager(out);
        Client client = new Client(host, port, in, out);
        client.start();
    }
}
