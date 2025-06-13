package lab7.main.java.command;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.data.User;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.DatabaseManager;

public class ClearCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("clear", "clear : clear the collection");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) {
            return new CommandResponse(false, "This command doesn't require an argument.", null);
        }
        if (args.length < 1 || !(args[0] instanceof User)) {
            return new CommandResponse(false, "User is required for this command.", null);
        }
        User user = (User) args[0];
        collectionManager.clear(user.getId());
        return new CommandResponse(true, "Collection cleared successfully!", null);
    }
}