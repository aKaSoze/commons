package fractal.code.persistence.pojo;

import fractal.code.id.Identifiable;

import java.util.UUID;

/**
 * Created by sorin.nica in February 2017
 */
public class Address implements Identifiable {

    private final String city;

    private final String street;

    private final StringWrapper uuid = new StringWrapper(UUID.randomUUID().toString());

    public Address(String city, String street) {
        this.city = city;
        this.street = street;
    }

    @Override
    public String getId() {
        return city + "_" + street;
    }

}
