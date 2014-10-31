package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.converters.MovieToMovieTOConverter;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link MovieToMovieTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class MovieToMovieTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link MovieToMovieTOConverter#convert(Movie)}. */
	@Test
	public void testConvert() {
		final Movie movie = objectGenerator.generate(Movie.class);
		final MovieTO movieTO = conversionService.convert(movie, MovieTO.class);
		DeepAsserts.assertNotNull(movieTO);
		DeepAsserts.assertEquals(movie, movieTO, "subtitlesAsString", "media", "totalLength", "genresAsString");
		DeepAsserts.assertEquals(movie.getMedia().size(), movieTO.getMedia().size());
		for (int i = 0; i < movie.getMedia().size(); i++) {
			DeepAsserts.assertEquals(movie.getMedia().get(i).getLength(), movieTO.getMedia().get(i));
		}
	}

	/** Test method for {@link MovieToMovieTOConverter#convert(Movie)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, MovieTO.class));
	}

}
