package cz.vhromada.catalog.facade.to;

import java.io.Serializable;

/**
 * A class represents TO for genre.
 *
 * @author Vladimir Hromada
 */
public class GenreTO implements Comparable<GenreTO>, Serializable {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** Name */
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
        if (obj == null || !(obj instanceof GenreTO) || id == null) {
            return false;
        }
        final GenreTO genre = (GenreTO) obj;
        return id.equals(genre.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("GenreTO [id=%d, name=%s]", id, name);
    }

    @Override
    public int compareTo(final GenreTO o) {
        final int result = name.compareTo(o.name);
        return result == 0 ? id - o.id : result;
    }

}
