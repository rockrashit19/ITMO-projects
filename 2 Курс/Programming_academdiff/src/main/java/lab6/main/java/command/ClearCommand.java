package lab6.main.java.command;

import lab6.main.java.collection.CollectionManager;
import lab6.main.java.network.CommandResponse;

public class ClearCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "clear : clear the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) {
            return new CommandResponse(false, "This command doesn't require an argument.", null);
        }
        collectionManager.clear();
        return new CommandResponse(true, "Collection cleared successfully!", null);
    }
}