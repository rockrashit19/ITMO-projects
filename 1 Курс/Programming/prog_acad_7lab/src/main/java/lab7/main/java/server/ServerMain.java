package lab7.main.java.server;

import lab6.main.java.server.Server;

public class ServerMain {
    public static void main(String[] args) {
        int port = 2222;
        String filePath = "src/main/java/lab6/resources/data.json";
        lab6.main.java.server.Server server = new Server(port, filePath);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }
}