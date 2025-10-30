package lab5.main.java.util;

import lab5.main.java.command.*;
import lab5.main.java.collection.*;

import java.util.*;


public class CommandManager {

    private final Map<String, AbstractCommand> commands = new HashMap<>();
    private final OutputManager outputManager;

    public CommandManager(CollectionManager collectionManager, InputManager inputManager, CollectionSaver collectionSaver, OutputManager outputManager) {
        this.outputManager = outputManager;
        addCommand(new HelpCommand(this, outputManager));
        addCommand(new InfoCommand(collectionManager, outputManager));
        addCommand(new ShowCommand(collectionManager, outputManager));
        addCommand(new AddCommand(collectionManager, inputManager, outputManager));
        addCommand(new UpdateIdCommand(collectionManager, inputManager, outputManager));
        addCommand(new RemoveByIdCommand(collectionManager, outputManager));
        addCommand(new ClearCommand(collectionManager, outputManager));
        addCommand(new SaveCommand(collectionManager, collectionSaver, outputManager));
        addCommand(new ExecuteScriptCommand(this, inputManager, outputManager));
        addCommand(new ExitCommand(outputManager));
        addCommand(new ShuffleCommand(collectionManager, outputManager));
        addCommand(new ReorderCommand(collectionManager, outputManager));
        addCommand(new SortCommand(collectionManager, outputManager));
        addCommand(new RemoveAllByDifficultyCommand(collectionManager, outputManager));
        addCommand(new RemoveAnyByDifficultyCommand(collectionManager, outputManager));
        addCommand(new FilterGreaterThanMinimalPointCommand(collectionManager, outputManager));
    }

    public void addCommand(AbstractCommand command) {
        commands.put(command.getName(), command);
    }

    public boolean executeCommand(String line) {
        String[] parts = line.trim().split("\\s+", 2);
        String commandName = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        AbstractCommand command = commands.get(commandName);
        if (command == null) {
            outputManager.println("Command not found: " + commandName);
            return false;
        }

        return command.execute(argument);
    }

    public List<AbstractCommand> getAllCommands() {
        return new ArrayList<>(commands.values());
    }

}
