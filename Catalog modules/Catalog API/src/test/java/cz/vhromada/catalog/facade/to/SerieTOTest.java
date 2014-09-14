package cz.vhromada.catalog.facade.to;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;

/**
 * A class represents test for class {@link SerieTO}.
 *
 * @author Vladimir Hromada
 */
public class SerieTOTest {

	/** Test method for {@link SerieTO#getGenresAsString()}. */
	@Test
	public void testGenresAsString() {
		final SerieTO serie = new SerieTO();
		DeepAsserts.assertEquals("", serie.getGenresAsString());

		final GenreTO genre1 = createGenreTO("Genre1");
		serie.setGenres(CollectionUtils.newList(genre1));
		DeepAsserts.assertEquals(genre1.getName(), serie.getGenresAsString());

		final GenreTO genre2 = createGenreTO("Genre2");
		serie.setGenres(CollectionUtils.newList(genre1, genre2));
		DeepAsserts.assertEquals(genre1.getName() + ", " + genre2.getName(), serie.getGenresAsString());

		final GenreTO genre3 = createGenreTO("Genre3");
		serie.setGenres(CollectionUtils.newList(genre1, genre2, genre3));
		DeepAsserts.assertEquals(genre1.getName() + ", " + genre2.getName() + ", " + genre3.getName(), serie.getGenresAsString());
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
