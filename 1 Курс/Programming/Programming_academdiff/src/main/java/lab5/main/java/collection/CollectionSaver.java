package lab5.main.java.collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lab5.main.java.data.LabWork;
import lab5.main.java.exception.FileSavingException;
import lab5.main.java.util.OutputManager;
import lab5.main.java.util.ZonedDateTimeAdapter;

import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.List;

public class CollectionSaver {
    private final String filePath;
    private final OutputManager outputManager;

    public CollectionSaver(String filePath, OutputManager outputManager) {
        this.filePath = filePath;
        this.outputManager = outputManager;
    }

    public void saveCollection(List<LabWork> collection) throws FileSavingException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        String json = gson.toJson(collection);

        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.write(json);
            outputManager.println("Collection successfully saved to " + filePath);
        } catch (Exception e) {
            throw new FileSavingException("Error saving collection to file: " + e.getMessage());
        }
    }
}