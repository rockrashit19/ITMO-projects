package command;

import collection.CollectionManager;
import data.Difficulty;
import network.CommandResponse;

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
        if (args.length < 2 || !(args[1] instanceof String username))
            return new CommandResponse(false, "Username is required.", null);
        try {
            Difficulty difficulty = Difficulty.valueOf(argument.toUpperCase());
            boolean removed = collectionManager.removeAllByDifficulty(difficulty, username);
            String msg = removed
                    ? "Removed all LabWorks with difficulty " + difficulty
                    : "No LabWorks found with difficulty " + difficulty + " for this user";
            return new CommandResponse(removed, msg, null);
        } catch (IllegalArgumentException e) {
            return new CommandResponse(false, "Invalid difficulty. Available difficulties: " + String.join(", ", Difficulty.names()), null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error removing LabWorks: " + e.getMessage(), null);
        }
    }
}