package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static cz.vhromada.catalog.commons.TestConstants.INNER_ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link BookTOToBookConverter}.
 *
 * @author Vladimir Hromada
 */
public class BookTOToBookConverterTest {

	/** Instance of {@link BookTOToBookConverter} */
	private BookTOToBookConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new BookTOToBookConverter();
		converter.setConverter(new BookCategoryTOToBookCategoryConverter());
	}

	/** Test method for {@link BookTOToBookConverter#getConverter()} and {@link BookTOToBookConverter#setConverter(BookCategoryTOToBookCategoryConverter)}. */
	@Test
	public void testConverter() {
		final BookCategoryTOToBookCategoryConverter bookCategoryConverter = new BookCategoryTOToBookCategoryConverter();
		converter.setConverter(bookCategoryConverter);
		DeepAsserts.assertEquals(bookCategoryConverter, converter.getConverter());
	}

	/** Test method for {@link BookTOToBookConverter#convert(BookTO)}. */
	@Test
	public void testConvert() {
		final BookTO bookTO = ToGenerator.createBook(ID, ToGenerator.createBookCategory(INNER_ID));
		final Book book = converter.convert(bookTO);
		DeepAsserts.assertNotNull(book);
		DeepAsserts.assertEquals(bookTO, book, "booksCount");
	}

	/** Test method for {@link BookTOToBookConverter#convert(BookTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

	/** Test method for {@link BookTOToBookConverter#convert(BookTO)} with not set converter for book category. */
	@Test(expected = IllegalStateException.class)
	public void testConvertWithNotSetBookCategoryConverter() {
		converter.setConverter(null);
		converter.convert(new BookTO());
	}

}
