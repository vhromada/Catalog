package cz.vhromada.catalog.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
    @Before
    public void setUp() {
        timeLength = new Time(LENGTH);
        timeHMS = new Time(HOURS, MINUTES, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_BadLength() {
        new Time(-1);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad hours.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_BadHours() {
        new Time(-1, MINUTES, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with negative minutes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NegativeMinutes() {
        new Time(HOURS, -1, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad minutes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_BadMinutes() {
        new Time(HOURS, BAD_MAX_TIME, SECONDS);
    }

    /**
     * Test method for {@link Time#Time(int)} with negative seconds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NegativeSeconds() {
        new Time(HOURS, MINUTES, -1);
    }

    /**
     * Test method for {@link Time#Time(int)} with bad seconds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_BadSeconds() {
        new Time(HOURS, MINUTES, BAD_MAX_TIME);
    }

    /**
     * Test method for {@link Time#getLength()}.
     */
    @Test
    public void getLength() {
        assertThat(timeLength.getLength(), is(LENGTH));
        assertThat(timeHMS.getLength(), is(LENGTH));
    }

    /**
     * Test method for {@link Time#getData(Time.TimeData)}.
     */
    @Test
    public void getData() {
        assertThat(timeLength.getData(Time.TimeData.HOUR), is(HOURS));
        assertThat(timeLength.getData(Time.TimeData.MINUTE), is(MINUTES));
        assertThat(timeLength.getData(Time.TimeData.SECOND), is(SECONDS));
        assertThat(timeHMS.getData(Time.TimeData.HOUR), is(HOURS));
        assertThat(timeHMS.getData(Time.TimeData.MINUTE), is(MINUTES));
        assertThat(timeHMS.getData(Time.TimeData.SECOND), is(SECONDS));
    }

    /**
     * Test method for {@link Time#getData(Time.TimeData)} with null data type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getData_NegativeDataType() {
        timeLength.getData(null);
    }

    /**
     * Test method for {@link Time#toString()}.
     */
    @Test
    public void testToString() {
        assertThat(timeLength.toString(), is("2:35:26"));
        assertThat(timeHMS.toString(), is("2:35:26"));
        assert TIME_LENGTHS.length == TIME_STRINGS.length;
        for (int i = 0; i < TIME_LENGTHS.length; i++) {
            assertThat(new Time(TIME_LENGTHS[i]).toString(), is(TIME_STRINGS[i]));
        }
    }

}
