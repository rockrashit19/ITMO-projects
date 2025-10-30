package lab7.main.java.collection;

import lab7.main.java.data.*;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CollectionManager {
    private final Vector<LabWork> labWorks = new Vector<>();
    private final ZonedDateTime initializationDate = ZonedDateTime.now();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ZonedDateTime getInitializationDate() { return initializationDate; }
    public String getCollectionType() { return labWorks.getClass().getName(); }
    public int getSize() {
        lock.readLock().lock();
        try { return labWorks.size(); } finally { lock.readLock().unlock(); }
    }

    public void add(LabWork labWork) {
        lock.writeLock().lock();
        try { labWorks.add(labWork); } finally { lock.writeLock().unlock(); }
    }

    public void update(long id, LabWork newLabWork) {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < labWorks.size(); i++) {
                if (labWorks.get(i).getId() == id) {
                    newLabWork.setId(id);
                    newLabWork.setCreationDate(labWorks.get(i).getCreationDate());
                    newLabWork.setCreator(labWorks.get(i).getCreator());
                    labWorks.set(i, newLabWork);
                    return;
                }
            }
        } finally { lock.writeLock().unlock(); }
    }

    public boolean removeById(long id) {
        lock.writeLock().lock();
        try { return labWorks.removeIf(lw -> lw.getId() == id); } finally { lock.writeLock().unlock(); }
    }

    public void clear(int userId) {
        lock.writeLock().lock();
        try {
            labWorks.removeIf(lw -> lw.getCreator().getId() == userId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<LabWork> getAll() {
        lock.readLock().lock();
        try { return labWorks.stream().sorted(Comparator.comparing(LabWork::getName)).collect(Collectors.toList()); }
        finally { lock.readLock().unlock(); }
    }

    public void shuffle() {
        lock.writeLock().lock();
        try { Collections.shuffle(labWorks); } finally { lock.writeLock().unlock(); }
    }

    public void reorder() {
        lock.writeLock().lock();
        try { Collections.reverse(labWorks); } finally { lock.writeLock().unlock(); }
    }

    public void sort() {
        lock.writeLock().lock();
        try { labWorks.sort(Comparator.comparingLong(LabWork::getId)); } finally { lock.writeLock().unlock(); }
    }

    public void removeAllByDifficulty(Difficulty difficulty, int userId) {
        lock.writeLock().lock();
        try { labWorks.removeIf(lw -> lw.getDifficulty() == difficulty && lw.getCreator().getId() == userId); }
        finally { lock.writeLock().unlock(); }
    }

    public boolean removeAnyByDifficulty(Difficulty difficulty, int userId) {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < labWorks.size(); i++) {
                LabWork lw = labWorks.get(i);
                if (lw.getDifficulty() == difficulty && lw.getCreator().getId() == userId) {
                    labWorks.remove(i);
                    return true;
                }
            }
            return false;
        } finally { lock.writeLock().unlock(); }
    }

    public void loadData(List<LabWork> data) {
        lock.writeLock().lock();
        try {
            labWorks.clear();
            labWorks.addAll(data);
        } finally { lock.writeLock().unlock(); }
    }

    public boolean containsId(long id) {
        lock.readLock().lock();
        try { return labWorks.stream().anyMatch(lw -> lw.getId() == id); } finally { lock.readLock().unlock(); }
    }
}