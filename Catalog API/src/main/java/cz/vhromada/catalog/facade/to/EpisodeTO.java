package cz.vhromada.catalog.facade.to;

import java.util.Objects;

import cz.vhromada.catalog.commons.Movable;

/**
 * A class represents TO for episode.
 *
 * @author Vladimir Hromada
 */
public class EpisodeTO implements Movable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * Number of episode
     */
    private int number;

    /**
     * Name
     */
    private String name;

    /**
     * Length
     */
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
     * Returns number of episode.
     *
     * @return number of episode
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets a new value to number of episode.
     *
     * @param number new value
     */
    public void setNumber(final int number) {
        this.number = number;
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
        if (obj == null || !(obj instanceof EpisodeTO) || id == null) {
            return false;
        }

        return id.equals(((EpisodeTO) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("EpisodeTO [id=%d, number=%d, name=%s, length=%d, note=%s, position=%d]", id, number, name, length, note, position);
    }

}
