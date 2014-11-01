package cz.vhromada.catalog.commons;

import java.util.List;

/**
 * A class represents constants for tests.
 *
 * @author Vladimir Hromada
 */
@Deprecated
public final class TestConstants {

	/** Language */
	public static final Language LANGUAGE = Language.FR;

	/** Subtitles */
	public static final List<Language> SUBTITLES = CollectionUtils.newList(Language.CZ, Language.EN);

	/** IMDB code */
	public static final int IMDB = 653;

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

	/** Primary ID */
	public static final int PRIMARY_ID = 1;

	/** Inner ID */
	public static final int INNER_ID = 11;

	/** Inner count */
	public static final int INNER_COUNT = 5;

	/** Total length */
	public static final Time TOTAL_LENGTH = new Time(500);

	/** Creates a new instance of TestConstants. */
	private TestConstants() {
	}

}
