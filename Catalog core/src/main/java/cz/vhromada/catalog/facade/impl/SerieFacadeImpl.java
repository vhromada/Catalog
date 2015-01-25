package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SerieFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GenreTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.catalog.service.GenreService;
import cz.vhromada.catalog.service.SerieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for series.
 *
 * @author Vladimir Hromada
 */
@Component("serieFacade")
@Transactional
public class SerieFacadeImpl implements SerieFacade {

    /** Service for series field */
    private static final String SERIE_SERVICE_FIELD = "Service for series";

    /** Service for genres field */
    private static final String GENRE_SERVICE_FIELD = "Service for genres";

    /** Conversion service field */
    private static final String CONVERSION_SERVICE_FIELD = "Conversion service";

    /** Validator for TO for serie field */
    private static final String SERIE_TO_VALIDATOR_FIELD = "Validator for TO for serie";

    /** Serie argument */
    private static final String SERIE_ARGUMENT = "serie";

    /** TO for serie argument */
    private static final String SERIE_TO_ARGUMENT = "TO for serie";

    /** TO for genre argument */
    private static final String GENRE_TO_ARGUMENT = "TO for genre";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link FacadeOperationException} */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /** Message for not setting ID */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /** Service for series */
    @Autowired
    private SerieService serieService;

    /** Service for genres */
    @Autowired
    private GenreService genreService;

    /** Conversion service */
    @Autowired
    @Qualifier("coreConversionService")
    private ConversionService conversionService;

    /** Validator for TO for serie */
    @Autowired
    private SerieTOValidator serieTOValidator;

    /**
     * Returns service for series.
     *
     * @return service for series
     */
    public SerieService getSerieService() {
        return serieService;
    }

    /**
     * Sets a new value to service for series.
     *
     * @param serieService new value
     */
    public void setSerieService(final SerieService serieService) {
        this.serieService = serieService;
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
     * Returns validator for TO for serie.
     *
     * @return validator for TO for serie
     */
    public SerieTOValidator getSerieTOValidator() {
        return serieTOValidator;
    }

    /**
     * Sets a new value to validator for TO for serie.
     *
     * @param serieTOValidator new value
     */
    public void setSerieTOValidator(final SerieTOValidator serieTOValidator) {
        this.serieTOValidator = serieTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);

        try {
            serieService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or conversion service isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<SerieTO> getSeries() {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);

        try {
            final List<SerieTO> series = new ArrayList<>();
            for (final Serie serie : serieService.getSeries()) {
                series.add(conversionService.convert(serie, SerieTO.class));
            }
            Collections.sort(series);
            return series;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or conversion service isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SerieTO getSerie(final Integer id) {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return conversionService.convert(serieService.getSerie(id), SerieTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or service for genres isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for serie isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final SerieTO serie) {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(serieTOValidator, SERIE_TO_VALIDATOR_FIELD);
        serieTOValidator.validateNewSerieTO(serie);
        try {
            for (final GenreTO genre : serie.getGenres()) {
                Validators.validateExists(genreService.getGenre(genre.getId()), GENRE_TO_ARGUMENT);
            }

            final Serie serieEntity = conversionService.convert(serie, Serie.class);
            serieService.add(serieEntity);
            if (serieEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            serie.setId(serieEntity.getId());
            serie.setPosition(serieEntity.getPosition());
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or service for genres isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for serie isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void update(final SerieTO serie) {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(genreService, GENRE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(serieTOValidator, SERIE_TO_VALIDATOR_FIELD);
        serieTOValidator.validateExistingSerieTO(serie);
        try {
            final Serie serieEntity = conversionService.convert(serie, Serie.class);
            Validators.validateExists(serieService.exists(serieEntity), SERIE_TO_ARGUMENT);
            for (final GenreTO genre : serie.getGenres()) {
                Validators.validateExists(genreService.getGenre(genre.getId()), GENRE_TO_ARGUMENT);
            }

            serieService.update(serieEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or validator for TO for serie isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void remove(final SerieTO serie) {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(serieTOValidator, SERIE_TO_VALIDATOR_FIELD);
        serieTOValidator.validateSerieTOWithId(serie);
        try {
            final Serie serieEntity = serieService.getSerie(serie.getId());
            Validators.validateExists(serieEntity, SERIE_TO_ARGUMENT);

            serieService.remove(serieEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or validator for TO for serie isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final SerieTO serie) {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(serieTOValidator, SERIE_TO_VALIDATOR_FIELD);
        serieTOValidator.validateSerieTOWithId(serie);
        try {
            final Serie oldSerie = serieService.getSerie(serie.getId());
            Validators.validateExists(oldSerie, SERIE_TO_ARGUMENT);

            serieService.duplicate(oldSerie);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or validator for TO for serie isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final SerieTO serie) {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(serieTOValidator, SERIE_TO_VALIDATOR_FIELD);
        serieTOValidator.validateSerieTOWithId(serie);
        try {
            final Serie serieEntity = serieService.getSerie(serie.getId());
            Validators.validateExists(serieEntity, SERIE_TO_ARGUMENT);
            final List<Serie> series = serieService.getSeries();
            Validators.validateMoveUp(series, serieEntity, SERIE_ARGUMENT);

            serieService.moveUp(serieEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or validator for TO for serie isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final SerieTO serie) {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(serieTOValidator, SERIE_TO_VALIDATOR_FIELD);
        serieTOValidator.validateSerieTOWithId(serie);
        try {
            final Serie serieEntity = serieService.getSerie(serie.getId());
            Validators.validateExists(serieEntity, SERIE_TO_ARGUMENT);
            final List<Serie> series = serieService.getSeries();
            Validators.validateMoveDown(series, serieEntity, SERIE_ARGUMENT);

            serieService.moveDown(serieEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     *                                  or conversion service isn't set
     *                                  or validator for TO for serie isn't set
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final SerieTO serie) {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);
        Validators.validateFieldNotNull(conversionService, CONVERSION_SERVICE_FIELD);
        Validators.validateFieldNotNull(serieTOValidator, SERIE_TO_VALIDATOR_FIELD);
        serieTOValidator.validateSerieTOWithId(serie);
        try {

            return serieService.exists(conversionService.convert(serie, Serie.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);

        try {
            serieService.updatePositions();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Time getTotalLength() {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);

        try {
            return serieService.getTotalLength();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getSeasonsCount() {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);

        try {
            return serieService.getSeasonsCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException    if service for series isn't set
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getEpisodesCount() {
        Validators.validateFieldNotNull(serieService, SERIE_SERVICE_FIELD);

        try {
            return serieService.getEpisodesCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
