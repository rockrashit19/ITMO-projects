package lab7.main.java.command;

import lab7.main.java.network.CommandResponse;

public abstract class AbstractCommand {
    private final String name;
    private final String description;

    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
//    public abstract boolean execute(String argument);

    public abstract CommandResponse execute(String argument, Object... args);
}
