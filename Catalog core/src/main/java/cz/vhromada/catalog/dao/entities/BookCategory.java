package cz.vhromada.catalog.dao.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A class represents book category.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "book_categories")
@NamedQuery(name = BookCategory.SELECT_BOOK_CATEGORIES, query = "SELECT bc FROM BookCategory bc ORDER BY bc.position, bc.id")
public class BookCategory implements Serializable {

    /**
     * Name for query - select book categories
     */
    public static final String SELECT_BOOK_CATEGORIES = "BookCategory.selectBookCategories";

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "book_category_generator", sequenceName = "book_categories_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_category_generator")
    private Integer id;

    /**
     * Name
     */
    @Column(name = "book_category_name")
    private String name;

    /**
     * Note
     */
    private String note;

    /**
     * Position
     */
    private int position;

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
     * Returns name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new value to name.
     *
     * @param name new value
     */
    public void setName(final String name) {
        this.name = name;
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof BookCategory) || id == null) {
            return false;
        }
        final BookCategory bookCategory = (BookCategory) obj;
        return id.equals(bookCategory.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("BookCategory [id=%d, name=%s, note=%s, position=%d]", id, name, note, position);
    }

}
