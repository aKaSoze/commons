package fractal.code.persistence.pojo;

import fractal.code.id.UUIdentifiable;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by sorin.nica in February 2017
 */
public class Account extends UUIdentifiable {

    private final String firstName;

    private final String lastName;

    private final Long age;

    private final Address address;

    private AccountType accountType;

    private Account bestFriend;

    private final Set<Account> friends = new HashSet<>();

    private final Set<String> nickNames = new LinkedHashSet<>();

    private final Map<Object, Object> miscellaneous = new HashMap<>();

    private final DateTime birthday = DateTime.now();

    public Account(String firstName, String lastName, Long age, Address address, AccountType accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.accountType = accountType;
    }

    public void addFriend(Account friend) {
        friends.add(friend);
    }

    public void addNickName(String nickName) {
        nickNames.add(nickName);
    }

    public void addMisc(Object property, Object obj) {
        miscellaneous.put(property, obj);
    }

    public Account getBestFriend() {
        return bestFriend;
    }

    public void setBestFriend(Account bestFriend) {
        this.bestFriend = bestFriend;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
