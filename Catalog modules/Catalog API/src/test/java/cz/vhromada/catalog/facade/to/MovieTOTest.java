package cz.vhromada.catalog.facade.to;

import static cz.vhromada.catalog.common.TestConstants.MEDIUM_1;
import static cz.vhromada.catalog.common.TestConstants.MEDIUM_2;
import static cz.vhromada.catalog.common.TestConstants.SUBTITLES_1;
import static cz.vhromada.catalog.common.TestConstants.SUBTITLES_2;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MovieTO}.
 *
 * @author Vladimir Hromada
 */
public class MovieTOTest {

	/** Instance of {@link MovieTO} */
	private MovieTO movie;

	/** Initializes TO for movie. */
	@Before
	public void setUp() {
		movie = new MovieTO();
	}

	/** Test method for {@link MovieTO#getSubtitlesAsString()}. */
	@Test
	public void testSubtitlesAsString() {
		DeepAsserts.assertEquals("", movie.getSubtitlesAsString());

		movie.setSubtitles(CollectionUtils.newList(SUBTITLES_1));
		DeepAsserts.assertEquals(SUBTITLES_1.toString(), movie.getSubtitlesAsString());

		movie.setSubtitles(CollectionUtils.newList(SUBTITLES_1, SUBTITLES_2));
		DeepAsserts.assertEquals(SUBTITLES_1 + " / " + SUBTITLES_2, movie.getSubtitlesAsString());

		movie.setSubtitles(CollectionUtils.newList(Language.CZ, Language.EN, Language.FR));
		DeepAsserts.assertEquals(Language.CZ + " / " + Language.EN + " / " + Language.FR, movie.getSubtitlesAsString());
	}

	/** Test method for {@link MovieTO#getTotalLength()}. */
	@Test
	public void testGetTotalLength() {
		DeepAsserts.assertEquals(0, movie.getTotalLength());

		movie.setMedia(CollectionUtils.newList(MEDIUM_1));
		DeepAsserts.assertEquals(MEDIUM_1, movie.getTotalLength());

		movie.setMedia(CollectionUtils.newList(MEDIUM_1, MEDIUM_2));
		DeepAsserts.assertEquals(MEDIUM_1 + MEDIUM_2, movie.getTotalLength());
	}

	/** Test method for {@link MovieTO#getGenresAsString()}. */
	@Test
	public void testGenresAsString() {
		DeepAsserts.assertEquals("", movie.getGenresAsString());

		final GenreTO genre1 = createGenreTO("Genre1");
		movie.setGenres(CollectionUtils.newList(genre1));
		DeepAsserts.assertEquals(genre1.getName(), movie.getGenresAsString());

		final GenreTO genre2 = createGenreTO("Genre2");
		movie.setGenres(CollectionUtils.newList(genre1, genre2));
		DeepAsserts.assertEquals(genre1.getName() + ", " + genre2.getName(), movie.getGenresAsString());

		final GenreTO genre3 = createGenreTO("Genre3");
		movie.setGenres(CollectionUtils.newList(genre1, genre2, genre3));
		DeepAsserts.assertEquals(genre1.getName() + ", " + genre2.getName() + ", " + genre3.getName(), movie.getGenresAsString());
	}

	/**
	 * Returns new TO for genre.
	 *
	 * @param name name
	 * @return new TO for genre
	 */
	private GenreTO createGenreTO(String name) {
		final GenreTO genre = new GenreTO();
		genre.setName(name);

		return genre;
	}

}
