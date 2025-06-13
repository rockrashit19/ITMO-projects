package lab6.main.java.collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lab6.main.java.data.*;
import lab6.main.java.exception.FileSavingException;
import lab6.main.java.util.ZonedDateTimeAdapter;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.io.PrintWriter;

public class CollectionManager {
    private Vector<LabWork> labWorks;
    private ZonedDateTime initializationDate;
    private final String filePath;

    public CollectionManager(String filePath) {
        this.labWorks = new Vector<>();
        this.initializationDate = ZonedDateTime.now();
        this.filePath = filePath;
    }

    public ZonedDateTime getInitializationDate() {
        return initializationDate;
    }

    public String getCollectionType() {
        return labWorks.getClass().getName();
    }

    public int getSize() {
        return labWorks.size();
    }

    public void add(LabWork labWork) {
        labWorks.add(labWork);
    }

    public void update(long id, LabWork newLabWork) {
        for (int i = 0; i < labWorks.size(); i++) {
            if (labWorks.get(i).getId() == id) {
                newLabWork.setId(id);
                newLabWork.setCreationDate(labWorks.get(i).getCreationDate());
                labWorks.set(i, newLabWork);
                return;
            }
        }
    }

    public boolean removeById(long id) {
        return labWorks.removeIf(labWork -> labWork.getId() == id);
    }

    public void clear() {
        labWorks.clear();
    }

    public List<LabWork> getAll() {
        return labWorks.stream()
                .sorted(Comparator.comparing(LabWork::getName))
                .collect(Collectors.toList());
    }

    public void shuffle() {
        Collections.shuffle(labWorks);
    }

    public void reorder() {
        Collections.reverse(labWorks);
    }

    public void sort() {
        labWorks.sort(Comparator.comparingLong(LabWork::getId));
    }

    public void removeAllByDifficulty(Difficulty difficulty) {
        labWorks.removeIf(labWork -> labWork.getDifficulty() == difficulty);
    }

    public boolean removeAnyByDifficulty(Difficulty difficulty) {
        for (int i = 0; i < labWorks.size(); i++) {
            LabWork labWork = labWorks.get(i);
            if (labWork.getDifficulty() == difficulty) {
                labWorks.remove(i);
                return true;
            }
        }
        return false;
    }

    public long generateNextId() {
        if (labWorks.isEmpty()) {
            return 1;
        }
        return labWorks.stream()
                .max(Comparator.comparing(LabWork::getId))
                .map(labWork -> labWork.getId() + 1)
                .orElse(1L);
    }

    public boolean containsId(long id) {
        return labWorks.stream().anyMatch(labWork -> labWork.getId() == id);
    }

    public void loadData(List<LabWork> data) {
        labWorks.addAll(data);
    }

    public void saveToFile() throws FileSavingException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        String json = gson.toJson(labWorks);

        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.write(json);
        } catch (Exception e) {
            throw new FileSavingException("Error saving collection to file: " + e.getMessage());
        }
    }
}