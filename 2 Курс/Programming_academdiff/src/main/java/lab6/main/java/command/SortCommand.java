package lab6.main.java.command;

import lab6.main.java.collection.CollectionManager;
import lab6.main.java.network.CommandResponse;

public class SortCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public SortCommand(CollectionManager collectionManager) {
        super("sort", "sort : sort the collection by id");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) {
            return new CommandResponse(false, "This command doesn't require an argument.", null);
        }
        try {
            collectionManager.sort();
            return new CommandResponse(true, "Collection sorted successfully!", null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error sorting collection: " + e.getMessage(), null);
        }
    }
}