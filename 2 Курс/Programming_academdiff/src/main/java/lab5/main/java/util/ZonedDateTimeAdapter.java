package lab5.main.java.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(FORMATTER));
        }
    }

    @Override
    public ZonedDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String zonedDateTimeString = in.nextString();
        try {
            return ZonedDateTime.parse(zonedDateTimeString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IOException("Failed to parse ZonedDateTime: " + zonedDateTimeString, e);
        }
    }
}