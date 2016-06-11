package cz.vhromada.catalog.facade.to;

import java.util.List;

import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.Movable;

/**
 * A class represents TO for movie.
 *
 * @author Vladimir Hromada
 */
public class MovieTO implements Movable {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * Czech name
     */
    private String czechName;

    /**
     * Original name
     */
    private String originalName;

    /**
     * Year
     */
    private int year;

    /**
     * Language
     */
    private Language language;

    /**
     * Subtitles
     */
    private List<Language> subtitles;

    /**
     * Media
     */
    private List<MediumTO> media;

    /**
     * URL to ČSFD page about movie
     */
    private String csfd;

    /**
     * IMDB code
     */
    private int imdbCode;

    /**
     * URL to english Wikipedia page about movie
     */
    private String wikiEn;

    /**
     * URL to czech Wikipedia page about movie
     */
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
     * List of TO for genre
     */
    private List<GenreTO> genres;

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
    public List<MediumTO> getMedia() {
        return media;
    }

    /**
     * Sets a new value to media.
     *
     * @param media new value
     */
    public void setMedia(final List<MediumTO> media) {
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
     * Returns list of TO for genre.
     *
     * @return list of TO for genre
     */
    public List<GenreTO> getGenres() {
        return genres;
    }

    /**
     * Sets a new value to list of TO for genre.
     *
     * @param genres new value
     */
    public void setGenres(final List<GenreTO> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof MovieTO) || id == null) {
            return false;
        }

        return id.equals(((MovieTO) obj).id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("MovieTO [id=%d, czechName=%s, originalName=%s, year=%d, language=%s, subtitles=%s, media=%s, csfd=%s, imdbCode=%d, wikiEn=%s, "
                        + "wikiCz=%s, picture=%s, note=%s, position=%d, genres=%s]", id, czechName, originalName, year, language, subtitles, media, csfd,
                imdbCode, wikiEn, wikiCz, picture, note, position, genres);
    }

}
