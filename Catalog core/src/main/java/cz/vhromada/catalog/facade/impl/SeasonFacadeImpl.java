package cz.vhromada.catalog.facade.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.SerieTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.facade.validators.SerieTOValidator;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.SerieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonFacade")
@Transactional
public class SeasonFacadeImpl implements SeasonFacade {

    /** Service for series argument */
    private static final String SERIE_SERVICE_ARGUMENT = "Service for series";

    /** Service for seasons argument */
    private static final String SEASON_SERVICE_ARGUMENT = "Service for seasons";

    /** Converter argument */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /** Validator for TO for serie argument */
    private static final String SERIE_TO_VALIDATOR_ARGUMENT = "Validator for TO for serie";

    /** Validator for TO for season argument */
    private static final String SEASON_TO_VALIDATOR_ARGUMENT = "Validator for TO for season";

    /** Season argument */
    private static final String SEASON_ARGUMENT = "season";

    /** TO for serie argument */
    private static final String SERIE_TO_ARGUMENT = "TO for serie";

    /** TO for season argument */
    private static final String SEASON_TO_ARGUMENT = "TO for season";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link FacadeOperationException} */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /** Message for not setting ID */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /** Service for series */
    private SerieService serieService;

    /** Service for seasons */
    private SeasonService seasonService;

    /** Converter */
    private Converter converter;

    /** Validator for TO for serie */
    private SerieTOValidator serieTOValidator;

    /** Validator for TO for season */
    private SeasonTOValidator seasonTOValidator;

    /**
     * Creates a new instance of SeasonFacadeImpl.
     *
     * @param serieService service for series
     * @param seasonService service for seasons
     * @param converter converter
     * @param serieTOValidator validator for TO for serie
     * @param seasonTOValidator validator for TO for season
     * @throws IllegalArgumentException if service for series is null
     *                                  or service for seasons is null
     *                                  or converter is null
     *                                  or validator for TO for serie is null
     *                                  or validator for TO for season is null
     */
    @Autowired
    public SeasonFacadeImpl(final SerieService serieService,
            final SeasonService seasonService,
            final Converter converter,
            final SerieTOValidator serieTOValidator,
            final SeasonTOValidator seasonTOValidator) {
        Validators.validateArgumentNotNull(serieService, SERIE_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(seasonService, SEASON_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(serieTOValidator, SERIE_TO_VALIDATOR_ARGUMENT);
        Validators.validateArgumentNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_ARGUMENT);

        this.serieService = serieService;
        this.seasonService = seasonService;
        this.converter = converter;
        this.serieTOValidator = serieTOValidator;
        this.seasonTOValidator = seasonTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SeasonTO getSeason(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(seasonService.getSeason(id), SeasonTO.class);
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
    public void add(final SeasonTO season) {
        seasonTOValidator.validateNewSeasonTO(season);
        try {
            final Serie serie = serieService.getSerie(season.getSerie().getId());
            Validators.validateExists(serie, SERIE_TO_ARGUMENT);

            final Season seasonEntity = converter.convert(season, Season.class);
            seasonEntity.setSerie(serie);
            seasonService.add(seasonEntity);
            if (seasonEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            season.setId(seasonEntity.getId());
            season.setPosition(seasonEntity.getPosition());
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
    public void update(final SeasonTO season) {
        seasonTOValidator.validateExistingSeasonTO(season);
        try {
            final Season seasonEntity = converter.convert(season, Season.class);
            Validators.validateExists(seasonService.exists(seasonEntity), SEASON_TO_ARGUMENT);
            final Serie serie = serieService.getSerie(season.getSerie().getId());
            Validators.validateExists(serie, SERIE_TO_ARGUMENT);

            seasonEntity.setSerie(serie);
            seasonService.update(seasonEntity);
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
    public void remove(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {
            final Season seasonEntity = seasonService.getSeason(season.getId());
            Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);

            seasonService.remove(seasonEntity);
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
    public void duplicate(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {
            final Season oldSeason = seasonService.getSeason(season.getId());
            Validators.validateExists(oldSeason, SEASON_TO_ARGUMENT);

            seasonService.duplicate(oldSeason);
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
    public void moveUp(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {
            final Season seasonEntity = seasonService.getSeason(season.getId());
            Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
            final List<Season> seasons = seasonService.findSeasonsBySerie(seasonEntity.getSerie());
            Validators.validateMoveUp(seasons, seasonEntity, SEASON_ARGUMENT);

            seasonService.moveUp(seasonEntity);
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
    public void moveDown(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {
            final Season seasonEntity = seasonService.getSeason(season.getId());
            Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
            final List<Season> seasons = seasonService.findSeasonsBySerie(seasonEntity.getSerie());
            Validators.validateMoveDown(seasons, seasonEntity, SEASON_ARGUMENT);

            seasonService.moveDown(seasonEntity);
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
    public boolean exists(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {

            return seasonService.exists(converter.convert(season, Season.class));
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
    @Transactional(readOnly = true)
    public List<SeasonTO> findSeasonsBySerie(final SerieTO serie) {
        serieTOValidator.validateSerieTOWithId(serie);
        try {
            final Serie serieEntity = serieService.getSerie(serie.getId());
            Validators.validateExists(serieEntity, SERIE_TO_ARGUMENT);

            final List<SeasonTO> seasons = converter.convertCollection(seasonService.findSeasonsBySerie(serieEntity), SeasonTO.class);
            Collections.sort(seasons);
            return seasons;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
