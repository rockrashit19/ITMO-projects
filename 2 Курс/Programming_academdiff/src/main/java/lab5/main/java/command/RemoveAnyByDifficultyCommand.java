package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;
import lab5.main.java.data.Difficulty;

public class RemoveAnyByDifficultyCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public RemoveAnyByDifficultyCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("remove_any_by_difficulty", "remove_any_by_difficulty {difficulty} : удалить из коллекции " +
                "один элемент, значение поля difficulty которого эквивалентно заданному");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (argument == null || argument.isEmpty()) {
            outputManager.println("Usage: remove_any_by_difficulty <difficulty>");
            outputManager.println("Available difficulties: " + String.join(", ", Difficulty.names()));
            return false;
        }

        try {
            Difficulty difficulty = Difficulty.valueOf(argument.toUpperCase());
            collectionManager.removeAnyByDifficulty(difficulty);
            outputManager.println("Removed any LabWork with difficulty " + difficulty);
            return true;
        } catch (IllegalArgumentException e) {
            outputManager.println("Invalid difficulty. Available difficulties: " + String.join(", ",
                    Difficulty.names()));
            return false;
        }
    }
}
