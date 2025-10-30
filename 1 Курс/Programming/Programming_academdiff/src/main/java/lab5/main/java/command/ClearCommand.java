package lab5.main.java.command;

import lab5.main.java.util.OutputManager;
import lab5.main.java.collection.CollectionManager;


public class ClearCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public ClearCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("clear", "clear : очистить коллекцию");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        collectionManager.clear();
        outputManager.println("Collection cleared successfully.");
        return true;
    }
}
