package cz.vhromada.catalog.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.entity.Movie;

/**
 * A class represents utility class for movies.
 *
 * @author Vladimir Hromada
 */
public final class MovieUtils {

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Count of movies
     */
    public static final int MOVIES_COUNT = 3;

    /**
     * Position
     */
    public static final int POSITION = 10;

    /**
     * Movie name
     */
    private static final String MOVIE = "Movie ";

    /**
     * Start year
     */
    private static final int START_YEAR = 2000;

    /**
     * Creates a new instance of MovieUtils.
     */
    private MovieUtils() {
    }

    /**
     * Returns movie.
     *
     * @param id ID
     * @return movie
     */
    public static cz.vhromada.catalog.domain.Movie newMovieDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Movie movie = new cz.vhromada.catalog.domain.Movie();
        updateMovie(movie);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumDomain(id)));
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreDomain(id)));
        if (id != null) {
            movie.setId(id);
            movie.setPosition(id - 1);
        }

        return movie;
    }

    /**
     * Updates movie fields.
     *
     * @param movie movie
     */
    @SuppressWarnings("Duplicates")
    public static void updateMovie(final cz.vhromada.catalog.domain.Movie movie) {
        movie.setCzechName("czName");
        movie.setOriginalName("origName");
        movie.setYear(START_YEAR);
        movie.setLanguage(Language.EN);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ));
        movie.setCsfd("Csfd");
        movie.setImdbCode(1000);
        movie.setWikiEn("enWiki");
        movie.setWikiCz("czWiki");
        movie.setPicture("Picture");
        movie.setNote("Note");
    }

    /**
     * Returns movie.
     *
     * @param id ID
     * @return movie
     */
    public static Movie newMovie(final Integer id) {
        final Movie movie = new Movie();
        updateMovie(movie);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(id)));
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(id)));
        if (id != null) {
            movie.setId(id);
            movie.setPosition(id - 1);
        }

        return movie;
    }

    /**
     * Updates movie fields.
     *
     * @param movie movie
     */
    @SuppressWarnings("Duplicates")
    public static void updateMovie(final Movie movie) {
        movie.setCzechName("czName");
        movie.setOriginalName("origName");
        movie.setYear(START_YEAR);
        movie.setLanguage(Language.EN);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ));
        movie.setCsfd("Csfd");
        movie.setImdbCode(1000);
        movie.setWikiEn("enWiki");
        movie.setWikiCz("czWiki");
        movie.setPicture("Picture");
        movie.setNote("Note");
    }

    /**
     * Returns movies.
     *
     * @return movies
     */
    public static List<cz.vhromada.catalog.domain.Movie> getMovies() {
        final List<cz.vhromada.catalog.domain.Movie> movies = new ArrayList<>();
        for (int i = 0; i < MOVIES_COUNT; i++) {
            movies.add(getMovie(i + 1));
        }

        return movies;
    }

    /**
     * Returns movie for index.
     *
     * @param index index
     * @return movie for index
     */
    public static cz.vhromada.catalog.domain.Movie getMovie(final int index) {
        final cz.vhromada.catalog.domain.Movie movie = new cz.vhromada.catalog.domain.Movie();
        movie.setId(index);
        movie.setCzechName(MOVIE + index + " czech name");
        movie.setOriginalName(MOVIE + index + " original name");
        movie.setYear(START_YEAR + index);
        movie.setLanguage(getLanguage(index));
        movie.setCsfd(MOVIE + index + " CSFD");
        movie.setImdbCode(index);
        movie.setWikiEn(MOVIE + index + " English Wikipedia");
        movie.setWikiCz(MOVIE + index + " Czech Wikipedia");
        movie.setPicture(MOVIE + index + " pc");
        movie.setNote(index == 3 ? MOVIE + "3 note" : "");
        movie.setPosition(index - 1);
        final List<Language> subtitles = new ArrayList<>();
        final List<Medium> media = new ArrayList<>();
        final List<Genre> genres = new ArrayList<>();
        media.add(MediumUtils.getMedium(index));
        genres.add(GenreUtils.getGenreDomain(index));
        fillData(index, subtitles, media, genres);
        movie.setSubtitles(subtitles);
        movie.setMedia(media);
        movie.setGenres(genres);

        return movie;
    }

    /**
     * Returns language for index.
     *
     * @param index index
     * @return language for index
     */
    private static Language getLanguage(final int index) {
        switch (index) {
            case 1:
                return Language.CZ;
            case 2:
                return Language.JP;
            case 3:
                return Language.FR;
            default:
                throw new IllegalArgumentException("Bad index");
        }
    }

    /**
     * Fills data for index.
     *
     * @param index     index
     * @param subtitles subtitles
     * @param media     media
     * @param genres    genres
     */
    private static void fillData(final int index, final List<Language> subtitles, final List<Medium> media, final List<Genre> genres) {
        switch (index) {
            case 1:
                break;
            case 2:
                subtitles.add(Language.EN);
                break;
            case 3:
                subtitles.add(Language.CZ);
                subtitles.add(Language.EN);
                media.add(MediumUtils.getMedium(4));
                genres.add(GenreUtils.getGenreDomain(4));
                break;
            default:
                throw new IllegalArgumentException("Bad index");
        }
    }

    /**
     * Returns movie.
     *
     * @param entityManager entity manager
     * @param id            movie ID
     * @return movie
     */
    public static cz.vhromada.catalog.domain.Movie getMovie(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Movie.class, id);
    }

    /**
     * Returns movie with updated fields.
     *
     * @param id            movie ID
     * @param entityManager entity manager
     * @return movie with updated fields
     */
    @SuppressWarnings("SameParameterValue")
    public static cz.vhromada.catalog.domain.Movie updateMovie(final EntityManager entityManager, final int id) {
        final cz.vhromada.catalog.domain.Movie movie = getMovie(entityManager, id);
        updateMovie(movie);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));
        movie.setPosition(POSITION);

        return movie;
    }

    /**
     * Returns count of movies.
     *
     * @param entityManager entity manager
     * @return count of movies
     */
    public static int getMoviesCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(m.id) FROM Movie m", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts movies deep equals.
     *
     * @param expected expected movies
     * @param actual   actual movies
     */
    public static void assertMoviesDeepEquals(final List<cz.vhromada.catalog.domain.Movie> expected, final List<cz.vhromada.catalog.domain.Movie> actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertMovieDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts movie deep equals.
     *
     * @param expected expected movie
     * @param actual   actual movie
     */
    public static void assertMovieDeepEquals(final cz.vhromada.catalog.domain.Movie expected, final cz.vhromada.catalog.domain.Movie actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertAll(
            () -> assertEquals(expected.getId(), actual.getId()),
            () -> assertEquals(expected.getCzechName(), actual.getCzechName()),
            () -> assertEquals(expected.getOriginalName(), actual.getOriginalName()),
            () -> assertEquals(expected.getYear(), actual.getYear()),
            () -> assertEquals(expected.getLanguage(), actual.getLanguage()),
            () -> assertEquals(expected.getSubtitles(), actual.getSubtitles()),
            () -> MediumUtils.assertMediaDeepEquals(expected.getMedia(), actual.getMedia()),
            () -> assertEquals(expected.getCsfd(), actual.getCsfd()),
            () -> assertEquals(expected.getImdbCode(), actual.getImdbCode()),
            () -> assertEquals(expected.getWikiEn(), actual.getWikiEn()),
            () -> assertEquals(expected.getWikiCz(), actual.getWikiCz()),
            () -> assertEquals(expected.getPicture(), actual.getPicture()),
            () -> assertEquals(expected.getNote(), actual.getNote()),
            () -> assertEquals(expected.getPosition(), actual.getPosition()),
            () -> GenreUtils.assertGenresDeepEquals(expected.getGenres(), actual.getGenres())
        );
    }

    /**
     * Asserts movies deep equals.
     *
     * @param expected expected list of To for movie
     * @param actual   actual movies
     */
    public static void assertMovieListDeepEquals(final List<Movie> expected, final List<cz.vhromada.catalog.domain.Movie> actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertMovieDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts movie deep equals.
     *
     * @param expected expected movie
     * @param actual   actual movie
     */
    public static void assertMovieDeepEquals(final Movie expected, final cz.vhromada.catalog.domain.Movie actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertAll(
            () -> assertEquals(expected.getId(), actual.getId()),
            () -> assertEquals(expected.getCzechName(), actual.getCzechName()),
            () -> assertEquals(expected.getOriginalName(), actual.getOriginalName()),
            () -> assertEquals(expected.getYear(), actual.getYear()),
            () -> assertEquals(expected.getLanguage(), actual.getLanguage()),
            () -> assertEquals(expected.getSubtitles(), actual.getSubtitles()),
            () -> MediumUtils.assertMediumListDeepEquals(expected.getMedia(), actual.getMedia()),
            () -> assertEquals(expected.getCsfd(), actual.getCsfd()),
            () -> assertEquals(expected.getImdbCode(), actual.getImdbCode()),
            () -> assertEquals(expected.getWikiEn(), actual.getWikiEn()),
            () -> assertEquals(expected.getWikiCz(), actual.getWikiCz()),
            () -> assertEquals(expected.getPicture(), actual.getPicture()),
            () -> assertEquals(expected.getNote(), actual.getNote()),
            () -> assertEquals(expected.getPosition(), actual.getPosition()),
            () -> GenreUtils.assertGenreListDeepEquals(expected.getGenres(), actual.getGenres())
        );
    }

}
