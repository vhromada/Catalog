package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link BookCategoryTOToBookCategoryConverter}.
 *
 * @author Vladimir Hromada
 */
public class BookCategoryTOToBookCategoryConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link BookCategoryTOToBookCategoryConverter} */
    private BookCategoryTOToBookCategoryConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new BookCategoryTOToBookCategoryConverter();
    }

    /** Test method for {@link BookCategoryTOToBookCategoryConverter#convert(BookCategoryTO)}. */
    @Test
    public void testConvert() {
        final BookCategoryTO bookCategoryTO = generate(BookCategoryTO.class);
        final BookCategory bookCategory = converter.convert(bookCategoryTO);
        DeepAsserts.assertNotNull(bookCategory);
        DeepAsserts.assertEquals(bookCategoryTO, bookCategory, "booksCount");
    }

    /** Test method for {@link BookCategoryTOToBookCategoryConverter#convert(BookCategoryTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

}
