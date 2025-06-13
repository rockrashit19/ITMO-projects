package lab7.main.java.command;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.data.*;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.DatabaseManager;

import java.time.ZonedDateTime;

public class AddCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseManager dbManager;

    public AddCommand(CollectionManager collectionManager, DatabaseManager dbManager) {
        super("add", "add : add a new LabWork");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) return new CommandResponse(false, "This command doesn't require an argument.", null);
        if (args.length < 2 || !(args[1] instanceof LabWork) || !(args[0] instanceof User))
            return new CommandResponse(false, "LabWork and User are required.", null);

        LabWork labWork = (LabWork) args[1];
        User user = (User) args[0];
        try {
            long id = dbManager.insertLabWork(labWork, user.getId());
            labWork.setId(id);
            labWork.setCreationDate(ZonedDateTime.now());
            labWork.setCreator(user);
            collectionManager.add(labWork);
            return new CommandResponse(true, "LabWork added successfully!", null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error adding LabWork: " + e.getMessage(), null);
        }
    }
}