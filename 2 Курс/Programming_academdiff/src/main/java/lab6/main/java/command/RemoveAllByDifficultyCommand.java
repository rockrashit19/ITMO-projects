package lab6.main.java.command;

import lab6.main.java.collection.CollectionManager;
import lab6.main.java.data.Difficulty;
import lab6.main.java.network.CommandResponse;

public class RemoveAllByDifficultyCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveAllByDifficultyCommand(CollectionManager collectionManager) {
        super("remove_all_by_difficulty", "remove_all_by_difficulty {difficulty} : remove all LabWorks with specified difficulty");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (argument.isEmpty()) {
            return new CommandResponse(false, "Usage: remove_all_by_difficulty <difficulty>\nAvailable difficulties: " + String.join(", ", Difficulty.names()), null);
        }
        try {
            Difficulty difficulty = Difficulty.valueOf(argument.toUpperCase());
            collectionManager.removeAllByDifficulty(difficulty);
            return new CommandResponse(true, "Removed all LabWorks with difficulty " + difficulty, null);
        } catch (IllegalArgumentException e) {
            return new CommandResponse(false, "Invalid difficulty. Available difficulties: " + String.join(", ", Difficulty.names()), null);
        }
    }
}