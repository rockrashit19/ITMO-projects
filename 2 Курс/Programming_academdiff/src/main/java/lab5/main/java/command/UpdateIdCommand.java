package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.data.LabWork;
import lab5.main.java.exception.InvalidDataException;
import lab5.main.java.util.InputManager;
import lab5.main.java.util.OutputManager;

public class UpdateIdCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final InputManager inputManager;
    private final OutputManager outputManager;

    public UpdateIdCommand(CollectionManager collectionManager, InputManager inputManager, OutputManager outputManager) {
        super("update", "update {id} : обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (argument == null || argument.isEmpty()) {
            outputManager.println("Usage: update <id>");
            return false;
        }

        try {
            long id = Long.parseLong(argument);

            if (!collectionManager.containsId(id)) {
                outputManager.println("LabWork with ID " + id + " not found.");
                return false;
            }


            LabWork newLabWork;
            try {
                newLabWork = inputManager.getLabWorkFromInput();
            } catch (InvalidDataException e) {
                outputManager.println("Error creating LabWork: " + e.getMessage());
                return false;
            }

            if (collectionManager.update(id, newLabWork)) {
                outputManager.println("LabWork with ID " + id + " updated successfully.");
                return true;
            } else {
                outputManager.println("Failed to update LabWork with ID " + id + ".");
                return false;
            }
        } catch (NumberFormatException e) {
            outputManager.println("Invalid ID format. Please enter a valid number.");
            return false;
        }
    }
}
