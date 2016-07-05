package cz.vhromada.catalog.entities;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import cz.vhromada.catalog.commons.Movable;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * A class represents music.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "music")
public class Music implements Movable {

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
     * Songs
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "music", referencedColumnName = "id")
    @OrderBy("position, id")
    @Fetch(FetchMode.SELECT)
    private List<Song> songs;

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

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(final int position) {
        this.position = position;
    }

    /**
     * Returns songs.
     *
     * @return songs
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * Sets a new value to songs.
     *
     * @param songs new value
     */
    public void setSongs(final List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || !(obj instanceof Music) || id == null) {
            return false;
        }

        return id.equals(((Music) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Music [id=%d, name=%s, wikiEn=%s, wikiCz=%s, mediaCount=%d, note=%s, position=%d, songs=%s]", id, name, wikiEn, wikiCz,
                mediaCount, note, position, songs);
    }

}
