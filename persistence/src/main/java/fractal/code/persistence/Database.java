package fractal.code.persistence;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import fractal.code.id.Identifiable;
import fractal.code.persistence.gson.adapters.DateTimeGsonAdapter;
import fractal.code.persistence.reflection.ReflectionExtensions;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fractal.code.io.Utils.uncheckedIO;

/**
 * Created by sorin.nica in March 2017
 */
@SuppressWarnings("Duplicates")
public class Database {

    private final List<Class<?>> allowedTypes = new ArrayList<>(Arrays.asList(
            Identifiable.class,
            String.class,
            Number.class,
            int.class,
            long.class,
            double.class,
            Enum.class,
            Collection.class,
            Map.class));

    private final Path pathToObjectFolder;

    private final JsonParser jsonParser;

    private final Map<Class<?>, TypeAdapter<?>> typeAdapters = new HashMap<>();

    public Database(Path pathToObjectFolder, Map<Class<?>, TypeAdapter<?>> adapters) {
        this.pathToObjectFolder = pathToObjectFolder;

        typeAdapters.put(DateTime.class, new DateTimeGsonAdapter());
        typeAdapters.putAll(adapters);
        allowedTypes.addAll(typeAdapters.keySet());
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        typeAdapters.forEach(builder::registerTypeAdapter);

        jsonParser = new JsonParser(builder.create(), new com.google.gson.JsonParser(), new ReflectionExtensions(allowedTypes), loadFunction());
    }

    public void persist(Collection<? extends Identifiable> identifiable) {
        Set<String> knownIdentities = new HashSet<>();
        identifiable.forEach(persistable -> persist(persistable, knownIdentities));
    }

    public void persist(Identifiable head, Identifiable... tail) {
        Set<String> knownIdentities = new HashSet<>();
        persist(head, knownIdentities);
        Arrays.stream(tail).forEach(it -> persist(it, knownIdentities));
    }

    public void persist(Identifiable identifiable) {
        persist(identifiable, new HashSet<>());
    }

    private void persist(Identifiable identifiable, Set<String> knownIdentities) {
        Objects.requireNonNull(identifiable, "null is not persistable");
        Objects.requireNonNull(identifiable.getId(), "The object " + identifiable + " has a null id.");

        if (knownIdentities.contains(identifiable.getId())) return;
        knownIdentities.add(identifiable.getId());

        JsonObject jsonObject = jsonParser.toJsonObject(identifiable, (childIdentifiable -> persist(childIdentifiable, knownIdentities)));
        Path path = determineFilePath(identifiable);
        write(jsonObject, path);
    }

    private List<Identifiable> loadAll(String relativePath) {
        try {
            Map<String, Identifiable> loadedObjects = new HashMap<>();
            return Files.walk(pathToObjectFolder.resolve(relativePath))
                    .filter(path -> Files.isRegularFile(path))
                    .map(this::newReader)
                    .map(jsonParser::parse)
                    .map(JsonElement::getAsJsonObject)
                    .map(json -> jsonParser.deserializeJson(json, loadedObjects))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public List<Identifiable> loadAll() {
        return loadAll("");
    }

    @SuppressWarnings("unchecked")
    public <T extends Identifiable> List<T> loadAllOfType(Class<T> classOf) {
        return (List<T>) loadAll(classOf.getName());
    }

    public <T extends Identifiable> Optional<T> loadOne(Class<T> classOf) {
        return loadAllOfType(classOf).isEmpty() ? Optional.empty() : Optional.of(loadAllOfType(classOf).get(0));
    }

    public void clear() {
        uncheckedIO(() -> FileUtils.deleteDirectory(pathToObjectFolder.toFile()));
    }

    private BufferedReader newReader(Path path) {
        return uncheckedIO(() -> Files.newBufferedReader(path));
    }

    private void write(JsonObject jsonObject, Path path) {
        uncheckedIO(() -> Files.write(path, jsonParser.toString(jsonObject).getBytes(StandardCharsets.UTF_8)));
    }

    private Path determineFilePath(Identifiable identifiable) {
        uncheckedIO(() -> Files.createDirectories(pathToObjectFolder.resolve(identifiable.getClass().getName())));
        return pathToObjectFolder.resolve(identifiable.getClass().getName()).resolve(identifiable.getId() + ".json");
    }

    private Path determineFilePath(ObjectIdentity objectIdentity) {
        return pathToObjectFolder.resolve(objectIdentity.getType()).resolve(objectIdentity.getId() + ".json");
    }

    private Function<ObjectIdentity, Reader> loadFunction() {
        return (objectIdentity) -> uncheckedIO(() -> Files.newBufferedReader(determineFilePath(objectIdentity)));
    }

    public List<Class<?>> getAllowedTypes() {
        return allowedTypes;
    }

    //    public static class Index<V> extends ArrayList<V> {
//
//        private final Map<Class<?>, Map<Object, List<V>>> cache = new HashMap<>();
//
//        public void classifyBy(Function<? super V, ?> classifier) {
//            Map<Object, List<V>> collect = stream().collect(Collectors.groupingBy(classifier));
//        }
//
//        public <K> List<V> getGroup(K key) {
//            Map<Object, List<V>> objectListMap = cache.get(key.getClass());
//            return objectListMap.get(key);
//        }
//
//        public <K> V get(K key) {
//            return getGroup(key).get(0);
//        }
//    }

    //    public <T extends Identifiable> T sync(T source) {
//        Optional<T> maybeDestination = (Optional<T>) getFromSession(source.getClass(), source.getId());
//        if (maybeDestination.isPresent()) {
//            T destination = maybeDestination.get();
//            if (source != destination) {
//                if (!source.getId().equals(destination.getId()))
//                    throw new IllegalArgumentException("Source and destination objects must have the same id.");
//
//                Class<?> classOf = source.getClass();
//                List<Field> fields = reflectionExtensions.getFields(classOf);
//
//                for (Field field : fields) {
//                    Class<?> classOfField = field.getType();
//                    Optional<Object> maybeValue = reflectionExtensions.getValueFromField(source, field);
//                    Optional<Object> receiver = reflectionExtensions.getValueFromField(destination, field);
//
//                    if (!maybeValue.isPresent() ||
//                            !receiver.isPresent() ||
//                            Enum.class.isAssignableFrom(classOfField) ||
//                            maybeValue.get() instanceof String ||
//                            maybeValue.get() instanceof Number) {
//                        reflectionExtensions.setField(field, destination, maybeValue.orElse(null));
//                    } else {
//                        Object value = maybeValue.get();
//                        if (Identifiable.class.isAssignableFrom(classOfField)) {
//                            sync(Identifiable.class.cast(receiver.get()));
//                            sync(Identifiable.class.cast(value));
//                        } else if (Collection.class.isAssignableFrom(classOfField)) {
//                            Collection<?> collection = Collection.class.cast(value);
//                            Collection<Object> newCollection = (Collection<Object>) reflectionExtensions.newEmptyInstance(classOfField);
//                            collection.forEach(element -> {
//                                if (element instanceof Identifiable) newCollection.add(sync(Identifiable.class.cast(element)));
//                                else newCollection.add(element);
//                            });
//                            reflectionExtensions.setField(field, destination, newCollection);
//                        }
//                    }
//                }
//
//            }
//            return destination;
//        } else {
//            addToSession(source);
//            return source;
//        }
//    }

}
