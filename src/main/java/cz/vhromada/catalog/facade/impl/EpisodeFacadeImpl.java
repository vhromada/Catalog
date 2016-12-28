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
import cz.vhromada.catalog.utils.CatalogUtils;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.EpisodeValidator;
import cz.vhromada.catalog.validator.SeasonValidator;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of facade for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeFacade")
public class EpisodeFacadeImpl implements EpisodeFacade {

    /**
     * Message for not existing episode
     */
    private static final String NOT_EXISTING_EPISODE_MESSAGE = "Episode doesn't exist.";

    /**
     * Message for not movable episode
     */
    private static final String NOT_MOVABLE_EPISODE_MESSAGE = "ID isn't valid - episode can't be moved.";

    /**
     * Message for not existing season
     */
    private static final String NOT_EXISTING_SEASON_MESSAGE = "Season doesn't exist.";

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
            final Converter converter,
            final SeasonValidator seasonValidator,
            final EpisodeValidator episodeValidator) {
        Assert.notNull(showService, "Service for shows mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(seasonValidator, "Validator for season mustn't be null.");
        Assert.notNull(episodeValidator, "Validator for episode mustn't be null.");

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
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(getEpisodeDomain(id), Episode.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void add(final Season season, final Episode episode) {
        seasonValidator.validateSeasonWithId(season);
        episodeValidator.validateNewEpisode(episode);
        final Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonDomain = getSeason(show, season);
        if (seasonDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SEASON_MESSAGE);
        }

        final cz.vhromada.catalog.domain.Episode episodeDomain = converter.convert(episode, cz.vhromada.catalog.domain.Episode.class);
        episodeDomain.setPosition(Integer.MAX_VALUE);
        seasonDomain.getEpisodes().add(episodeDomain);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void update(final Episode episode) {
        episodeValidator.validateExistingEpisode(episode);
        final Show show = getShow(episode);
        final cz.vhromada.catalog.domain.Episode episodeDomain = getEpisode(episode.getId(), show);
        if (episodeDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_EPISODE_MESSAGE);
        }

        updateEpisode(show, converter.convert(episode, cz.vhromada.catalog.domain.Episode.class));

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void remove(final Episode episode) {
        episodeValidator.validateEpisodeWithId(episode);
        final Show show = getShow(episode);
        final cz.vhromada.catalog.domain.Episode episodeDomain = getEpisode(episode.getId(), show);
        if (episodeDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_EPISODE_MESSAGE);
        }

        final cz.vhromada.catalog.domain.Season season = getSeason(show, episode);
        final List<cz.vhromada.catalog.domain.Episode> episodes = season.getEpisodes().stream()
                .filter(episodeValue -> !episodeValue.getId().equals(episode.getId()))
                .collect(Collectors.toList());
        season.setEpisodes(episodes);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void duplicate(final Episode episode) {
        episodeValidator.validateEpisodeWithId(episode);
        final Show show = getShow(episode);
        final cz.vhromada.catalog.domain.Episode episodeDomain = getEpisode(episode.getId(), show);
        if (episodeDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_EPISODE_MESSAGE);
        }

        final cz.vhromada.catalog.domain.Episode newEpisode = CatalogUtils.duplicateEpisode(episodeDomain);
        final cz.vhromada.catalog.domain.Season seasonDomain = getSeason(show, episode);
        seasonDomain.getEpisodes().add(newEpisode);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveUp(final Episode episode) {
        move(episode, true);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveDown(final Episode episode) {
        move(episode, false);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public List<Episode> findEpisodesBySeason(final Season season) {
        seasonValidator.validateSeasonWithId(season);
        final List<Show> shows = showService.getAll();
        for (final Show show : shows) {
            for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
                if (season.getId().equals(seasonDomain.getId())) {
                    return CollectionUtils.getSortedData(converter.convertCollection(seasonDomain.getEpisodes(), Episode.class));
                }
            }
        }

        throw new IllegalArgumentException(NOT_EXISTING_SEASON_MESSAGE);
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
        for (final cz.vhromada.catalog.domain.Episode episodeDomain : season.getEpisodes()) {
            if (episodeDomain.equals(episode)) {
                episodes.add(episode);
            } else {
                episodes.add(episodeDomain);
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
            for (final cz.vhromada.catalog.domain.Episode episodeDomain : season.getEpisodes()) {
                if (episode.getId().equals(episodeDomain.getId())) {
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

        for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
            if (season.getId().equals(seasonDomain.getId())) {
                return seasonDomain;
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
    private cz.vhromada.catalog.domain.Episode getEpisodeDomain(final Integer id) {
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
                for (final cz.vhromada.catalog.domain.Episode episodeDomain : season.getEpisodes()) {
                    if (episode.getId().equals(episodeDomain.getId())) {
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
            for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
                if (season.getId().equals(seasonDomain.getId())) {
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
        final cz.vhromada.catalog.domain.Episode episodeDomain = getEpisode(episode.getId(), show);
        if (episodeDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_EPISODE_MESSAGE);
        }
        final cz.vhromada.catalog.domain.Season season = getSeason(show, episode);
        final List<cz.vhromada.catalog.domain.Episode> episodes = CollectionUtils.getSortedData(season.getEpisodes());
        if (up) {
            if (episodes.indexOf(episodeDomain) <= 0) {
                throw new IllegalArgumentException(NOT_MOVABLE_EPISODE_MESSAGE);
            }
        } else if (episodes.indexOf(episodeDomain) >= episodes.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_EPISODE_MESSAGE);
        }

        final int index = episodes.indexOf(episodeDomain);
        final cz.vhromada.catalog.domain.Episode other = episodes.get(up ? index - 1 : index + 1);
        final int position = episodeDomain.getPosition();
        episodeDomain.setPosition(other.getPosition());
        other.setPosition(position);

        updateEpisode(show, episodeDomain);
        updateEpisode(show, other);

        showService.update(show);
    }

}

