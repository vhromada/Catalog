package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link BookToBookTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class BookToBookTOConverterTest extends ObjectGeneratorTest {

    /** Instance of {@link BookToBookTOConverter} */
    private BookToBookTOConverter converter;

    /** Initializes converter. */
    @Before
    public void setUp() {
        converter = new BookToBookTOConverter();
        converter.setConverter(new BookCategoryToBookCategoryTOConverter());
    }

    /** Test method for {@link BookToBookTOConverter#convert(Book)}. */
    @Test
    public void testConvert() {
        final Book book = generate(Book.class);
        final BookTO bookTO = converter.convert(book);
        DeepAsserts.assertNotNull(bookTO);
        DeepAsserts.assertEquals(book, bookTO, "booksCount");
    }

    /** Test method for {@link BookToBookTOConverter#convert(Book)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null));
    }

    /** Test method for {@link BookToBookTOConverter#convert(Book)} with not set converter for book category. */
    @Test(expected = IllegalStateException.class)
    public void testConvertWithNotSetBookCategoryConverter() {
        converter.setConverter(null);
        converter.convert(new Book());
    }

}
