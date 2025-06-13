package lab7.main.java.data;

import lab6.main.java.data.Color;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ZonedDateTime birthday;
    private String passportID;
    private lab6.main.java.data.Color eyeColor;
    private lab6.main.java.data.Color hairColor;

    public Person(String name, ZonedDateTime birthday, String passportID, lab6.main.java.data.Color eyeColor, lab6.main.java.data.Color hairColor) {
        this.name = name;
        this.birthday = birthday;
        this.passportID = passportID;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    public String getPassportID() {
        return passportID;
    }

    public lab6.main.java.data.Color getEyeColor() {
        return eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', birthday=" + birthday + ", passportID='" + passportID + "', hairColor=" + hairColor + "}";
    }
}