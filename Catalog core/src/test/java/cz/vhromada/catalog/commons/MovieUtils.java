package cz.vhromada.catalog.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.dao.entities.Movie;

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
    public static void updateMovie(final Movie movie) {
        movie.setCzechName("czName");
        movie.setOriginalName("origName");
        movie.setYear(2000);
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
    public static List<Movie> getMovies() {
        final List<Movie> movies = new ArrayList<>();
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
    public static Movie getMovie(final int index) {
        final Movie movie = new Movie();
        movie.setId(index);
        movie.setCzechName("Movie " + index + " czech name");
        movie.setOriginalName("Movie " + index + " original name");
        movie.setYear(index + 2000);
        movie.setCsfd("Movie " + index + " CSFD");
        movie.setImdbCode(index);
        movie.setWikiEn("Movie " + index + " English Wikipedia");
        movie.setWikiCz("Movie " + index + " Czech Wikipedia");
        movie.setPicture("Movie " + index + " pc");
        movie.setNote(index == 3 ? "Movie 3 note" : "");
        movie.setPosition(index - 1);
        final List<Language> subtitles = new ArrayList<>();
        final List<Medium> media = new ArrayList<>();
        final List<Genre> genres = new ArrayList<>();
        final Language language;
        media.add(MediumUtils.getMedium(index));
        genres.add(GenreUtils.getGenre(index));
        switch (index) {
            case 1:
                language = Language.CZ;
                break;
            case 2:
                language = Language.JP;
                subtitles.add(Language.EN);
                break;
            case 3:
                language = Language.FR;
                subtitles.add(Language.CZ);
                subtitles.add(Language.EN);
                media.add(MediumUtils.getMedium(4));
                genres.add(GenreUtils.getGenre(4));
                break;
            default:
                throw new IllegalArgumentException("Bad index");
        }
        movie.setLanguage(language);
        movie.setSubtitles(subtitles);
        movie.setMedia(media);
        movie.setGenres(genres);

        return movie;
    }

    /**
     * Returns movie.
     *
     * @param entityManager entity manager
     * @param id            movie ID
     * @return movie
     */
    public static Movie getMovie(final EntityManager entityManager, final int id) {
        return entityManager.find(Movie.class, id);
    }

    /**
     * Returns movie with updated fields.
     *
     * @param id            movie ID
     * @param entityManager entity manager
     * @return movie with updated fields
     */
    public static Movie updateMovie(final int id, final EntityManager entityManager) {
        final Movie movie = getMovie(entityManager, id);
        updateMovie(movie);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
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
    public static void assertMoviesDeepEquals(final List<Movie> expected, final List<Movie> actual) {
        assertNotNull(actual);
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
    public static void assertMovieDeepEquals(final Movie expected, final Movie actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCzechName(), actual.getCzechName());
        assertEquals(expected.getOriginalName(), actual.getOriginalName());
        assertEquals(expected.getYear(), actual.getYear());
        assertEquals(expected.getLanguage(), actual.getLanguage());
        assertEquals(expected.getSubtitles(), actual.getSubtitles());
        MediumUtils.assertMediaDeepEquals(expected.getMedia(), actual.getMedia());
        assertEquals(expected.getCsfd(), actual.getCsfd());
        assertEquals(expected.getImdbCode(), actual.getImdbCode());
        assertEquals(expected.getWikiEn(), actual.getWikiEn());
        assertEquals(expected.getWikiCz(), actual.getWikiCz());
        assertEquals(expected.getPicture(), actual.getPicture());
        assertEquals(expected.getNote(), actual.getNote());
        assertEquals(expected.getPosition(), actual.getPosition());
        GenreUtils.assertGenresDeepEquals(expected.getGenres(), actual.getGenres());
    }

}