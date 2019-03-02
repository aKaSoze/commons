package fractal.code.persistence.reflection;

import java.lang.reflect.Type;

public class MapType {

    private final Class<?> rawType;
    private final Type keyType;
    private final Type valueType;

    public MapType(Class<?> rawType, Type keyType, Type valueType) {
        this.rawType = rawType;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public Class<?> getRawType() {
        return rawType;
    }

    public Type getKeyType() {
        return keyType;
    }

    public Type getValueType() {
        return valueType;
    }
}
