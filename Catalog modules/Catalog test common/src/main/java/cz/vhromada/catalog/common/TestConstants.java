package cz.vhromada.catalog.common;

import java.lang.reflect.Field;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Constants;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class represents constants for tests.
 *
 * @author Vladimir Hromada
 */
public final class TestConstants {

	/** ID */
	public static final int ID = 2;

	/** Czech name */
	public static final String CZECH_NAME = "Czech name";

	/** Original name */
	public static final String ORIGINAL_NAME = "Original name";

	/** Year */
	public static final int YEAR = 2000;

	/** Language */
	public static final Language LANGUAGE = Language.FR;

	/** 1st subtitles */
	public static final Language SUBTITLES_1 = Language.CZ;

	/** 2st subtitles */
	public static final Language SUBTITLES_2 = Language.EN;

	/** Subtitles */
	public static final List<Language> SUBTITLES = CollectionUtils.newList(SUBTITLES_1, SUBTITLES_2);

	/** 1st medium length */
	public static final int MEDIUM_1 = 100;

	/** 2st medium length */
	public static final int MEDIUM_2 = 200;

	/** Media */
	public static final List<Integer> MEDIA = CollectionUtils.newList(MEDIUM_1, MEDIUM_2);

	/** URL to ČSFD */
	public static final String CSFD = "CSFD";

	/** IMDB code */
	public static final int IMDB = 653;

	/** URL to english Wikipedia */
	public static final String WIKIPEDIA_EN = "Wikipedia_en";

	/** URL to czech Wikipedia */
	public static final String WIKIPEDIA_CZ = "Wikipedia_cz";

	/** Path to picture */
	public static final String PICTURE = "Picture";

	/** Note */
	public static final String NOTE = "Note";

	/** Position */
	public static final int POSITION = 44;

	/** Number */
	public static final int NUMBER = 5;

	/** Starting year */
	public static final int START_YEAR = 2000;

	/** Ending year */
	public static final int END_YEAR = 2001;

	/** Name */
	public static final String NAME = "Name";

	/** Length */
	public static final int LENGTH = 10;

	/** Count of media */
	public static final int MEDIA_COUNT = 4;

	/** Crack */
	public static final boolean CRACK = true;

	/** Serial key */
	public static final boolean SERIAL_KEY = false;

	/** Patch */
	public static final boolean PATCH = true;

	/** Trainer */
	public static final boolean TRAINER = true;

	/** Data for trainer */
	public static final boolean TRAINER_DATA = false;

	/** Editor */
	public static final boolean EDITOR = false;

	/** Saves */
	public static final boolean SAVES = true;

	/** URL to Wikipedia */
	public static final String OTHER_DATA = "Other data";

	/** Author */
	public static final String AUTHOR = "Author";

	/** Title */
	public static final String TITLE = "Title";

	/** 1st language */
	public static final Language LANGUAGE_1 = Language.CZ;

	/** 2nd language */
	public static final Language LANGUAGE_2 = Language.EN;

	/** Languages */
	public static final List<Language> LANGUAGES = CollectionUtils.newList(LANGUAGE_1, LANGUAGE_2);

	/** Category */
	public static final String CATEGORY = "Category";

	/** Primary ID */
	public static final int PRIMARY_ID = 1;

	/** Inner ID */
	public static final int INNER_ID = 11;

	/** Inner inner ID */
	public static final int INNER_INNER_ID = 21;

	/** Secondary ID */
	public static final int SECONDARY_ID = 2;

	/** Secondary inner ID */
	public static final int SECONDARY_INNER_ID = 12;

	/** Secondary inner inner ID */
	public static final int SECONDARY_INNER_INNER_ID = 22;

	/** Add ID */
	public static final int ADD_ID = 5;

	/** Add position */
	public static final int ADD_POSITION = 10;

	/** Move position */
	public static final int MOVE_POSITION = 2;

	/** 1st name */
	public static final String NAME_1 = "Name 1";

	/** 2nd name */
	public static final String NAME_2 = "Name 2";

	/** Inner count */
	public static final int INNER_COUNT = 5;

	/** Inner inner count */
	public static final int INNER_INNER_COUNT = 10;

	/** Total length */
	public static final Time TOTAL_LENGTH = new Time(500);

	/** Count */
	public static final int COUNT = 50;

	/** Hours */
	public static final int HOURS = 10;

	/** Minutes */
	public static final int MINUTES = 20;

	/** Seconds */
	public static final int SECONDS = 30;

	/** Bad minimal year */
	public static final int BAD_MIN_YEAR = Constants.MIN_YEAR - 1;

	/** Bad maximal year */
	public static final int BAD_MAX_YEAR = Constants.CURRENT_YEAR + 1;

	/** Bad minimum IMDB code */
	public static final int BAD_MIN_IMDB_CODE = -2;

	/** Bad maximum IMDB code */
	public static final int BAD_MAX_IMDB_CODE = Constants.MAX_IMDB_CODE + 1;

	/** Bad subtitles */
	public static final List<Language> BAD_SUBTITLES = CollectionUtils.newList(Language.CZ, null);

	/** Bad languages */
	public static final List<Language> BAD_LANGUAGES = CollectionUtils.newList(Language.CZ, null);

	/** Negative time */
	public static final Time NEGATIVE_TIME = new Time(0);

	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(TestConstants.class);

	static {
		try {
			Field length = Time.class.getDeclaredField("length");
			length.setAccessible(true);
			length.setInt(NEGATIVE_TIME, -1);
		} catch (IllegalAccessException | NoSuchFieldException ex) {
			logger.error("Time with negative length can't be created.", ex);
		}
	}

	/** Creates a new instance of TestConstants. */
	private TestConstants() {
	}

}
