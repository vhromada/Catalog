package cz.vhromada.catalog.utils;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.entity.Genre;

/**
 * A class represents utility class for genres.
 *
 * @author Vladimir Hromada
 */
public final class GenreUtils {

    /**
     * Count of genres
     */
    public static final int GENRES_COUNT = 4;

    /**
     * ID
     */
    public static final Integer ID = 1;

    /**
     * Position
     */
    public static final Integer POSITION = 10;

    /**
     * Creates a new instance of GenreUtils.
     */
    private GenreUtils() {
    }

    /**
     * Returns genre.
     *
     * @param id ID
     * @return genre
     */
    public static cz.vhromada.catalog.domain.Genre newGenreDomain(final Integer id) {
        final cz.vhromada.catalog.domain.Genre genre = new cz.vhromada.catalog.domain.Genre();
        updateGenre(genre);
        if (id != null) {
            genre.setId(id);
            genre.setPosition(id - 1);
        }

        return genre;
    }

    /**
     * Updates genre fields.
     *
     * @param genre genre
     */
    public static void updateGenre(final cz.vhromada.catalog.domain.Genre genre) {
        genre.setName("Name");
    }

    /**
     * Returns genre.
     *
     * @param id ID
     * @return genre
     */
    public static Genre newGenre(final Integer id) {
        final Genre genre = new Genre();
        updateGenre(genre);
        if (id != null) {
            genre.setId(id);
            genre.setPosition(id - 1);
        }

        return genre;
    }

    /**
     * Updates genre fields.
     *
     * @param genre genre
     */
    public static void updateGenre(final Genre genre) {
        genre.setName("Name");
    }

    /**
     * Returns genres.
     *
     * @return genres
     */
    public static List<cz.vhromada.catalog.domain.Genre> getGenres() {
        final List<cz.vhromada.catalog.domain.Genre> genres = new ArrayList<>();
        for (int i = 0; i < GENRES_COUNT; i++) {
            genres.add(getGenreDomain(i + 1));
        }

        return genres;
    }

    /**
     * Returns genre for index.
     *
     * @param index index
     * @return genre for index
     */
    public static cz.vhromada.catalog.domain.Genre getGenreDomain(final int index) {
        final cz.vhromada.catalog.domain.Genre genre = new cz.vhromada.catalog.domain.Genre();
        genre.setId(index);
        genre.setName("Genre " + index + " name");
        genre.setPosition(index - 1);

        return genre;
    }

    /**
     * Returns genre for index.
     *
     * @param index index
     * @return genre for index
     */
    public static Genre getGenre(final int index) {
        final Genre genre = new Genre();
        genre.setId(index);
        genre.setName("Genre " + index + " name");
        genre.setPosition(index - 1);

        return genre;
    }

    /**
     * Returns genre.
     *
     * @param entityManager entity manager
     * @param id            genre ID
     * @return genre
     */
    public static cz.vhromada.catalog.domain.Genre getGenre(final EntityManager entityManager, final int id) {
        return entityManager.find(cz.vhromada.catalog.domain.Genre.class, id);
    }

    /**
     * Returns genre with updated fields.
     *
     * @param entityManager entity manager
     * @param id            genre ID
     * @return genre with updated fields
     */
    public static cz.vhromada.catalog.domain.Genre updateGenre(final EntityManager entityManager, final int id) {
        final cz.vhromada.catalog.domain.Genre genre = getGenre(entityManager, id);
        updateGenre(genre);
        genre.setPosition(POSITION);

        return genre;
    }

    /**
     * Returns count of genres.
     *
     * @param entityManager entity manager
     * @return count of genres
     */
    public static int getGenresCount(final EntityManager entityManager) {
        return entityManager.createQuery("SELECT COUNT(g.id) FROM Genre g", Long.class).getSingleResult().intValue();
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected genres
     * @param actual   actual genres
     */
    public static void assertGenresDeepEquals(final List<cz.vhromada.catalog.domain.Genre> expected, final List<cz.vhromada.catalog.domain.Genre> actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertGenreDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    public static void assertGenreDeepEquals(final cz.vhromada.catalog.domain.Genre expected, final cz.vhromada.catalog.domain.Genre actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertAll(
            () -> assertEquals(expected.getId(), actual.getId()),
            () -> assertEquals(expected.getName(), actual.getName()),
            () -> assertEquals(expected.getPosition(), actual.getPosition())
        );
    }

    /**
     * Asserts genres deep equals.
     *
     * @param expected expected list of genre
     * @param actual   actual genres
     */
    public static void assertGenreListDeepEquals(final List<Genre> expected, final List<cz.vhromada.catalog.domain.Genre> actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertEquals(expected.size(), actual.size());
        if (!expected.isEmpty()) {
            for (int i = 0; i < expected.size(); i++) {
                assertGenreDeepEquals(expected.get(i), actual.get(i));
            }
        }
    }

    /**
     * Asserts genre deep equals.
     *
     * @param expected expected genre
     * @param actual   actual genre
     */
    public static void assertGenreDeepEquals(final Genre expected, final cz.vhromada.catalog.domain.Genre actual) {
        assertAll(
            () -> assertNotNull(expected),
            () -> assertNotNull(actual)
        );
        assertAll(
            () -> assertEquals(expected.getId(), actual.getId()),
            () -> assertEquals(expected.getName(), actual.getName()),
            () -> assertEquals(expected.getPosition(), actual.getPosition())
        );
    }

}
