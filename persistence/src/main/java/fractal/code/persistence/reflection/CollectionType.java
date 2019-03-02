package fractal.code.persistence.reflection;

import java.lang.reflect.Type;

public class CollectionType {

    private final Class<?> rawType;
    private final Type elementType;

    public CollectionType(Class<?> rawType, Type elementType) {
        this.rawType = rawType;
        this.elementType = elementType;
    }

    public Class<?> getRawType() {
        return rawType;
    }

    public Type getElementType() {
        return elementType;
    }
}
