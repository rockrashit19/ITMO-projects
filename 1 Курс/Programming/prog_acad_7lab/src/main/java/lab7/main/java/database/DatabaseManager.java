package lab7.main.java.database;

import lab7.main.java.data.*;
import lab7.main.java.exception.InvalidDataException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DatabaseManager {
    private final String url = "jdbc:postgresql://pg/studs";
    private final String user;
    private final String password;
    private final ReentrantLock lock = new ReentrantLock();
    private Connection connection;

    public DatabaseManager(String user, String password) throws SQLException {
        this.user = user;
        this.password = password;
        connect();
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
    }

    public boolean registerUser(String username, String password) throws SQLException {
        lock.lock();
        try {
            String hashedPassword = hashPassword(password);
            String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                int rows = stmt.executeUpdate();
                connection.commit();
                return rows > 0;
            }
        } catch (NoSuchAlgorithmException e) {
            connection.rollback();
            throw new SQLException("Hashing error: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public boolean authenticateUser(String username, String password) throws SQLException {
        lock.lock();
        try {
            String hashedPassword = hashPassword(password);
            String sql = "SELECT password_hash FROM users WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("password_hash").equals(hashedPassword);
                }
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SQLException("Hashing error: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hashedBytes = digest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public boolean addLabWork(LabWork labWork, String username) throws SQLException {
        lock.lock();
        try {
            // Get user ID
            String userSql = "SELECT id FROM users WHERE username = ?";
            int userId;
            try (PreparedStatement stmt = connection.prepareStatement(userSql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) return false;
                userId = rs.getInt("id");
            }

            // Insert Coordinates
            String coordSql = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
            int coordId;
            try (PreparedStatement stmt = connection.prepareStatement(coordSql)) {
                stmt.setDouble(1, labWork.getCoordinates().getX());
                stmt.setInt(2, labWork.getCoordinates().getY());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                coordId = rs.getInt("id");
            }

            // Insert Person
            Integer personId = null;
            if (labWork.getAuthor() != null) {
                String personSql = "INSERT INTO person (name, birthday, passport_id, eye_color, hair_color) VALUES (?, ?, ?, ?, ?) RETURNING id";
                try (PreparedStatement stmt = connection.prepareStatement(personSql)) {
                    stmt.setString(1, labWork.getAuthor().getName());
                    stmt.setObject(2, labWork.getAuthor().getBirthday());
                    stmt.setString(3, labWork.getAuthor().getPassportID());
                    stmt.setString(4, labWork.getAuthor().getEyeColor().name());
                    stmt.setString(5, labWork.getAuthor().getHairColor() != null ? labWork.getAuthor().getHairColor().name() : null);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    personId = rs.getInt("id");
                }
            }

            // Insert LabWork
            String labWorkSql = "INSERT INTO labwork (name, coordinates_id, creation_date, minimal_point, difficulty, author_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
            try (PreparedStatement stmt = connection.prepareStatement(labWorkSql)) {
                stmt.setString(1, labWork.getName());
                stmt.setInt(2, coordId);
                stmt.setObject(3, labWork.getCreationDate());
                stmt.setObject(4, labWork.getMinimalPoint());
                stmt.setString(5, labWork.getDifficulty() != null ? labWork.getDifficulty().name() : null);
                stmt.setObject(6, personId);
                stmt.setInt(7, userId);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                labWork.setId(rs.getInt("id"));
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public boolean updateLabWork(LabWork labWork, String username) throws SQLException {
        lock.lock();
        try {
            // Verify ownership
            String ownerSql = "SELECT user_id FROM labwork WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(ownerSql)) {
                stmt.setLong(1, labWork.getId());
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) return false;
                int userId = rs.getInt("user_id");
                String userSql = "SELECT id FROM users WHERE username = ?";
                try (PreparedStatement userStmt = connection.prepareStatement(userSql)) {
                    userStmt.setString(1, username);
                    ResultSet userRs = userStmt.executeQuery();
                    if (!userRs.next() || userRs.getInt("id") != userId) return false;
                }
            }

            // Update Coordinates
            String coordSql = "UPDATE coordinates SET x = ?, y = ? WHERE id = (SELECT coordinates_id FROM labwork WHERE id = ?)";
            try (PreparedStatement stmt = connection.prepareStatement(coordSql)) {
                stmt.setDouble(1, labWork.getCoordinates().getX());
                stmt.setInt(2, labWork.getCoordinates().getY());
                stmt.setLong(3, labWork.getId());
                stmt.executeUpdate();
            }

            // Update Person
            if (labWork.getAuthor() != null) {
                String personSql = "UPDATE person SET name = ?, birthday = ?, passport_id = ?, eye_color = ?, hair_color = ? WHERE id = (SELECT author_id FROM labwork WHERE id = ?)";
                try (PreparedStatement stmt = connection.prepareStatement(personSql)) {
                    stmt.setString(1, labWork.getAuthor().getName());
                    stmt.setObject(2, labWork.getAuthor().getBirthday());
                    stmt.setString(3, labWork.getAuthor().getPassportID());
                    stmt.setString(4, labWork.getAuthor().getEyeColor().name());
                    stmt.setString(5, labWork.getAuthor().getHairColor() != null ? labWork.getAuthor().getHairColor().name() : null);
                    stmt.setLong(6, labWork.getId());
                    stmt.executeUpdate();
                }
            }

            // Update LabWork
            String labWorkSql = "UPDATE labwork SET name = ?, creation_date = ?, minimal_point = ?, difficulty = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(labWorkSql)) {
                stmt.setString(1, labWork.getName());
                stmt.setObject(2, labWork.getCreationDate());
                stmt.setObject(3, labWork.getMinimalPoint());
                stmt.setString(4, labWork.getDifficulty() != null ? labWork.getDifficulty().name() : null);
                stmt.setLong(5, labWork.getId());
                stmt.executeUpdate();
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeLabWork(long id, String username) throws SQLException {
        lock.lock();
        try {
            String ownerSql = "SELECT user_id FROM labwork WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(ownerSql)) {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) return false;
                int userId = rs.getInt("user_id");
                String userSql = "SELECT id FROM users WHERE username = ?";
                try (PreparedStatement userStmt = connection.prepareStatement(userSql)) {
                    userStmt.setString(1, username);
                    ResultSet userRs = userStmt.executeQuery();
                    if (!userRs.next() || userRs.getInt("id") != userId) return false;
                }
            }

            String sql = "DELETE FROM labwork WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, id);
                int rows = stmt.executeUpdate();
                connection.commit();
                return rows > 0;
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeAllByDifficulty(Difficulty difficulty, String username) throws SQLException {
        lock.lock();
        try {
            String ownerSql = "SELECT id FROM labwork WHERE difficulty = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
            List<Long> idsToRemove = new ArrayList<>();
            try (PreparedStatement stmt = connection.prepareStatement(ownerSql)) {
                stmt.setString(1, difficulty != null ? difficulty.name() : null);
                stmt.setString(2, username);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    idsToRemove.add(rs.getLong("id"));
                }
            }

            if (idsToRemove.isEmpty()) return false;

            String deleteSql = "DELETE FROM labwork WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
                for (Long id : idsToRemove) {
                    stmt.setLong(1, id);
                    stmt.executeUpdate();
                }
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public boolean removeAnyByDifficulty(Difficulty difficulty, String username) throws SQLException {
        lock.lock();
        try {
            String sql = "DELETE FROM labwork WHERE id = (SELECT id FROM labwork WHERE difficulty = ? AND user_id = (SELECT id FROM users WHERE username = ?) LIMIT 1)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, difficulty != null ? difficulty.name() : null);
                stmt.setString(2, username);
                int rows = stmt.executeUpdate();
                connection.commit();
                return rows > 0;
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public List<LabWork> loadAllLabWorks() throws SQLException {
        lock.lock();
        try {
            List<LabWork> labWorks = new ArrayList<>();
            String sql = "SELECT l.id, l.name, l.creation_date, l.minimal_point, l.difficulty, l.user_id, " +
                    "c.x, c.y, p.name AS person_name, p.birthday, p.passport_id, p.eye_color, p.hair_color " +
                    "FROM labwork l " +
                    "JOIN coordinates c ON l.coordinates_id = c.id " +
                    "LEFT JOIN person p ON l.author_id = p.id";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Coordinates coords = new Coordinates(rs.getDouble("x"), rs.getInt("y"));
                    Person author = null;
                    if (rs.getString("person_name") != null) {
                        author = new Person(
                                rs.getString("person_name"),
                                rs.getObject("birthday", ZonedDateTime.class),
                                rs.getString("passport_id"),
                                Color.valueOf(rs.getString("eye_color")),
                                rs.getString("hair_color") != null ? Color.valueOf(rs.getString("hair_color")) : null
                        );
                    }
                    LabWork labWork = new LabWork(
                            rs.getLong("id"),
                            rs.getString("name"),
                            coords,
                            rs.getObject("creation_date", ZonedDateTime.class),
                            rs.getObject("minimal_point", Long.class),
                            rs.getString("difficulty") != null ? Difficulty.valueOf(rs.getString("difficulty")) : null,
                            author
                    );
                    labWorks.add(labWork);
                }
            }
            return labWorks;
        } finally {
            lock.unlock();
        }
    }
}