package cz.vhromada.catalog.facade.impl;

import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.validators.EpisodeTOValidator;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.service.EpisodeService;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeFacade")
@Transactional
public class EpisodeFacadeImpl implements EpisodeFacade {

    /** Service for seasons argument */
    private static final String SEASON_SERVICE_ARGUMENT = "Service for seasons";

    /** Service for episodes argument */
    private static final String EPISODE_SERVICE_ARGUMENT = "Service for episodes";

    /** Converter argument */
    private static final String CONVERTER_ARGUMENT = "Converter";

    /** Validator for TO for season field */
    private static final String SEASON_TO_VALIDATOR_ARGUMENT = "Validator for TO for season";

    /** Validator for TO for episode field */
    private static final String EPISODE_TO_VALIDATOR_ARGUMENT = "Validator for TO for episode";

    /** Episode argument */
    private static final String EPISODE_ARGUMENT = "episode";

    /** TO for season argument */
    private static final String SEASON_TO_ARGUMENT = "TO for season";

    /** TO for episode argument */
    private static final String EPISODE_TO_ARGUMENT = "TO for episode";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link FacadeOperationException} */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /** Message for not setting ID */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /** Service for seasons */
    private SeasonService seasonService;

    /** Service for episodes */
    private EpisodeService episodeService;

    /** Converter */
    private Converter converter;

    /** Validator for TO for season */
    private SeasonTOValidator seasonTOValidator;

    /** Validator for TO for episode */
    private EpisodeTOValidator episodeTOValidator;

    /**
     * Creates a new instance of EpisodeFacadeImpl.
     *
     * @param seasonService      service for seasons
     * @param episodeService     service for episodes
     * @param converter          converter
     * @param seasonTOValidator  validator for TO for season
     * @param episodeTOValidator validator for TO for episode
     * @throws IllegalArgumentException if service for seasons is null
     *                                  or service for episodes is null
     *                                  or converter is null
     *                                  or validator for TO for season is null
     *                                  or validator for TO for episode is null
     */
    @Autowired
    public EpisodeFacadeImpl(final SeasonService seasonService,
            final EpisodeService episodeService,
            final Converter converter,
            final SeasonTOValidator seasonTOValidator,
            final EpisodeTOValidator episodeTOValidator) {
        Validators.validateArgumentNotNull(seasonService, SEASON_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(episodeService, EPISODE_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(converter, CONVERTER_ARGUMENT);
        Validators.validateArgumentNotNull(seasonTOValidator, SEASON_TO_VALIDATOR_ARGUMENT);
        Validators.validateArgumentNotNull(episodeTOValidator, EPISODE_TO_VALIDATOR_ARGUMENT);

        this.seasonService = seasonService;
        this.episodeService = episodeService;
        this.converter = converter;
        this.seasonTOValidator = seasonTOValidator;
        this.episodeTOValidator = episodeTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public EpisodeTO getEpisode(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return converter.convert(episodeService.getEpisode(id), EpisodeTO.class);
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
    public void add(final EpisodeTO episode) {
        episodeTOValidator.validateNewEpisodeTO(episode);
        try {
            final Season season = seasonService.getSeason(episode.getSeason().getId());
            Validators.validateExists(season, SEASON_TO_ARGUMENT);

            final Episode episodeEntity = converter.convert(episode, Episode.class);
            episodeEntity.setSeason(season);
            episodeService.add(episodeEntity);
            if (episodeEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            episode.setId(episodeEntity.getId());
            episode.setPosition(episodeEntity.getPosition());
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
    public void update(final EpisodeTO episode) {
        episodeTOValidator.validateExistingEpisodeTO(episode);
        try {
            final Episode episodeEntity = converter.convert(episode, Episode.class);
            Validators.validateExists(episodeService.exists(episodeEntity), EPISODE_TO_ARGUMENT);
            final Season season = seasonService.getSeason(episode.getSeason().getId());
            Validators.validateExists(season, SEASON_TO_ARGUMENT);

            episodeEntity.setSeason(season);
            episodeService.update(episodeEntity);
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
    public void remove(final EpisodeTO episode) {
        episodeTOValidator.validateEpisodeTOWithId(episode);
        try {
            final Episode episodeEntity = episodeService.getEpisode(episode.getId());
            Validators.validateExists(episodeEntity, EPISODE_TO_ARGUMENT);

            episodeService.remove(episodeEntity);
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
    public void duplicate(final EpisodeTO episode) {
        episodeTOValidator.validateEpisodeTOWithId(episode);
        try {
            final Episode oldEpisode = episodeService.getEpisode(episode.getId());
            Validators.validateExists(oldEpisode, EPISODE_TO_ARGUMENT);

            episodeService.duplicate(oldEpisode);
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
    public void moveUp(final EpisodeTO episode) {
        episodeTOValidator.validateEpisodeTOWithId(episode);
        try {
            final Episode episodeEntity = episodeService.getEpisode(episode.getId());
            Validators.validateExists(episodeEntity, EPISODE_TO_ARGUMENT);
            final List<Episode> episodes = episodeService.findEpisodesBySeason(episodeEntity.getSeason());
            Validators.validateMoveUp(episodes, episodeEntity, EPISODE_ARGUMENT);

            episodeService.moveUp(episodeEntity);
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
    public void moveDown(final EpisodeTO episode) {
        episodeTOValidator.validateEpisodeTOWithId(episode);
        try {
            final Episode episodeEntity = episodeService.getEpisode(episode.getId());
            Validators.validateExists(episodeEntity, EPISODE_TO_ARGUMENT);
            final List<Episode> episodes = episodeService.findEpisodesBySeason(episodeEntity.getSeason());
            Validators.validateMoveDown(episodes, episodeEntity, EPISODE_ARGUMENT);

            episodeService.moveDown(episodeEntity);
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
    public boolean exists(final EpisodeTO episode) {
        episodeTOValidator.validateEpisodeTOWithId(episode);
        try {

            return episodeService.exists(converter.convert(episode, Episode.class));
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
    @Transactional(readOnly = true)
    public List<EpisodeTO> findEpisodesBySeason(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        try {
            final Season seasonEntity = seasonService.getSeason(season.getId());
            Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);

            final List<EpisodeTO> episodes = converter.convertCollection(episodeService.findEpisodesBySeason(seasonEntity), EpisodeTO.class);
            Collections.sort(episodes);
            return episodes;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}

