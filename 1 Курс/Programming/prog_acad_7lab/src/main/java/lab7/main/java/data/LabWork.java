package lab7.main.java.data;

import lab6.main.java.data.Coordinates;
import lab6.main.java.data.Difficulty;
import lab6.main.java.data.Person;

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
    private lab6.main.java.data.Person author;

    public LabWork(long id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long minimalPoint, Difficulty difficulty, lab6.main.java.data.Person author) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getMinimalPoint() {
        return minimalPoint;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Person getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "LabWork{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", minimalPoint=" + minimalPoint +
                ", difficulty=" + difficulty +
                ", author=" + author +
                '}';
    }
}