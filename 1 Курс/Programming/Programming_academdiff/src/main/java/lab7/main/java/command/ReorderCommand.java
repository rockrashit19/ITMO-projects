package lab7.main.java.command;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.network.CommandResponse;

public class ReorderCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ReorderCommand(CollectionManager collectionManager) {
        super("reorder", "reorder : reverse the collection order");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) {
            return new CommandResponse(false, "This command doesn't require an argument.", null);
        }
        collectionManager.reorder();
        return new CommandResponse(true, "Collection reordered successfully!", null);
    }
}