package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.EntityGenerator;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link BookCategoryToBookCategoryTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class BookCategoryToBookCategoryTOConverterTest {

	/** Instance of {@link BookCategoryToBookCategoryTOConverter} */
	private BookCategoryToBookCategoryTOConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new BookCategoryToBookCategoryTOConverter();
	}

	/** Test method for {@link BookCategoryToBookCategoryTOConverter#convert(BookCategory)}. */
	@Test
	public void testConvert() {
		final BookCategory bookCategory = EntityGenerator.createBookCategory(ID);
		final BookCategoryTO bookCategoryTO = converter.convert(bookCategory);
		DeepAsserts.assertNotNull(bookCategoryTO);
		DeepAsserts.assertEquals(bookCategory, bookCategoryTO, "booksCount");
	}

	/** Test method for {@link BookCategoryToBookCategoryTOConverter#convert(BookCategory)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
