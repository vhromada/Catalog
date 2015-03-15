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
 * A class represents music.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "music")
@NamedQuery(name = Music.SELECT_MUSIC, query = "SELECT m FROM Music m ORDER BY m.position, m.id")
public class Music implements Serializable {

    /**
     * Name for query - select music
     */
    public static final String SELECT_MUSIC = "Music.selectMusic";

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "music_generator", sequenceName = "music_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_generator")
    private Integer id;

    /**
     * Name
     */
    @Column(name = "music_name")
    private String name;

    /**
     * URL to english Wikipedia page about music
     */
    @Column(name = "wiki_en")
    private String wikiEn;

    /**
     * URL to czech Wikipedia page about music
     */
    @Column(name = "wiki_cz")
    private String wikiCz;

    /**
     * Count of media
     */
    @Column(name = "media_count")
    private int mediaCount;

    /**
     * Note
     */
    private String note;

    /**
     * Position
     */
    private int position;

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

    /**
     * Returns URL to english Wikipedia page about music.
     *
     * @return URL to english Wikipedia page about music
     */
    public String getWikiEn() {
        return wikiEn;
    }

    /**
     * Sets a new value to URL to english Wikipedia page about music.
     *
     * @param wikiEn new value
     */
    public void setWikiEn(final String wikiEn) {
        this.wikiEn = wikiEn;
    }

    /**
     * Returns URL to czech Wikipedia page about music.
     *
     * @return URL to czech Wikipedia page about music
     */
    public String getWikiCz() {
        return wikiCz;
    }

    /**
     * Sets a new value to URL to czech Wikipedia page about music.
     *
     * @param wikiCz new value
     */
    public void setWikiCz(final String wikiCz) {
        this.wikiCz = wikiCz;
    }

    /**
     * Returns count of media.
     *
     * @return count of media
     */
    public int getMediaCount() {
        return mediaCount;
    }

    /**
     * Sets a new value to count of media.
     *
     * @param mediaCount new value
     */
    public void setMediaCount(final int mediaCount) {
        this.mediaCount = mediaCount;
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Music) || id == null) {
            return false;
        }
        final Music music = (Music) obj;
        return id.equals(music.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Music [id=%d, name=%s, wikiEn=%s, wikiCz=%s, mediaCount=%d, note=%s, position=%d]", id, name, wikiEn, wikiCz, mediaCount, note,
                position);
    }

}
