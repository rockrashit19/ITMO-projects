package lab6.main.java.data;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private ZonedDateTime birthday;
    private String passportID;
    private Color eyeColor;
    private Color hairColor;

    public Person(String name, ZonedDateTime birthday, String passportID, Color eyeColor, Color hairColor) {
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

    public Color getEyeColor() {
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