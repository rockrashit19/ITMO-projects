package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;

public class InfoCommand  extends AbstractCommand{
    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public InfoCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("info", "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        outputManager.println("Collection Type: " + collectionManager.getCollectionType());
        outputManager.println("Initialization Date: " + collectionManager.getInitializationDate());
        outputManager.println("Number of elements: " + collectionManager.getSize());
        return true;
    }
}
