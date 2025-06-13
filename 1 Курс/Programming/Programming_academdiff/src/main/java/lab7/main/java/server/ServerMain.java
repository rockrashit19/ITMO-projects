package lab7.main.java.server;

public class ServerMain {
    public static void main(String[] args) {
        int port = 12345;
        String dbUrl = "jdbc:postgresql://localhost/studs";
        String dbUser = "studs";
        String dbPassword = "ZaA6g7nf3o84NQy4";
        Server server = new Server(port, dbUrl, dbUser, dbPassword);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }
}