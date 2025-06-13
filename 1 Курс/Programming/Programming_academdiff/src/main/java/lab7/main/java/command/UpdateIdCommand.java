package lab7.main.java.command;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.data.LabWork;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.DatabaseManager;

public class UpdateIdCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;

    public UpdateIdCommand(CollectionManager collectionManager, DatabaseManager dbManager) {
        super("update", "update {id} : update LabWork with specified id");
        this.collectionManager = collectionManager;
        this.databaseManager = dbManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (argument.isEmpty()) {
            return new CommandResponse(false, "Usage: update <id>", null);
        }
        if (args.length == 0 || !(args[1] instanceof LabWork)) {
            return new CommandResponse(false, "LabWork is required for update command.", null);
        }
        try {
            long id = Long.parseLong(argument);
            LabWork newLabWork = (LabWork) args[1];
            if (!collectionManager.containsId(id)) {
                return new CommandResponse(false, "No LabWork found with id " + id, null);
            }
            LabWork oldLabWork = collectionManager.getAll().stream()
                    .filter(lw -> lw.getId() == id)
                    .findFirst()
                    .orElse(null);
            if (oldLabWork != null) {
                newLabWork.setCreationDate(oldLabWork.getCreationDate());
            }
            newLabWork.setId(id);
            collectionManager.update(id, newLabWork);
            return new CommandResponse(true, "LabWork updated successfully!", null);
        } catch (NumberFormatException e) {
            return new CommandResponse(false, "Invalid id format: " + argument, null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error updating LabWork: " + e.getMessage(), null);
        }
    }
}