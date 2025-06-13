package lab7.main.java.server;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.command.*;
import lab7.main.java.data.LabWork;
import lab7.main.java.data.User;
import lab7.main.java.network.CommandRequest;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.DatabaseManager;
import lab7.main.java.util.OutputManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final Map<String, AbstractCommand> commands = new HashMap<>();
    private final OutputManager outputManager;
    private final DatabaseManager dbManager;
    private final CollectionManager collectionManager;

    public CommandManager(CollectionManager collectionManager, DatabaseManager dbManager, OutputManager outputManager) {
        this.outputManager = outputManager;
        this.dbManager = dbManager;
        this.collectionManager = collectionManager;
        List<AbstractCommand> commandList = new ArrayList<>();
        commandList.add(new HelpCommand(commandList));
        commandList.add(new InfoCommand(collectionManager));
        commandList.add(new ShowCommand(collectionManager));
        commandList.add(new AddCommand(collectionManager, dbManager));
        commandList.add(new UpdateIdCommand(collectionManager, dbManager));
        commandList.add(new RemoveByIdCommand(collectionManager, dbManager));
        commandList.add(new ClearCommand(collectionManager, dbManager));
        commandList.add(new ShuffleCommand(collectionManager));
        commandList.add(new ReorderCommand(collectionManager));
        commandList.add(new SortCommand(collectionManager));
        commandList.add(new RemoveAllByDifficultyCommand(collectionManager, dbManager));
        commandList.add(new RemoveAnyByDifficultyCommand(collectionManager, dbManager));
        commandList.add(new FilterGreaterThanMinimalPointCommand(collectionManager));
        commandList.add(new ExitCommand());
        commandList.add(new ServerExecuteScriptCommand());
        for (AbstractCommand command : commandList) {
            commands.put(command.getName(), command);
        }
    }

    public CommandResponse executeCommand(CommandRequest request) {
        String commandName = request.getCommandName();
        String argument = request.getArgument();
        LabWork labWork = request.getLabWork();

        if (commandName.equals("register")) {
            boolean success = dbManager.registerUser(argument, request.getPassword());
            return new CommandResponse(success, success ? "User registered successfully" : "Username already exists", null);
        }

        User user = dbManager.authenticate(request.getUsername(), request.getPassword());
        if (user == null) return new CommandResponse(false, "Authentication failed", null);

        AbstractCommand command = commands.get(commandName);
        if (command == null) return new CommandResponse(false, "Command not found: " + commandName, null);

        return command.execute(argument, user, labWork);
    }
}