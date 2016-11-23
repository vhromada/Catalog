package cz.vhromada.catalog.common;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents constants for tests.
 *
 * @author Vladimir Hromada
 */
public final class TestConstants {

    /**
     * Bad minimal year
     */
    public static final int BAD_MIN_YEAR = Constants.MIN_YEAR - 1;

    /**
     * Bad maximal year
     */
    public static final int BAD_MAX_YEAR = Constants.CURRENT_YEAR + 1;

    /**
     * Bad minimum IMDB code
     */
    public static final int BAD_MIN_IMDB_CODE = -2;

    /**
     * Bad maximum IMDB code
     */
    public static final int BAD_MAX_IMDB_CODE = Constants.MAX_IMDB_CODE + 1;

    /**
     * Negative time
     */
    public static final Time NEGATIVE_TIME = new Time(0);

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(TestConstants.class);

    /**
     * Creates a new instance of TestConstants.
     */
    private TestConstants() {
    }

    static {
        try {
            final Field length = Time.class.getDeclaredField("length");
            length.setAccessible(true);
            length.setInt(NEGATIVE_TIME, -1);
        } catch (final IllegalAccessException | NoSuchFieldException ex) {
            logger.error("Time with negative length can't be created.", ex);
        }
    }

}
