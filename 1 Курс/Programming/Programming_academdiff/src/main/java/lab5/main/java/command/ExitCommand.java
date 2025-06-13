package lab5.main.java.command;

import lab5.main.java.util.OutputManager;

public class ExitCommand extends AbstractCommand{
    private final OutputManager outputManager;

    public ExitCommand(OutputManager outputManager){
        super("exit", "exit : завершить программу (без сохранения в файл)");
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument){
        if (!argument.isEmpty()) {
            outputManager.println("This command doesn't require an argument.");
            return false;
        }

        outputManager.println("Exiting the program...");
        return true;
    }
}
