package cz.vhromada.catalog.entity;

import java.util.List;
import java.util.Objects;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.Movable;

/**
 * A class represents season.
 *
 * @author Vladimir Hromada
 */
public class Season implements Movable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * Number of season
     */
    private int number;

    /**
     * Starting year
     */
    private int startYear;

    /**
     * Ending year
     */
    private int endYear;

    /**
     * Language
     */
    private Language language;

    /**
     * Subtitles
     */
    private List<Language> subtitles;

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
     * Returns number of season.
     *
     * @return number of season
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets a new value to number of season.
     *
     * @param number new value
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * Returns starting year.
     *
     * @return starting year
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * Sets a new value to starting year.
     *
     * @param startYear new value
     */
    public void setStartYear(final int startYear) {
        this.startYear = startYear;
    }

    /**
     * Returns ending year.
     *
     * @return ending year
     */
    public int getEndYear() {
        return endYear;
    }

    /**
     * Sets a new value to ending year.
     *
     * @param endYear new value
     */
    public void setEndYear(final int endYear) {
        this.endYear = endYear;
    }

    /**
     * Returns language.
     *
     * @return language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets a new value to language.
     *
     * @param language new value
     * @throws IllegalArgumentException if new value is null
     */
    public void setLanguage(final Language language) {
        this.language = language;
    }

    /**
     * Returns subtitles.
     *
     * @return subtitles
     */
    public List<Language> getSubtitles() {
        return subtitles;
    }

    /**
     * Sets a new value to subtitles.
     *
     * @param subtitles new value
     */
    public void setSubtitles(final List<Language> subtitles) {
        this.subtitles = subtitles;
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

        if (!(obj instanceof Season) || id == null) {
            return false;
        }

        return id.equals(((Season) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Season [id=%d, number=%d, startYear=%d, endYear=%d, language=%s, subtitles=%s, note=%s, position=%d]", id, number, startYear,
                endYear, language, subtitles, note, position);
    }

}
