package lab7.main.java.data;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class LabWork implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Long minimalPoint;
    private Difficulty difficulty;
    private Person author;
    private User creator;

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Long getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(Long minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public LabWork(long id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long minimalPoint, Difficulty difficulty, Person author, User creator) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;
        this.creator = creator;
    }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public void setId(long id) {

    }

    public void setCreationDate(ZonedDateTime now) {
        this.creationDate = now;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}