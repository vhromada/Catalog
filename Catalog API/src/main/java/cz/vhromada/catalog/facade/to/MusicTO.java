package cz.vhromada.catalog.facade.to;

import java.io.Serializable;

import cz.vhromada.catalog.commons.Time;

/**
 * A class represents TO for music.
 *
 * @author Vladimir Hromada
 */
public class MusicTO implements Comparable<MusicTO>, Serializable {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** Name */
    private String name;

    /** URL to english Wikipedia page about music */
    private String wikiEn;

    /** URL to czech Wikipedia page about music */
    private String wikiCz;

    /** Count of media */
    private int mediaCount;

    /** Count of songs */
    private int songsCount;

    /** Total length of songs */
    private Time totalLength;

    /** Note */
    private String note;

    /** Position */
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
     * Returns count of songs.
     *
     * @return count of songs
     */
    public int getSongsCount() {
        return songsCount;
    }

    /**
     * Sets a new value to count of songs.
     *
     * @param songsCount new value
     */
    public void setSongsCount(final int songsCount) {
        this.songsCount = songsCount;
    }

    /**
     * Returns total length of songs.
     *
     * @return total length of songs
     */
    public Time getTotalLength() {
        return totalLength;
    }

    /**
     * Sets a new value to total length of songs.
     *
     * @param totalLength new value
     */
    public void setTotalLength(final Time totalLength) {
        this.totalLength = totalLength;
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
        if (obj == null || !(obj instanceof MusicTO) || id == null) {
            return false;
        }
        final MusicTO music = (MusicTO) obj;
        return id.equals(music.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("MusicTO [id=%d, name=%s, wikiEn=%s, wikiCz=%s, mediaCount=%d, songsCount=%d, totalLength=%s, note=%s, position=%d]", id, name,
                wikiEn, wikiCz, mediaCount, songsCount, totalLength, note, position);
    }

    @Override
    public int compareTo(final MusicTO o) {
        final int result = position - o.position;
        return result == 0 ? id - o.id : result;
    }

}
