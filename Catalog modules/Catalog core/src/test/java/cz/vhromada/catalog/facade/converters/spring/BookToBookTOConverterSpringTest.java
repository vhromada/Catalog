package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.facade.converters.BookToBookTOConverter;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link BookToBookTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class BookToBookTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link BookToBookTOConverter#convert(Book)}. */
	@Test
	public void testConvert() {
		final Book book = EntityGenerator.createBook(ID, EntityGenerator.createBookCategory(INNER_ID));
		final BookTO bookTO = conversionService.convert(book, BookTO.class);
		DeepAsserts.assertNotNull(bookTO);
		DeepAsserts.assertEquals(book, bookTO, "booksCount");
	}

	/** Test method for {@link BookToBookTOConverter#convert(Book)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, BookTO.class));
	}

}
