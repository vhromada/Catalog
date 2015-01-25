package cz.vhromada.catalog.facade.to;

import java.io.Serializable;
import java.util.List;

import cz.vhromada.catalog.commons.Language;

/**
 * A class represents TO for season.
 *
 * @author Vladimir Hromada
 */
public class SeasonTO implements Comparable<SeasonTO>, Serializable {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** Number of season */
    private int number;

    /** Starting year */
    private int startYear;

    /** Ending year */
    private int endYear;

    /** Language */
    private Language language;

    /** Subtitles */
    private List<Language> subtitles;

    /** Note */
    private String note;

    /** Position */
    private int position;

    /** TO for serie */
    private SerieTO serie;

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
     * Returns TO for serie.
     *
     * @return TO for serie
     */
    public SerieTO getSerie() {
        return serie;
    }

    /**
     * Sets a new value to TO for serie.
     *
     * @param serie new value
     */
    public void setSerie(final SerieTO serie) {
        this.serie = serie;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof SeasonTO) || id == null) {
            return false;
        }
        final SeasonTO season = (SeasonTO) obj;
        return id.equals(season.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("SeasonTO [id=%d, number=%d, startYear=%d, endYear=%d, language=%s, subtitles=%s, note=%s, position=%d, serie=%s]", id, number,
                startYear, endYear, language, subtitles, note, position, serie);
    }

    @Override
    public int compareTo(final SeasonTO o) {
        final int result = position - o.position;
        return result == 0 ? id - o.id : result;
    }

}
