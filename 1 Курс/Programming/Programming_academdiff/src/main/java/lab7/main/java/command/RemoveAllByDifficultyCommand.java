package lab7.main.java.command;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.command.AbstractCommand;
import lab7.main.java.data.Difficulty;
import lab7.main.java.data.User;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.DatabaseManager;

public class RemoveAllByDifficultyCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;

    public RemoveAllByDifficultyCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_all_by_difficulty", "remove_all_by_difficulty {difficulty} : remove all LabWorks with specified difficulty");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (argument.isEmpty()) {
            return new CommandResponse(false, "Usage: remove_all_by_difficulty <difficulty>\nAvailable difficulties: " + String.join(", ", Difficulty.names()), null);
        }
        try {
            Difficulty difficulty = Difficulty.valueOf(argument.trim().toUpperCase());
            User user = (User) args[0];
            collectionManager.removeAllByDifficulty(difficulty, user.getId());
            return new CommandResponse(true, "Removed all LabWorks with difficulty " + difficulty, null);
        } catch (IllegalArgumentException e) {
            return new CommandResponse(false, "Invalid difficulty. Available difficulties: " + String.join(", ", Difficulty.names()), null);
        }
    }
}