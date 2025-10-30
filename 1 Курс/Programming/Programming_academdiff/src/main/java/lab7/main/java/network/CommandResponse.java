package lab7.main.java.network;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final boolean success;
    private final String message;
    private final List<Object> data;

    public CommandResponse(boolean success, String message, List<Object> data) {
        this.success = success;
        this.message = message;
        this.data = data != null ? new ArrayList<>(data) : null;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Object> getData() {
        return data;
    }
}