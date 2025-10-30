package beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResultBean implements Serializable {
    private List<Result> results = new ArrayList<>();

    public void addResult(double x, double y, double r, boolean inside, LocalDateTime timestamp) {
        results.add(new Result(x, y, r, inside, timestamp));
    }

    public List<Result> getResults() {
        return results;
    }

    public static class Result {
        private double x, y, r;
        private boolean inside;
        private LocalDateTime timestamp;

        public Result(double x, double y, double r, boolean inside, LocalDateTime timestamp) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.inside = inside;
            this.timestamp = timestamp;
        }
        
        public double getX() { return x; }
        public double getY() { return y; }
        public double getR() { return r; }
        public boolean isInside() { return inside; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
}