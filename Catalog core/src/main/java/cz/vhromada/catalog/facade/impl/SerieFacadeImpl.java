package cz.vhromada.catalog.facade.impl;

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
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Service for series argument
     */
    private static final String SERIE_SERVICE_ARGUMENT = "Service for series";

    /**
     * Service for genres argument
     */
    private static final String GENRE_SERVICE_ARGUMENT = "Service for genres";

    /**
     * Converter argument
     */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /**
     * Validator for TO for serie argument
     */
    private static final String SERIE_TO_VALIDATOR_ARGUMENT = "Validator for TO for serie";

    /**
     * Serie argument
     */
    private static final String SERIE_ARGUMENT = "serie";

    /**
     * TO for serie argument
     */
    private static final String SERIE_TO_ARGUMENT = "TO for serie";

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
     * Service for series
     */
    private SerieService serieService;

    /**
     * Service for genres
     */
    private GenreService genreService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for serie
     */
    private SerieTOValidator serieTOValidator;

    /**
     * Creates a new instance of SerieFacadeImpl.
     *
     * @param serieService     service for series
     * @param genreService     service for genres
     * @param converter        converter
     * @param serieTOValidator validator for TO for serie
     * @throws IllegalArgumentException if service for series is null
     *                                  or service for genres is null
     *                                  or converter is null
     *                                  or validator for TO for serie is null
     */
    @Autowired
    public SerieFacadeImpl(final SerieService serieService,
            final GenreService genreService,
            final Converter converter,
            final SerieTOValidator serieTOValidator) {
        Validators.validateArgumentNotNull(serieService, SERIE_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(genreService, GENRE_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(serieTOValidator, SERIE_TO_VALIDATOR_ARGUMENT);

        this.serieService = serieService;
        this.genreService = genreService;
        this.converter = converter;
        this.serieTOValidator = serieTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            serieService.newData();
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
    public List<SerieTO> getSeries() {
        try {
            final List<SerieTO> series = converter.convertCollection(serieService.getSeries(), SerieTO.class);
            Collections.sort(series);
            return series;
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
    public SerieTO getSerie(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(serieService.getSerie(id), SerieTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    public void add(final SerieTO serie) {
        serieTOValidator.validateNewSerieTO(serie);
        try {
            for (final GenreTO genre : serie.getGenres()) {
                Validators.validateExists(genreService.getGenre(genre.getId()), GENRE_TO_ARGUMENT);
            }

            final Serie serieEntity = converter.convert(serie, Serie.class);
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void update(final SerieTO serie) {
        serieTOValidator.validateExistingSerieTO(serie);
        try {
            final Serie serieEntity = converter.convert(serie, Serie.class);
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void remove(final SerieTO serie) {
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
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    public void duplicate(final SerieTO serie) {
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveUp(final SerieTO serie) {
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveDown(final SerieTO serie) {
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
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws FacadeOperationException                              {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final SerieTO serie) {
        serieTOValidator.validateSerieTOWithId(serie);
        try {

            return serieService.exists(converter.convert(serie, Serie.class));
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
    public void updatePositions() {
        try {
            serieService.updatePositions();
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
    public Time getTotalLength() {
        try {
            return serieService.getTotalLength();
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
    public int getSeasonsCount() {
        try {
            return serieService.getSeasonsCount();
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
    public int getEpisodesCount() {
        try {
            return serieService.getEpisodesCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
