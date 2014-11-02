package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Genre;
import cz.vhromada.catalog.facade.GenreFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.validators.GenreTOValidator;
import cz.vhromada.catalog.service.GenreService;
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
 * A class represents implementation of facade for genres.
 *
 * @author Vladimir Hromada
 */
@Component("genreFacade")
@Transactional
public class GenreFacadeImpl implements GenreFacade {

	/** Service for genres */
	@Autowired
	private GenreService genreService;

	/** Conversion service */
	@Autowired
	@Qualifier("coreConversionService")
	private ConversionService conversionService;

	/** Validator for TO for genre */
	@Autowired
	private GenreTOValidator genreTOValidator;

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
	 * Returns validator for TO for genre.
	 *
	 * @return validator for TO for genre
	 */
	public GenreTOValidator getGenreTOValidator() {
		return genreTOValidator;
	}

	/**
	 * Sets a new value to validator for TO for genre.
	 *
	 * @param genreTOValidator new value
	 */
	public void setGenreTOValidator(final GenreTOValidator genreTOValidator) {
		this.genreTOValidator = genreTOValidator;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void newData() {
		Validators.validateFieldNotNull(genreService, "Service for genres");

		try {
			genreService.newData();
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 *                                  or conversion service isn't set
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<GenreTO> getGenres() {
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");

		try {
			final List<GenreTO> genres = new ArrayList<>();
			for (Genre genre : genreService.getGenres()) {
				genres.add(conversionService.convert(genre, GenreTO.class));
			}
			Collections.sort(genres);
			return genres;
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 *                                  or conversion service isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public GenreTO getGenre(final Integer id) {
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateArgumentNotNull(id, "ID");

		try {
			return conversionService.convert(genreService.getGenre(id), GenreTO.class);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for genre isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final GenreTO genre) {
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(genreTOValidator, "Validator for TO for genre");
		genreTOValidator.validateNewGenreTO(genre);

		try {
			final Genre genreEntity = conversionService.convert(genre, Genre.class);
			genreService.add(genreEntity);
			if (genreEntity.getId() == null) {
				throw new FacadeOperationException("Service tier doesn't set ID.");
			}
			genre.setId(genreEntity.getId());
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void add(final List<String> genres) {
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateArgumentNotNull(genres, "List of genre names");
		Validators.validateCollectionNotContainNull(genres, "List of genre names");

		try {
			genreService.add(genres);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for genre isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void update(final GenreTO genre) {
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(genreTOValidator, "Validator for TO for genre");
		genreTOValidator.validateExistingGenreTO(genre);
		try {
			final Genre genreEntity = conversionService.convert(genre, Genre.class);
			Validators.validateExists(genreService.exists(genreEntity), "TO for genre");

			genreService.update(genreEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 *                                  or validator for TO for genre isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void remove(final GenreTO genre) {
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(genreTOValidator, "Validator for TO for genre");
		genreTOValidator.validateGenreTOWithId(genre);
		try {
			final Genre genreEntity = genreService.getGenre(genre.getId());
			Validators.validateExists(genreEntity, "TO for genre");

			genreService.remove(genreEntity);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 *                                  or validator for TO for genre isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws RecordNotFoundException  {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	public void duplicate(final GenreTO genre) {
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(genreTOValidator, "Validator for TO for genre");
		genreTOValidator.validateGenreTOWithId(genre);
		try {
			final Genre oldGenre = genreService.getGenre(genre.getId());
			Validators.validateExists(oldGenre, "TO for genre");

			final Genre newGenre = new Genre();
			newGenre.setName(oldGenre.getName());
			genreService.add(newGenre);
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalStateException    if service for genres isn't set
	 *                                  or conversion service isn't set
	 *                                  or validator for TO for genre isn't set
	 * @throws IllegalArgumentException {@inheritDoc}
	 * @throws ValidationException      {@inheritDoc}
	 * @throws FacadeOperationException {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(final GenreTO genre) {
		Validators.validateFieldNotNull(genreService, "Service for genres");
		Validators.validateFieldNotNull(conversionService, "Conversion service");
		Validators.validateFieldNotNull(genreTOValidator, "Validator for TO for genre");
		genreTOValidator.validateGenreTOWithId(genre);
		try {

			return genreService.exists(conversionService.convert(genre, Genre.class));
		} catch (final ServiceOperationException ex) {
			throw new FacadeOperationException("Error in working with service tier.", ex);
		}
	}

}
