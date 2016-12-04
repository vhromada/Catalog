package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.common.Language;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Medium;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GenreUtils;
import cz.vhromada.catalog.utils.MediumUtils;
import cz.vhromada.catalog.utils.MovieUtils;
import cz.vhromada.catalog.utils.TestConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testCatalogContext.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MovieFacadeImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link MovieFacade}
     */
    @Autowired
    private MovieFacade movieFacade;

    /**
     * Test method for {@link MovieFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void testNewData() {
        movieFacade.newData();

        assertEquals(0, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getMovies()}.
     */
    @Test
    public void testGetMovies() {
        MovieUtils.assertMovieListDeepEquals(movieFacade.getMovies(), MovieUtils.getMovies());

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)}.
     */
    @Test
    public void testGetMovie() {
        for (int i = 1; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(movieFacade.getMovie(i), MovieUtils.getMovie(i));
        }

        assertNull(movieFacade.getMovie(Integer.MAX_VALUE));

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMovie_NullArgument() {
        movieFacade.getMovie(null);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));
        final cz.vhromada.catalog.domain.Movie expectedMovie = MovieUtils.newMovieDomain(MovieUtils.MOVIES_COUNT + 1);
        expectedMovie.setMedia(CollectionUtils.newList(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1)));
        expectedMovie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(1)));

        movieFacade.add(movie);

        final cz.vhromada.catalog.domain.Movie addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        MovieUtils.assertMovieDeepEquals(expectedMovie, addedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT + 1, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT + 1, MediumUtils.getMediaCount(entityManager));
        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        movieFacade.add(null);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotNullId() {
        movieFacade.add(MovieUtils.newMovie(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullCzechName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setCzechName(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with empty string as czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyCzechName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setCzechName("");

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullOriginalName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setOriginalName(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with empty string as original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyOriginalName() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setOriginalName("");

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad minimum year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMinimumYear() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad maximum year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMaximumYear() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null language.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullLanguage() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setLanguage(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null subtitles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullSubtitles() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setSubtitles(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with subtitles with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadSubtitles() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullMedia() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with media with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMedia() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with media with negative value as medium.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMedium() {
        final Medium badMedium = MediumUtils.newMedium(Integer.MAX_VALUE);
        badMedium.setLength(-1);
        final Movie movie = MovieUtils.newMovie(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to ČSFD page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullCsfd() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setCsfd(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad minimal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMinimalImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad divider IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadDividerImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setImdbCode(0);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with bad maximal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadMaximalImdb() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullWikiEn() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setWikiEn(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullWikiCz() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setWikiCz(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null path to file with movie's picture.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullPicture() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setPicture(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullNote() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setNote(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with null genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullGenres() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_BadGenres() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullGenreId() {
        final Movie movie = MovieUtils.newMovie(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with movie with genres with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullGenreName() {
        final Movie movie = MovieUtils.newMovie(null);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));

        movieFacade.update(movie);

        final cz.vhromada.catalog.domain.Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        MovieUtils.assertMovieDeepEquals(movie, updatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT, MediumUtils.getMediaCount(entityManager));
        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        movieFacade.update(null);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullId() {
        movieFacade.update(MovieUtils.newMovie(null));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullCzechName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCzechName(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with empty string as czech name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyCzechName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCzechName("");

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullOriginalName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setOriginalName(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with empty string as original name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyOriginalName() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setOriginalName("");

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad minimum year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMinimumYear() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad maximum year.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMaximumYear() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null language.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullLanguage() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setLanguage(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null subtitles.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullSubtitles() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setSubtitles(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with subtitles with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadSubtitles() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullMedia() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with media with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMedia() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with media with negative value as medium.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMedium() {
        final Medium badMedium = MediumUtils.newMedium(Integer.MAX_VALUE);
        badMedium.setLength(-1);
        final Movie movie = MovieUtils.newMovie(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(1), badMedium));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to ČSFD page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullCsfd() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setCsfd(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad minimal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMinimalImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad divider IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadDividerImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(0);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad maximal IMDB code.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadMaximalImdb() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullWikiEn() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setWikiEn(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullWikiCz() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setWikiCz(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null path to file with movie's picture.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullPicture() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setPicture(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullNote() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setNote(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with null genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullGenres() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadGenres() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with genre with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullGenreId() {
        final Movie movie = MovieUtils.newMovie(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), GenreUtils.newGenre(null)));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with genres with genre with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullGenreName() {
        final Movie movie = MovieUtils.newMovie(1);
        final Genre badGenre = GenreUtils.newGenre(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenre(1), badGenre));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with movie with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadId() {
        movieFacade.update(MovieUtils.newMovie(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#remove(Movie)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        movieFacade.remove(MovieUtils.newMovie(1));

        assertNull(MovieUtils.getMovie(entityManager, 1));

        assertEquals(MovieUtils.MOVIES_COUNT - 1, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#remove(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        movieFacade.remove(null);
    }

    /**
     * Test method for {@link MovieFacade#remove(Movie)} with movie with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        movieFacade.remove(MovieUtils.newMovie(null));
    }

    /**
     * Test method for {@link MovieFacade#remove(Movie)} with movie with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadId() {
        movieFacade.remove(MovieUtils.newMovie(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(Movie)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Medium medium1 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT - 1);
        medium1.setId(MediumUtils.MEDIA_COUNT + 1);
        final cz.vhromada.catalog.domain.Medium medium2 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT);
        medium2.setId(MediumUtils.MEDIA_COUNT + 2);
        final cz.vhromada.catalog.domain.Movie movie = MovieUtils.getMovie(MovieUtils.MOVIES_COUNT);
        movie.setId(MovieUtils.MOVIES_COUNT + 1);
        movie.setMedia(CollectionUtils.newList(medium1, medium2));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT - 1), GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT)));

        movieFacade.duplicate(MovieUtils.newMovie(MovieUtils.MOVIES_COUNT));

        final cz.vhromada.catalog.domain.Movie duplicatedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        MovieUtils.assertMovieDeepEquals(movie, duplicatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT + 1, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT + 2, MediumUtils.getMediaCount(entityManager));
        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        movieFacade.duplicate(null);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(Movie)} with movie with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullId() {
        movieFacade.duplicate(MovieUtils.newMovie(null));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(Movie)} with movie with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadId() {
        movieFacade.duplicate(MovieUtils.newMovie(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Movie movie1 = MovieUtils.getMovie(1);
        movie1.setPosition(1);
        final cz.vhromada.catalog.domain.Movie movie2 = MovieUtils.getMovie(2);
        movie2.setPosition(0);

        movieFacade.moveUp(MovieUtils.newMovie(2));
        MovieUtils.assertMovieDeepEquals(movie1, MovieUtils.getMovie(entityManager, 1));
        MovieUtils.assertMovieDeepEquals(movie2, MovieUtils.getMovie(entityManager, 2));
        for (int i = 3; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), MovieUtils.getMovie(entityManager, i));
        }

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        movieFacade.moveUp(null);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)} with movie with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullId() {
        movieFacade.moveUp(MovieUtils.newMovie(null));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        movieFacade.moveUp(MovieUtils.newMovie(1));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadId() {
        movieFacade.moveUp(MovieUtils.newMovie(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Movie movie1 = MovieUtils.getMovie(1);
        movie1.setPosition(1);
        final cz.vhromada.catalog.domain.Movie movie2 = MovieUtils.getMovie(2);
        movie2.setPosition(0);

        movieFacade.moveDown(MovieUtils.newMovie(1));
        MovieUtils.assertMovieDeepEquals(movie1, MovieUtils.getMovie(entityManager, 1));
        MovieUtils.assertMovieDeepEquals(movie2, MovieUtils.getMovie(entityManager, 2));
        for (int i = 3; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), MovieUtils.getMovie(entityManager, i));
        }

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        movieFacade.moveDown(null);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)} with movie with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullId() {
        movieFacade.moveDown(MovieUtils.newMovie(null));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        movieFacade.moveDown(MovieUtils.newMovie(MovieUtils.MOVIES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadId() {
        movieFacade.moveDown(MovieUtils.newMovie(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void testUpdatePositions() {
        movieFacade.updatePositions();

        for (int i = 1; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), MovieUtils.getMovie(entityManager, i));
        }

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        assertEquals(MediumUtils.MEDIA_COUNT, movieFacade.getTotalMediaCount());

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        assertEquals(new Time(1000), movieFacade.getTotalLength());

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

}
