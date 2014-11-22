package cz.vhromada.catalog.facade.to;

import java.io.Serializable;

/**
 * A class represents TO for song.
 *
 * @author Vladimir Hromada
 */
public class SongTO implements Comparable<SongTO>, Serializable {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** Name */
    private String name;

    /** Length */
    private int length;

    /** Note */
    private String note;

    /** Position */
    private int position;

    /** TO for music */
    private MusicTO music;

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
     * Returns TO for music.
     *
     * @return TO for music
     */
    public MusicTO getMusic() {
        return music;
    }

    /**
     * Sets a new value to TO for music
     *
     * @param music new value
     */
    public void setMusic(final MusicTO music) {
        this.music = music;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof SongTO) || id == null) {
            return false;
        }
        final SongTO song = (SongTO) obj;
        return id.equals(song.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("SongTO [id=%d, name=%s, length=%d, note=%s, position=%d, music=%s]", id, name, length, note, position, music);
    }

    @Override
    public int compareTo(final SongTO o) {
        final int result = position - o.position;
        return result == 0 ? id - o.id : result;
    }

}
