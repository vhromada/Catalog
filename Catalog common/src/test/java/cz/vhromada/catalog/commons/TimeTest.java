package cz.vhromada.catalog.commons;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link Time}.
 *
 * @author Vladimir Hromada
 */
public class TimeTest {

    /**
     * Length
     */
    private static final int LENGTH = 9326;

    /**
     * Array of Time in length
     */
    private static final int[] TIME_LENGTHS = { 106261, 88261, 104401, 106260, 45061, 19861, 18000, 211, 12, 0 };

    /**
     * Array of Time in strings
     */
    private static final String[] TIME_STRINGS = { "1:05:31:01", "1:00:31:01", "1:05:00:01", "1:05:31:00", "12:31:01", "5:31:01", "5:00:00", "0:03:31",
            "0:00:12", "0:00:00" };

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
    @Before
    public void setUp() {
        timeLength = new Time(LENGTH);
        timeHMS = new Time(HOURS, MINUTES, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_BadLength() {
        new Time(-1);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad hours.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_BadHours() {
        new Time(-1, MINUTES, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with negative minutes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeMinutes() {
        new Time(HOURS, -1, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad minutes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_BadMinutes() {
        new Time(HOURS, BAD_MAX_TIME, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with negative seconds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeSeconds() {
        new Time(HOURS, MINUTES, -1);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad seconds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_BadSeconds() {
        new Time(HOURS, MINUTES, BAD_MAX_TIME);
    }

    /**
     * Test method for {@link Time#getLength()}.
     */
    @Test
    public void testGetLength() {
        assertEquals(LENGTH, timeLength.getLength());
        assertEquals(LENGTH, timeHMS.getLength());
    }

    /**
     * Test method for {@link Time#getData(Time.TimeData)}.
     */
    @Test
    public void testGetData() {
        assertEquals(HOURS, timeLength.getData(Time.TimeData.HOUR));
        assertEquals(MINUTES, timeLength.getData(Time.TimeData.MINUTE));
        assertEquals(SECONDS, timeLength.getData(Time.TimeData.SECOND));
        assertEquals(HOURS, timeHMS.getData(Time.TimeData.HOUR));
        assertEquals(MINUTES, timeHMS.getData(Time.TimeData.MINUTE));
        assertEquals(SECONDS, timeHMS.getData(Time.TimeData.SECOND));
    }

    /**
     * Test method for {@link Time#getData(Time.TimeData)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetData_NegativeArgument() {
        timeLength.getData(null);
    }

    /**
     * Test method for {@link Time#equals(Object)} and {@link Time#hashCode()}.
     */
    @Test
    public void testEqualsHashCode() {
        final int count = 10;
        final Time[] times = new Time[count];
        times[0] = timeLength;
        for (int i = 1; i < count; i++) {
            times[i] = new Time(i);
        }

        for (int i = 0; i < count; i++) {
            final Time time1 = times[i];
            for (int j = 0; j < count; j++) {
                final Time time2 = times[j];
                if (i == j) {
                    assertTrue(time1.equals(time2));
                    assertThat(time1.hashCode(), is(time2.hashCode()));
                } else {
                    assertFalse(time1.equals(time2));
                    assertThat(time1.hashCode(), not(time2.hashCode()));
                    assertFalse(time2.equals(time1));
                    assertThat(time2.hashCode(), not(time1.hashCode()));
                }
            }
        }
    }

    /**
     * Test method for {@link Time#toString()}.
     */
    @Test
    public void testToString() {
        assertEquals("2:35:26", timeLength.toString());
        assertEquals("2:35:26", timeHMS.toString());
        assertTrue(TIME_LENGTHS.length == TIME_STRINGS.length);
        for (int i = 0; i < TIME_LENGTHS.length; i++) {
            assertEquals(TIME_STRINGS[i], new Time(TIME_LENGTHS[i]).toString());
        }
    }

}
