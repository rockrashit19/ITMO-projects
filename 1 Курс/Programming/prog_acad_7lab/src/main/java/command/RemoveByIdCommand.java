package command;

import collection.CollectionManager;
import network.CommandResponse;

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
        if (args.length < 2 || !(args[1] instanceof String username))
            return new CommandResponse(false, "Username is required.", null);
        try {
            long id = Long.parseLong(argument);
            boolean success = collectionManager.removeById(id, username);
            return new CommandResponse(success, success ? "LabWork removed successfully!" : "No LabWork found with id " + id, null);
        } catch (NumberFormatException e) {
            return new CommandResponse(false, "Invalid id format: " + argument, null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error removing LabWork: " + e.getMessage(), null);
        }
    }
}