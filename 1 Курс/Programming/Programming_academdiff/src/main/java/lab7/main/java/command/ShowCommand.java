package lab7.main.java.command;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.network.CommandResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ShowCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "show : display all elements in the collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) {
            return new CommandResponse(false, "This command doesn't require an argument.", null);
        }
        try {
            List<Object> collection = collectionManager.getAll().stream()
                    .collect(Collectors.toList());
            String message = collection.isEmpty() ? "Collection is empty." : "Collection retrieved with " + collection.size() + " elements.";
            return new CommandResponse(true, message, collection);
        } catch (Exception e) {
            return new CommandResponse(false, "Error retrieving collection: " + e.getMessage(), null);
        }
    }
}