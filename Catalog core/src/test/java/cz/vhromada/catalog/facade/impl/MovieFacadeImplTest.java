package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.dao.entities.Movie;
import cz.vhromada.catalog.facade.MovieFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.MovieTO;
import cz.vhromada.catalog.facade.validators.MovieTOValidator;
import cz.vhromada.catalog.service.GenreService;
import cz.vhromada.catalog.service.MovieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link MovieFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieFacadeImplTest extends ObjectGeneratorTest {

    /**
     * Instance of {@link MovieService}
     */
    @Mock
    private MovieService movieService;

    /**
     * Instance of {@link GenreService}
     */
    @Mock
    private GenreService genreService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link MovieTOValidator}
     */
    @Mock
    private MovieTOValidator movieTOValidator;

    /**
     * Instance of {@link MovieFacade}
     */
    private MovieFacade movieFacade;

    /**
     * Initializes facade for movies.
     */
    @Before
    public void setUp() {
        movieFacade = new MovieFacadeImpl(movieService, genreService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(MovieService, GenreService, Converter, MovieTOValidator)} with null service for movies.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMovieService() {
        new MovieFacadeImpl(null, genreService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(MovieService, GenreService, Converter, MovieTOValidator)} with null service for genres.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenreService() {
        new MovieFacadeImpl(movieService, null, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(MovieService, GenreService, Converter, MovieTOValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConverter() {
        new MovieFacadeImpl(movieService, genreService, null, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacadeImpl#MovieFacadeImpl(MovieService, GenreService, Converter, MovieTOValidator)} with null validator for TO for movie.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMovieTOValidator() {
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
    }

    /**
     * Test method for {@link MovieFacade#newData()} with exception in service tier.
     */
    @Test
    public void testNewDataWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(movieService).newData();

        try {
            movieFacade.newData();
            fail("Can't create new data with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).newData();
        verifyNoMoreInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#getMovies()}.
     */
    @Test
    public void testGetMovies() {
        final List<Movie> movies = CollectionUtils.newList(generate(Movie.class), generate(Movie.class));
        final List<MovieTO> moviesList = CollectionUtils.newList(generate(MovieTO.class), generate(MovieTO.class));
        when(movieService.getMovies()).thenReturn(movies);
        when(converter.convertCollection(movies, MovieTO.class)).thenReturn(moviesList);

        DeepAsserts.assertEquals(moviesList, movieFacade.getMovies());

        verify(movieService).getMovies();
        verify(converter).convertCollection(movies, MovieTO.class);
        verifyNoMoreInteractions(movieService, converter);
    }

    /**
     * Test method for {@link MovieFacade#getMovies()} with exception in service tier.
     */
    @Test
    public void testGetMoviesWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(movieService).getMovies();

        try {
            movieFacade.getMovies();
            fail("Can't get movies with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).getMovies();
        verifyNoMoreInteractions(movieService);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)} with existing movie.
     */
    @Test
    public void testGetMovieWithExistingMovie() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(movie);
        when(converter.convert(any(Movie.class), eq(MovieTO.class))).thenReturn(movieTO);

        DeepAsserts.assertEquals(movieTO, movieFacade.getMovie(movieTO.getId()));

        verify(movieService).getMovie(movieTO.getId());
        verify(converter).convert(movie, MovieTO.class);
        verify(converter).convert(movie, MovieTO.class);
        verifyNoMoreInteractions(movieService, converter);
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)} with not existing movie.
     */
    @Test
    public void testGetMovieWithNotExistingMovie() {
        when(movieService.getMovie(anyInt())).thenReturn(null);
        when(converter.convert(any(Movie.class), eq(MovieTO.class))).thenReturn(null);

        assertNull(movieFacade.getMovie(Integer.MAX_VALUE));

        verify(movieService).getMovie(Integer.MAX_VALUE);
        verify(converter).convert(null, MovieTO.class);
        verifyNoMoreInteractions(movieService, converter);
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)} with null argument.
     */
    @Test
    public void testGetMovieWithNullArgument() {
        try {
            movieFacade.getMovie(null);
            fail("Can't get movie with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(movieService, converter);
    }

    /**
     * Test method for {@link MovieFacade#getMovie(Integer)} with exception in service tier.
     */
    @Test
    public void testGetMovieWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(movieService).getMovie(anyInt());

        try {
            movieFacade.getMovie(Integer.MAX_VALUE);
            fail("Can't get movie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).getMovie(Integer.MAX_VALUE);
        verifyNoMoreInteractions(movieService);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)}.
     */
    @Test
    public void testAdd() {
        final Movie movie = generate(Movie.class);
        movie.setId(null);
        final MovieTO movieTO = generate(MovieTO.class);
        movieTO.setId(null);
        final int id = generate(Integer.class);
        final int position = generate(Integer.class);
        doAnswer(setMovieIdAndPosition(id, position)).when(movieService).add(any(Movie.class));
        when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
        when(converter.convert(any(MovieTO.class), eq(Movie.class))).thenReturn(movie);

        movieFacade.add(movieTO);
        DeepAsserts.assertEquals(id, movie.getId());
        DeepAsserts.assertEquals(position, movie.getPosition());

        verify(movieService).add(movie);
        for (final GenreTO genre : movieTO.getGenres()) {
            verify(genreService).getGenre(genre.getId());
        }
        verify(converter).convert(movieTO, Movie.class);
        verify(movieTOValidator).validateNewMovieTO(movieTO);
        verifyNoMoreInteractions(movieService, genreService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with null argument.
     */
    @Test
    public void testAddWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(movieTOValidator).validateNewMovieTO(any(MovieTO.class));

        try {
            movieFacade.add(null);
            fail("Can't add movie with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(movieTOValidator).validateNewMovieTO(null);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService, genreService, converter);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with argument with bad data.
     */
    @Test
    public void testAddWithBadArgument() {
        final MovieTO movie = generate(MovieTO.class);
        movie.setId(null);
        doThrow(ValidationException.class).when(movieTOValidator).validateNewMovieTO(any(MovieTO.class));

        try {
            movieFacade.add(movie);
            fail("Can't add movie with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieTOValidator).validateNewMovieTO(movie);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService, genreService, converter);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with service tier not setting ID.
     */
    @Test
    public void testAddWithNotServiceTierSettingID() {
        final Movie movie = generate(Movie.class);
        movie.setId(null);
        final MovieTO movieTO = generate(MovieTO.class);
        movieTO.setId(null);
        when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
        when(converter.convert(any(MovieTO.class), eq(Movie.class))).thenReturn(movie);

        try {
            movieFacade.add(movieTO);
            fail("Can't add movie with service tier not setting ID.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).add(movie);
        for (final GenreTO genre : movieTO.getGenres()) {
            verify(genreService).getGenre(genre.getId());
        }
        verify(converter).convert(movieTO, Movie.class);
        verify(movieTOValidator).validateNewMovieTO(movieTO);
        verifyNoMoreInteractions(movieService, genreService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#add(MovieTO)} with exception in service tier.
     */
    @Test
    public void testAddWithServiceTierException() {
        final MovieTO movie = generate(MovieTO.class);
        movie.setId(null);
        doThrow(ServiceOperationException.class).when(genreService).getGenre(anyInt());

        try {
            movieFacade.add(movie);
            fail("Can't add movie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(genreService).getGenre(movie.getGenres().get(0).getId());
        verify(movieTOValidator).validateNewMovieTO(movie);
        verifyNoMoreInteractions(genreService, movieTOValidator);
        verifyZeroInteractions(movieService, converter);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)}.
     */
    @Test
    public void testUpdate() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.exists(any(Movie.class))).thenReturn(true);
        when(genreService.getGenre(anyInt())).thenReturn(mock(Genre.class));
        when(converter.convert(any(MovieTO.class), eq(Movie.class))).thenReturn(movie);

        movieFacade.update(movieTO);

        verify(movieService).exists(movie);
        verify(movieService).update(movie);
        for (final GenreTO genre : movieTO.getGenres()) {
            verify(genreService).getGenre(genre.getId());
        }
        verify(converter).convert(movieTO, Movie.class);
        verify(movieTOValidator).validateExistingMovieTO(movieTO);
        verifyNoMoreInteractions(movieService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with null argument.
     */
    @Test
    public void testUpdateWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(movieTOValidator).validateExistingMovieTO(any(MovieTO.class));

        try {
            movieFacade.update(null);
            fail("Can't update movie with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(movieTOValidator).validateExistingMovieTO(null);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService, genreService, converter);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with argument with bad data.
     */
    @Test
    public void testUpdateWithBadArgument() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ValidationException.class).when(movieTOValidator).validateExistingMovieTO(any(MovieTO.class));

        try {
            movieFacade.update(movie);
            fail("Can't update movie with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieTOValidator).validateExistingMovieTO(movie);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService, genreService, converter);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with not existing argument.
     */
    @Test
    public void testUpdateWithNotExistingArgument() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.exists(any(Movie.class))).thenReturn(false);
        when(converter.convert(any(MovieTO.class), eq(Movie.class))).thenReturn(movie);

        try {
            movieFacade.update(movieTO);
            fail("Can't update movie with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(movieService).exists(movie);
        verify(converter).convert(movieTO, Movie.class);
        verify(movieTOValidator).validateExistingMovieTO(movieTO);
        verifyNoMoreInteractions(movieService, genreService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#update(MovieTO)} with exception in service tier.
     */
    @Test
    public void testUpdateWithServiceTierException() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        doThrow(ServiceOperationException.class).when(movieService).exists(any(Movie.class));
        when(converter.convert(any(MovieTO.class), eq(Movie.class))).thenReturn(movie);

        try {
            movieFacade.update(movieTO);
            fail("Can't update movie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).exists(movie);
        verify(converter).convert(movieTO, Movie.class);
        verify(movieTOValidator).validateExistingMovieTO(movieTO);
        verifyNoMoreInteractions(movieService, converter, movieTOValidator);
        verifyZeroInteractions(genreService);
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)}.
     */
    @Test
    public void testRemove() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(movie);

        movieFacade.remove(movieTO);

        verify(movieService).getMovie(movieTO.getId());
        verify(movieService).remove(movie);
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with null argument.
     */
    @Test
    public void testRemoveWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.remove(null);
            fail("Can't remove movie with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(null);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with argument with bad data.
     */
    @Test
    public void testRemoveWithBadArgument() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ValidationException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.remove(movie);
            fail("Can't remove movie with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with not existing argument.
     */
    @Test
    public void testRemoveWithNotExistingArgument() {
        final MovieTO movie = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(null);

        try {
            movieFacade.remove(movie);
            fail("Can't remove movie with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(movieService).getMovie(movie.getId());
        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#remove(MovieTO)} with exception in service tier.
     */
    @Test
    public void testRemoveWithServiceTierException() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ServiceOperationException.class).when(movieService).getMovie(anyInt());

        try {
            movieFacade.remove(movie);
            fail("Can't remove movie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).getMovie(movie.getId());
        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)}.
     */
    @Test
    public void testDuplicate() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(movie);

        movieFacade.duplicate(movieTO);

        verify(movieService).getMovie(movieTO.getId());
        verify(movieService).duplicate(movie);
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with null argument.
     */
    @Test
    public void testDuplicateWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.duplicate(null);
            fail("Can't duplicate movie with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(null);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with argument with bad data.
     */
    @Test
    public void testDuplicateWithBadArgument() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ValidationException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.duplicate(movie);
            fail("Can't duplicate movie with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with not existing argument.
     */
    @Test
    public void testDuplicateWithNotExistingArgument() {
        final MovieTO movie = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(null);

        try {
            movieFacade.duplicate(movie);
            fail("Can't duplicate movie with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(movieService).getMovie(movie.getId());
        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#duplicate(MovieTO)} with exception in service tier.
     */
    @Test
    public void testDuplicateWithServiceTierException() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ServiceOperationException.class).when(movieService).getMovie(anyInt());

        try {
            movieFacade.duplicate(movie);
            fail("Can't duplicate movie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).getMovie(movie.getId());
        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)}.
     */
    @Test
    public void testMoveUp() {
        final Movie movie = generate(Movie.class);
        final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), movie);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(movie);
        when(movieService.getMovies()).thenReturn(movies);

        movieFacade.moveUp(movieTO);

        verify(movieService).getMovie(movieTO.getId());
        verify(movieService).getMovies();
        verify(movieService).moveUp(movie);
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with null argument.
     */
    @Test
    public void testMoveUpWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.moveUp(null);
            fail("Can't move up movie with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(null);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with argument with bad data.
     */
    @Test
    public void testMoveUpWithBadArgument() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ValidationException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.moveUp(movie);
            fail("Can't move up movie with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with not existing argument.
     */
    @Test
    public void testMoveUpWithNotExistingArgument() {
        final MovieTO movie = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(null);

        try {
            movieFacade.moveUp(movie);
            fail("Can't move up movie with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(movieService).getMovie(movie.getId());
        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with not movable argument.
     */
    @Test
    public void testMoveUpWithNotMovableArgument() {
        final Movie movie = generate(Movie.class);
        final List<Movie> movies = CollectionUtils.newList(movie, mock(Movie.class));
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(movie);
        when(movieService.getMovies()).thenReturn(movies);

        try {
            movieFacade.moveUp(movieTO);
            fail("Can't move up movie with not thrown ValidationException for not movable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieService).getMovie(movieTO.getId());
        verify(movieService).getMovies();
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#moveUp(MovieTO)} with exception in service tier.
     */
    @Test
    public void testMoveUpWithServiceTierException() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ServiceOperationException.class).when(movieService).getMovie(anyInt());

        try {
            movieFacade.moveUp(movie);
            fail("Can't move up movie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).getMovie(movie.getId());
        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)}.
     */
    @Test
    public void testMoveDown() {
        final Movie movie = generate(Movie.class);
        final List<Movie> movies = CollectionUtils.newList(movie, mock(Movie.class));
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(movie);
        when(movieService.getMovies()).thenReturn(movies);

        movieFacade.moveDown(movieTO);

        verify(movieService).getMovie(movieTO.getId());
        verify(movieService).getMovies();
        verify(movieService).moveDown(movie);
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with null argument.
     */
    @Test
    public void testMoveDownWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.moveDown(null);
            fail("Can't move down movie with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(null);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with argument with bad data.
     */
    @Test
    public void testMoveDownWithBadArgument() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ValidationException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.moveDown(movie);
            fail("Can't move down movie with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with not existing argument.
     */
    @Test
    public void testMoveDownWithNotExistingArgument() {
        final MovieTO movie = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(null);

        try {
            movieFacade.moveDown(movie);
            fail("Can't move down movie with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(movieService).getMovie(movie.getId());
        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with not movable argument.
     */
    @Test
    public void testMoveDownWithNotMovableArgument() {
        final Movie movie = generate(Movie.class);
        final List<Movie> movies = CollectionUtils.newList(mock(Movie.class), movie);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.getMovie(anyInt())).thenReturn(movie);
        when(movieService.getMovies()).thenReturn(movies);

        try {
            movieFacade.moveDown(movieTO);
            fail("Can't move down movie with not thrown ValidationException for not movable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieService).getMovie(movieTO.getId());
        verify(movieService).getMovies();
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#moveDown(MovieTO)} with exception in service tier.
     */
    @Test
    public void testMoveDownWithServiceTierException() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ServiceOperationException.class).when(movieService).getMovie(anyInt());

        try {
            movieFacade.moveDown(movie);
            fail("Can't move down movie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).getMovie(movie.getId());
        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieService, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#exists(MovieTO)} with existing movie.
     */
    @Test
    public void testExistsWithExistingMovie() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.exists(any(Movie.class))).thenReturn(true);
        when(converter.convert(any(MovieTO.class), eq(Movie.class))).thenReturn(movie);

        assertTrue(movieFacade.exists(movieTO));

        verify(movieService).exists(movie);
        verify(converter).convert(movieTO, Movie.class);
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#exists(MovieTO)} with not existing movie.
     */
    @Test
    public void testExistsWithNotExistingMovie() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        when(movieService.exists(any(Movie.class))).thenReturn(false);
        when(converter.convert(any(MovieTO.class), eq(Movie.class))).thenReturn(movie);

        assertFalse(movieFacade.exists(movieTO));

        verify(movieService).exists(movie);
        verify(converter).convert(movieTO, Movie.class);
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#exists(MovieTO)} with null argument.
     */
    @Test
    public void testExistsWithNullArgument() {
        doThrow(IllegalArgumentException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.exists(null);
            fail("Can't exists movie with not thrown IllegalArgumentException for null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(null);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService, converter);
    }

    /**
     * Test method for {@link MovieFacade#exists(MovieTO)} with argument with bad data.
     */
    @Test
    public void testExistsWithBadArgument() {
        final MovieTO movie = generate(MovieTO.class);
        doThrow(ValidationException.class).when(movieTOValidator).validateMovieTOWithId(any(MovieTO.class));

        try {
            movieFacade.exists(movie);
            fail("Can't exists movie with not thrown ValidationException for argument with bad data.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(movieTOValidator).validateMovieTOWithId(movie);
        verifyNoMoreInteractions(movieTOValidator);
        verifyZeroInteractions(movieService, converter);
    }

    /**
     * Test method for {@link MovieFacade#exists(MovieTO)} with exception in service tier.
     */
    @Test
    public void testExistsWithServiceTierException() {
        final Movie movie = generate(Movie.class);
        final MovieTO movieTO = generate(MovieTO.class);
        doThrow(ServiceOperationException.class).when(movieService).exists(any(Movie.class));
        when(converter.convert(any(MovieTO.class), eq(Movie.class))).thenReturn(movie);

        try {
            movieFacade.exists(movieTO);
            fail("Can't exists movie with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).exists(movie);
        verify(converter).convert(movieTO, Movie.class);
        verify(movieTOValidator).validateMovieTOWithId(movieTO);
        verifyNoMoreInteractions(movieService, converter, movieTOValidator);
    }

    /**
     * Test method for {@link MovieFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        movieFacade.updatePositions();

        verify(movieService).updatePositions();
        verifyNoMoreInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#updatePositions()} with exception in service tier.
     */
    @Test
    public void testUpdatePositionsWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(movieService).updatePositions();

        try {
            movieFacade.updatePositions();
            fail("Can't update positions with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).updatePositions();
        verifyNoMoreInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        final int count = generate(Integer.class);
        when(movieService.getTotalMediaCount()).thenReturn(count);

        DeepAsserts.assertEquals(count, movieFacade.getTotalMediaCount());

        verify(movieService).getTotalMediaCount();
        verifyNoMoreInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#getTotalMediaCount()} with exception in service tier.
     */
    @Test
    public void testGetTotalMediaCountWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(movieService).getTotalMediaCount();

        try {
            movieFacade.getTotalMediaCount();
            fail("Can't get total media count with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).getTotalMediaCount();
        verifyNoMoreInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()}.
     */
    @Test
    public void testGetTotalLength() {
        final Time length = generate(Time.class);
        when(movieService.getTotalLength()).thenReturn(length);

        DeepAsserts.assertEquals(length, movieFacade.getTotalLength());

        verify(movieService).getTotalLength();
        verifyNoMoreInteractions(movieService);
    }

    /**
     * Test method for {@link MovieFacade#getTotalLength()} with exception in service tier.
     */
    @Test
    public void testGetTotalLengthWithServiceTierException() {
        doThrow(ServiceOperationException.class).when(movieService).getTotalLength();

        try {
            movieFacade.getTotalLength();
            fail("Can't get total length count with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(movieService).getTotalLength();
        verifyNoMoreInteractions(movieService);
    }

    /**
     * Sets movie's ID and position.
     *
     * @param id       ID
     * @param position position
     * @return mocked answer
     */
    private static Answer<Void> setMovieIdAndPosition(final Integer id, final int position) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                final Movie movie = (Movie) invocation.getArguments()[0];
                movie.setId(id);
                movie.setPosition(position);
                return null;
            }

        };
    }

}
