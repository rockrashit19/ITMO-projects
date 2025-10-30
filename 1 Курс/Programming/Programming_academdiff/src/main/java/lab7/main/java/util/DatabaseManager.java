package lab7.main.java.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lab7.main.java.data.*;
import lab7.main.java.data.Colour;
import lab7.main.java.data.User;

import java.security.MessageDigest;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final HikariDataSource dataSource;

    public DatabaseManager(String url, String user, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public User authenticate(String username, String password) {
        String hashedPassword = hashPassword(password);
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id FROM Users WHERE username = ? AND password_hash = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, hashedPassword);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new User(rs.getInt("id"), username);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Authentication error", e);
        }
        return null;
    }

    public boolean registerUser(String username, String password) {
        String hashedPassword = hashPassword(password);
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO Users (username, password_hash) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, hashedPassword);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) return false; // Duplicate username
            throw new RuntimeException("Registration error", e);
        }
    }

    public List<LabWork> loadLabWorks() {
        List<LabWork> labWorks = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT lw.*, p.*, u.id AS user_id, u.username " +
                    "FROM LabWorks lw JOIN Persons p ON lw.author_id = p.id " +
                    "JOIN Users u ON lw.user_id = u.id";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        LabWork labWork = extractLabWorkFromResultSet(rs);
                        labWorks.add(labWork);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading LabWorks", e);
        }
        return labWorks;
    }

    public long insertLabWork(LabWork labWork, int userId) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int authorId = insertPerson(conn, labWork.getAuthor());
                String sql = "INSERT INTO LabWorks (name, coordinates_x, coordinates_y, creationDate, minimalPoint, difficulty, author_id, user_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    prepareLabWorkStatement(ps, labWork, authorId, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            long id = rs.getLong("id");
                            conn.commit();
                            return id;
                        }
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting LabWork", e);
        }
        return -1;
    }

    public boolean updateLabWork(long id, LabWork newLabWork, int userId) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String checkSql = "SELECT user_id FROM LabWorks WHERE id = ?";
                try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                    psCheck.setLong(1, id);
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (!rs.next() || rs.getInt("user_id") != userId) return false;
                    }
                }
                int authorId = insertPerson(conn, newLabWork.getAuthor());
                String sql = "UPDATE LabWorks SET name=?, coordinates_x=?, coordinates_y=?, minimalPoint=?, difficulty=?, author_id=? WHERE id=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, newLabWork.getName());
                    ps.setDouble(2, newLabWork.getCoordinates().getX());
                    ps.setInt(3, newLabWork.getCoordinates().getY());
                    ps.setObject(4, newLabWork.getMinimalPoint(), Types.BIGINT);
                    ps.setString(5, newLabWork.getDifficulty().name());
                    ps.setInt(6, authorId);
                    ps.setLong(7, id);
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        conn.commit();
                        return true;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating LabWork", e);
        }
        return false;
    }

    public boolean removeLabWork(long id, int userId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM LabWorks WHERE id = ? AND user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, id);
                ps.setInt(2, userId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error removing LabWork", e);
        }
    }

    private int insertPerson(Connection conn, Person person) throws SQLException {
        String sql = "INSERT INTO Persons (name, birthday, passportID, eyeColour, hairColour) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, person.getName());
            ps.setTimestamp(2, Timestamp.from(person.getBirthday().toInstant()));
            ps.setString(3, person.getPassportID());
            ps.setString(4, person.getEyeColor().name());
            ps.setString(5, person.getHairColor() != null ? person.getHairColor().name() : null);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Failed to insert Person");
    }

    private void prepareLabWorkStatement(PreparedStatement ps, LabWork labWork, int authorId, int userId) throws SQLException {
        ps.setString(1, labWork.getName());
        ps.setDouble(2, labWork.getCoordinates().getX());
        ps.setInt(3, labWork.getCoordinates().getY());
        ps.setTimestamp(4, Timestamp.from(labWork.getCreationDate().toInstant()));
        ps.setObject(5, labWork.getMinimalPoint(), Types.BIGINT);
        ps.setString(6, labWork.getDifficulty().name());
        ps.setInt(7, authorId);
        ps.setInt(8, userId);
    }

    private LabWork extractLabWorkFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        Coordinates coordinates = new Coordinates(rs.getDouble("coordinates_x"), rs.getInt("coordinates_y"));
        ZonedDateTime creationDate = rs.getTimestamp("creationDate").toInstant().atZone(ZoneId.systemDefault());
        Long minimalPoint = rs.getLong("minimalPoint");
        if (rs.wasNull()) minimalPoint = null;
        Difficulty difficulty = Difficulty.valueOf(rs.getString("difficulty"));
        Person author = new Person(
                rs.getInt("author_id"), rs.getString("p.name"),
                rs.getTimestamp("birthday").toInstant().atZone(ZoneId.systemDefault()),
                rs.getString("passportID"), Colour.valueOf(rs.getString("eyeColour")),
                rs.getString("hairColour") != null ? Colour.valueOf(rs.getString("hairColour")) : null
        );
        User user = new User(rs.getInt("user_id"), rs.getString("username"));
        return new LabWork(id, name, coordinates, creationDate, minimalPoint, difficulty, author, user);
    }
}