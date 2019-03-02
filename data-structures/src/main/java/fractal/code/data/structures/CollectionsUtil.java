package fractal.code.data.structures;

import java.util.List;

/**
 * Created by sorin.nica in March 2017
 */
public class CollectionsUtil {

    public static <E> Long countLongestSequence(List<E> list, E element) {
        long count = 0;
        long maxCount = 0;

        for (E ele : list) {
            if (ele == element) count++;
            else count = 0;

            if (count > maxCount) maxCount = count;
        }

        return maxCount;
    }

}
