package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;

public class RemoveByIdCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public RemoveByIdCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("remove_by_id", "remove_by_id id : удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (argument == null || argument.isEmpty()) {
            outputManager.println("Usage: remove_by_id <id>");
            return false;
        }

        try {
            long id = Long.parseLong(argument);
            if (collectionManager.removeById(id)) {
                outputManager.println("LabWork with ID " + id + " removed successfully.");
                return true;
            } else {
                outputManager.println("LabWork with ID " + id + " not found.");
                return false;
            }
        } catch (NumberFormatException e) {
            outputManager.println("Invalid ID format. Please enter a valid number.");
            return false;
        }
    }
}
