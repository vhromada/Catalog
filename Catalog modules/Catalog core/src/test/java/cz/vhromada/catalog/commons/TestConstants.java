package cz.vhromada.catalog.commons;

import java.util.List;

/**
 * A class represents constants for tests.
 *
 * @author Vladimir Hromada
 */
@Deprecated
public final class TestConstants {

	/** ID */
	public static final int ID = 2;

	/** Czech name */
	public static final String CZECH_NAME = "Czech name";

	/** Original name */
	public static final String ORIGINAL_NAME = "Original name";

	/** Language */
	public static final Language LANGUAGE = Language.FR;

	/** 1st subtitles */
	public static final Language SUBTITLES_1 = Language.CZ;

	/** 2st subtitles */
	public static final Language SUBTITLES_2 = Language.EN;

	/** Subtitles */
	public static final List<Language> SUBTITLES = CollectionUtils.newList(SUBTITLES_1, SUBTITLES_2);

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

	/** Secondary inner ID */
	public static final int SECONDARY_INNER_ID = 12;

	/** Inner count */
	public static final int INNER_COUNT = 5;

	/** Inner inner count */
	public static final int INNER_INNER_COUNT = 10;

	/** Total length */
	public static final Time TOTAL_LENGTH = new Time(500);

	/** Bad minimal year */
	public static final int BAD_MIN_YEAR = TC.BAD_MIN_YEAR;

	/** Bad maximal year */
	public static final int BAD_MAX_YEAR = TC.BAD_MAX_YEAR;

	/** Bad subtitles */
	public static final List<Language> BAD_SUBTITLES = CollectionUtils.newList(Language.CZ, null);

	/** Bad languages */
	public static final List<Language> BAD_LANGUAGES = CollectionUtils.newList(Language.CZ, null);

	/** Negative time */
	public static final Time NEGATIVE_TIME = TC.NEGATIVE_TIME;

	/** Creates a new instance of TestConstants. */
	private TestConstants() {
	}

}
