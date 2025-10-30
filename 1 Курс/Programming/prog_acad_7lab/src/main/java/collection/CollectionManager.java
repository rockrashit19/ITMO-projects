package collection;

import data.*;
import database.DatabaseManager;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CollectionManager {
    private Vector<LabWork> labWorks = new Vector<>();
    private ZonedDateTime initializationDate;
    private final DatabaseManager dbManager;
    private final ReentrantLock lock = new ReentrantLock();

    public CollectionManager(DatabaseManager dbManager) throws SQLException {
        this.dbManager = dbManager;
        this.initializationDate = ZonedDateTime.now();
        loadInitialCollection();
    }

    private void loadInitialCollection() throws SQLException {
        lock.lock();
        try {
            labWorks.addAll(dbManager.loadAllLabWorks());
        } finally {
            lock.unlock();
        }
    }

    public ZonedDateTime getInitializationDate() {
        lock.lock();
        try {
            return initializationDate;
        } finally {
            lock.unlock();
        }
    }

    public String getCollectionType() {
        lock.lock();
        try {
            return labWorks.getClass().getName();
        } finally {
            lock.unlock();
        }
    }

    public int getSize() {
        lock.lock();
        try {
            return labWorks.size();
        } finally {
            lock.unlock();
        }
    }

    public void add(LabWork labWork, String username) throws SQLException {
        lock.lock();
        try {
            if (dbManager.addLabWork(labWork, username)) {
                labWorks.add(labWork);
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean update(long id, LabWork newLabWork, String username) throws SQLException {
        lock.lock();
        try {
            newLabWork.setId(id);
            if (dbManager.updateLabWork(newLabWork, username)) {
                labWorks.removeIf(lw -> lw.getId() == id);
                labWorks.add(newLabWork);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeById(long id, String username) throws SQLException {
        lock.lock();
        try {
            if (dbManager.removeLabWork(id, username)) {
                return labWorks.removeIf(lw -> lw.getId() == id);
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void clear(String username) throws SQLException {
        lock.lock();
        try {
            if (dbManager.clearUserLabWorks(username)) {
                reloadFromDb();
            } else {
                reloadFromDb();
            }
        } finally {
            lock.unlock();
        }
    }

    public List<LabWork> getAll() {
        lock.lock();
        try {
            return new ArrayList<>(labWorks);
        } finally {
            lock.unlock();
        }
    }

    public void shuffle() {
        lock.lock();
        try {
            Collections.shuffle(labWorks);
        } finally {
            lock.unlock();
        }
    }

    public void reorder() {
        lock.lock();
        try {
            Collections.reverse(labWorks);
        } finally {
            lock.unlock();
        }
    }

    public void sort() {
        lock.lock();
        try {
            labWorks.sort(Comparator.comparingLong(LabWork::getId));
        } finally {
            lock.unlock();
        }
    }

    public boolean removeAllByDifficulty(Difficulty difficulty, String username) throws SQLException {
        lock.lock();
        try {
            boolean ok = dbManager.removeAllByDifficulty(difficulty, username);
            if (ok) reloadFromDb();
            return ok;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeAnyByDifficulty(Difficulty difficulty, String username) throws SQLException {
        lock.lock();
        try {
            boolean ok = dbManager.removeAnyByDifficulty(difficulty, username);
            if (ok) reloadFromDb();
            return ok;
        } finally {
            lock.unlock();
        }
    }

    public boolean containsId(long id) {
        lock.lock();
        try {
            return labWorks.stream().anyMatch(labWork -> labWork.getId() == id);
        } finally {
            lock.unlock();
        }
    }

    private void reloadFromDb() throws SQLException {
        labWorks.clear();
        labWorks.addAll(dbManager.loadAllLabWorks());
    }


}