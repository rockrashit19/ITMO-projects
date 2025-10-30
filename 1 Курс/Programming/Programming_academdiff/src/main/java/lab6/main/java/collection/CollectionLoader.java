package lab6.main.java.collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lab6.main.java.data.LabWork;
import lab6.main.java.exception.FileLoadingException;
import lab6.main.java.util.OutputManager;
import lab6.main.java.util.ZonedDateTimeAdapter;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CollectionLoader {
    private final String filePath;
    private final Gson gson;
    private final OutputManager outputManager;

    public CollectionLoader(String filePath, OutputManager outputManager) {
        this.filePath = filePath;
        this.outputManager = outputManager;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter());
        this.gson = gsonBuilder.create();
    }

    public List<LabWork> loadCollection() throws FileLoadingException {
        outputManager.println("Loading collection from file: " + filePath);

        try {
            if (filePath == null || filePath.isEmpty()) {
                throw new FileLoadingException("File path is not set. Please set the environment variable.");
            }

            if (!Files.exists(Paths.get(filePath))) {
                outputManager.println("File not found: " + filePath + ". Starting with an empty collection.");
                return new ArrayList<>();
            }

            if (!Files.isReadable(Paths.get(filePath))) {
                throw new FileLoadingException("Cannot read file: " + filePath + ". Check file permissions.");
            }

            StringBuilder jsonContent = new StringBuilder();
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
            }

            if (jsonContent.isEmpty() || jsonContent.toString().trim().isEmpty()) {
                outputManager.println("File is empty or contains only whitespace. Starting with an empty collection.");
                return new ArrayList<>();
            }

            Type collectionType = new TypeToken<List<LabWork>>(){}.getType();
            outputManager.println("JSON content (partial or full for debug): " + jsonContent.substring(0, Math.min(jsonContent.length(), 500)) + "...");
            List<LabWork> labWorks = gson.fromJson(jsonContent.toString(), collectionType);

            if (labWorks == null) {
                outputManager.println("JSON content was 'null'. Starting with an empty collection.");
                return new ArrayList<>();
            }

            outputManager.println("Collection loaded successfully from " + filePath);
            return labWorks;

        } catch (JsonSyntaxException e) {
            throw new FileLoadingException("Error parsing JSON syntax in file " + filePath + ": " + e.getMessage());
        } catch (java.io.IOException e) {
            throw new FileLoadingException("Error reading or accessing file " + filePath + ": " + e.getMessage());
        } catch (Exception e) {
            outputManager.println("An unexpected error occurred during file loading: " + e.getMessage());
            throw new FileLoadingException("An unexpected error occurred during file loading: " + e.getMessage());
        }
    }
}