package fractal.code.persistence;

/**
 * Created by sorin.nica in March 2017
 */
public class ObjectIdentity {

    private final String type;

    private final String id;

    ObjectIdentity(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
