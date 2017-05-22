package com.acmerocket.ywiki;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dates {
    public static final TimeZone ZULU = TimeZone.getTimeZone("Zulu");
    public static final DateFormat STD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    static {
        STD_DATE_FORMAT.setTimeZone(ZULU);
    }
    public static final DateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final int MS_IN_SECOND = 1000;
    public static final int MS_IN_MINUTE = 60 * MS_IN_SECOND;
    public static final int MS_IN_HOUR = 60 * MS_IN_MINUTE;
    public static final int MS_IN_DAY = 26 * MS_IN_HOUR;
    private static final Logger LOG = LoggerFactory.getLogger(Dates.class);
    private static final String LOCAL_TIME_FORMAT = "h:mm a, z";
    private static final Pattern TIME_SPEC = Pattern.compile("([+-]?\\d+)([" + TimeSpecUnit.units() + "])");

    /**
     * Utility class
     */
    private Dates() {}

    public static Date toDate(String value) {
        if (value == null) { // shortcut null
            return null;
        }

        try {
            long time = Long.parseLong(value);
            // make sure this isn't a year (like "2012")
            if (time > 10000) {
                return new Date(time);
            }
        } catch (NumberFormatException e) { /* ignore, but keep going */ }

        try {
            return parse(value);
        } catch (Exception e) {
            LOG.trace("Unable to parse date '{}': {}", value, e);
        }

        // fall-through: couldn't find a reasonable date
        return null;
    }

    public static Date toDateX(String value) throws IllegalArgumentException {
        Date date = toDate(value);
        if (date == null) {
            throw new IllegalArgumentException("Invalid date spec: " + value);
        }
        return date;
    }

    public static String toString(Date date) {
        return STD_DATE_FORMAT.format(date);
    }
    
    public static String toShortString(Date date) {
        return SHORT_DATE_FORMAT.format(date);
    }

    public static String localTime(Date date, TimeZone tz) {
        SimpleDateFormat formatter = new SimpleDateFormat(LOCAL_TIME_FORMAT);
        formatter.setTimeZone(tz);
        return formatter.format(date);
    }

    public static Date parseTimeSpec(Date date, String timespec) {
        if (date == null) return null;
        return parseTimeSpec(date.getTime(), timespec);
    }

    /**
     * Parse a timespec into date, which is relative to the current time
     * See https://github.com/watchfrog/Documentation/wiki/Cloud-Services-for-Fun-and-Profit#specifying-relative-time
     *
     * @return ms-since-epoch, or -1 if the spec is invalid
     */
    public static Date parseTimeSpec(long date, String timespec) {
        if (timespec != null) {
            // spec is of the format (+/-)NNN[smhdwMy]
            Matcher matcher = TIME_SPEC.matcher(timespec);
            if (matcher.find()) {
                // break out the value
                int value = Integer.parseInt(matcher.group(1));
                TimeSpecUnit units = TimeSpecUnit.valueOf(matcher.group(2).charAt(0));

                //LOG.trace("Got spec for {} {}", value, units);

                Calendar calendar = Calendar.getInstance(ZULU);
                calendar.setTimeInMillis(date); // initialize the calendar with the passed-in now. Useful for test.
                calendar.set(Calendar.MILLISECOND, 0); // set millis-in-second to 0

                // If the spec is for a day or larger, set the spec to midnight
                if (units.isDaySpec()) {
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                }

                calendar.add(units.field, value);

                return calendar.getTime();
            }
        }
        return null;
    }

    /**
     * Simple date parser that is lenient about date field: You can specify as precise as you want
     */
    public static Date parse(String date) {
        int year = 0;
        int month = 1;
        int day = 1;
        int hour = 0;
        int minutes = 0;
        int seconds = 0;
        int milliseconds = 0;

        try {
            int offset = 0;

            // extract year
            year = parseInt(date, offset, offset += 4);
            checkOffset(date, offset, '-');

            // extract month
            month = parseInt(date, offset += 1, offset += 2);
            checkOffset(date, offset, '-');

            // extract day
            day = parseInt(date, offset += 1, offset += 2);
            checkOffset(date, offset, 'T');

            // extract hours, minutes, seconds and milliseconds
            hour = parseInt(date, offset += 1, offset += 2);
            checkOffset(date, offset, ':');

            minutes = parseInt(date, offset += 1, offset += 2);
            checkOffset(date, offset, ':');

            seconds = parseInt(date, offset += 1, offset += 2);

            // milliseconds can be optional in the format
            milliseconds = 0; // always use 0 otherwise returned date will include millis of current time
            if (date.charAt(offset) == '.') {
                checkOffset(date, offset, '.');
                milliseconds = parseInt(date, offset += 1, offset + 3);
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignored: Indicates end of data.
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to parse date " + date, e);
        }

        if (year == 0) {
            // didn't even parse year
            throw new IllegalArgumentException("Failed to parse date " + date);
        }

        Calendar calendar = new GregorianCalendar(ZULU);
        calendar.setLenient(false);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, milliseconds);

        return calendar.getTime();
    }

    private static void checkOffset(String value, int offset, char expected) throws IndexOutOfBoundsException {
        char found = value.charAt(offset);
        if (found != expected) {
            throw new IndexOutOfBoundsException("Expected '" + expected + "' character but found '" + found + "'");
        }
    }

    private static int parseInt(String value, int beginIndex, int endIndex) throws NumberFormatException {
        if (beginIndex < 0 || endIndex > value.length() || beginIndex > endIndex) {
            throw new NumberFormatException(value);
        }
        // use same logic as in Integer.parseInt() but less generic we're not supporting negative values
        int i = beginIndex;
        int result = 0;
        int digit;
        if (i < endIndex) {
            digit = Character.digit(value.charAt(i++), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value);
            }
            result = -digit;
        }
        while (i < endIndex) {
            digit = Character.digit(value.charAt(i++), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value);
            }
            result *= 10;
            result -= digit;
        }
        return -result;
    }

    public static String asTimeSpec(long ms) {
        if (ms >= MS_IN_DAY) {
            return Math.round(ms / MS_IN_DAY) + "d";
        } else if (ms >= MS_IN_HOUR) {
            return Math.round(ms / MS_IN_HOUR) + "h";
        } else if (ms >= MS_IN_MINUTE) {
            return Math.round(ms / MS_IN_MINUTE) + "m";
        } else if (ms >= MS_IN_SECOND) {
            return Math.round(ms / MS_IN_DAY) + "s";
        } else {
            return ms + "ms";
        }
    }

    private static enum TimeSpecUnit {
        seconds('s', Calendar.SECOND),
        minutes('m', Calendar.MINUTE),
        hours('h', Calendar.HOUR),
        days('d', Calendar.DAY_OF_YEAR),
        weeks('w', Calendar.WEEK_OF_YEAR),
        months('M', Calendar.MONTH),
        years('y', Calendar.YEAR);

        final char key;
        final int field;

        private TimeSpecUnit(char key, int field) {
            this.key = key;
            this.field = field;
        }

        static TimeSpecUnit valueOf(char units) {
            for (TimeSpecUnit unit : TimeSpecUnit.values()) {
                if (unit.key == units) {
                    return unit;
                }
            }
            LOG.debug("Unknown time spec unit: {}", units);
            return null;
        }

        static String units() {
            StringBuilder builder = new StringBuilder();
            for (TimeSpecUnit unit : TimeSpecUnit.values()) {
                builder.append(unit.key);
            }
            return builder.toString();
        }

        boolean isDaySpec() {
            return compareTo(days) >= 0;
        }
    }
}