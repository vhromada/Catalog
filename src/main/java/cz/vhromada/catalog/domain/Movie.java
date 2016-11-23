package cz.vhromada.catalog.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.Movable;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * A class represents movie.
 *
 * @author Vladimir Hromada
 */
@Entity
@Table(name = "movies")
public class Movie implements Movable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @SequenceGenerator(name = "movie_generator", sequenceName = "movies_sq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_generator")
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
     * Year
     */
    @Column(name = "movie_year")
    private int year;

    /**
     * Language
     */
    @Column(name = "movie_language")
    @Enumerated(EnumType.STRING)
    private Language language;

    /**
     * Subtitles
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_subtitles", joinColumns = @JoinColumn(name = "movie"))
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SELECT)
    private List<Language> subtitles;

    /**
     * Media
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "movie_media", joinColumns = @JoinColumn(name = "movie"), inverseJoinColumns = @JoinColumn(name = "medium"))
    @OrderBy("id")
    @Fetch(FetchMode.SELECT)
    private List<Medium> media;

    /**
     * URL to ČSFD page about movie
     */
    private String csfd;

    /**
     * IMDB code
     */
    @Column(name = "imdb_code")
    private int imdbCode;

    /**
     * URL to english Wikipedia page about movie
     */
    @Column(name = "wiki_en")
    private String wikiEn;

    /**
     * URL to czech Wikipedia page about movie
     */
    @Column(name = "wiki_cz")
    private String wikiCz;

    /**
     * Path to file with movie's picture
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
    @JoinTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie"), inverseJoinColumns = @JoinColumn(name = "genre"))
    @OrderBy("id")
    @Fetch(FetchMode.SELECT)
    private List<Genre> genres;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
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
     * Returns year.
     *
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets a new value to year.
     *
     * @param year new value
     */
    public void setYear(final int year) {
        this.year = year;
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
     * Returns media.
     *
     * @return media
     */
    public List<Medium> getMedia() {
        return media;
    }

    /**
     * Sets a new value to media.
     *
     * @param media new value
     */
    public void setMedia(final List<Medium> media) {
        this.media = media;
    }

    /**
     * Returns URL to ČSFD page about movie.
     *
     * @return URL to ČSFD page about movie
     */
    public String getCsfd() {
        return csfd;
    }

    /**
     * Sets a new value to URL to ČSFD page about movie.
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
     * Returns URL to english Wikipedia page about movie.
     *
     * @return URL to english Wikipedia page about movie
     */
    public String getWikiEn() {
        return wikiEn;
    }

    /**
     * Sets a new value to URL to english Wikipedia page about movie.
     *
     * @param wikiEn new value
     */
    public void setWikiEn(final String wikiEn) {
        this.wikiEn = wikiEn;
    }

    /**
     * Returns URL to czech Wikipedia page about movie.
     *
     * @return URL to czech Wikipedia page about movie
     */
    public String getWikiCz() {
        return wikiCz;
    }

    /**
     * Sets a new value to URL to czech Wikipedia page about movie.
     *
     * @param wikiCz new value
     */
    public void setWikiCz(final String wikiCz) {
        this.wikiCz = wikiCz;
    }

    /**
     * Returns path to file with movie's picture.
     *
     * @return path to file with movie's picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets a new value to path to file with movie's picture.
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

    @Override
    public int getPosition() {
        return position;
    }

    @Override
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

        if (obj == null || !(obj instanceof Movie) || id == null) {
            return false;
        }

        return id.equals(((Movie) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Movie [id=%d, czechName=%s, originalName=%s, year=%d, language=%s, subtitles=%s, media=%s, csfd=%s, imdbCode=%d, wikiEn=%s, "
                        + "wikiCz=%s, picture=%s, note=%s, position=%d, genres=%s]", id, czechName, originalName, year, language, subtitles, media, csfd,
                imdbCode, wikiEn, wikiCz, picture, note, position, genres);
    }

}
