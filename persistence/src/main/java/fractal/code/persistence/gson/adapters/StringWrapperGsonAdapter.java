package fractal.code.persistence.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.function.Function;

public class StringWrapperGsonAdapter<Type> extends TypeAdapter<Type> {

    private final Function<Type, String> serializer;
    private final Function<String, Type> deserializer;

    public StringWrapperGsonAdapter(Function<Type, String> serializer, Function<String, Type> deserializer) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public void write(JsonWriter out, Type value) throws IOException {
        if (value == null) out.nullValue();
        else out.value(serializer.apply(value));
    }

    @Override
    public Type read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else return deserializer.apply(in.nextString());
    }
}
