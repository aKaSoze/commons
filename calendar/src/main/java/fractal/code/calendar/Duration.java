package fractal.code.calendar;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.TimeUnit;

/**
 * Created by sorin.nica in May 2016
 */
@Data
public class Duration implements Comparable<Duration> {

    public static final String PRETTY_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static final String LEXICOGRAPHIC_FORMAT = "yyyy-MM-dd_HH";

    public static final DateTimeFormatter PRETTY_FORMATTER = DateTimeFormat.forPattern(PRETTY_FORMAT).withZoneUTC();

    public static final DateTimeFormatter LEXICOGRAPHIC_FORMATTER = DateTimeFormat
            .forPattern(LEXICOGRAPHIC_FORMAT)
            .withZoneUTC();

    public static DateTime now() {
        return new DateTime(DateTimeZone.UTC);
    }

    public static String nowAsString() {
        return new DateTime(DateTimeZone.UTC).toString(PRETTY_FORMAT);
    }

    public static Integer deltaMinutesTo(Integer wantedStartingPoint) {
        int currentMinutesOfHour = now().getMinuteOfHour();
        return wantedStartingPoint >= currentMinutesOfHour ?
                wantedStartingPoint - currentMinutesOfHour : // otherwise
                60 - currentMinutesOfHour + wantedStartingPoint;
    }

    private final Long quantity;

    private final TimeUnit unit;

    public static Duration of(Long quantity, TimeUnit unit) {
        return new Duration(quantity, unit);
    }

    public Long toMillis() {
        return unit.toMillis(quantity);
    }

    public DateTime extractFromNow() {
        return now().minus(toMillis());
    }

    @Override
    public int compareTo(Duration other) {
        return toMillis().compareTo(other.toMillis());
    }
}
