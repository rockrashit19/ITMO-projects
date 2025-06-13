package lab6.main.java.server;

public class ServerMain {
    public static void main(String[] args) {
        int port = 12345;
        String filePath = "src/main/java/lab6/resources/data.json";
        Server server = new Server(port, filePath);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }
}