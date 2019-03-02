package fractal.code.calendar;

import org.joda.time.DateTime;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Objects;

/**
 * Created by sorin.nica in October 2016
 */
public class CalendarUtils {

    public static Long getNumberOfWeekDays(Integer year, Month month) {
        Objects.requireNonNull(year);
        Objects.requireNonNull(month);

        long counter = 0;
        DateTime dateCursor = new DateTime(year, month.getValue(), 1, 0, 0);

        while (dateCursor.getMonthOfYear() == month.getValue()) {
            if (dateCursor.getDayOfWeek() < DayOfWeek.SATURDAY.getValue()) counter++;
            dateCursor = dateCursor.plusDays(1);
        }

        return counter;
    }

    public static Long getNumberOfWeekDays(Month month) {
        return getNumberOfWeekDays(DateTime.now().getYear(), month);
    }

}
