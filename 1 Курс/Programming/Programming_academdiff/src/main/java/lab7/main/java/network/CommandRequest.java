package lab7.main.java.network;

import lab7.main.java.data.LabWork;
import java.io.Serializable;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String commandName;
    private final String argument;
    private final LabWork labWork;
    private final String username;
    private final String password;

    public CommandRequest(String commandName, String argument, LabWork labWork, String username, String password) {
        this.commandName = commandName;
        this.argument = argument;
        this.labWork = labWork;
        this.username = username;
        this.password = password;
    }

    public String getCommandName() { return commandName; }
    public String getArgument() { return argument; }
    public LabWork getLabWork() { return labWork; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}