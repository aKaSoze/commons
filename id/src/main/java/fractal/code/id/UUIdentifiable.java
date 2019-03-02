package fractal.code.id;

import java.util.UUID;

/**
 * Created by sorin.nica in March 2017
 */
public class UUIdentifiable implements Identifiable {
    private String id;

    @Override
    public String getId() {
        if (id == null) synchronized (this) {
            if (id == null) id = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
        }
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UUIdentifiable that = (UUIdentifiable) o;

        return getId() != null ?
                getId().equals(that.getId()) :
                that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
