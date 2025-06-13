package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;

public class SortCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public SortCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("sort", "sort : отсортировать коллекцию в естественном порядке");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        collectionManager.sort();
        outputManager.println("Collection sorted successfully.");
        return true;
    }
}
