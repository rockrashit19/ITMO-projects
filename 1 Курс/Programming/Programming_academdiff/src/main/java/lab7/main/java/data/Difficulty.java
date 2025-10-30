package lab7.main.java.data;

import java.io.Serializable;

public enum Difficulty implements Serializable {
    VERY_EASY, EASY, NORMAL, HARD, INSANE;

    public static String[] names() {
        return new String[]{"VERY_EASY", "EASY", "NORMAL", "HARD", "INSANE"};
    }
}