package lab7.main.java.data;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private Double x;
    private int y;

    public Coordinates(Double x, int y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{x=" + x + ", y=" + y + "}";
    }
}