package lab7.main.java.command;

import lab7.main.java.collection.CollectionManager;
import lab7.main.java.command.AbstractCommand;
import lab7.main.java.data.Difficulty;
import lab7.main.java.data.User;
import lab7.main.java.network.CommandResponse;
import lab7.main.java.util.DatabaseManager;

public class RemoveAnyByDifficultyCommand extends AbstractCommand {
    private final CollectionManager collectionManager;
    private final DatabaseManager databaseManager;

    public RemoveAnyByDifficultyCommand(CollectionManager collectionManager, DatabaseManager databaseManager) {
        super("remove_any_by_difficulty", "remove_any_by_difficulty {difficulty} : remove one LabWork with specified difficulty");
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (argument == null || argument.trim().isEmpty()) {
            return new CommandResponse(false, "Usage: remove_any_by_difficulty <difficulty>", null);
        }
        try {
            User user = (User) args[0];
            Difficulty difficulty = Difficulty.valueOf(argument.trim().toUpperCase());
            boolean removed = collectionManager.removeAnyByDifficulty(difficulty, user.getId());
            if (removed) {
                return new CommandResponse(true, "LabWork with difficulty " + difficulty + " removed successfully!", null);
            } else {
                return new CommandResponse(false, "No LabWork found with difficulty " + difficulty, null);
            }
        } catch (IllegalArgumentException e) {
            return new CommandResponse(false, "Invalid difficulty: " + argument + ". Available options: " + String.join(", ", Difficulty.names()), null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error removing LabWork: " + e.getMessage(), null);
        }
    }
}