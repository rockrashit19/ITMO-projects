package lab7.main.java.command;

import lab6.main.java.command.AbstractCommand;
import lab6.main.java.network.CommandResponse;

public class ExitCommand extends AbstractCommand {
    public ExitCommand() {
        super("exit", "exit : exit the client (without saving)");
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (!argument.isEmpty()) {
            return new CommandResponse(false, "This command doesn't require an argument.", null);
        }
        return new CommandResponse(true, "Client shutting down...", null);
    }
}