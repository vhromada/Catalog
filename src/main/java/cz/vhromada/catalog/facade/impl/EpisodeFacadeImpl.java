package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.EpisodeTO;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CatalogUtils;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.EpisodeValidator;
import cz.vhromada.catalog.validator.SeasonValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;
import cz.vhromada.validators.exceptions.RecordNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeFacade")
public class EpisodeFacadeImpl implements EpisodeFacade {

    /**
     * TO for episode argument
     */
    private static final String EPISODE_TO_ARGUMENT = "TO for episode";

    /**
     * Service for shows
     */
    private CatalogService<Show> showService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for season
     */
    private SeasonValidator seasonValidator;

    /**
     * Validator for TO for episode
     */
    private EpisodeValidator episodeValidator;

    /**
     * Creates a new instance of EpisodeFacadeImpl.
     *
     * @param showService      service for shows
     * @param converter        converter
     * @param seasonValidator  validator for TO for season
     * @param episodeValidator validator for TO for episode
     * @throws IllegalArgumentException if service for shows is null
     *                                  or converter is null
     *                                  or validator for TO for season is null
     *                                  or validator for TO for episode is null
     */
    @Autowired
    public EpisodeFacadeImpl(final CatalogService<Show> showService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final SeasonValidator seasonValidator,
            final EpisodeValidator episodeValidator) {
        Validators.validateArgumentNotNull(showService, "Service for shows");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(seasonValidator, "Validator for TO for season");
        Validators.validateArgumentNotNull(episodeValidator, "Validator for TO for episode");

        this.showService = showService;
        this.converter = converter;
        this.seasonValidator = seasonValidator;
        this.episodeValidator = episodeValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public EpisodeTO getEpisode(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(getEpisodeEntity(id), EpisodeTO.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void add(final SeasonTO season, final EpisodeTO episode) {
        seasonValidator.validateSeasonTOWithId(season);
        episodeValidator.validateNewEpisodeTO(episode);
        final Show show = getShow(season);
        final Season seasonEntity = getSeason(show, season);
        Validators.validateExists(seasonEntity, "TO for season");
        assert seasonEntity != null;

        final Episode episodeEntity = converter.convert(episode, Episode.class);
        episodeEntity.setPosition(Integer.MAX_VALUE);
        seasonEntity.getEpisodes().add(episodeEntity);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void update(final EpisodeTO episode) {
        episodeValidator.validateExistingEpisodeTO(episode);
        final Show show = getShow(episode);
        final Episode episodeEntity = getEpisode(episode.getId(), show);
        Validators.validateExists(episodeEntity, EPISODE_TO_ARGUMENT);

        updateEpisode(show, converter.convert(episode, Episode.class));

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void remove(final EpisodeTO episode) {
        episodeValidator.validateEpisodeTOWithId(episode);
        final Show show = getShow(episode);
        final Episode episodeEntity = getEpisode(episode.getId(), show);
        Validators.validateExists(episodeEntity, EPISODE_TO_ARGUMENT);

        final Season season = getSeason(show, episode);
        final List<Episode> episodes = season.getEpisodes().stream().filter(episodeValue -> !episodeValue.getId().equals(episode.getId()))
                .collect(Collectors.toList());
        season.setEpisodes(episodes);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void duplicate(final EpisodeTO episode) {
        episodeValidator.validateEpisodeTOWithId(episode);
        final Show show = getShow(episode);
        final Episode episodeEntity = getEpisode(episode.getId(), show);
        Validators.validateExists(episodeEntity, EPISODE_TO_ARGUMENT);

        final Episode newEpisode = CatalogUtils.duplicateEpisode(episodeEntity);
        final Season seasonEntity = getSeason(show, episode);
        seasonEntity.getEpisodes().add(newEpisode);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void moveUp(final EpisodeTO episode) {
        move(episode, true);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void moveDown(final EpisodeTO episode) {
        move(episode, false);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public List<EpisodeTO> findEpisodesBySeason(final SeasonTO season) {
        seasonValidator.validateSeasonTOWithId(season);
        final List<Show> shows = showService.getAll();
        for (final Show show : shows) {
            for (final Season seasonEntity : show.getSeasons()) {
                if (season.getId().equals(seasonEntity.getId())) {
                    return CollectionUtils.getSortedData(converter.convertCollection(seasonEntity.getEpisodes(), EpisodeTO.class));
                }
            }
        }

        throw new RecordNotFoundException("TO for season doesn't exist.");
    }

    /**
     * Returns episode with ID.
     *
     * @param id   ID
     * @param show show
     * @return episode with ID
     */
    private static Episode getEpisode(final Integer id, final Show show) {
        if (show == null) {
            return null;
        }

        for (final Season season : show.getSeasons()) {
            for (final Episode episode : season.getEpisodes()) {
                if (id.equals(episode.getId())) {
                    return episode;
                }
            }
        }

        return null;
    }

    /**
     * Updates episode in show.
     *
     * @param show    show
     * @param episode episode
     */
    private static void updateEpisode(final Show show, final Episode episode) {
        final List<Episode> episodes = new ArrayList<>();
        final Season season = getSeason(show, episode);
        for (final Episode episodeEntity : season.getEpisodes()) {
            if (episodeEntity.equals(episode)) {
                episodes.add(episode);
            } else {
                episodes.add(episodeEntity);
            }
        }
        season.setEpisodes(episodes);
    }

    /**
     * Returns season for episode.
     *
     * @param show    show
     * @param episode episode
     * @return season for episode
     */
    private static Season getSeason(final Show show, final Movable episode) {
        for (final Season season : show.getSeasons()) {
            for (final Episode episodeEntity : season.getEpisodes()) {
                if (episode.getId().equals(episodeEntity.getId())) {
                    return season;
                }
            }
        }

        throw new IllegalStateException("Unknown season");
    }

    /**
     * Returns season for show.
     *
     * @param show   show
     * @param season season
     * @return season for show
     */
    private static Season getSeason(final Show show, final SeasonTO season) {
        if (show == null) {
            return null;
        }

        for (final Season seasonEntity : show.getSeasons()) {
            if (season.getId().equals(seasonEntity.getId())) {
                return seasonEntity;
            }
        }

        return null;
    }

    /**
     * Returns episode with ID.
     *
     * @param id ID
     * @return episode with ID
     */
    private Episode getEpisodeEntity(final Integer id) {
        final List<Show> shows = showService.getAll();
        for (final Show show : shows) {
            for (final Season season : show.getSeasons()) {
                for (final Episode episode : season.getEpisodes()) {
                    if (id.equals(episode.getId())) {
                        return episode;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns show for TO for episode.
     *
     * @param episode TO for episode
     * @return show for TO for episode
     */
    private Show getShow(final EpisodeTO episode) {
        for (final Show show : showService.getAll()) {
            for (final Season season : show.getSeasons()) {
                for (final Episode episodeEntity : season.getEpisodes()) {
                    if (episode.getId().equals(episodeEntity.getId())) {
                        return show;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns show for TO for season.
     *
     * @param season TO for season
     * @return show for TO for season
     */
    private Show getShow(final SeasonTO season) {
        for (final Show show : showService.getAll()) {
            for (final Season seasonEntity : show.getSeasons()) {
                if (season.getId().equals(seasonEntity.getId())) {
                    return show;
                }
            }
        }

        return null;
    }

    /**
     * Moves TO for episode in list one position up or down.
     *
     * @param episode TO for episode
     * @param up      true if moving TO for episode up
     */
    private void move(final EpisodeTO episode, final boolean up) {
        episodeValidator.validateEpisodeTOWithId(episode);
        final Show show = getShow(episode);
        final Episode episodeEntity = getEpisode(episode.getId(), show);
        Validators.validateExists(episodeEntity, EPISODE_TO_ARGUMENT);
        final Season season = getSeason(show, episode);
        final List<Episode> episodes = CollectionUtils.getSortedData(season.getEpisodes());
        if (up) {
            Validators.validateMoveUp(episodes, episodeEntity, EPISODE_TO_ARGUMENT);
        } else {
            Validators.validateMoveDown(episodes, episodeEntity, EPISODE_TO_ARGUMENT);
        }
        assert episodeEntity != null;

        final int index = episodes.indexOf(episodeEntity);
        final Episode other = episodes.get(up ? index - 1 : index + 1);
        final int position = episodeEntity.getPosition();
        episodeEntity.setPosition(other.getPosition());
        other.setPosition(position);

        updateEpisode(show, episodeEntity);
        updateEpisode(show, other);

        showService.update(show);
    }

}

