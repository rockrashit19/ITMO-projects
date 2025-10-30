package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.data.LabWork;
import lab5.main.java.util.OutputManager;

import java.util.List;

public class FilterGreaterThanMinimalPointCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public FilterGreaterThanMinimalPointCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("filter_greater_than_minimal_point", "filter_greater_than_minimal_point minimalPoint : вывести элементы, значение поля minimalPoint которых больше заданного");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (argument == null || argument.isEmpty()) {
            outputManager.println("Usage: filter_greater_than_minimal_point <minimalPoint>");
            return false;
        }

        try {
            Long minimalPoint = Long.parseLong(argument);
            List<LabWork> filteredLabWorks = collectionManager.filterGreaterThanMinimalPoint(minimalPoint);

            if (filteredLabWorks.isEmpty()) {
                outputManager.println("No LabWork found with minimalPoint greater than " + minimalPoint);
            } else {
                outputManager.println("LabWork with minimalPoint greater than " + minimalPoint + ":");
                filteredLabWorks.forEach(labWork -> outputManager.println(labWork.toString()));
            }
            return true;
        } catch (NumberFormatException e) {
            outputManager.println("Invalid minimalPoint format. Please enter a valid number.");
            return false;
        }
    }

}
