package command;

import collection.CollectionManager;
import network.CommandResponse;

public class ShuffleCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ShuffleCommand(CollectionManager collectionManager) {
        super("shuffle", "shuffle : shuffle the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        System.out.println("[CMD] ShuffleCommand invoked");
        if (!argument.isEmpty()) {
            return new CommandResponse(false, "This command doesn't require an argument.", null);
        }
        collectionManager.shuffle();
        return new CommandResponse(true, "Collection shuffled successfully!", null);
    }
}