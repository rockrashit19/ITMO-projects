package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;

public class ReorderCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public ReorderCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("reorder", "reorder : отсортировать коллекцию в порядке, обратном нынешнему");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        collectionManager.reorder();
        outputManager.println("Collection reordered successfully.");
        return true;
    }
}
