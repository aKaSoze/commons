package fractal.code.persistence.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.function.Function;

public class IntegerWrapperGsonAdapter<Type> extends TypeAdapter<Type> {

    private final Function<Type, Integer> serializer;
    private final Function<Integer, Type> deserializer;

    public IntegerWrapperGsonAdapter(Function<Type, Integer> serializer, Function<Integer, Type> deserializer) {
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
        } else return deserializer.apply(in.nextInt());
    }
}
