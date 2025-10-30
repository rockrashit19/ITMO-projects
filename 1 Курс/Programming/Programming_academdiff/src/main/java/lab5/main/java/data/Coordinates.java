package lab5.main.java.data;

public class Coordinates {
    private Double x; //Поле не может быть null
    private int y; //Значение поля должно быть больше -563

    public Coordinates(Double x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
