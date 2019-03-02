package fractal.code.calendar.test;

import fractal.code.calendar.Duration;
import fractal.code.calendar.TimeInterval;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sorin.nica in October 2016
 */
public class TimeIntervalTest {

    @Test
    public void testSplit() {
        TimeInterval timeInterval = new TimeInterval("25-08-2016 03:45:00", "29-09-2016 00:00:00");
        List<TimeInterval> split = timeInterval.split(Duration.of(7L, TimeUnit.DAYS));
        split.stream().map(TimeInterval::toString).forEach(System.out::println);
    }

}
