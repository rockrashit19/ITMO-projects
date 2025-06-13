package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;

public class ShuffleCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public ShuffleCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("shuffle", "shuffle : перемешать элементы коллекции в случайном порядке");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        collectionManager.shuffle();
        outputManager.println("Collection shuffled successfully.");
        return true;
    }
}
