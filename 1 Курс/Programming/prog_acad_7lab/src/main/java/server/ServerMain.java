package server;

import database.DatabaseManager;
import io.github.cdimascio.dotenv.Dotenv;

public class ServerMain {
    public static void main(String[] args) {
        try {

            int port = 4242;
            String dbUser = System.getenv("DB_USER");
            String dbPass = System.getenv("DB_PASSWORD");
            if (dbUser == null || dbPass == null) {
                System.err.println("Set DB_USER and DB_PASSWORD env vars.");
                return;
            }
            DatabaseManager dbManager = new DatabaseManager(dbUser, dbPass);
            Server server = new Server(port, dbManager);
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
