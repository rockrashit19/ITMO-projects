//package command;
//
//import collection.CollectionManager;
//import network.CommandResponse;
//
//public class SaveCommand extends AbstractCommand {
//    private final CollectionManager collectionManager;
//    public SaveCommand(CollectionManager collectionManager) {
//        super("save", "save : save collection to file");
//        this.collectionManager = collectionManager;
//    }
//    @Override
//    public CommandResponse execute(String argument, Object... args) {
//        try {
//            collectionManager.saveToFile();
//            return new CommandResponse(true, "Collection saved successfully!", null);
//        } catch (Exception e) {
//            return new CommandResponse(false, "Error saving collection: " + e.getMessage(), null);
//        }
//    }
//}