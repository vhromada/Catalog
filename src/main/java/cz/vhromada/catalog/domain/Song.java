package cz.vhromada.catalog.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import cz.vhromada.catalog.common.Movable;

/**
 * A class represents song.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "songs")
public class Song implements Movable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "song_generator", sequenceName = "songs_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "song_generator")
    private Integer id;

    /**
     * Name
     */
    @Column(name = "song_name")
    private String name;

    /**
     * Length
     */
    @Column(name = "song_length")
    private int length;

    /**
     * Note
     */
    private String note;

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

        if (obj == null || !(obj instanceof Song) || id == null) {
            return false;
        }

        return id.equals(((Song) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Song [id=%d, name=%s, length=%d, note=%s, position=%d]", id, name, length, note, position);
    }

}
