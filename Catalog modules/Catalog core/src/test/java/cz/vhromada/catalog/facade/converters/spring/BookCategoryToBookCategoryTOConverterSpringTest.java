package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.converters.BookCategoryToBookCategoryTOConverter;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link BookCategoryToBookCategoryTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class BookCategoryToBookCategoryTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link BookCategoryToBookCategoryTOConverter#convert(BookCategory)}. */
	@Test
	public void testConvert() {
		final BookCategory bookCategory = objectGenerator.generate(BookCategory.class);
		final BookCategoryTO bookCategoryTO = conversionService.convert(bookCategory, BookCategoryTO.class);
		DeepAsserts.assertNotNull(bookCategoryTO);
		DeepAsserts.assertEquals(bookCategory, bookCategoryTO, "booksCount");
	}

	/** Test method for {@link BookCategoryToBookCategoryTOConverter#convert(BookCategory)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, BookCategoryTO.class));
	}

}
