package lab5.main.java.command;

import lab5.main.java.collection.CollectionManager;
import lab5.main.java.util.OutputManager;
import lab5.main.java.data.Difficulty;

public class RemoveAllByDifficultyCommand extends AbstractCommand{
    private final CollectionManager collectionManager;
    private final OutputManager outputManager;

    public RemoveAllByDifficultyCommand(CollectionManager collectionManager, OutputManager outputManager) {
        super("remove_all_by_difficulty", "remove_all_by_difficulty {difficulty} : удалить из коллекции все элементы, значение поля difficulty которого эквивалентно заданному");
        this.collectionManager = collectionManager;
        this.outputManager = outputManager;
    }

    @Override
    public boolean execute(String argument) {
        if (argument == null || argument.isEmpty()) {
            outputManager.println("Usage: remove_all_by_difficulty <difficulty>");
            outputManager.println("Available difficulties: " + String.join(", ", Difficulty.names()));
            return false;
        }

        try {
            Difficulty difficulty = Difficulty.valueOf(argument.toUpperCase());
            collectionManager.removeAllByDifficulty(difficulty);
            outputManager.println("Removed all LabWork with difficulty " + difficulty);
            return true;
        } catch (IllegalArgumentException e) {
            outputManager.println("Invalid difficulty. Available difficulties: " + String.join(", ", Difficulty.names())); // Assuming Difficulty has a names() method
            return false;
        }
    }
}
