package cz.vhromada.catalog.domain;

import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import cz.vhromada.catalog.common.Movable;

/**
 * A class represents picture.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "pictures")
public class Picture implements Movable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "picture_generator", sequenceName = "pictures_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "picture_generator")
    private Integer id;

    /**
     * Picture
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    /**
     * Position
     */
    private int position;

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
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(final int position) {
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
