package lab5.main.java.data;

public enum Difficulty {
    VERY_EASY,
    VERY_HARD,
    INSANE,
    TERRIBLE;

    public static String[] names() {
        Difficulty[] values = values();
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].name();
        }
        return names;
    }
}
