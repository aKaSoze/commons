package fractal.code.persistence;

/**
 * Created by sorin.nica in March 2017
 */
public class NamedInstance<T> {

    private final String name;

    private final T instance;



    public NamedInstance(String name, T instance) {
        this.name = name;
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public T getInstance() {
        return instance;
    }
}
