package cz.vhromada.catalog.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.domain.Genre;
import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.common.Language;

/**
 * A class represents utility class for movies.
 *
 * @author Vladimir Hromada
 */
public final class MovieUtils {

    /**
     * ID
     */
    public static final int ID = 1;

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
        movie.setMedia(Collections.singletonList(MediumUtils.newMediumDomain(id)));
        movie.setGenres(Collections.singletonList(GenreUtils.newGenreDomain(id)));
        movie.setPicture(id);
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
        movie.setSubtitles(new ArrayList<>(Collections.singletonList(Language.CZ)));
        movie.setCsfd("Csfd");
        movie.setImdbCode(1000);
        movie.setWikiEn("enWiki");
        movie.setWikiCz("czWiki");
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
        movie.setMedia(Collections.singletonList(MediumUtils.newMedium(id)));
        movie.setGenres(Collections.singletonList(GenreUtils.newGenre(id)));
        movie.setPicture(id);
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
        movie.setSubtitles(Collections.singletonList(Language.CZ));
        movie.setCsfd("Csfd");
        movie.setImdbCode(1000);
        movie.setWikiEn("enWiki");
        movie.setWikiCz("czWiki");
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
        movie.setPicture(index);
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
    public static cz.vhromada.catalog.domain.Movie updateMovie(final EntityManager entityManager, final int id) {
        final cz.vhromada.catalog.domain.Movie movie = getMovie(entityManager, id);
        updateMovie(movie);
        movie.setGenres(new ArrayList<>(Collections.singletonList(GenreUtils.getGenreDomain(1))));
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
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
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
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(expected.getId()).isEqualTo(actual.getId());
            softly.assertThat(expected.getCzechName()).isEqualTo(actual.getCzechName());
            softly.assertThat(expected.getOriginalName()).isEqualTo(actual.getOriginalName());
            softly.assertThat(expected.getYear()).isEqualTo(actual.getYear());
            softly.assertThat(expected.getLanguage()).isEqualTo(actual.getLanguage());
            softly.assertThat(expected.getSubtitles()).isEqualTo(actual.getSubtitles());
            MediumUtils.assertMediaDeepEquals(expected.getMedia(), actual.getMedia());
            softly.assertThat(expected.getCsfd()).isEqualTo(actual.getCsfd());
            softly.assertThat(expected.getImdbCode()).isEqualTo(actual.getImdbCode());
            softly.assertThat(expected.getWikiEn()).isEqualTo(actual.getWikiEn());
            softly.assertThat(expected.getWikiCz()).isEqualTo(actual.getWikiCz());
            softly.assertThat(expected.getPicture()).isEqualTo(actual.getPicture());
            softly.assertThat(expected.getNote()).isEqualTo(actual.getNote());
            softly.assertThat(expected.getPosition()).isEqualTo(actual.getPosition());
            GenreUtils.assertGenresDeepEquals(expected.getGenres(), actual.getGenres());
        });
    }

    /**
     * Asserts movies deep equals.
     *
     * @param expected expected list of To for movie
     * @param actual   actual movies
     */
    public static void assertMovieListDeepEquals(final List<Movie> expected, final List<cz.vhromada.catalog.domain.Movie> actual) {
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertThat(expected.size()).isEqualTo(actual.size());
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
        assertSoftly(softly -> {
            softly.assertThat(expected).isNotNull();
            softly.assertThat(actual).isNotNull();
        });
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isEqualTo(expected.getId());
            softly.assertThat(actual.getCzechName()).isEqualTo(expected.getCzechName());
            softly.assertThat(actual.getOriginalName()).isEqualTo(expected.getOriginalName());
            softly.assertThat(actual.getYear()).isEqualTo(expected.getYear());
            softly.assertThat(actual.getLanguage()).isEqualTo(expected.getLanguage());
            softly.assertThat(actual.getSubtitles())
                .hasSameSizeAs(expected.getSubtitles())
                .hasSameElementsAs(expected.getSubtitles());
            MediumUtils.assertMediumListDeepEquals(expected.getMedia(), actual.getMedia());
            softly.assertThat(actual.getCsfd()).isEqualTo(expected.getCsfd());
            softly.assertThat(actual.getImdbCode()).isEqualTo(expected.getImdbCode());
            softly.assertThat(actual.getWikiEn()).isEqualTo(expected.getWikiEn());
            softly.assertThat(actual.getWikiCz()).isEqualTo(expected.getWikiCz());
            softly.assertThat(actual.getPicture()).isEqualTo(expected.getPicture());
            softly.assertThat(actual.getNote()).isEqualTo(expected.getNote());
            softly.assertThat(actual.getPosition()).isEqualTo(expected.getPosition());
            GenreUtils.assertGenreListDeepEquals(expected.getGenres(), actual.getGenres());
        });
    }

}
