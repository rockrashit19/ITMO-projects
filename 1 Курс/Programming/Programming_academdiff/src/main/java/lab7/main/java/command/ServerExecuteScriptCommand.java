package lab7.main.java.command;

import lab7.main.java.network.CommandResponse;

public class ServerExecuteScriptCommand extends AbstractCommand {
    public ServerExecuteScriptCommand() {
        super("execute_script", "execute_script <file_name> : execute commands from a file (client-side)");
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        return new CommandResponse(false, "Command 'execute_script' must be executed on the client.", null);
    }
}