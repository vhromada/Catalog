package cz.vhromada.catalog.facade.to;

import java.io.Serializable;
import java.util.List;

import cz.vhromada.catalog.commons.Language;

/**
 * A class represents TO for book.
 *
 * @author Vladimir Hromada
 */
public class BookTO implements Comparable<BookTO>, Serializable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * Author
     */
    private String author;

    /**
     * Title
     */
    private String title;

    /**
     * Languages
     */
    private List<Language> languages;

    /**
     * Note
     */
    private String note;

    /**
     * Position
     */
    private int position;

    /**
     * TO for book category
     */
    private BookCategoryTO bookCategory;

    /**
     * Returns ID.
     *
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets a new value to ID.
     *
     * @param id new value
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Returns author.
     *
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets a new value to author.
     *
     * @param author new value
     */
    public void setAuthor(final String author) {
        this.author = author;
    }

    /**
     * Returns title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets a new value to title.
     *
     * @param title new value
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns languages.
     *
     * @return languages
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * Sets a new value to languages.
     *
     * @param languages new value
     */
    public void setLanguages(final List<Language> languages) {
        this.languages = languages;
    }

    /**
     * Returns note.
     *
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets a new value to note.
     *
     * @param note new value
     */
    public void setNote(final String note) {
        this.note = note;
    }

    /**
     * Returns position.
     *
     * @return position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets a new value to position.
     *
     * @param position new value
     */
    public void setPosition(final int position) {
        this.position = position;
    }

    /**
     * Returns TO for book category.
     *
     * @return TO for book category
     */
    public BookCategoryTO getBookCategory() {
        return bookCategory;
    }

    /**
     * Sets a new value to TO for book category..
     *
     * @param bookCategory new value
     */
    public void setBookCategory(final BookCategoryTO bookCategory) {
        this.bookCategory = bookCategory;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof BookTO) || id == null) {
            return false;
        }
        final BookTO book = (BookTO) obj;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("BookTO [id=%d, author=%s, title=%s, languages=%s, note=%s, position=%d, bookCategory=%s]", id, author, title, languages, note,
                position, bookCategory);
    }

    @Override
    public int compareTo(final BookTO o) {
        final int result = position - o.position;
        return result == 0 ? id - o.id : result;
    }

}
