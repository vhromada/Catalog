package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for book category to entity book category.
 *
 * @author Vladimir Hromada
 */
@Component("bookCategoryTOToBookCategoryConverter")
public class BookCategoryTOToBookCategoryConverter implements Converter<BookCategoryTO, BookCategory> {

	@Override
	public BookCategory convert(final BookCategoryTO source) {
		if (source == null) {
			return null;
		}

		final BookCategory bookCategory = new BookCategory();
		bookCategory.setId(source.getId());
		bookCategory.setName(source.getName());
		bookCategory.setNote(source.getNote());
		bookCategory.setPosition(source.getPosition());
		return bookCategory;
	}

}
