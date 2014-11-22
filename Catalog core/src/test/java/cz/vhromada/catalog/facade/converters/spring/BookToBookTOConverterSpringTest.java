package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.converters.BookToBookTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class BookToBookTOConverterSpringTest {

    /** Instance of {@link ConversionService} */
    @Autowired
    private ConversionService conversionService;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Test method for {@link cz.vhromada.catalog.facade.converters.BookToBookTOConverter#convert(Book)}. */
    @Test
    public void testConvert() {
        final Book book = objectGenerator.generate(Book.class);
        final BookTO bookTO = conversionService.convert(book, BookTO.class);
        DeepAsserts.assertNotNull(bookTO);
        DeepAsserts.assertEquals(book, bookTO, "booksCount");
    }

    /** Test method for {@link cz.vhromada.catalog.facade.converters.BookToBookTOConverter#convert(Book)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(conversionService.convert(null, BookTO.class));
    }

}
