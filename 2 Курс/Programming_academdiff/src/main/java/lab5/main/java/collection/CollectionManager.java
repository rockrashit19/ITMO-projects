package lab5.main.java.collection;

import lab5.main.java.data.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class CollectionManager {
    private Vector<LabWork> labWorks;
    private ZonedDateTime initializationDate;

    public CollectionManager() {
        labWorks = new Vector<>();
        initializationDate = ZonedDateTime.now();
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

    public boolean update(long id, LabWork newLabWork) {
        for (int i = 0; i < labWorks.size(); i++) {
            if (labWorks.get(i).getId() == id) {
                newLabWork.setId(id);
                newLabWork.setCreationDate(labWorks.get(i).getCreationDate());
                labWorks.set(i, newLabWork);
                return true;
            }
        }
        return false;
    }

    public boolean removeById(long id) {
        return labWorks.removeIf(labWork -> labWork.getId() == id);
    }

    public void clear() {
        labWorks.clear();
    }

    public List<LabWork> getAll() {
        return new Vector<>(labWorks);
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

    public void removeAnyByDifficulty(Difficulty difficulty) {
        for (LabWork labWork : labWorks) {
            if (labWork.getDifficulty() == difficulty) {
                labWorks.remove(labWork);
                break;
            }
        }
    }

    public List<LabWork> filterGreaterThanMinimalPoint(Long minimalPoint) {
        return labWorks.stream()
                .filter(labWork -> labWork.getMinimalPoint() != null && labWork.getMinimalPoint() > minimalPoint)
                .collect(Collectors.toList());
    }

    public long generateNextId() {
        if (labWorks.isEmpty()) {
            return 1;
        }
        return labWorks.stream()
                .max(Comparator.comparing(LabWork::getId))
                .get()
                .getId() + 1;
    }

    public boolean containsId(long id) {
        for (LabWork labWork : labWorks) {
            if (labWork.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void loadData(List<LabWork> data) {
        labWorks.addAll(data);
    }
}
