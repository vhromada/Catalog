package cz.vhromada.catalog.facade.to;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link SeasonTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testGeneratorContext.xml")
public class SeasonTOTest {

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

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
		final int startYear = objectGenerator.generate(Integer.class);
		final int endYear = objectGenerator.generate(Integer.class);

		season.setStartYear(startYear);
		season.setEndYear(endYear);
		DeepAsserts.assertEquals(startYear + " - " + endYear, season.getYear());

		season.setEndYear(startYear);
		DeepAsserts.assertEquals(Integer.toString(startYear), season.getYear());
	}

	/** Test method for {@link SeasonTO#getSubtitlesAsString()}. */
	@Test
	public void testSubtitlesAsString() {
		final Language subtitles1 = objectGenerator.generate(Language.class);
		final Language subtitles2 = objectGenerator.generate(Language.class);
		final Language subtitles3 = objectGenerator.generate(Language.class);

		DeepAsserts.assertEquals("", season.getSubtitlesAsString());

		season.setSubtitles(CollectionUtils.newList(subtitles1));
		DeepAsserts.assertEquals(subtitles1.toString(), season.getSubtitlesAsString());

		season.setSubtitles(CollectionUtils.newList(subtitles1, subtitles2));
		DeepAsserts.assertEquals(subtitles1 + " / " + subtitles2, season.getSubtitlesAsString());

		season.setSubtitles(CollectionUtils.newList(subtitles1, subtitles2, subtitles3));
		DeepAsserts.assertEquals(subtitles1 + " / " + subtitles2 + " / " + subtitles3, season.getSubtitlesAsString());
	}

}
