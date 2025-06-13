package lab7.main.java.command;

import lab6.main.java.collection.CollectionManager;
import lab6.main.java.command.AbstractCommand;
import lab6.main.java.network.CommandResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FilterGreaterThanMinimalPointCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public FilterGreaterThanMinimalPointCommand(CollectionManager collectionManager) {
        super("filter_greater_than_minimal_point", "filter_greater_than_minimal_point {point} : display elements with minimalPoint greater than given");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        try {
            if (argument.isEmpty()) {
                return new CommandResponse(false, "Argument minimalPoint is required.", null);
            }
            long minimalPoint = Long.parseLong(argument);
            List<Object> filtered = collectionManager.getAll().stream()
                    .filter(labWork -> labWork.getMinimalPoint() > minimalPoint)
                    .collect(Collectors.toList());
            String message = filtered.isEmpty() ? "No elements found with minimalPoint greater than " + minimalPoint :
                    "Found " + filtered.size() + " elements with minimalPoint greater than " + minimalPoint;
            return new CommandResponse(true, message, filtered);
        } catch (NumberFormatException e) {
            return new CommandResponse(false, "Invalid minimalPoint format: " + argument, null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error filtering collection: " + e.getMessage(), null);
        }
    }
}