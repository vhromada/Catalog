package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.GenreUtils;
import cz.vhromada.catalog.commons.Language;
import cz.vhromada.catalog.commons.MediumUtils;
import cz.vhromada.catalog.commons.MovieUtils;
import cz.vhromada.catalog.commons.TestConstants;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.entities.Medium;
import cz.vhromada.catalog.entities.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MediumTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

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
@ContextConfiguration("classpath:testFacadeContext.xml")
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
     * Test method for {@link MovieFacade#add(MovieTO)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenreTO(1)));
        final Movie expectedMovie = MovieUtils.newMovie(MovieUtils.MOVIES_COUNT + 1);
        expectedMovie.setMedia(CollectionUtils.newList(MediumUtils.newMedium(MediumUtils.MEDIA_COUNT + 1)));
        expectedMovie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(1)));

        movieFacade.add(movie);

        final Movie addedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        MovieUtils.assertMovieDeepEquals(expectedMovie, addedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT + 1, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT + 1, MediumUtils.getMediaCount(entityManager));
        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        movieFacade.add(null);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotNullId() {
        movieFacade.add(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullCzechName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setCzechName(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyCzechName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setCzechName("");

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullOriginalName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setOriginalName(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyOriginalName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setOriginalName("");

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad minimum year.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMinimumYear() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad maximum year.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMaximumYear() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null language.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullLanguage() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setLanguage(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullSubtitles() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setSubtitles(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadSubtitles() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null media.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullMedia() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setMedia(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with media with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMedia() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with media with negative value as medium.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMedium() {
        final MediumTO badMedium = MediumUtils.newMediumTO(Integer.MAX_VALUE);
        badMedium.setLength(-1);
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), badMedium));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null URL to ČSFD page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullCsfd() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setCsfd(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMinimalImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadDividerImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(0);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadMaximalImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullWikiEn() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setWikiEn(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullWikiCz() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setWikiCz(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null path to file with movie's picture.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullPicture() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setPicture(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullNote() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setNote(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullGenres() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setGenres(null);

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadGenres() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullGenreId() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with movie with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullGenreName() {
        final MovieTO movie = MovieUtils.newMovieTO(null);
        final GenreTO badGenre = GenreUtils.newGenreTO(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), badGenre));

        movieFacade.add(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenreTO(1)));

        movieFacade.update(movie);

        final Movie updatedMovie = MovieUtils.getMovie(entityManager, 1);
        MovieUtils.assertMovieDeepEquals(movie, updatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT, MediumUtils.getMediaCount(entityManager));
        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        movieFacade.update(null);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullId() {
        movieFacade.update(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null czech name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullCzechName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setCzechName(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with empty string as czech name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyCzechName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setCzechName("");

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null original name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullOriginalName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setOriginalName(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with empty string as original name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyOriginalName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setOriginalName("");

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad minimum year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMinimumYear() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setYear(TestConstants.BAD_MIN_YEAR);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad maximum year.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMaximumYear() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setYear(TestConstants.BAD_MAX_YEAR);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null language.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullLanguage() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setLanguage(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null subtitles.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullSubtitles() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setSubtitles(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with subtitles with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadSubtitles() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setSubtitles(CollectionUtils.newList(Language.CZ, null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null media.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullMedia() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setMedia(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with media with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMedia() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with media with negative value as medium.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMedium() {
        final MediumTO badMedium = MediumUtils.newMediumTO(Integer.MAX_VALUE);
        badMedium.setLength(-1);
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setMedia(CollectionUtils.newList(MediumUtils.newMediumTO(1), badMedium));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null URL to ČSFD page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullCsfd() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setCsfd(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad minimal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMinimalImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(TestConstants.BAD_MIN_IMDB_CODE);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad divider IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadDividerImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(0);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad maximal IMDB code.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadMaximalImdb() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setImdbCode(TestConstants.BAD_MAX_IMDB_CODE);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null URL to english Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullWikiEn() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setWikiEn(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null URL to czech Wikipedia page about movie.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullWikiCz() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setWikiCz(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null path to file with movie's picture.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullPicture() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setPicture(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullNote() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setNote(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with null genres.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullGenres() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setGenres(null);

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with genres with null value.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadGenres() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), null));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with genres with genre with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullGenreId() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), GenreUtils.newGenreTO(null)));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with genres with genre with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullGenreName() {
        final MovieTO movie = MovieUtils.newMovieTO(1);
        final GenreTO badGenre = GenreUtils.newGenreTO(1);
        badGenre.setName(null);
        movie.setGenres(CollectionUtils.newList(GenreUtils.newGenreTO(1), badGenre));

        movieFacade.update(movie);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with movie with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_BadId() {
        movieFacade.update(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        movieFacade.remove(MovieUtils.newMovieTO(1));

        assertNull(MovieUtils.getMovie(entityManager, 1));

        assertEquals(MovieUtils.MOVIES_COUNT - 1, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        movieFacade.remove(null);
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_NullId() {
        movieFacade.remove(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with movie with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_BadId() {
        movieFacade.remove(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final Medium medium1 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT - 1);
        medium1.setId(MediumUtils.MEDIA_COUNT + 1);
        final Medium medium2 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT);
        medium2.setId(MediumUtils.MEDIA_COUNT + 2);
        final Movie movie = MovieUtils.getMovie(MovieUtils.MOVIES_COUNT);
        movie.setId(MovieUtils.MOVIES_COUNT + 1);
        movie.setMedia(CollectionUtils.newList(medium1, medium2));
        movie.setGenres(CollectionUtils.newList(GenreUtils.getGenre(GenreUtils.GENRES_COUNT - 1), GenreUtils.getGenre(GenreUtils.GENRES_COUNT)));

        movieFacade.duplicate(MovieUtils.newMovieTO(MovieUtils.MOVIES_COUNT));

        final Movie duplicatedMovie = MovieUtils.getMovie(entityManager, MovieUtils.MOVIES_COUNT + 1);
        MovieUtils.assertMovieDeepEquals(movie, duplicatedMovie);

        assertEquals(MovieUtils.MOVIES_COUNT + 1, MovieUtils.getMoviesCount(entityManager));
        assertEquals(MediumUtils.MEDIA_COUNT + 2, MediumUtils.getMediaCount(entityManager));
        assertEquals(GenreUtils.GENRES_COUNT, GenreUtils.getGenresCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        movieFacade.duplicate(null);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_NullId() {
        movieFacade.duplicate(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with movie with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_BadId() {
        movieFacade.duplicate(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final Movie movie1 = MovieUtils.getMovie(1);
        movie1.setPosition(1);
        final Movie movie2 = MovieUtils.getMovie(2);
        movie2.setPosition(0);

        movieFacade.moveUp(MovieUtils.newMovieTO(2));
        MovieUtils.assertMovieDeepEquals(movie1, MovieUtils.getMovie(entityManager, 1));
        MovieUtils.assertMovieDeepEquals(movie2, MovieUtils.getMovie(entityManager, 2));
        for (int i = 3; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), MovieUtils.getMovie(entityManager, i));
        }

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        movieFacade.moveUp(null);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NullId() {
        movieFacade.moveUp(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        movieFacade.moveUp(MovieUtils.newMovieTO(1));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_BadId() {
        movieFacade.moveUp(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final Movie movie1 = MovieUtils.getMovie(1);
        movie1.setPosition(1);
        final Movie movie2 = MovieUtils.getMovie(2);
        movie2.setPosition(0);

        movieFacade.moveDown(MovieUtils.newMovieTO(1));
        MovieUtils.assertMovieDeepEquals(movie1, MovieUtils.getMovie(entityManager, 1));
        MovieUtils.assertMovieDeepEquals(movie2, MovieUtils.getMovie(entityManager, 2));
        for (int i = 3; i <= MovieUtils.MOVIES_COUNT; i++) {
            MovieUtils.assertMovieDeepEquals(MovieUtils.getMovie(i), MovieUtils.getMovie(entityManager, i));
        }

        assertEquals(MovieUtils.MOVIES_COUNT, MovieUtils.getMoviesCount(entityManager));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        movieFacade.moveDown(null);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with movie with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NullId() {
        movieFacade.moveDown(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        movieFacade.moveDown(MovieUtils.newMovieTO(MovieUtils.MOVIES_COUNT));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_BadId() {
        movieFacade.moveDown(MovieUtils.newMovieTO(Integer.MAX_VALUE));
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
