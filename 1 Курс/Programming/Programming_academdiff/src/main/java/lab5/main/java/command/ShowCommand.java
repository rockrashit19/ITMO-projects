package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;
import lab5.main.java.data.LabWork;

import java.util.List;

public class ShowCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public ShowCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("show", "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        List<LabWork> labWorks = collectionManager.getAll();

        if (labWorks.isEmpty()) {
            outputManager.println("Collection is empty.");
            return true;
        }

        labWorks.forEach(labWork -> outputManager.println(labWork.toString()));
        return true;
    }
}
