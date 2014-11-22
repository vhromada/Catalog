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

    /** Service for genres field */
    private static final String GENRE_SERVICE_FIELD = "Service for genres";

    /** Conversion service field */
    private static final String CONVERSION_SERVICE_FIELD = "Conversion service";

    /** Validator for TO for genre field */
    private static final String GENRE_TO_VALIDATOR_FIELD = "Validator for TO for genre";

    /** TO for genre argument */
    private static final String GENRE_TO_ARGUMENT = "TO for genre";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Genre names argument */
    private static final String GENRE_NAMES_ARGUMENT = "List of genre names";

    /** Message for {@link FacadeOperationException} */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /** Message for not setting ID */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";


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
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);

        try {
            genreService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
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
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);

        try {
            final List<GenreTO> genres = new ArrayList<>();
            for (final Genre genre : genreService.getGenres()) {
                genres.add(conversionService.convert(genre, GenreTO.class));
            }
            Collections.sort(genres);
            return genres;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
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
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return conversionService.convert(genreService.getGenre(id), GenreTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for genres isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for genre isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final GenreTO genre) {
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(genreTOValidator, GENRE_TO_VALIDATOR_FIELD);
        genreTOValidator.validateNewGenreTO(genre);

        try {
            final Genre genreEntity = conversionService.convert(genre, Genre.class);
            genreService.add(genreEntity);
            if (genreEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            genre.setId(genreEntity.getId());
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for genres isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final List<String> genres) {
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateArgumentNotNull(genres, GENRE_NAMES_ARGUMENT);
        Validators.validateCollectionNotContainNull(genres, GENRE_NAMES_ARGUMENT);

        try {
            genreService.add(genres);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for genres isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for genre isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void update(final GenreTO genre) {
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(genreTOValidator, GENRE_TO_VALIDATOR_FIELD);
        genreTOValidator.validateExistingGenreTO(genre);
        try {
            final Genre genreEntity = conversionService.convert(genre, Genre.class);
            Validators.validateExists(genreService.exists(genreEntity), GENRE_TO_ARGUMENT);

            genreService.update(genreEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for genres isn't set
     *                                  or validator for TO for genre isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void remove(final GenreTO genre) {
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(genreTOValidator, GENRE_TO_VALIDATOR_FIELD);
        genreTOValidator.validateGenreTOWithId(genre);
        try {
            final Genre genreEntity = genreService.getGenre(genre.getId());
            Validators.validateExists(genreEntity, GENRE_TO_ARGUMENT);

            genreService.remove(genreEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for genres isn't set
     *                                  or validator for TO for genre isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final GenreTO genre) {
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(genreTOValidator, GENRE_TO_VALIDATOR_FIELD);
        genreTOValidator.validateGenreTOWithId(genre);
        try {
            final Genre oldGenre = genreService.getGenre(genre.getId());
            Validators.validateExists(oldGenre, GENRE_TO_ARGUMENT);

            final Genre newGenre = new Genre();
            newGenre.setName(oldGenre.getName());
            genreService.add(newGenre);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for genres isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for genre isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final GenreTO genre) {
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(genreTOValidator, GENRE_TO_VALIDATOR_FIELD);
        genreTOValidator.validateGenreTOWithId(genre);
        try {

            return genreService.exists(conversionService.convert(genre, Genre.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
