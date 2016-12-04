package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.Episode;
import cz.vhromada.catalog.entity.Season;
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
     * Episode argument
     */
    private static final String EPISODE_ARGUMENT = "episode";

    /**
     * Service for shows
     */
    private CatalogService<Show> showService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for season
     */
    private SeasonValidator seasonValidator;

    /**
     * Validator for episode
     */
    private EpisodeValidator episodeValidator;

    /**
     * Creates a new instance of EpisodeFacadeImpl.
     *
     * @param showService      service for shows
     * @param converter        converter
     * @param seasonValidator  validator for season
     * @param episodeValidator validator for episode
     * @throws IllegalArgumentException if service for shows is null
     *                                  or converter is null
     *                                  or validator for season is null
     *                                  or validator for episode is null
     */
    @Autowired
    public EpisodeFacadeImpl(final CatalogService<Show> showService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final SeasonValidator seasonValidator,
            final EpisodeValidator episodeValidator) {
        Validators.validateArgumentNotNull(showService, "Service for shows");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(seasonValidator, "Validator for season");
        Validators.validateArgumentNotNull(episodeValidator, "Validator for episode");

        this.showService = showService;
        this.converter = converter;
        this.seasonValidator = seasonValidator;
        this.episodeValidator = episodeValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Episode getEpisode(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(getEpisodeEntity(id), Episode.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void add(final Season season, final Episode episode) {
        seasonValidator.validateSeasonWithId(season);
        episodeValidator.validateNewEpisode(episode);
        final Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonEntity = getSeason(show, season);
        Validators.validateExists(seasonEntity, "season");
        assert seasonEntity != null;

        final cz.vhromada.catalog.domain.Episode episodeEntity = converter.convert(episode, cz.vhromada.catalog.domain.Episode.class);
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
    public void update(final Episode episode) {
        episodeValidator.validateExistingEpisode(episode);
        final Show show = getShow(episode);
        final cz.vhromada.catalog.domain.Episode episodeEntity = getEpisode(episode.getId(), show);
        Validators.validateExists(episodeEntity, EPISODE_ARGUMENT);

        updateEpisode(show, converter.convert(episode, cz.vhromada.catalog.domain.Episode.class));

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void remove(final Episode episode) {
        episodeValidator.validateEpisodeWithId(episode);
        final Show show = getShow(episode);
        final cz.vhromada.catalog.domain.Episode episodeEntity = getEpisode(episode.getId(), show);
        Validators.validateExists(episodeEntity, EPISODE_ARGUMENT);

        final cz.vhromada.catalog.domain.Season season = getSeason(show, episode);
        final List<cz.vhromada.catalog.domain.Episode> episodes = season.getEpisodes().stream()
                .filter(episodeValue -> !episodeValue.getId().equals(episode.getId()))
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
    public void duplicate(final Episode episode) {
        episodeValidator.validateEpisodeWithId(episode);
        final Show show = getShow(episode);
        final cz.vhromada.catalog.domain.Episode episodeEntity = getEpisode(episode.getId(), show);
        Validators.validateExists(episodeEntity, EPISODE_ARGUMENT);

        final cz.vhromada.catalog.domain.Episode newEpisode = CatalogUtils.duplicateEpisode(episodeEntity);
        final cz.vhromada.catalog.domain.Season seasonEntity = getSeason(show, episode);
        seasonEntity.getEpisodes().add(newEpisode);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void moveUp(final Episode episode) {
        move(episode, true);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public void moveDown(final Episode episode) {
        move(episode, false);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     * @throws RecordNotFoundException                               {@inheritDoc}
     */
    @Override
    public List<Episode> findEpisodesBySeason(final Season season) {
        seasonValidator.validateSeasonWithId(season);
        final List<Show> shows = showService.getAll();
        for (final Show show : shows) {
            for (final cz.vhromada.catalog.domain.Season seasonEntity : show.getSeasons()) {
                if (season.getId().equals(seasonEntity.getId())) {
                    return CollectionUtils.getSortedData(converter.convertCollection(seasonEntity.getEpisodes(), Episode.class));
                }
            }
        }

        throw new RecordNotFoundException("season doesn't exist.");
    }

    /**
     * Returns episode with ID.
     *
     * @param id   ID
     * @param show show
     * @return episode with ID
     */
    private static cz.vhromada.catalog.domain.Episode getEpisode(final Integer id, final Show show) {
        if (show == null) {
            return null;
        }

        for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
            for (final cz.vhromada.catalog.domain.Episode episode : season.getEpisodes()) {
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
    private static void updateEpisode(final Show show, final cz.vhromada.catalog.domain.Episode episode) {
        final List<cz.vhromada.catalog.domain.Episode> episodes = new ArrayList<>();
        final cz.vhromada.catalog.domain.Season season = getSeason(show, episode);
        for (final cz.vhromada.catalog.domain.Episode episodeEntity : season.getEpisodes()) {
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
    private static cz.vhromada.catalog.domain.Season getSeason(final Show show, final Movable episode) {
        for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
            for (final cz.vhromada.catalog.domain.Episode episodeEntity : season.getEpisodes()) {
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
    private static cz.vhromada.catalog.domain.Season getSeason(final Show show, final Season season) {
        if (show == null) {
            return null;
        }

        for (final cz.vhromada.catalog.domain.Season seasonEntity : show.getSeasons()) {
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
    private cz.vhromada.catalog.domain.Episode getEpisodeEntity(final Integer id) {
        final List<Show> shows = showService.getAll();
        for (final Show show : shows) {
            for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
                for (final cz.vhromada.catalog.domain.Episode episode : season.getEpisodes()) {
                    if (id.equals(episode.getId())) {
                        return episode;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns show for episode.
     *
     * @param episode episode
     * @return show for episode
     */
    private Show getShow(final Episode episode) {
        for (final Show show : showService.getAll()) {
            for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
                for (final cz.vhromada.catalog.domain.Episode episodeEntity : season.getEpisodes()) {
                    if (episode.getId().equals(episodeEntity.getId())) {
                        return show;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns show for season.
     *
     * @param season season
     * @return show for season
     */
    private Show getShow(final Season season) {
        for (final Show show : showService.getAll()) {
            for (final cz.vhromada.catalog.domain.Season seasonEntity : show.getSeasons()) {
                if (season.getId().equals(seasonEntity.getId())) {
                    return show;
                }
            }
        }

        return null;
    }

    /**
     * Moves episode in list one position up or down.
     *
     * @param episode episode
     * @param up      true if moving episode up
     */
    private void move(final Episode episode, final boolean up) {
        episodeValidator.validateEpisodeWithId(episode);
        final Show show = getShow(episode);
        final cz.vhromada.catalog.domain.Episode episodeEntity = getEpisode(episode.getId(), show);
        Validators.validateExists(episodeEntity, EPISODE_ARGUMENT);
        final cz.vhromada.catalog.domain.Season season = getSeason(show, episode);
        final List<cz.vhromada.catalog.domain.Episode> episodes = CollectionUtils.getSortedData(season.getEpisodes());
        if (up) {
            Validators.validateMoveUp(episodes, episodeEntity, EPISODE_ARGUMENT);
        } else {
            Validators.validateMoveDown(episodes, episodeEntity, EPISODE_ARGUMENT);
        }
        assert episodeEntity != null;

        final int index = episodes.indexOf(episodeEntity);
        final cz.vhromada.catalog.domain.Episode other = episodes.get(up ? index - 1 : index + 1);
        final int position = episodeEntity.getPosition();
        episodeEntity.setPosition(other.getPosition());
        other.setPosition(position);

        updateEpisode(show, episodeEntity);
        updateEpisode(show, other);

        showService.update(show);
    }

}

