package ru.rmntim.web;

import java.util.LinkedHashMap;

public class Parameters {
    private final int x;
    private final int y;
    private final int r;
    public Parameters(LinkedHashMap<String, String> params) throws ValidationException{
        if (params == null || params.isEmpty()) {
            throw new ValidationException("Nothing in query");
        }
        validateParams(params);
        this.x = Integer.parseInt(params.get("x"));
        this.y = Integer.parseInt(params.get("y"));
        this.r = Integer.parseInt(params.get("r"));
    }


    private static void validateParams(LinkedHashMap<String, String> params) throws ValidationException {
        var x = params.get("x");
        if (x == null || x.isEmpty()) {
            throw new ValidationException("x is invalid");
        }

        try {
            var xValue = Integer.parseInt(x);
            if (xValue < -3 || xValue > 3) {
                throw new ValidationException("x is not in limits");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("x is not a number");
        }

        var y = params.get("y");
        if (y == null || y.isEmpty()) {
            throw new ValidationException("y is invalid");
        }

        try {
            var yValue = Integer.parseInt(y);
            if (yValue < -3 || yValue > 5) {
                throw new ValidationException("y is not in limits");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("y is not a number");
        }

        var r = params.get("r");
        if (r == null || r.isEmpty()) {
            throw new ValidationException("r is invalid");
        }

        try {
            var rValue = Integer.parseInt(r);
            if (rValue < 1 || rValue > 5) {
                throw new ValidationException("r is not in limits");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("r is not a number");
        }
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }
}
