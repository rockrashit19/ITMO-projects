package command;

import collection.CollectionManager;
import network.CommandResponse;

public class ClearCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "clear : clear the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (args.length < 2 || !(args[1] instanceof String username))
            return new CommandResponse(false, "Username is required.", null);
        try {
            collectionManager.clear(username);
            return new CommandResponse(true, "Collection cleared successfully!", null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error clearing collection: " + e.getMessage(), null);
        }
    }
}