package cz.vhromada.catalog.dao.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A class represents episode.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "episodes")
@NamedQuery(name = Episode.FIND_BY_SEASON, query = "SELECT e FROM Episode e WHERE e.season.id = :season ORDER BY e.position, e.id")
public class Episode implements Serializable {

    /** Name for query - select count of episodes */
    /**
     * Name for query - find by season
     */
    public static final String FIND_BY_SEASON = "Episode.findBySeason";

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "episode_generator", sequenceName = "episodes_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "episode_generator")
    private Integer id;

    /**
     * Number of episode
     */
    @Column(name = "episode_number")
    private int number;

    /**
     * Name
     */
    @Column(name = "episode_name")
    private String name;

    /**
     * Length
     */
    @Column(name = "episode_length")
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
     * Season
     */
    @ManyToOne
    @JoinColumn(name = "season", referencedColumnName = "id")
    private Season season;

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
     * Returns season.
     *
     * @return season
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Sets a new value to season.
     *
     * @param season new value
     */
    public void setSeason(final Season season) {
        this.season = season;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Episode) || id == null) {
            return false;
        }
        final Episode episode = (Episode) obj;
        return id.equals(episode.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Episode [id=%d, number=%d, name=%s, length=%d, note=%s, position=%d, season=%s]", id, number, name, length, note, position,
                season);
    }

}
