package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;
import lab5.main.java.collection.CollectionSaver;
import lab5.main.java.exception.FileSavingException;

public class SaveCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final CollectionSaver collectionSaver;
    private final OutputManager outputManager;

    public SaveCommand(CollectionManager collectionManager, CollectionSaver collectionSaver, OutputManager outputManager) {
        super("save", "save : сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
        this.collectionSaver = collectionSaver;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        try {
            collectionSaver.saveCollection(collectionManager.getAll());
            return true;
        } catch (FileSavingException e) {
            outputManager.println("Error saving collection: " + e.getMessage());
            return false;
        }
    }
}
