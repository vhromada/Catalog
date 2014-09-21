package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.converters.MovieTOToMovieConverter;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link MovieTOToMovieConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class MovieTOToMovieConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link MovieTOToMovieConverter#convert(MovieTO)}. */
	@Test
	public void testConvert() {
		final MovieTO movieTO = ToGenerator.createMovie(ID);
		final Movie movie = conversionService.convert(movieTO, Movie.class);
		DeepAsserts.assertNotNull(movie);
		DeepAsserts.assertEquals(movieTO, movie, "subtitlesAsString", "media", "totalLength", "genresAsString");
		DeepAsserts.assertEquals(movieTO.getMedia().size(), movie.getMedia().size());
		for (int i = 0; i < movieTO.getMedia().size(); i++) {
			DeepAsserts.assertEquals(movieTO.getMedia().get(i), movie.getMedia().get(i).getLength());
		}
	}

	/** Test method for {@link MovieTOToMovieConverter#convert(MovieTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Movie.class));
	}

}