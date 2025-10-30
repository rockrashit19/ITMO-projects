package lab6.main.java.network;

import lab6.main.java.data.LabWork;

import java.io.Serial;
import java.io.Serializable;

public class CommandRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String commandName;
    private final String argument;
    private final LabWork labWork;

    // Конструктор для команд с LabWork (add, update)
    public CommandRequest(String commandName, String argument, LabWork labWork) {
        this.commandName = commandName;
        this.argument = argument;
        this.labWork = labWork;
    }

    // Новый конструктор для команд без LabWork (info, show, и т.д.)
    public CommandRequest(String commandName, String argument) {
        this.commandName = commandName;
        this.argument = argument;
        this.labWork = null;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArgument() {
        return argument;
    }

    public LabWork getLabWork() {
        return labWork;
    }
}