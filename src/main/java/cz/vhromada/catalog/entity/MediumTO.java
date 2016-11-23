package cz.vhromada.catalog.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class represents TO for medium.
 *
 * @author Vladimir Hromada
 */
public class MediumTO implements Serializable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * Number
     */
    private int number;

    /**
     * Length
     */
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
     * Sets a new value to ID.
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

        if (obj == null || !(obj instanceof MediumTO) || id == null) {
            return false;
        }

        return id.equals(((MediumTO) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("MediumTO [id=%d, number=%d, length=%d]", id, number, length);
    }

}
