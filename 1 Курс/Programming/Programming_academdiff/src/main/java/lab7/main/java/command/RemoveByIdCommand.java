package lab7.main.java.command;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.command.AbstractCommand;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.DatabaseManager;

public class RemoveByIdCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;

    public RemoveByIdCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_by_id", "remove_by_id {id} : remove LabWork by id");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (argument.isEmpty()) {
            return new CommandResponse(false, "Usage: remove_by_id <id>", null);
        }
        try {
            long id = Long.parseLong(argument);
            boolean success = collectionManager.removeById(id);
            return new CommandResponse(success, success ? "LabWork removed successfully!" : "No LabWork found with id " + id, null);
        } catch (NumberFormatException e) {
            return new CommandResponse(false, "Invalid id format: " + argument, null);
        }
    }
}