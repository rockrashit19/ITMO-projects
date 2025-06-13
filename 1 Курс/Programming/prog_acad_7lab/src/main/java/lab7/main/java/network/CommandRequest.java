package lab7.main.java.network;

import java.io.Serializable;

public class CommandRequest implements Serializable {
    private final String commandName;
    private final String argument;
    private final Object data;
    private final String username;
    private final String password;

    public CommandRequest(String commandName, String argument, Object data, String username, String password) {
        this.commandName = commandName;
        this.argument = argument;
        this.data = data;
        this.username = username;
        this.password = password;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArgument() {
        return argument;
    }

    public Object getData() {
        return data;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}