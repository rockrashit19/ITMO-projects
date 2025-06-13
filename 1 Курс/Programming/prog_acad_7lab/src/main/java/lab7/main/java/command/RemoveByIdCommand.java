package lab7.main.java.command;

import lab6.main.java.collection.CollectionManager;
import lab6.main.java.command.AbstractCommand;
import lab6.main.java.network.CommandResponse;

public class RemoveByIdCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "remove_by_id {id} : remove LabWork by id");
        this.collectionManager = collectionManager;
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