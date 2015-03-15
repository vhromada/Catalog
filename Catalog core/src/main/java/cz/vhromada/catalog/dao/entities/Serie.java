package cz.vhromada.catalog.dao.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * A class represents serie.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "series")
@NamedQuery(name = Serie.SELECT_SERIES, query = "SELECT s FROM Serie s ORDER BY s.position, s.id")
public class Serie implements Serializable {

    /**
     * Name for query - select series
     */
    public static final String SELECT_SERIES = "Serie.selectSeries";

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "serie_generator", sequenceName = "series_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serie_generator")
    private Integer id;

    /**
     * Czech name
     */
    @Column(name = "czech_name")
    private String czechName;

    /**
     * Original name
     */
    @Column(name = "original_name")
    private String originalName;

    /**
     * URL to ČSFD page about serie
     */
    private String csfd;

    /**
     * IMDB code
     */
    @Column(name = "imdb_code")
    private int imdbCode;

    /**
     * URL to english Wikipedia page about serie
     */
    @Column(name = "wiki_en")
    private String wikiEn;

    /**
     * URL to czech Wikipedia page about serie
     */
    @Column(name = "wiki_cz")
    private String wikiCz;

    /**
     * Path to file with serie's picture
     */
    private String picture;

    /**
     * Note
     */
    private String note;

    /**
     * Position
     */
    private int position;

    /**
     * Genres
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "serie_genres", joinColumns = @JoinColumn(name = "serie"), inverseJoinColumns = @JoinColumn(name = "genre"))
    @Fetch(FetchMode.SELECT)
    private List<Genre> genres;

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
     * Returns czech name.
     *
     * @return czech name
     */
    public String getCzechName() {
        return czechName;
    }

    /**
     * Sets a new value to czech name.
     *
     * @param czechName new value
     */
    public void setCzechName(final String czechName) {
        this.czechName = czechName;
    }

    /**
     * Returns original name.
     *
     * @return original name
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * Sets a new value to original name.
     *
     * @param originalName new value
     */
    public void setOriginalName(final String originalName) {
        this.originalName = originalName;
    }

    /**
     * Returns URL to ČSFD page about serie.
     *
     * @return URL to ČSFD page about serie
     */
    public String getCsfd() {
        return csfd;
    }

    /**
     * Sets a new value to URL to ČSFD page about serie.
     *
     * @param csfd new value
     */
    public void setCsfd(final String csfd) {
        this.csfd = csfd;
    }

    /**
     * Returns IMDB code.
     *
     * @return IMDB code
     */
    public int getImdbCode() {
        return imdbCode;
    }

    /**
     * Sets a new value to IMDB code.
     *
     * @param imdbCode new value
     */
    public void setImdbCode(final int imdbCode) {
        this.imdbCode = imdbCode;
    }

    /**
     * Returns URL to english Wikipedia page about serie.
     *
     * @return URL to english Wikipedia page about serie
     */
    public String getWikiEn() {
        return wikiEn;
    }

    /**
     * Sets a new value to URL to english Wikipedia page about serie.
     *
     * @param wikiEn new value
     */
    public void setWikiEn(final String wikiEn) {
        this.wikiEn = wikiEn;
    }

    /**
     * Returns URL to czech Wikipedia page about serie.
     *
     * @return URL to czech Wikipedia page about serie
     */
    public String getWikiCz() {
        return wikiCz;
    }

    /**
     * Sets a new value to URL to czech Wikipedia page about serie.
     *
     * @param wikiCz new value
     */
    public void setWikiCz(final String wikiCz) {
        this.wikiCz = wikiCz;
    }

    /**
     * Returns path to file with serie's picture.
     *
     * @return path to file with serie's picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets a new value to path to file with serie's picture.
     *
     * @param picture new value
     */
    public void setPicture(final String picture) {
        this.picture = picture;
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
     * Returns genres.
     *
     * @return genres
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * Sets a new value to genres.
     *
     * @param genres new value
     */
    public void setGenres(final List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Serie) || id == null) {
            return false;
        }
        final Serie serie = (Serie) obj;
        return id.equals(serie.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Serie [id=%d, czechName=%s, originalName=%s, csfd=%s, imdbCode=%d, wikiEn=%s, wikiCz=%s, picture=%s, note=%s, position=%d, "
                + "genres=%s]", id, czechName, originalName, csfd, imdbCode, wikiEn, wikiCz, picture, note, position, genres);
    }

}
