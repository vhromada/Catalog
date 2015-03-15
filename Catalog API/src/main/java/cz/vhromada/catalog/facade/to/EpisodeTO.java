package cz.vhromada.catalog.facade.to;

import java.io.Serializable;

/**
 * A class represents TO for episode.
 *
 * @author Vladimir Hromada
 */
public class EpisodeTO implements Comparable<EpisodeTO>, Serializable {

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

    /**
     * TO for season
     */
    private SeasonTO season;

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
     * Returns TO for season.
     *
     * @return TO for season
     */
    public SeasonTO getSeason() {
        return season;
    }

    /**
     * Sets a new value to TO for season.
     *
     * @param season new value
     */
    public void setSeason(final SeasonTO season) {
        this.season = season;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof EpisodeTO) || id == null) {
            return false;
        }
        final EpisodeTO episode = (EpisodeTO) obj;
        return id.equals(episode.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("EpisodeTO [id=%d, number=%d, name=%s, length=%d, note=%s, position=%d, season=%s]", id, number, name, length, note, position,
                season);
    }

    @Override
    public int compareTo(final EpisodeTO o) {
        final int result = position - o.position;
        return result == 0 ? id - o.id : result;
    }

}
