package cz.vhromada.catalog.commons;

import java.util.Calendar;

/**
 * A class represents constants.
 *
 * @author Vladimir Hromada
 */
public final class Constants {

    /** Minimal year */
    public static final int MIN_YEAR = 1940;

    /** Current year */
    public static final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    /** Maximum IMDB code */
    public static final int MAX_IMDB_CODE = 9999999;

    /** Creates a new instance of Constants. */
    private Constants() {
    }

}
