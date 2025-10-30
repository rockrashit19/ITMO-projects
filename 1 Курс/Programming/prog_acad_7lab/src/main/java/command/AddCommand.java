package command;

import collection.CollectionManager;
import data.LabWork;
import network.CommandResponse;

public class AddCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add", "add : add a new LabWork");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) return new CommandResponse(false, "This command doesn't require an argument.", null);
        if (args.length == 0 || !(args[0] instanceof LabWork)) return new CommandResponse(false, "LabWork is required for add command.", null);
        if (args.length < 2 || !(args[1] instanceof String username)) return new CommandResponse(false, "Username is required.", null);
        try {
            LabWork labWork = (LabWork) args[0];
            labWork.setCreationDate(java.time.ZonedDateTime.now());
            collectionManager.add(labWork, username);
            return new CommandResponse(true, "LabWork added successfully!", null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error adding LabWork: " + e.getMessage(), null);
        }
    }

}