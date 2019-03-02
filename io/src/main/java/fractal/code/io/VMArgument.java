package fractal.code.io;

import java.util.ArrayList;
import java.util.List;

public class VMArgument {

    private final List<String> keys = new ArrayList<>();

    private String defaultValue = "";

    public VMArgument(String name) {
        keys.add(name);
    }

    public VMArgument withAlias(String keyAlias) {
        keys.add(keyAlias);
        return this;
    }

    public VMArgument withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String get() {
        return keys.stream()
                .map(System::getProperty)
                .filter(this::isNotEmpty)
                .findFirst()
                .orElse(defaultValue);
    }

    public void publish() {
        System.setProperty(keys.get(0), get());
    }

    private boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }

}
