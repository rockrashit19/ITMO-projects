package command;

import collection.CollectionManager;
import network.CommandResponse;

public class InfoCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "info : display collection information");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        String message = "Collection Type: " + collectionManager.getCollectionType() + "\n" +
                "Initialization Date: " + collectionManager.getInitializationDate() + "\n" +
                "Number of elements: " + collectionManager.getSize();
        return new CommandResponse(true, message, null);
    }
}