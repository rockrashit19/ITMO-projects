package lab6.main.java.command;

import lab6.main.java.network.CommandResponse;

import java.util.List;

public class HelpCommand extends AbstractCommand {
    private final List<AbstractCommand> commands;

    public HelpCommand(List<AbstractCommand> commands) {
        super("help", "help : display available commands");
        this.commands = commands;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        StringBuilder message = new StringBuilder("Available commands:\n");
        for (AbstractCommand command : commands) {
            if (!command.getName().equals("save")) {
                message.append(command.getDescription()).append("\n");
            }
        }
        return new CommandResponse(true, message.toString(), null);
    }
}