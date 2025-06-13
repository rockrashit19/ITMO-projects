package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.data.LabWork;
import lab5.main.java.exception.InvalidDataException;
import lab5.main.java.util.InputManager;
import lab5.main.java.util.OutputManager;

public class AddCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final InputManager inputManager;
    private final OutputManager outputManager;

    public AddCommand(CollectionManager collectionManager, InputManager inputManager, OutputManager outputManager) {
        super("add", "add : добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) {
                throw new IllegalArgumentException("This command doesn't require an argument.");
            }

            LabWork newLabWork;
            try {
                newLabWork = inputManager.getLabWorkFromInput();
            } catch (InvalidDataException e) {
                outputManager.println("Error creating LabWork: " + e.getMessage());
                return false;
            }

            newLabWork.setId(collectionManager.generateNextId());
            newLabWork.setCreationDate(java.time.ZonedDateTime.now());
            collectionManager.add(newLabWork);

            outputManager.println("LabWork added successfully!");
            return true;
        } catch (IllegalArgumentException e) {
            outputManager.println("Error adding lab work: " + e.getMessage());
            return false;
        }
    }
}