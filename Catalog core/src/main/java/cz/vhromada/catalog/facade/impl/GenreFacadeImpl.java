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
    private static final String GENRE_SERVICE_ARGUMENT = "Service for genres";

    /** Conversion service field */
    private static final String CONVERSION_SERVICE_ARGUMENT = "Conversion service";

    /** Validator for TO for genre field */
    private static final String GENRE_TO_VALIDATOR_ARGUMENT = "Validator for TO for genre";

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
    private GenreService genreService;

    /** Conversion service */
    private ConversionService conversionService;

    /** Validator for TO for genre */
    private GenreTOValidator genreTOValidator;

    /**
     * Creates a new instance of GenreFacadeImpl.
     *
     * @param genreService service for genres
     * @param conversionService conversion service
     * @param genreTOValidator validator for TO for genre
     * @throws IllegalArgumentException if service for genres is null
     *                                  or conversion service is null
     *                                  or validator for TO for genre is null
     */
    @Autowired
    public GenreFacadeImpl(final GenreService genreService,
            @Qualifier("coreConversionService") final ConversionService conversionService,
            final GenreTOValidator genreTOValidator) {
        Validators.validateArgumentNotNull(genreService, GENRE_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(conversionService, CONVERSION_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(genreTOValidator, GENRE_TO_VALIDATOR_ARGUMENT);

        this.genreService = genreService;
        this.conversionService = conversionService;
        this.genreTOValidator = genreTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            genreService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GenreTO> getGenres() {
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
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GenreTO getGenre(final Integer id) {
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
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final GenreTO genre) {
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
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final List<String> genres) {
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
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void update(final GenreTO genre) {
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
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void remove(final GenreTO genre) {
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
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final GenreTO genre) {
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
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final GenreTO genre) {
        genreTOValidator.validateGenreTOWithId(genre);
        try {

            return genreService.exists(conversionService.convert(genre, Genre.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
