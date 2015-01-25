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
 * A class represents genre.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "genres")
@NamedQuery(name = Genre.SELECT_GENRES, query = "SELECT g FROM Genre g ORDER BY g.id")
public class Genre implements Serializable {

    /** Name for query - select genres */
    public static final String SELECT_GENRES = "Genre.selectGenres";

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @SequenceGenerator(name = "genre_generator", sequenceName = "genres_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_generator")
    private Integer id;

    /** Name */
    @Column(name = "genre_name")
    private String name;

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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Genre) || id == null) {
            return false;
        }
        final Genre genre = (Genre) obj;
        return id.equals(genre.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Genre [id=%d, name=%s]", id, name);
    }

}
