package lab7.main.java.collection;

import lab7.main.java.data.*;
import lab7.main.java.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CollectionManager {
    private Vector<LabWork> labWorks;
    private ZonedDateTime initializationDate;
    private final DatabaseManager dbManager;
    private final ReentrantLock lock = new ReentrantLock();

    public CollectionManager(DatabaseManager dbManager) throws SQLException {
        this.dbManager = dbManager;
        this.labWorks = new Vector<>();
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
            String sql = "DELETE FROM labwork WHERE user_id = (SELECT id FROM users WHERE username = ?)";
            try (Connection conn = dbManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
                conn.commit();
            }
            labWorks.removeIf(lw -> {
                try {
                    String ownerSql = "SELECT user_id FROM labwork WHERE id = ?";
                    try (Connection conn = dbManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(ownerSql)) {
                        stmt.setLong(1, lw.getId());
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            int userId = rs.getInt("user_id");
                            String userSql = "SELECT id FROM users WHERE username = ?";
                            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                                userStmt.setString(1, username);
                                ResultSet userRs = userStmt.executeQuery();
                                return userRs.next() && userRs.getInt("id") == userId;
                            }
                        }
                        return false;
                    }
                } catch (SQLException e) {
                    return false;
                }
            });
        } finally {
            lock.unlock();
        }
    }

    public List<LabWork> getAll() {
        lock.lock();
        try {
            return labWorks.stream()
                    .sorted(Comparator.comparing(LabWork::getName))
                    .collect(Collectors.toList());
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
            if (dbManager.removeAllByDifficulty(difficulty, username)) {
                labWorks.removeIf(lw -> lw.getDifficulty() == difficulty &&
                        dbManager.authenticateUser(username, lw.getId()));
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeAnyByDifficulty(Difficulty difficulty, String username) throws SQLException {
        lock.lock();
        try {
            if (dbManager.removeAnyByDifficulty(difficulty, username)) {
                for (int i = 0; i < labWorks.size(); i++) {
                    if (labWorks.get(i).getDifficulty() == difficulty &&
                            dbManager.authenticateUser(username, labWorks.get(i).getId())) {
                        labWorks.remove(i);
                        return true;
                    }
                }
            }
            return false;
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
}