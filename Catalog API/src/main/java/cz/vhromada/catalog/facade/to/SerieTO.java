package cz.vhromada.catalog.facade.to;

import java.io.Serializable;
import java.util.List;

import cz.vhromada.catalog.commons.Time;

/**
 * A class represents TO for serie.
 *
 * @author Vladimir Hromada
 */
public class SerieTO implements Comparable<SerieTO>, Serializable {

    /** SerialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** Czech name */
    private String czechName;

    /** Original name */
    private String originalName;

    /** URL to ČSFD page about serie */
    private String csfd;

    /** IMDB code */
    private int imdbCode;

    /** URL to english Wikipedia page about serie */
    private String wikiEn;

    /** URL to czech Wikipedia page about serie */
    private String wikiCz;

    /** Path to file with serie's picture */
    private String picture;

    /** Count of seasons */
    private int seasonsCount;

    /** Count of episodes */
    private int episodesCount;

    /** Total length of seasons */
    private Time totalLength;

    /** Note */
    private String note;

    /** Position */
    private int position;

    /** List of TO for genre */
    private List<GenreTO> genres;

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
     * Returns count of seasons.
     *
     * @return count of seasons
     */
    public int getSeasonsCount() {
        return seasonsCount;
    }

    /**
     * Sets a new value to count of seasons.
     *
     * @param seasonsCount new value
     */
    public void setSeasonsCount(final int seasonsCount) {
        this.seasonsCount = seasonsCount;
    }

    /**
     * Returns count of episodes.
     *
     * @return count of episodes
     */
    public int getEpisodesCount() {
        return episodesCount;
    }

    /**
     * Sets a new value to count of episodes.
     *
     * @param episodesCount new value
     */
    public void setEpisodesCount(final int episodesCount) {
        this.episodesCount = episodesCount;
    }

    /**
     * Returns total length of seasons.
     *
     * @return total length of seasons
     */
    public Time getTotalLength() {
        return totalLength;
    }

    /**
     * Sets a new value to total length of seasons.
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

    /**
     * Returns genres as string.
     *
     * @return genres as string
     */
    public String getGenresAsString() {
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        final StringBuilder genresString = new StringBuilder();
        for (final GenreTO genre : genres) {
            genresString.append(genre.getName());
            genresString.append(", ");
        }
        return genresString.substring(0, genresString.length() - 2);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof SerieTO) || id == null) {
            return false;
        }
        final SerieTO serie = (SerieTO) obj;
        return id.equals(serie.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("SerieTO [id=%d, czechName=%s, originalName=%s, csfd=%s, imdbCode=%d, wikiEn=%s, wikiCz=%s, picture=%s, seasonsCount=%d, "
                        + "episodesCount=%d, totalLength=%s, note=%s, position=%d, genres=%s]", id, czechName, originalName, csfd, imdbCode, wikiEn, wikiCz,
                picture,
                seasonsCount, episodesCount, totalLength, note, position, genres);
    }

    @Override
    public int compareTo(final SerieTO o) {
        final int result = position - o.position;
        return result == 0 ? id - o.id : result;
    }

}
