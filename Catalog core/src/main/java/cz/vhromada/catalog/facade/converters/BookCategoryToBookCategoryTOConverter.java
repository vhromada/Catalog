package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.BookCategory;
import cz.vhromada.catalog.facade.to.BookCategoryTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity book category to TO for book category.
 *
 * @author Vladimir Hromada
 */
@Component("bookCategoryToBookCategoryTOConverter")
public class BookCategoryToBookCategoryTOConverter implements Converter<BookCategory, BookCategoryTO> {

    @Override
    public BookCategoryTO convert(final BookCategory source) {
        if (source == null) {
            return null;
        }

        final BookCategoryTO bookCategory = new BookCategoryTO();
        bookCategory.setId(source.getId());
        bookCategory.setName(source.getName());
        bookCategory.setNote(source.getNote());
        bookCategory.setPosition(source.getPosition());
        return bookCategory;
    }

}
