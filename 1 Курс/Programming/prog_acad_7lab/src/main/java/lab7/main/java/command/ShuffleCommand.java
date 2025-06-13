package lab7.main.java.command;

import lab6.main.java.collection.CollectionManager;
import lab6.main.java.command.AbstractCommand;
import lab6.main.java.network.CommandResponse;

public class ShuffleCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ShuffleCommand(CollectionManager collectionManager) {
        super("shuffle", "shuffle : shuffle the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) {
            return new CommandResponse(false, "This command doesn't require an argument.", null);
        }
        collectionManager.shuffle();
        return new CommandResponse(true, "Collection shuffled successfully!", null);
    }
}