package fractal.code.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fractal.code.id.Identifiable;
import fractal.code.persistence.reflection.CollectionType;
import fractal.code.persistence.reflection.MapType;
import fractal.code.persistence.reflection.ReflectionExtensions;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static fractal.code.io.Utils.uncheckedIO;

public class JsonParser {

    private final Gson gson;

    private final com.google.gson.JsonParser gsonParser;

    private final ReflectionExtensions reflectionExtensions;

    private final Function<ObjectIdentity, Reader> loadFunction;

    public JsonParser(Gson gson,
                      com.google.gson.JsonParser gsonParser,
                      ReflectionExtensions reflectionExtensions, Function<ObjectIdentity, Reader> loadFunction) {
        this.gson = gson;
        this.gsonParser = gsonParser;
        this.reflectionExtensions = reflectionExtensions;
        this.loadFunction = loadFunction;
    }

    public JsonObject toJsonObject(Identifiable identifiable, Consumer<Identifiable> identifiableFoundCallback) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", identifiable.getId());
        jsonObject.addProperty("type", identifiable.getClass().getName());

        reflectionExtensions.getFields(identifiable).forEach(field -> {
            Optional<Object> value = reflectionExtensions.getValueFromField(identifiable, field);
            value.ifPresent(nonNullValue -> jsonObject.add(field.getName(), toJsonElement(nonNullValue, identifiableFoundCallback)));
        });

        return jsonObject;
    }

    private JsonElement toJsonElement(Object obj, Consumer<Identifiable> identifiableFoundCallback) {
        if (obj instanceof Identifiable) {
            Identifiable identifiable = Identifiable.class.cast(obj);
            if (identifiableFoundCallback != null) identifiableFoundCallback.accept(identifiable);
            return gson.toJsonTree(new ObjectIdentity(identifiable.getClass().getName(), identifiable.getId()));
        }

        if (obj instanceof Collection) {
            JsonArray jsonArray = new JsonArray();
            Collection<?> collection = Collection.class.cast(obj);
            collection.stream()
                    .filter(reflectionExtensions::isObjectPersistable)
                    .map(ele -> toJsonElement(ele, identifiableFoundCallback))
                    .forEach(jsonArray::add);
            return jsonArray;
        }

        if (obj instanceof Map) {
            JsonArray jsonArray = new JsonArray();
            Map<?, ?> map = Map.class.cast(obj);
            map.entrySet().stream()
                    .filter(entry -> reflectionExtensions.isObjectPersistable(entry.getKey()))
                    .filter(entry -> reflectionExtensions.isObjectPersistable(entry.getValue()))
                    .map(entry -> {
                        JsonObject entryJson = new JsonObject();
                        entryJson.add("key", toJsonElement(entry.getKey(), identifiableFoundCallback));
                        entryJson.add("value", toJsonElement(entry.getValue(), identifiableFoundCallback));
                        return entryJson;
                    })
                    .forEach(jsonArray::add);
            return jsonArray;
        }

        return gson.toJsonTree(obj);
    }

    public Identifiable deserializeJson(JsonObject json, Map<String, Identifiable> loadedObjects) {
        String id = json.getAsJsonPrimitive("id").getAsString();
        if (loadedObjects.containsKey(id)) return loadedObjects.get(id);

        Class<?> aClass = reflectionExtensions.classForName(json.getAsJsonPrimitive("type").getAsString());
        Identifiable identifiable = Identifiable.class.cast(reflectionExtensions.newEmptyInstance(aClass));
        loadedObjects.put(id, identifiable);

        List<Field> fields = reflectionExtensions.getFields(identifiable);

        fields.stream()
                .filter(field -> json.has(field.getName()))
                .forEach(field -> {
                    Object fieldValue = deserializeJsonElement(json.get(field.getName()), field.getGenericType(), loadedObjects);
                    reflectionExtensions.setField(field, identifiable, fieldValue);
                });

        return identifiable;
    }

    @SuppressWarnings("unchecked")
    private Object deserializeJsonElement(JsonElement jsonElement, Type targetType, Map<String, Identifiable> loadedObjects) {
        if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
            if (isJsonMap(jsonElement)) {
                MapType mapType = reflectionExtensions.getMapType(targetType);

                Map<Object, Object> map = (Map<Object, Object>) reflectionExtensions.newEmptyInstance(mapType.getRawType());
                jsonElement.getAsJsonArray().forEach(ele -> {
                    JsonElement keyJson = ele.getAsJsonObject().get("key");
                    JsonElement valueJson = ele.getAsJsonObject().get("value");
                    map.put(deserializeJsonElement(keyJson, mapType.getKeyType(), loadedObjects),
                            deserializeJsonElement(valueJson, mapType.getValueType(), loadedObjects));
                });
                return map;
            } else {
                CollectionType collectionType = reflectionExtensions.getCollectionType(targetType);
                Collection<Object> collection = (Collection<Object>) reflectionExtensions.newEmptyInstance(collectionType.getRawType());
                jsonElement.getAsJsonArray().forEach(ele -> collection.add(deserializeJsonElement(ele, collectionType.getElementType(), loadedObjects)));
                return collection;
            }
        }

        if (jsonElement.isJsonObject()) {
            ObjectIdentity objectIdentity = fromJson(jsonElement.getAsJsonObject(), ObjectIdentity.class);
            return deserializeObjectIdentity(objectIdentity, loadedObjects);
        }

        return fromJson(jsonElement, targetType);
    }

    public JsonElement parse(Reader reader) {
        return gsonParser.parse(reader);
    }

    public String toString(JsonObject jsonObject) {
        return gson.toJson(jsonObject);
    }

    public <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public <T> T fromJson(JsonElement json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    private Boolean isJsonMap(JsonElement jsonElement) {
        return jsonElement.isJsonArray() &&
                jsonElement.getAsJsonArray().size() > 0 &&
                jsonElement.getAsJsonArray().get(0).isJsonObject() &&
                jsonElement.getAsJsonArray().get(0).getAsJsonObject().has("key") &&
                jsonElement.getAsJsonArray().get(0).getAsJsonObject().has("value");
    }

    private Identifiable deserializeObjectIdentity(ObjectIdentity objectIdentity, Map<String, Identifiable> loadedObjects) {
        return uncheckedIO(() -> {
            try (Reader reader = loadFunction.apply(objectIdentity)) {
                JsonObject json = parse(reader).getAsJsonObject();
                return deserializeJson(json, loadedObjects);
            }
        });
    }
}
