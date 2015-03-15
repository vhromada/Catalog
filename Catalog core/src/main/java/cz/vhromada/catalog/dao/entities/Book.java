package cz.vhromada.catalog.dao.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import cz.vhromada.catalog.commons.Language;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * A class represents book.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "books")
@NamedQuery(name = Book.FIND_BY_BOOK_CATEGORY, query = "SELECT b FROM Book b WHERE b.bookCategory.id = :bookCategory ORDER BY b.position, b.id")
public class Book implements Serializable {

    /**
     * Name for query - find by book category
     */
    public static final String FIND_BY_BOOK_CATEGORY = "Book.findByBookCategory";

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "book_generator", sequenceName = "books_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book"))
    @Enumerated(EnumType.STRING)
    @Column(name = "book_language")
    @Fetch(FetchMode.SELECT)
    private List<Language> languages;

    /**
     * Category
     */
    private String category;

    /**
     * Note
     */
    private String note;

    /**
     * Position
     */
    private int position;

    /**
     * Book category
     */
    @ManyToOne
    @JoinColumn(name = "book_category", referencedColumnName = "id")
    private BookCategory bookCategory;

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
     * Returns category.
     *
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets a new value to category.
     *
     * @param category new value
     */
    public void setCategory(final String category) {
        this.category = category;
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
     * Returns book category.
     *
     * @return book category
     */
    public BookCategory getBookCategory() {
        return bookCategory;
    }

    /**
     * Sets a new value to book category.
     *
     * @param bookCategory new value
     */
    public void setBookCategory(final BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Book) || id == null) {
            return false;
        }
        final Book book = (Book) obj;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Book [id=%d, author=%s, title=%s, languages=%s, category=%s, note=%s, position=%d, bookCategory=%s]", id, author, title,
                languages, category, note, position, bookCategory);
    }

}
