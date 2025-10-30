package command;

import collection.CollectionManager;
import data.LabWork;
import network.CommandResponse;

public class UpdateIdCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public UpdateIdCommand(CollectionManager collectionManager) {
        super("update", "update {id} : update LabWork with specified id");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (args.length == 0 || !(args[0] instanceof LabWork)) return new CommandResponse(false, "LabWork is required for update command.", null);
        if (args.length < 2 || !(args[1] instanceof String username)) return new CommandResponse(false, "Username is required.", null);
        try {
            long id = Long.parseLong(argument);
            LabWork newLabWork = (LabWork) args[0];
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
            boolean ok = collectionManager.update(id, newLabWork, username);
            return new CommandResponse(ok,
                    ok ? "LabWork updated successfully!" : "Update failed: Not owner or not found",
                    null);
        } catch (NumberFormatException e) {
            return new CommandResponse(false, "Invalid id format: " + argument, null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error updating LabWork: " + e.getMessage(), null);
        }
    }
}