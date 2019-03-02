package fractal.code.persistence;

import com.google.gson.TypeAdapter;
import fractal.code.id.Identifiable;
import fractal.code.persistence.gson.adapters.StringWrapperGsonAdapter;
import fractal.code.persistence.pojo.Account;
import fractal.code.persistence.pojo.AccountType;
import fractal.code.persistence.pojo.Address;
import fractal.code.persistence.pojo.StringWrapper;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sorin.nica in February 2017
 */
public class Playground {

    public static void main(String[] args) {

        HashMap<Class<?>, TypeAdapter<?>> adapters = new HashMap<>();
        adapters.put(StringWrapper.class, new StringWrapperGsonAdapter<>(StringWrapper::getValue, StringWrapper::new));

        Database database = new Database(
                Paths.get("/home/akasoze/Work/Java/Projects/Repositories/Repositories/commons/tmp"),
                adapters
        );

        database.persist(complex());
        List<Identifiable> identifiables = database.loadAll();
        System.out.println(identifiables);
    }

    public static Account complex() {
        Address address = new Address("Bucharest", "Pastorului");
        Account account = new Account("Dino", "Matusenco", 27L, address, AccountType.VIP);
        account.addNickName("Jordani");
        account.addNickName("Marocani");

        account.addMisc("fingers", 2);
        account.addMisc("eyes", 5L);
        account.addMisc("hobby", "he likes to stare");
        account.addMisc("alternate address", new Address(null, "Argetoaia"));
        account.addMisc("buffer", new StringBuffer());
        account.addMisc("list", Arrays.asList(22, 50, 7));
        account.addMisc(new Account("Misc Acc", "Nurma de gov", 28L, new Address(null, null), AccountType.VIP), Arrays.asList(22, 50, 7));

        Map<String, Object> aMap = new HashMap<>();
        Map<String, Object> bMap = new HashMap<>();
        aMap.put("b", bMap);
        bMap.put("a", aMap);

//        account.addMisc("recursive", aMap);

        Account friend = new Account("Milo", "Nurma de gov", 28L, new Address(null, null), AccountType.User);
        friend.addNickName("dog");

        Account bestFriend = new Account("Leah", "Jennah", 28L, new Address("Bucharest", "Pastorului"), AccountType.User);

        account.setBestFriend(bestFriend);
        bestFriend.setBestFriend(account);
        account.addFriend(friend);

        return account;
    }

    private static String someIteratingMethod(List<?> list) {
        return list.stream().map(Object::toString).reduce((s1, s2) -> s1 + s2).orElse("");

    }

    private static void offf() {
        List<?> myL = Arrays.asList("a", "b");

        System.out.println(someIteratingMethod(myL));

        myL.forEach(System.out::println);
    }

}
