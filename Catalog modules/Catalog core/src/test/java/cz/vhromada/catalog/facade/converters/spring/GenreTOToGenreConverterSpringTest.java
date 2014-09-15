package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.converters.GenreTOToGenreConverter;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link GenreTOToGenreConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class GenreTOToGenreConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link GenreTOToGenreConverter#convert(GenreTO)}. */
	@Test
	public void testConvert() {
		final GenreTO genreTO = ToGenerator.createGenre(ID);
		final Genre genre = conversionService.convert(genreTO, Genre.class);
		DeepAsserts.assertNotNull(genre);
		DeepAsserts.assertEquals(genreTO, genre);
	}

	/** Test method for {@link GenreTOToGenreConverter#convert(GenreTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Genre.class));
	}

}
