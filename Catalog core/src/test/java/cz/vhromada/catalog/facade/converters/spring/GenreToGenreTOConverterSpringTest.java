package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.converters.GenreToGenreTOConverter;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link GenreToGenreTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class GenreToGenreTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link GenreToGenreTOConverter#convert(Genre)}. */
	@Test
	public void testConvert() {
		final Genre genre = objectGenerator.generate(Genre.class);
		final GenreTO genreTO = conversionService.convert(genre, GenreTO.class);
		DeepAsserts.assertNotNull(genreTO);
		DeepAsserts.assertEquals(genre, genreTO);
	}

	/** Test method for {@link GenreToGenreTOConverter#convert(Genre)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, GenreTO.class));
	}

}
