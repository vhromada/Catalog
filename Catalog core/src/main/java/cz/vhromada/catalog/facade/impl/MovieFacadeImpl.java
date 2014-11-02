package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
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
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
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

	/** Service for movies */
	@Autowired
	private MovieService movieService;

	/** Service for genres */
	@Autowired
	private GenreService genreService;

	/** Conversion service */
	@Autowired
	@Qualifier("coreConversionService")
	private ConversionService conversionService;

	/** Validator for TO for movie */
	@Autowired
	private MovieTOValidator movieTOValidator;

	/**
	 * Returns service for movies.
	 *
	 * @return service for movies
	 */
	public MovieService getMovieService() {
		return movieService;
	}

	/**
	 * Sets a new value to service for movies.
	 *
	 * @param movieService new value
	 */
	public void setMovieService(final MovieService movieService) {
		this.movieService = movieService;
	}

	/**
	 * Returns service for genres.
	 *
	 * @return service for genres
	 */
	public GenreService getGenreService() {
		return genreService;
	}

	/**
	 * Sets a new value to service for genres.
	 *
	 * @param genreService new value
	 */
	public void setGenreService(final GenreService genreService) {
		this.genreService = genreService;
	}

	/**
	 * Returns conversion service.
	 *
	 * @return conversion service
	 */
	public ConversionService getConversionService() {
		return conversionService;
	}

	/**
	 * Sets a new value to conversion service.
	 *
	 * @param conversionService new value
	 */
	public void setConversionService(final ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	/**
	 * Returns validator for TO for movie.
	 *
	 * @return validator for TO for movie
	 */
	public MovieTOValidator getMovieTOValidator() {
		return movieTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for movie.
	 *
	 * @param movieTOValidator new value
	 */
	public void setMovieTOValidator(final MovieTOValidator movieTOValidator) {
		this.movieTOValidator = movieTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void newData() {
		Validators.validateFieldNotNull(movieService, "Service for movies");

		try {
			movieService.newData();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or conversion service isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MovieTO> getMovies() {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(conversionService, "Conversion service");

		try {
			final List<MovieTO> movies = new ArrayList<>();
			for (Movie movie : movieService.getMovies()) {
				movies.add(conversionService.convert(movie, MovieTO.class));
			}
			Collections.sort(movies);
			return movies;
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or conversion service isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public MovieTO getMovie(final Integer id) {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return conversionService.convert(movieService.getMovie(id), MovieTO.class);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or service for genres isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for movie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final MovieTO movie) {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(movieTOValidator, "Validator for TO for movie");
		movieTOValidator.validateNewMovieTO(movie);
		try {
			for (GenreTO genre : movie.getGenres()) {
				Validators.validateExists(genreService.getGenre(genre.getId()), "TO for genre");
			}

			final Movie movieEntity = conversionService.convert(movie, Movie.class);
			movieService.add(movieEntity);
			if (movieEntity.getId() == null) {
				throw new FacadeOperationException("Service tier doesn't set ID.");
			}
			movie.setId(movieEntity.getId());
			movie.setPosition(movieEntity.getPosition());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or service for genres isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for movie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final MovieTO movie) {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(movieTOValidator, "Validator for TO for movie");
		movieTOValidator.validateExistingMovieTO(movie);
		try {
			final Movie movieEntity = conversionService.convert(movie, Movie.class);
			Validators.validateExists(movieService.exists(movieEntity), "TO for movie");
			for (GenreTO genre : movie.getGenres()) {
				Validators.validateExists(genreService.getGenre(genre.getId()), "TO for genre");
			}

			movieService.update(movieEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or validator for TO for movie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final MovieTO movie) {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(movieTOValidator, "Validator for TO for movie");
		movieTOValidator.validateMovieTOWithId(movie);
		try {
			final Movie movieEntity = movieService.getMovie(movie.getId());
			Validators.validateExists(movieEntity, "TO for movie");

			movieService.remove(movieEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or validator for TO for movie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final MovieTO movie) {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(movieTOValidator, "Validator for TO for movie");
		movieTOValidator.validateMovieTOWithId(movie);
		try {
			final Movie oldMovie = movieService.getMovie(movie.getId());
			Validators.validateExists(oldMovie, "TO for movie");

			movieService.duplicate(oldMovie);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or validator for TO for movie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveUp(final MovieTO movie) {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(movieTOValidator, "Validator for TO for movie");
		movieTOValidator.validateMovieTOWithId(movie);
		try {
			final Movie movieEntity = movieService.getMovie(movie.getId());
			Validators.validateExists(movieEntity, "TO for movie");
			final List<Movie> movies = movieService.getMovies();
			Validators.validateMoveUp(movies, movieEntity, "movie");

			movieService.moveUp(movieEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or validator for TO for movie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void moveDown(final MovieTO movie) {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(movieTOValidator, "Validator for TO for movie");
		movieTOValidator.validateMovieTOWithId(movie);
		try {
			final Movie movieEntity = movieService.getMovie(movie.getId());
			Validators.validateExists(movieEntity, "TO for movie");
			final List<Movie> movies = movieService.getMovies();
			Validators.validateMoveDown(movies, movieEntity, "movie");

			movieService.moveDown(movieEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for movie isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final MovieTO movie) {
		Validators.validateFieldNotNull(movieService, "Service for movies");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(movieTOValidator, "Validator for TO for movie");
		movieTOValidator.validateMovieTOWithId(movie);
		try {

			return movieService.exists(conversionService.convert(movie, Movie.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void updatePositions() {
		Validators.validateFieldNotNull(movieService, "Service for movies");

		try {
			movieService.updatePositions();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public int getTotalMediaCount() {
		Validators.validateFieldNotNull(movieService, "Service for movies");

		try {
			return movieService.getTotalMediaCount();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for movies isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public Time getTotalLength() {
		Validators.validateFieldNotNull(movieService, "Service for movies");

		try {
			return movieService.getTotalLength();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

}
