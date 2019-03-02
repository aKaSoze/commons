package fractal.code.calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by sorin.nica in October 2016
 */
public class TimeInterval implements Comparable<TimeInterval> {

    public static final String PRETTY_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static final String LEXICOGRAPHIC_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String LEXICOGRAPHIC_FORMAT_SHORT = "yyyy-MM-dd_HH";

    public static final DateTimeFormatter PRETTY_FORMATTER = DateTimeFormat.forPattern(PRETTY_FORMAT).withZoneUTC();

    public static final DateTimeFormatter LEXICOGRAPHIC_FORMATTER = DateTimeFormat
            .forPattern(LEXICOGRAPHIC_FORMAT)
            .withZoneUTC();

    private final DateTime from;

    private final DateTime to;

    public TimeInterval(DateTime from, DateTime to) {
        this.from = from;
        this.to = to;
    }

    public TimeInterval(String from, String to, String format) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        Objects.requireNonNull(format);

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        this.from = dateTimeFormatter.parseDateTime(from);
        this.to = dateTimeFormatter.parseDateTime(to);
    }

    public TimeInterval(String from, String to) {
        this(from, to, PRETTY_FORMAT);
    }

    public List<DateTime> iterateByHour() {
        DateTime fromHour = from.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime toHour = to.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        List<DateTime> hours = new ArrayList<>();
        while (fromHour.isBefore(toHour) || fromHour.isEqual(toHour)) {
            hours.add(fromHour);
            fromHour = fromHour.plusHours(1);
        }

        return hours;
    }

    public List<TimeInterval> split(Duration duration) {
        DateTime dynamicFrom = from;
        DateTime dynamicTo = from;

        List<TimeInterval> intervals = new ArrayList<>();
        while (dynamicFrom.isBefore(to) && (dynamicTo.isBefore(to) || dynamicTo.isEqual(to))) {
            switch (duration.getUnit()) {
                case DAYS:
                    dynamicTo = dynamicTo.plusDays(duration.getQuantity().intValue());
                    break;
                case HOURS:
                    dynamicTo = dynamicTo.plusHours(duration.getQuantity().intValue());
                    break;
            }

            intervals.add(new TimeInterval(dynamicFrom, dynamicTo.isAfter(to) ? to : dynamicTo));
            dynamicFrom = dynamicTo;
        }

        return intervals;
    }

    public String getFromAsString(String format) {
        return from.toString(format);
    }

    public String getToAsString(String format) {
        return to.toString(format);
    }

    @Override
    public int compareTo(TimeInterval other) {
        return to.compareTo(other.to);
    }

    @Override
    public String toString() {
        return "[" + from.toString(PRETTY_FORMAT) + " - " + to.toString(PRETTY_FORMAT) + "]";
    }

}
