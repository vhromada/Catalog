package cz.vhromada.catalog.common;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link Time}.
 *
 * @author Vladimir Hromada
 */
class TimeTest {

    /**
     * Length
     */
    private static final int LENGTH = 9326;

    /**
     * Array of {@link Time} in length
     */
    private static final int[] TIME_LENGTHS = { 106261, 88261, 104401, 106260, 45061, 19861, 18000, 211, 12, 0 };

    /**
     * Array of {@link Time} in strings
     */
    //CHECKSTYLE.OFF: Indentation
    private static final String[] TIME_STRINGS = { "1:05:31:01", "1:00:31:01", "1:05:00:01", "1:05:31:00", "12:31:01", "5:31:01", "5:00:00", "0:03:31",
        "0:00:12", "0:00:00" };
    //CHECKSTYLE.OFF: Indentation

    /**
     * Length - hours
     */
    private static final int HOURS = 2;

    /**
     * Length - minutes
     */
    private static final int MINUTES = 35;

    /**
     * Length - seconds
     */
    private static final int SECONDS = 26;

    /**
     * Bad maximum minutes or seconds
     */
    private static final int BAD_MAX_TIME = 60;

    /**
     * Instance of {@link Time}
     */
    private Time timeLength;

    /**
     * Instance of {@link Time}
     */
    private Time timeHMS;

    /**
     * Initializes time.
     */
    @BeforeEach
    void setUp() {
        timeLength = new Time(LENGTH);
        timeHMS = new Time(HOURS, MINUTES, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad length.
     */
    @Test
    void constructor_BadLength() {
        assertThrows(IllegalArgumentException.class, () -> new Time(-1));
    }

    /**
     * Test method for {@link Time#Time(int)} with bad hours.
     */
    @Test
    void constructor_BadHours() {
        assertThrows(IllegalArgumentException.class, () -> new Time(-1, MINUTES, SECONDS));
    }

    /**
     * Test method for {@link Time#Time(int)} with negative minutes.
     */
    @Test
    void constructor_NegativeMinutes() {
        assertThrows(IllegalArgumentException.class, () -> new Time(HOURS, -1, SECONDS));
    }

    /**
     * Test method for {@link Time#Time(int)} with bad minutes.
     */
    @Test
    void constructor_BadMinutes() {
        assertThrows(IllegalArgumentException.class, () -> new Time(HOURS, BAD_MAX_TIME, SECONDS));
    }

    /**
     * Test method for {@link Time#Time(int)} with negative seconds.
     */
    @Test
    void constructor_NegativeSeconds() {
        assertThrows(IllegalArgumentException.class, () -> new Time(HOURS, MINUTES, -1));
    }

    /**
     * Test method for {@link Time#Time(int)} with bad seconds.
     */
    @Test
    void constructor_BadSeconds() {
        assertThrows(IllegalArgumentException.class, () -> new Time(HOURS, MINUTES, BAD_MAX_TIME));
    }

    /**
     * Test method for {@link Time#getLength()}.
     */
    @Test
    void getLength() {
        assertAll(
            () -> assertEquals(LENGTH, timeLength.getLength()),
            () -> assertEquals(LENGTH, timeHMS.getLength())
        );
    }

    /**
     * Test method for {@link Time#getData(Time.TimeData)}.
     */
    @Test
    void getData() {
        assertAll(
            () -> assertEquals(HOURS, timeLength.getData(Time.TimeData.HOUR)),
            () -> assertEquals(MINUTES, timeLength.getData(Time.TimeData.MINUTE)),
            () -> assertEquals(SECONDS, timeLength.getData(Time.TimeData.SECOND)),
            () -> assertEquals(HOURS, timeHMS.getData(Time.TimeData.HOUR)),
            () -> assertEquals(MINUTES, timeHMS.getData(Time.TimeData.MINUTE)),
            () -> assertEquals(SECONDS, timeHMS.getData(Time.TimeData.SECOND))
        );
    }

    /**
     * Test method for {@link Time#getData(Time.TimeData)} with null data type.
     */
    @Test
    void getData_NegativeDataType() {
        assertThrows(IllegalArgumentException.class, () -> timeLength.getData(null));
    }

    /**
     * Test method for {@link Time#toString()}.
     */
    @Test
    void testToString() {
        assertAll(
            () -> assertEquals("2:35:26", timeLength.toString()),
            () -> assertEquals("2:35:26", timeHMS.toString()),
            () -> assertArrayEquals(TIME_STRINGS, Arrays.stream(TIME_LENGTHS).mapToObj(length -> new Time(length).toString()).toArray(String[]::new))
        );
    }

}
