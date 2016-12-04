package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.GenreUtils;
import cz.vhromada.catalog.common.MovieUtils;
import cz.vhromada.catalog.common.Time;
import cz.vhromada.catalog.domain.Medium;
import cz.vhromada.catalog.entity.Genre;
import cz.vhromada.catalog.entity.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.MovieValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieFacadeImplTest {

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<cz.vhromada.catalog.domain.Movie> movieService;

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<cz.vhromada.catalog.domain.Genre> genreService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link MovieValidator}
     */
    @Mock
    private MovieValidator movieValidator;

    /**
     * Instance of {@link MovieFacade}
     */
    private MovieFacade movieFacade;

    /**
     * Initializes facade for movies.
     */
    @Before
    public void setUp() {
        movieFacade = new MovieFacadeImpl(movieService, genreService, converter, movieValidator);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(CatalogService, CatalogService, Converter, MovieValidator)} with null service for movies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMovieService() {
        new MovieFacadeImpl(null, genreService, converter, movieValidator);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(CatalogService, CatalogService, Converter, MovieValidator)} with null service for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullGenreService() {
        new MovieFacadeImpl(movieService, null, converter, movieValidator);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(CatalogService, CatalogService, Converter, MovieValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new MovieFacadeImpl(movieService, genreService, null, movieValidator);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(CatalogService, CatalogService, Converter, MovieValidator)} with null validator for TO for
     * movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullMovieTOValidator() {
        new MovieFacadeImpl(movieService, genreService, converter, null);
    }

    /**
     * Test method for {@link MovieFacade#newData()}.
     */
    @Test
    public void testNewData() {
        movieFacade.newData();

        verify(movieService).newData();
        verifyNoMoreInteractions(movieService);
        verifyZeroInteractions(genreService, converter, movieValidator);
    }

    /**
     * Test method for {@link MovieFacade#getMovies()}.
     */
    @Test
    public void testGetMovies() {
        final List<cz.vhromada.catalog.domain.Movie> movieList = CollectionUtils.newList(MovieUtils.newMovie(1), MovieUtils.newMovie(2));
        final List<Movie> expectedMovies = CollectionUtils.newList(MovieUtils.newMovieTO(1), MovieUtils.newMovieTO(2));

        when(movieService.getAll()).thenReturn(movieList);
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Movie.class), eq(Movie.class))).thenReturn(expectedMovies);

        final List<Movie> movies = movieFacade.getMovies();

        assertNotNull(movies);
        assertEquals(expectedMovies, movies);

        verify(movieService).getAll();
        verify(converter).convertCollection(movieList, Movie.class);
        verifyNoMoreInteractions(movieService, converter);
        verifyZeroInteractions(genreService, movieValidator);
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)} with existing movie.
     */
    @Test
    public void testGetMovie_ExistingMovie() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(1);
        final Movie expectedMovie = MovieUtils.newMovieTO(1);

        when(movieService.get(anyInt())).thenReturn(movieEntity);
        when(converter.convert(any(cz.vhromada.catalog.domain.Movie.class), eq(Movie.class))).thenReturn(expectedMovie);

        final Movie movie = movieFacade.getMovie(1);

        assertNotNull(movie);
        assertEquals(expectedMovie, movie);

        verify(movieService).get(1);
        verify(converter).convert(movieEntity, Movie.class);
        verifyNoMoreInteractions(movieService, converter);
        verifyZeroInteractions(genreService, movieValidator);
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)} with not existing movie.
     */
    @Test
    public void testGetMovie_NotExistingMovie() {
        when(movieService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(cz.vhromada.catalog.domain.Movie.class), eq(Movie.class))).thenReturn(null);

        assertNull(movieFacade.getMovie(Integer.MAX_VALUE));

        verify(movieService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, Movie.class);
        verifyNoMoreInteractions(movieService, converter);
        verifyZeroInteractions(genreService, movieValidator);
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
    public void testAdd() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(null);
        final Movie movie = MovieUtils.newMovieTO(null);

        when(genreService.get(anyInt())).thenReturn(GenreUtils.newGenre(1));
        when(converter.convert(any(Movie.class), eq(cz.vhromada.catalog.domain.Movie.class))).thenReturn(movieEntity);

        movieFacade.add(movie);

        verify(movieService).add(movieEntity);
        for (final Genre genre : movie.getGenres()) {
            verify(genreService).get(genre.getId());
        }
        verify(converter).convert(movie, cz.vhromada.catalog.domain.Movie.class);
        verify(movieValidator).validateNewMovie(movie);
        verifyNoMoreInteractions(movieService, genreService, converter, movieValidator);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(movieValidator).validateNewMovie(any(Movie.class));

        movieFacade.add(null);
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadArgument() {
        doThrow(ValidationException.class).when(movieValidator).validateNewMovie(any(Movie.class));

        movieFacade.add(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#add(Movie)} with argument with not existing genre.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testAdd_NotExistingGenre() {
        when(genreService.get(anyInt())).thenReturn(null);

        movieFacade.add(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)}.
     */
    @Test
    public void testUpdate() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(null);
        final Movie movie = MovieUtils.newMovieTO(null);

        when(movieService.get(anyInt())).thenReturn(movieEntity);
        when(genreService.get(anyInt())).thenReturn(GenreUtils.newGenre(1));
        when(converter.convert(any(Movie.class), eq(cz.vhromada.catalog.domain.Movie.class))).thenReturn(movieEntity);

        movieFacade.update(movie);

        verify(movieService).get(movie.getId());
        verify(movieService).update(movieEntity);
        for (final Genre genre : movie.getGenres()) {
            verify(genreService).get(genre.getId());
        }
        verify(converter).convert(movie, cz.vhromada.catalog.domain.Movie.class);
        verify(movieValidator).validateExistingMovie(movie);
        verifyNoMoreInteractions(movieService, genreService, converter, movieValidator);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(movieValidator).validateExistingMovie(any(Movie.class));

        movieFacade.update(null);
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(movieValidator).validateExistingMovie(any(Movie.class));

        movieFacade.update(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with not existing movie.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingMovie() {
        when(movieService.get(anyInt())).thenReturn(null);

        movieFacade.update(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#update(Movie)} with not existing genre.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingGenre() {
        when(movieService.get(anyInt())).thenReturn(MovieUtils.newMovie(1));
        when(genreService.get(anyInt())).thenReturn(null);

        movieFacade.update(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#remove(Movie)}.
     */
    @Test
    public void testRemove() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(1);
        final Movie movie = MovieUtils.newMovieTO(1);

        when(movieService.get(anyInt())).thenReturn(movieEntity);

        movieFacade.remove(movie);

        verify(movieService).get(1);
        verify(movieService).remove(movieEntity);
        verify(movieValidator).validateMovieWithId(movie);
        verifyNoMoreInteractions(movieService, movieValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link MovieFacade#remove(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(movieValidator).validateMovieWithId(any(Movie.class));

        movieFacade.remove(null);
    }

    /**
     * Test method for {@link MovieFacade#remove(Movie)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(movieValidator).validateMovieWithId(any(Movie.class));

        movieFacade.remove(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#remove(Movie)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(movieService.get(anyInt())).thenReturn(null);

        movieFacade.remove(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(Movie)}.
     */
    @Test
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(1);
        final Movie movie = MovieUtils.newMovieTO(1);

        when(movieService.get(anyInt())).thenReturn(movieEntity);

        movieFacade.duplicate(movie);

        verify(movieService).get(1);
        verify(movieService).duplicate(movieEntity);
        verify(movieValidator).validateMovieWithId(movie);
        verifyNoMoreInteractions(movieService, movieValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(movieValidator).validateMovieWithId(any(Movie.class));

        movieFacade.duplicate(null);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(Movie)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(movieValidator).validateMovieWithId(any(Movie.class));

        movieFacade.duplicate(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#duplicate(Movie)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(movieService.get(anyInt())).thenReturn(null);

        movieFacade.duplicate(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)}.
     */
    @Test
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(2);
        final List<cz.vhromada.catalog.domain.Movie> movies = CollectionUtils.newList(MovieUtils.newMovie(1), movieEntity);
        final Movie movie = MovieUtils.newMovieTO(2);

        when(movieService.get(anyInt())).thenReturn(movieEntity);
        when(movieService.getAll()).thenReturn(movies);

        movieFacade.moveUp(movie);

        verify(movieService).get(2);
        verify(movieService).getAll();
        verify(movieService).moveUp(movieEntity);
        verify(movieValidator).validateMovieWithId(movie);
        verifyNoMoreInteractions(movieService, movieValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(movieValidator).validateMovieWithId(any(Movie.class));

        movieFacade.moveUp(null);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(movieValidator).validateMovieWithId(any(Movie.class));

        movieFacade.moveUp(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(movieService.get(anyInt())).thenReturn(null);

        movieFacade.moveUp(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveUp(Movie)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Movie> movies = CollectionUtils.newList(movieEntity, MovieUtils.newMovie(1));
        final Movie movie = MovieUtils.newMovieTO(Integer.MAX_VALUE);

        when(movieService.get(anyInt())).thenReturn(movieEntity);
        when(movieService.getAll()).thenReturn(movies);

        movieFacade.moveUp(movie);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)}.
     */
    @Test
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(1);
        final List<cz.vhromada.catalog.domain.Movie> movies = CollectionUtils.newList(movieEntity, MovieUtils.newMovie(2));
        final Movie movie = MovieUtils.newMovieTO(1);

        when(movieService.get(anyInt())).thenReturn(movieEntity);
        when(movieService.getAll()).thenReturn(movies);

        movieFacade.moveDown(movie);

        verify(movieService).get(1);
        verify(movieService).getAll();
        verify(movieService).moveDown(movieEntity);
        verify(movieValidator).validateMovieWithId(movie);
        verifyNoMoreInteractions(movieService, movieValidator);
        verifyZeroInteractions(genreService, converter);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(movieValidator).validateMovieWithId(any(Movie.class));

        movieFacade.moveDown(null);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(movieValidator).validateMovieWithId(any(Movie.class));

        movieFacade.moveDown(MovieUtils.newMovieTO(null));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(movieService.get(anyInt())).thenReturn(null);

        movieFacade.moveDown(MovieUtils.newMovieTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link MovieFacade#moveDown(Movie)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Movie movieEntity = MovieUtils.newMovie(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Movie> movies = CollectionUtils.newList(MovieUtils.newMovie(1), movieEntity);
        final Movie movie = MovieUtils.newMovieTO(Integer.MAX_VALUE);

        when(movieService.get(anyInt())).thenReturn(movieEntity);
        when(movieService.getAll()).thenReturn(movies);

        movieFacade.moveDown(movie);
    }

    /**
     * Test method for {@link MovieFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        movieFacade.updatePositions();

        verify(movieService).updatePositions();
        verifyNoMoreInteractions(movieService);
        verifyZeroInteractions(genreService, converter, movieValidator);
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        final cz.vhromada.catalog.domain.Movie movie1 = MovieUtils.newMovie(1);
        final cz.vhromada.catalog.domain.Movie movie2 = MovieUtils.newMovie(2);
        final int expectedCount = movie1.getMedia().size() + movie2.getMedia().size();

        when(movieService.getAll()).thenReturn(CollectionUtils.newList(movie1, movie2));

        assertEquals(expectedCount, movieFacade.getTotalMediaCount());

        verify(movieService).getAll();
        verifyNoMoreInteractions(movieService);
        verifyZeroInteractions(genreService, converter, movieValidator);
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final List<cz.vhromada.catalog.domain.Movie> movies = CollectionUtils.newList(MovieUtils.newMovie(1), MovieUtils.newMovie(2));
        int expectedTotalLength = 0;
        for (final cz.vhromada.catalog.domain.Movie movie : movies) {
            for (final Medium medium : movie.getMedia()) {
                expectedTotalLength += medium.getLength();
            }
        }

        when(movieService.getAll()).thenReturn(movies);

        assertEquals(new Time(expectedTotalLength), movieFacade.getTotalLength());

        verify(movieService).getAll();
        verifyNoMoreInteractions(movieService);
        verifyZeroInteractions(genreService, converter, movieValidator);
    }

}
