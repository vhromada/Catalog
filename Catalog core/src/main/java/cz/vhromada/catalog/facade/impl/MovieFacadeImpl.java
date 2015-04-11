package cz.vhromada.catalog.facade.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
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
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for movies.
 *
 * @author Vladimir Hromada
 */
@Component("movieFacade")
@Transactional
public class MovieFacadeImpl implements MovieFacade {

    /**
     * Service for movies argument
     */
    private static final String MOVIE_SERVICE_ARGUMENT = "Service for movies";

    /**
     * Service for genres argument
     */
    private static final String GENRE_SERVICE_ARGUMENT = "Service for genres";

    /**
     * Converter argument
     */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /**
     * Validator for TO for movie argument
     */
    private static final String MOVIE_TO_VALIDATOR_ARGUMENT = "Validator for TO for movie";

    /**
     * Movie argument
     */
    private static final String MOVIE_ARGUMENT = "movie";

    /**
     * TO for movie argument
     */
    private static final String MOVIE_TO_ARGUMENT = "TO for movie";

    /**
     * TO for genre argument
     */
    private static final String GENRE_TO_ARGUMENT = "TO for genre";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link FacadeOperationException}
     */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /**
     * Message for not setting ID
     */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /**
     * Service for movies
     */
    private MovieService movieService;

    /**
     * Service for genres
     */
    private GenreService genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for movie
     */
    private MovieTOValidator movieTOValidator;

    /**
     * Creates a new instance of MovieFacadeImpl.
     *
     * @param movieService     service for movies
     * @param genreService     service for genres
     * @param converter        converter
     * @param movieTOValidator validator for TO for movie
     * @throws IllegalArgumentException if service for movies is null
     *                                  or service for genres is null
     *                                  or converter is null
     *                                  or validator for TO for movie is null
     */
    @Autowired
    public MovieFacadeImpl(final MovieService movieService,
            final GenreService genreService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final MovieTOValidator movieTOValidator) {
        Validators.validateArgumentNotNull(movieService, MOVIE_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(genreService, GENRE_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(movieTOValidator, MOVIE_TO_VALIDATOR_ARGUMENT);

        this.movieService = movieService;
        this.genreService = genreService;
        this.converter = converter;
        this.movieTOValidator = movieTOValidator;
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            movieService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MovieTO> getMovies() {
        try {
            final List<MovieTO> movies = converter.convertCollection(movieService.getMovies(), MovieTO.class);
            Collections.sort(movies);
            return movies;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MovieTO getMovie(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(movieService.getMovie(id), MovieTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    public void add(final MovieTO movie) {
        movieTOValidator.validateNewMovieTO(movie);
        try {
            for (final GenreTO genre : movie.getGenres()) {
                Validators.validateExists(genreService.getGenre(genre.getId()), GENRE_TO_ARGUMENT);
            }

            final Movie movieEntity = converter.convert(movie, Movie.class);
            movieService.add(movieEntity);
            if (movieEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            movie.setId(movieEntity.getId());
            movie.setPosition(movieEntity.getPosition());
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void update(final MovieTO movie) {
        movieTOValidator.validateExistingMovieTO(movie);
        try {
            final Movie movieEntity = converter.convert(movie, Movie.class);
            Validators.validateExists(movieService.exists(movieEntity), MOVIE_TO_ARGUMENT);
            for (final GenreTO genre : movie.getGenres()) {
                Validators.validateExists(genreService.getGenre(genre.getId()), GENRE_TO_ARGUMENT);
            }

            movieService.update(movieEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void remove(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        try {
            final Movie movieEntity = movieService.getMovie(movie.getId());
            Validators.validateExists(movieEntity, MOVIE_TO_ARGUMENT);

            movieService.remove(movieEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void duplicate(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        try {
            final Movie oldMovie = movieService.getMovie(movie.getId());
            Validators.validateExists(oldMovie, MOVIE_TO_ARGUMENT);

            movieService.duplicate(oldMovie);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveUp(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        try {
            final Movie movieEntity = movieService.getMovie(movie.getId());
            Validators.validateExists(movieEntity, MOVIE_TO_ARGUMENT);
            final List<Movie> movies = movieService.getMovies();
            Validators.validateMoveUp(movies, movieEntity, MOVIE_ARGUMENT);

            movieService.moveUp(movieEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveDown(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        try {
            final Movie movieEntity = movieService.getMovie(movie.getId());
            Validators.validateExists(movieEntity, MOVIE_TO_ARGUMENT);
            final List<Movie> movies = movieService.getMovies();
            Validators.validateMoveDown(movies, movieEntity, MOVIE_ARGUMENT);

            movieService.moveDown(movieEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final MovieTO movie) {
        movieTOValidator.validateMovieTOWithId(movie);
        try {

            return movieService.exists(converter.convert(movie, Movie.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            movieService.updatePositions();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getTotalMediaCount() {
        try {
            return movieService.getTotalMediaCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Time getTotalLength() {
        try {
            return movieService.getTotalLength();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
