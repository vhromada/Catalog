package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Book;
import cz.vhromada.catalog.facade.to.BookTO;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from TO for book to entity book.
 *
 * @author Vladimir Hromada
 */
@Component("bookTOToBookConverter")
public class BookTOToBookConverter implements Converter<BookTO, Book> {

	/** Converter from TO for book category to entity book category */
	@Autowired
	private BookCategoryTOToBookCategoryConverter converter;

	/**
	 * Returns converter from TO for book category to entity book category.
	 *
	 * @return converter from TO for book category to entity book category
	 */
	public BookCategoryTOToBookCategoryConverter getConverter() {
		return converter;
	}

	/**
	 * Sets a new value to converter from TO for book category to entity book category.
	 *
	 * @param converter new value
	 */
	public void setConverter(final BookCategoryTOToBookCategoryConverter converter) {
		this.converter = converter;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws IllegalStateException    if converter from TO for book category to entity book category isn't set
	 */
	@Override
	public Book convert(final BookTO source) {
		Validators.validateFieldNotNull(converter, "Converter");

		if (source == null) {
			return null;
		}

		final Book book = new Book();
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
