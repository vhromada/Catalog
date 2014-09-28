package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.facade.converters.BookTOToBookConverter;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link BookTOToBookConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class BookTOToBookConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link BookTOToBookConverter#convert(BookTO)}. */
	@Test
	public void testConvert() {
		final BookTO bookTO = ToGenerator.createBook(ID, ToGenerator.createBookCategory(INNER_ID));
		final Book book = conversionService.convert(bookTO, Book.class);
		DeepAsserts.assertNotNull(book);
		DeepAsserts.assertEquals(bookTO, book, "booksCount");
	}

	/** Test method for {@link BookTOToBookConverter#convert(BookTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Book.class));
	}

}
