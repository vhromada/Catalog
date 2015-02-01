package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
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
public class BookTOToBookConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link BookTOToBookConverter} */
    private BookTOToBookConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new BookTOToBookConverter(new BookCategoryTOToBookCategoryConverter());
    }

    /**
     * Test method for {@link BookTOToBookConverter#BookTOToBookConverter(BookCategoryTOToBookCategoryConverter)} with null converter from TO for book category
     * to entity book category.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBookCategoryTOToBookCategoryConverter() {
        new BookTOToBookConverter(null);
    }

    /** Test method for {@link BookTOToBookConverter#convert(BookTO)}. */
    @Test
    public void testConvert() {
        final BookTO bookTO = generate(BookTO.class);
        final Book book = converter.convert(bookTO);
        DeepAsserts.assertNotNull(book);
        DeepAsserts.assertEquals(bookTO, book, "booksCount");
    }

    /** Test method for {@link BookTOToBookConverter#convert(BookTO)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

}
