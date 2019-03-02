package fractal.code.persistence.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

public class DateTimeGsonAdapter extends TypeAdapter<DateTime> {

    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy").withZoneUTC();

    @Override
    public void write(JsonWriter out, DateTime value) throws IOException {
        if (value == null) out.nullValue();
        else out.value(formatter.print(value));
    }

    @Override
    public DateTime read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return formatter.parseDateTime(reader.nextString());
    }
}
