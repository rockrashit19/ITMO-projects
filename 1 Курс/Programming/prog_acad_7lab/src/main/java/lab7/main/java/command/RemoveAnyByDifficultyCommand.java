package lab7.main.java.command;

import lab6.main.java.collection.CollectionManager;
import lab6.main.java.command.AbstractCommand;
import lab6.main.java.data.Difficulty;
import lab6.main.java.network.CommandResponse;

public class RemoveAnyByDifficultyCommand extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveAnyByDifficultyCommand(CollectionManager collectionManager) {
        super("remove_any_by_difficulty", "remove_any_by_difficulty {difficulty} : remove one LabWork with specified difficulty");
        this.collectionManager = collectionManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (argument == null || argument.trim().isEmpty()) {
            return new CommandResponse(false, "Usage: remove_any_by_difficulty <difficulty>", null);
        }
        try {
            Difficulty difficulty = Difficulty.valueOf(argument.trim().toUpperCase());
            boolean removed = collectionManager.removeAnyByDifficulty(difficulty);
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