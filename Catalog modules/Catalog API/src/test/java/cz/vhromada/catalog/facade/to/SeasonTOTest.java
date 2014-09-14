package cz.vhromada.catalog.facade.to;

import static cz.vhromada.catalog.common.TestConstants.END_YEAR;
import static cz.vhromada.catalog.common.TestConstants.START_YEAR;
import static cz.vhromada.catalog.common.TestConstants.SUBTITLES_1;
import static cz.vhromada.catalog.common.TestConstants.SUBTITLES_2;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link SeasonTO}.
 *
 * @author Vladimir Hromada
 */
public class SeasonTOTest {

	/** Instance of {@link SeasonTO} */
	private SeasonTO season;

	/** Initializes episodes, TO for season. */
	@Before
	public void setUp() {
		season = new SeasonTO();
	}

	/** Test method for {@link SeasonTO#getYear()}. */
	@Test
	public void testGetYear() {
		season.setStartYear(START_YEAR);
		season.setEndYear(END_YEAR);
		DeepAsserts.assertEquals(START_YEAR + " - " + END_YEAR, season.getYear());

		season.setEndYear(START_YEAR);
		DeepAsserts.assertEquals(Integer.toString(START_YEAR), season.getYear());
	}

	/** Test method for {@link SeasonTO#getSubtitlesAsString()}. */
	@Test
	public void testSubtitlesAsString() {
		DeepAsserts.assertEquals("", season.getSubtitlesAsString());

		season.setSubtitles(CollectionUtils.newList(SUBTITLES_1));
		DeepAsserts.assertEquals(SUBTITLES_1.toString(), season.getSubtitlesAsString());

		season.setSubtitles(CollectionUtils.newList(SUBTITLES_1, SUBTITLES_2));
		DeepAsserts.assertEquals(SUBTITLES_1 + " / " + SUBTITLES_2, season.getSubtitlesAsString());

		season.setSubtitles(CollectionUtils.newList(Language.CZ, Language.EN, Language.FR));
		DeepAsserts.assertEquals(Language.CZ + " / " + Language.EN + " / " + Language.FR, season.getSubtitlesAsString());
	}

}
