package lab5.main.java.data;

import java.time.ZonedDateTime;

public class Person {
    public String getName() {
        return name;
    }

    private String name; //Поле не может быть null, Строка не может быть пустой
    private ZonedDateTime birthday; //Поле не может быть null
    private String passportID; //Поле не может быть null
    private Color eyeColor; //Поле не может быть null
    private Color hairColor; //Поле может быть null

    public Person(String name, ZonedDateTime birthday, String passportID, Color eyeColor, Color hairColor) {
        this.name = name;
        this.birthday = birthday;
        this.passportID = passportID;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", passportID='" + passportID + '\'' +
                ", eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                '}';
    }

}
