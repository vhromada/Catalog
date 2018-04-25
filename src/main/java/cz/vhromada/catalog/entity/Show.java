package cz.vhromada.catalog.entity;

import java.util.List;
import java.util.Objects;

import cz.vhromada.common.Movable;

/**
 * A class represents show.
 *
 * @author Vladimir Hromada
 */
public class Show implements Movable {

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
     * URL to ČSFD page about show
     */
    private String csfd;

    /**
     * IMDB code
     */
    private int imdbCode;

    /**
     * URL to english Wikipedia page about show
     */
    private String wikiEn;

    /**
     * URL to czech Wikipedia page about show
     */
    private String wikiCz;

    /**
     * Picture's ID
     */
    private Integer picture;

    /**
     * Note
     */
    private String note;

    /**
     * Position
     */
    private Integer position;

    /**
     * Genres
     */
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
     * Returns URL to ČSFD page about show.
     *
     * @return URL to ČSFD page about show
     */
    public String getCsfd() {
        return csfd;
    }

    /**
     * Sets a new value to URL to ČSFD page about show.
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
     * Returns URL to english Wikipedia page about show.
     *
     * @return URL to english Wikipedia page about show
     */
    public String getWikiEn() {
        return wikiEn;
    }

    /**
     * Sets a new value to URL to english Wikipedia page about show.
     *
     * @param wikiEn new value
     */
    public void setWikiEn(final String wikiEn) {
        this.wikiEn = wikiEn;
    }

    /**
     * Returns URL to czech Wikipedia page about show.
     *
     * @return URL to czech Wikipedia page about show
     */
    public String getWikiCz() {
        return wikiCz;
    }

    /**
     * Sets a new value to URL to czech Wikipedia page about show.
     *
     * @param wikiCz new value
     */
    public void setWikiCz(final String wikiCz) {
        this.wikiCz = wikiCz;
    }

    /**
     * Returns picture's ID.
     *
     * @return picture's ID
     */
    public Integer getPicture() {
        return picture;
    }

    /**
     * Sets a new value to picture's ID.
     *
     * @param picture new value
     */
    public void setPicture(final Integer picture) {
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
    public Integer getPosition() {
        return position;
    }

    @Override
    public void setPosition(final Integer position) {
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

        if (!(obj instanceof Show) || id == null) {
            return false;
        }

        return id.equals(((Show) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Show [id=%d, czechName=%s, originalName=%s, csfd=%s, imdbCode=%d, wikiEn=%s, wikiCz=%s, picture=%d, note=%s, position=%d, "
            + "genres=%s]", id, czechName, originalName, csfd, imdbCode, wikiEn, wikiCz, picture, note, position, genres);
    }

}
