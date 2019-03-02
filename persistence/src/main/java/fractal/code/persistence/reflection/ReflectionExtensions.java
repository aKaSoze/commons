package fractal.code.persistence.reflection;

import fractal.code.id.Identifiable;
import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ReflectionExtensions {

    private final Unsafe unsafe;

    private final List<Class<?>> allowedTypes;

    public ReflectionExtensions(List<Class<?>> allowedTypes) {
        this.allowedTypes = allowedTypes;
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            unsafe = unsafeConstructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isClassPersistable(Class<?> someClass) {
        return allowedTypes.stream().anyMatch(aClass -> aClass.isAssignableFrom(someClass));
    }

    public Boolean isFieldPersistable(Field field) {
        return !Modifier.isStatic(field.getModifiers()) && isClassPersistable(field.getType());
    }

    public List<Field> getFields(Identifiable identifiable) {
        return getFields(identifiable.getClass());
    }

    public List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (Identifiable.class.isAssignableFrom(clazz)) {
            fields.addAll(getFields(clazz.getSuperclass()));
            fields.addAll(Arrays.stream(clazz.getDeclaredFields())
                    .filter(this::isFieldPersistable)
                    .collect(Collectors.toList()));
        }

        return fields;
    }

    public Class<?> classForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Object> getValueFromField(Identifiable persistable, Field field) {
        try {
            field.setAccessible(true);
            return Optional.ofNullable(field.get(persistable));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isObjectPersistable(Object element) {
        return element != null && isClassPersistable(element.getClass());
    }

    public <T> T newEmptyInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            try {
                return clazz.cast(unsafe.allocateInstance(clazz));
            } catch (InstantiationException ie) {
                throw new RuntimeException(ie);
            }
        }
    }

    public void setField(Field field, Object hostObject, Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(hostObject, fieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public MapType getMapType(Type type) {
        Class<?> rawType;
        Type keyType;
        Type valueType;

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            rawType = Class.class.cast(parameterizedType.getRawType());
            if (rawType.equals(Map.class)) rawType = HashMap.class;
            keyType = parameterizedType.getActualTypeArguments()[0];
            valueType = parameterizedType.getActualTypeArguments()[1];
        } else {
            rawType = HashMap.class;
            keyType = Object.class;
            valueType = Object.class;
        }

        return new MapType(rawType, keyType, valueType);
    }

    public CollectionType getCollectionType(Type type) {
        Class<?> rawType;
        Type elementType;

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
            rawType = Class.class.cast(parameterizedType.getRawType());
            if (rawType.equals(Set.class)) rawType = HashSet.class;
            if (rawType.equals(List.class)) rawType = ArrayList.class;
            elementType = parameterizedType.getActualTypeArguments()[0];
        } else {
            rawType = ArrayList.class;
            elementType = Object.class;
        }

        return new CollectionType(rawType, elementType);
    }

    public static class JhonnyWalker {
        private final Consumer<Object> consumer;

        private final ReflectionExtensions reflectionExtensions = new ReflectionExtensions(null);

        public JhonnyWalker(Consumer<Object> consumer) {
            this.consumer = consumer;
        }

        public void walk(Identifiable identifiable) {
            reflectionExtensions.getFields(identifiable).stream()
                    .map(field -> reflectionExtensions.getValueFromField(identifiable, field))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(this::walkObj);
        }

        private void walkObj(Object obj) {
            consumer.accept(obj);

            if (obj instanceof Identifiable) {
                walk(Identifiable.class.cast(obj));
            }

            if (obj instanceof Collection) {
                Collection<?> collection = Collection.class.cast(obj);
                collection.stream()
                        .filter(reflectionExtensions::isObjectPersistable)
                        .forEach(this::walkObj);
            }

            if (obj instanceof Map) {
                Map<?, ?> map = Map.class.cast(obj);
                map.entrySet().stream()
                        .filter(entry -> reflectionExtensions.isObjectPersistable(entry.getKey()))
                        .filter(entry -> reflectionExtensions.isObjectPersistable(entry.getValue()))
                        .forEach(entry -> {
                            walkObj(entry.getKey());
                            walkObj(entry.getValue());
                        });
            }

        }

    }


}
