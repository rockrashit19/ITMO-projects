package lab5.main;

import lab5.main.java.collection.*;
import lab5.main.java.data.LabWork;
import lab5.main.java.exception.FileLoadingException;
import lab5.main.java.util.*;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        OutputManager outputManager = new OutputManager(System.out);
        InputManager inputManager = new InputManager(outputManager);

        String filePath = System.getenv("LAB_DATA_FILE");

        if (filePath == null || filePath.isEmpty()) {
            outputManager.println("Environment variable LAB_DATA_FILE is not set. Using a default empty collection.");
        }

        CollectionManager collectionManager = new CollectionManager();
        CollectionLoader collectionLoader = new CollectionLoader(filePath, outputManager);
        CollectionSaver collectionSaver = new CollectionSaver(filePath, outputManager);

        try {
            if (filePath != null && !filePath.isEmpty()) {
                List<LabWork> initialData = collectionLoader.loadCollection();
                collectionManager.loadData(initialData);
            }
        } catch (FileLoadingException e) {
            outputManager.println("Error loading collection: " + e.getMessage());
            outputManager.println("Starting with an empty collection.");
        }


        CommandManager commandManager = new CommandManager(collectionManager, inputManager, collectionSaver, outputManager);

        Scanner scanner = new Scanner(System.in);
        String line;
        outputManager.println("Enter command (or 'help' for available commands):");
//        outputManager.println("Current Working Directory: " + System.getProperty("user.dir"));
        while (true) {
            outputManager.print("> ");
            line = scanner.nextLine();

            if (line.equals("exit")) {
                commandManager.executeCommand("exit");
                break;
            }
            if (!line.trim().isEmpty()) {
                commandManager.executeCommand(line);
            }
        }
        scanner.close();
    }
}
