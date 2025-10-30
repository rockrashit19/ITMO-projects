package lab7.main.java.data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.prefs.Preferences;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private ZonedDateTime birthday;
    private String passportID;
    private Colour eyeColour;

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(ZonedDateTime birthday) {
        this.birthday = birthday;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public void setEyeColour(Colour eyeColour) {
        this.eyeColour = eyeColour;
    }

    public void setHairColour(Colour hairColour) {
        this.hairColour = hairColour;
    }

    private Colour hairColour;

    public Person(int id, String name, ZonedDateTime birthday, String passportID, Colour eyeColour, Colour hairColour) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.passportID = passportID;
        this.eyeColour = eyeColour;
        this.hairColour = hairColour;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public ZonedDateTime getBirthday() { return birthday; }
    public String getPassportID() { return passportID; }
    public Colour getEyeColour() { return eyeColour; }
    public Colour getHairColour() { return hairColour; }

    @Override
    public String toString() {
        return "Person{id=" + id + ", name='" + name + "', birthday=" + birthday + ", passportID='" + passportID + "', hairColour=" + hairColour + "}";
    }

    public Colour getEyeColor() {
        return eyeColour;
    }

    public Colour getHairColor() {
        return hairColour;
    }
}