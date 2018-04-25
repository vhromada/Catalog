package cz.vhromada.catalog.entity;

import java.util.Arrays;
import java.util.Objects;

import cz.vhromada.common.Movable;

/**
 * A class represents picture.
 *
 * @author Vladimir Hromada
 */
public class Picture implements Movable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * Picture
     */
    private byte[] content;

    /**
     * Position
     */
    private Integer position;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Returns content.
     *
     * @return content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Sets a new value to content.
     *
     * @param content new value
     */
    public void setContent(final byte[] content) {
        this.content = content;
    }

    @Override
    public Integer getPosition() {
        return position;
    }

    @Override
    public void setPosition(final Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Picture) || id == null) {
            return false;
        }

        return id.equals(((Picture) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Picture [id=%d, content=%s, position=%d]", id, Arrays.toString(content), position);
    }

}
