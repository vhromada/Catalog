package cz.vhromada.catalog.dao.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import cz.vhromada.catalog.commons.Language;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * A class represents season.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "seasons")
@NamedQuery(name = Season.FIND_BY_SERIE, query = "SELECT s FROM Season s WHERE s.serie.id = :serie ORDER BY s.position, s.id")
public class Season implements Serializable {

    /** Name for query - find by serie */
    public static final String FIND_BY_SERIE = "Season.findBySerie";

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @SequenceGenerator(name = "season_generator", sequenceName = "seasons_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "season_generator")
    private Integer id;

    /** Number of season */
    @Column(name = "season_number")
    private int number;

    /** Starting year */
    @Column(name = "start_year")
    private int startYear;

    /** Ending year */
    @Column(name = "end_year")
    private int endYear;

    /** Language */
    @Column(name = "season_language")
    @Enumerated(EnumType.STRING)
    private Language language;

    /** Subtitles */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "season_subtitles", joinColumns = @JoinColumn(name = "season"))
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SELECT)
    private List<Language> subtitles;

    /** Note */
    private String note;

    /** Position */
    private int position;

    /** Serie */
    @ManyToOne
    @JoinColumn(name = "serie", referencedColumnName = "id")
    private Serie serie;

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
     * Returns serie.
     *
     * @return serie
     */
    public Serie getSerie() {
        return serie;
    }

    /**
     * Sets a new value to serie.
     *
     * @param serie new value
     */
    public void setSerie(final Serie serie) {
        this.serie = serie;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Season) || id == null) {
            return false;
        }
        final Season season = (Season) obj;
        return id.equals(season.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Season [id=%d, number=%d, startYear=%d, endYear=%d, language=%s, subtitles=%s, note=%s, position=%d, serie=%s]", id, number,
                startYear, endYear, language, subtitles, note, position, serie);
    }

}
