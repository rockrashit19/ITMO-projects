package lab7.main.java.server;

import lab6.main.java.collection.CollectionManager;
import lab6.main.java.command.*;
import lab6.main.java.data.LabWork;
import lab6.main.java.network.CommandRequest;
import lab6.main.java.network.CommandResponse;
import lab6.main.java.util.OutputManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final Map<String, AbstractCommand> commands = new HashMap<>();
    private final OutputManager outputManager;

    public CommandManager(CollectionManager collectionManager, OutputManager outputManager) {
        this.outputManager = outputManager;
        List<AbstractCommand> commandList = new ArrayList<>();
        commandList.add(new HelpCommand(commandList));
        commandList.add(new InfoCommand(collectionManager));
        commandList.add(new ShowCommand(collectionManager));
        commandList.add(new AddCommand(collectionManager));
        commandList.add(new UpdateIdCommand(collectionManager));
        commandList.add(new RemoveByIdCommand(collectionManager));
        commandList.add(new ClearCommand(collectionManager));
        commandList.add(new ShuffleCommand(collectionManager));
        commandList.add(new ReorderCommand(collectionManager));
        commandList.add(new SortCommand(collectionManager));
        commandList.add(new RemoveAllByDifficultyCommand(collectionManager));
        commandList.add(new RemoveAnyByDifficultyCommand(collectionManager));
        commandList.add(new FilterGreaterThanMinimalPointCommand(collectionManager));
        commandList.add(new ExitCommand());
        commandList.add(new SaveCommand(collectionManager));
        commandList.add(new ServerExecuteScriptCommand());
        for (AbstractCommand command : commandList) {
            commands.put(command.getName(), command);
        }
    }

    public CommandResponse executeCommand(CommandRequest request) {
        String commandName = request.getCommandName();
        String argument = request.getArgument();
        LabWork labWork = request.getLabWork();

        AbstractCommand command = commands.get(commandName);
        if (command == null) {
            return new CommandResponse(false, "Command not found: " + commandName, null);
        }

        if (commandName.equals("save")) {
            return new CommandResponse(false, "Command 'save' is not available for clients.", null);
        }

        return command.execute(argument, labWork);
    }

    public void executeSaveCommand() {
        AbstractCommand saveCommand = commands.get("save");
        if (saveCommand != null) {
            saveCommand.execute("", null);
        }
    }
}