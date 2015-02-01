package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity book to TO for book.
 *
 * @author Vladimir Hromada
 */
@Component("bookToBookTOConverter")
public class BookToBookTOConverter implements Converter<Book, BookTO> {

    /** Converter from entity book category to TO for book category */
    private BookCategoryToBookCategoryTOConverter converter;

    /**
     * Creates a new instance of BookToBookTOConverter.
     *
     * @param converter converter from entity book category to TO for book category
     * @throws IllegalArgumentException if converter from entity book category to TO for book category is null
     */
    @Autowired
    public BookToBookTOConverter(final BookCategoryToBookCategoryTOConverter converter) {
        Validators.validateArgumentNotNull(converter, "Converter");

        this.converter = converter;
    }

    @Override
    public BookTO convert(final Book source) {
        if (source == null) {
            return null;
        }

        final BookTO book = new BookTO();
        book.setId(source.getId());
        book.setAuthor(source.getAuthor());
        book.setTitle(source.getTitle());
        book.setLanguages(source.getLanguages());
        book.setCategory(source.getCategory());
        book.setNote(source.getNote());
        book.setPosition(source.getPosition());
        book.setBookCategory(converter.convert(source.getBookCategory()));
        return book;
    }

}
