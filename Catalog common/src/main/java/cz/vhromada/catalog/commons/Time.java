package cz.vhromada.catalog.commons;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

import cz.vhromada.validators.Validators;

/**
 * A class represents time.
 *
 * @author Vladimir Hromada
 */
public final class Time implements Serializable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Count of hours in day
     */
    private static final int DAY_HOURS = 24;

    /**
     * Count of seconds in hour
     */
    private static final int HOUR_SECONDS = 3600;

    /**
     * Count of seconds in minute
     */
    private static final int MINUTE_SECONDS = 60;

    /**
     * Minimum minutes or seconds
     */
    private static final int MIN_TIME = 0;

    /**
     * Maximum minutes or seconds
     */
    private static final int MAX_TIME = 59;

    /**
     * Time in seconds
     */
    private final int length;

    /**
     * Data
     */
    private final Map<TimeData, Integer> data;

    /**
     * Creates a new instance of Time.
     *
     * @param length time in seconds
     * @throws IllegalArgumentException if time in seconds is negative number
     */
    public Time(final int length) {
        Validators.validateArgumentNotNegativeNumber(length, "Length");

        this.length = length;
        this.data = new EnumMap<>(TimeData.class);
        this.data.put(TimeData.HOUR, length / HOUR_SECONDS);
        final int temp = length % HOUR_SECONDS;
        this.data.put(TimeData.MINUTE, temp / MINUTE_SECONDS);
        this.data.put(TimeData.SECOND, temp % MINUTE_SECONDS);
    }

    /**
     * Creates a new instance of Time.
     *
     * @param hours   hours
     * @param minutes minutes
     * @param seconds seconds
     * @throws IllegalArgumentException if hours is negative number
     *                                  or minutes isn't between 0 and 59
     *                                  or seconds isn't between 0 and 59
     */
    public Time(final int hours, final int minutes, final int seconds) {
        Validators.validateArgumentNotNegativeNumber(hours, "Hours");
        Validators.validateArgumentRange(minutes, MIN_TIME, MAX_TIME, "Minutes");
        Validators.validateArgumentRange(seconds, MIN_TIME, MAX_TIME, "Seconds");

        this.length = hours * HOUR_SECONDS + minutes * MINUTE_SECONDS + seconds;
        this.data = new EnumMap<>(TimeData.class);
        this.data.put(TimeData.HOUR, hours);
        this.data.put(TimeData.MINUTE, minutes);
        this.data.put(TimeData.SECOND, seconds);
    }

    /**
     * Returns time in seconds.
     *
     * @return time in seconds
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns data.
     *
     * @param dataType data type
     * @return data
     * @throws IllegalArgumentException if data type is null
     */
    public int getData(final TimeData dataType) {
        Validators.validateArgumentNotNull(dataType, "Data type");

        return data.get(dataType);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Time)) {
            return false;
        }
        final Time time = (Time) obj;
        return length == time.length;
    }

    @Override
    public int hashCode() {
        return length;
    }

    @Override
    public String toString() {
        final int days = data.get(TimeData.HOUR) / DAY_HOURS;
        final int hours = data.get(TimeData.HOUR) % DAY_HOURS;
        if (days > 0) {
            return String.format("%d:%02d:%02d:%02d", days, hours, data.get(TimeData.MINUTE), data.get(TimeData.SECOND));
        }
        return String.format("%d:%02d:%02d", hours, data.get(TimeData.MINUTE), data.get(TimeData.SECOND));
    }

    /**
     * An enumeration represents time.
     *
     * @author Vladimir Hromada
     */
    public enum TimeData {

        /**
         * Hour
         */
        HOUR,

        /**
         * Minute
         */
        MINUTE,

        /**
         * Second
         */
        SECOND

    }

}
