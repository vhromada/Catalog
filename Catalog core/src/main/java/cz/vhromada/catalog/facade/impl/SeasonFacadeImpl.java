package cz.vhromada.catalog.facade.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.ShowTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.facade.validators.ShowTOValidator;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.ShowService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    /**
     * Service for shows argument
     */
    private static final String SHOW_SERVICE_ARGUMENT = "Service for shows";

    /**
     * Service for seasons argument
     */
    private static final String SEASON_SERVICE_ARGUMENT = "Service for seasons";

    /**
     * Converter argument
     */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /**
     * Validator for TO for show argument
     */
    private static final String SHOW_TO_VALIDATOR_ARGUMENT = "Validator for TO for show";

    /**
     * Validator for TO for season argument
     */
    private static final String SEASON_TO_VALIDATOR_ARGUMENT = "Validator for TO for season";

    /**
     * Season argument
     */
    private static final String SEASON_ARGUMENT = "season";

    /**
     * TO for show argument
     */
    private static final String SHOW_TO_ARGUMENT = "TO for show";

    /**
     * TO for season argument
     */
    private static final String SEASON_TO_ARGUMENT = "TO for season";

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
     * Service for shows
     */
    private ShowService showService;

    /**
     * Service for seasons
     */
    private SeasonService seasonService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for show
     */
    private ShowTOValidator showTOValidator;

    /**
     * Validator for TO for season
     */
    private SeasonTOValidator seasonTOValidator;

    /**
     * Creates a new instance of SeasonFacadeImpl.
     *
     * @param showService       service for shows
     * @param seasonService     service for seasons
     * @param converter         converter
     * @param showTOValidator   validator for TO for show
     * @param seasonTOValidator validator for TO for season
     * @throws IllegalArgumentException if service for shows is null
     *                                  or service for seasons is null
     *                                  or converter is null
     *                                  or validator for TO for show is null
     *                                  or validator for TO for season is null
     */
    @Autowired
    public SeasonFacadeImpl(final ShowService showService,
            final SeasonService seasonService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final ShowTOValidator showTOValidator,
            final SeasonTOValidator seasonTOValidator) {
        Validators.validateArgumentNotNull(showService, SHOW_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(seasonService, SEASON_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(showTOValidator, SHOW_TO_VALIDATOR_ARGUMENT);
        Validators.validateArgumentNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_ARGUMENT);

        this.showService = showService;
        this.seasonService = seasonService;
        this.converter = converter;
        this.showTOValidator = showTOValidator;
        this.seasonTOValidator = seasonTOValidator;
    }

    /**
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void add(final SeasonTO season) {
        seasonTOValidator.validateNewSeasonTO(season);
        try {
            final Show show = showService.getShow(season.getShow().getId());
            Validators.validateExists(show, SHOW_TO_ARGUMENT);

            final Season seasonEntity = converter.convert(season, Season.class);
            seasonEntity.setShow(show);
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void update(final SeasonTO season) {
        seasonTOValidator.validateExistingSeasonTO(season);
        try {
            final Season seasonEntity = converter.convert(season, Season.class);
            Validators.validateExists(seasonService.exists(seasonEntity), SEASON_TO_ARGUMENT);
            final Show show = showService.getShow(season.getShow().getId());
            Validators.validateExists(show, SHOW_TO_ARGUMENT);

            seasonEntity.setShow(show);
            seasonService.update(seasonEntity);
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
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
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     * @throws FacadeOperationException                                  {@inheritDoc}
     */
    @Override
    public void moveUp(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {
            final Season seasonEntity = seasonService.getSeason(season.getId());
            Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
            final List<Season> seasons = seasonService.findSeasonsByShow(seasonEntity.getShow());
            Validators.validateMoveUp(seasons, seasonEntity, SEASON_ARGUMENT);

            seasonService.moveUp(seasonEntity);
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
    public void moveDown(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {
            final Season seasonEntity = seasonService.getSeason(season.getId());
            Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
            final List<Season> seasons = seasonService.findSeasonsByShow(seasonEntity.getShow());
            Validators.validateMoveDown(seasons, seasonEntity, SEASON_ARGUMENT);

            seasonService.moveDown(seasonEntity);
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
    public boolean exists(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {

            return seasonService.exists(converter.convert(season, Season.class));
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
    @Transactional(readOnly = true)
    public List<SeasonTO> findSeasonsByShow(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        try {
            final Show showEntity = showService.getShow(show.getId());
            Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);

            final List<SeasonTO> seasons = converter.convertCollection(seasonService.findSeasonsByShow(showEntity), SeasonTO.class);
            Collections.sort(seasons);
            return seasons;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
