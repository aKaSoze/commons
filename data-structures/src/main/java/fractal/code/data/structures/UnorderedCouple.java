package fractal.code.data.structures;

/**
 * Created by sorin.nica in September 2016
 */
public class UnorderedCouple<T> extends Tuple<T, T> {

    public UnorderedCouple(T first, T second) {
        super(first, second);
    }

    @Override
    public boolean equals(Object other) {
        return IdentityUtils.<UnorderedCouple<T>>equals(this, other, (thiz, ozer) ->
                (thiz.getFirst().equals(ozer.getFirst()) && thiz.getSecond().equals(ozer.getSecond())) ||
                        (thiz.getFirst().equals(ozer.getSecond()) && thiz.getSecond().equals(ozer.getFirst())));
    }


}
