package lab5.main.java.command;

import lab5.main.java.util.CommandManager;
import lab5.main.java.util.OutputManager;

public class HelpCommand extends AbstractCommand {
    private final CommandManager commandManager;
    private final OutputManager outputManager;

    public HelpCommand(CommandManager commandManager, OutputManager outputManager) {
        super("help", "help : вывести справку по доступным командам");
        this.commandManager = commandManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        outputManager.println("Available commands:");
        commandManager.getAllCommands().forEach(command -> outputManager.println(command.getDescription()));
        return true;
    }
}
