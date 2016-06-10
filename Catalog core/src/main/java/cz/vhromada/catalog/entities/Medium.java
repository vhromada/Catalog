package cz.vhromada.catalog.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A class represents medium.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "media")
public class Medium implements Serializable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Movie's ID
     */
    @Id
    @SequenceGenerator(name = "medium_generator", sequenceName = "media_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medium_generator")
    private Integer id;

    /**
     * Medium
     */
    @Column(name = "medium_number")
    private int number;

    /**
     * Length
     */
    @Column(name = "medium_length")
    private int length;

    /**
     * Returns ID.
     *
     * @return ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets a new value to movie's ID.
     *
     * @param id new value
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Returns number.
     *
     * @return number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets a new value to number.
     *
     * @param number new value
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * Returns length.
     *
     * @return length
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets a new value to length.
     *
     * @param length new value
     */
    public void setLength(final int length) {
        this.length = length;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Medium) || id == null) {
            return false;
        }

        return id.equals(((Medium) obj).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Medium [id=%d, number=%d, length=%d]", id, number, length);
    }

}
