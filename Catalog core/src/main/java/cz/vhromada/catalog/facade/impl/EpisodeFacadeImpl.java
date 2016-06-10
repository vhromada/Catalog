package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.Movable;
import cz.vhromada.catalog.entities.Episode;
import cz.vhromada.catalog.entities.Season;
import cz.vhromada.catalog.entities.Show;
import cz.vhromada.catalog.facade.EpisodeFacade;
import cz.vhromada.catalog.facade.to.EpisodeTO;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.validators.EpisodeTOValidator;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.service.CatalogService;
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
    private SeasonTOValidator seasonTOValidator;

    /**
     * Validator for TO for episode
     */
    private EpisodeTOValidator episodeTOValidator;

    /**
     * Creates a new instance of EpisodeFacadeImpl.
     *
     * @param showService      service for shows
     * @param converter          converter
     * @param seasonTOValidator  validator for TO for season
     * @param episodeTOValidator validator for TO for episode
     * @throws IllegalArgumentException if service for shows is null
     *                                  or converter is null
     *                                  or validator for TO for season is null
     *                                  or validator for TO for episode is null
     */
    @Autowired
    public EpisodeFacadeImpl(final CatalogService<Show> showService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final SeasonTOValidator seasonTOValidator,
            final EpisodeTOValidator episodeTOValidator) {
        Validators.validateArgumentNotNull(showService, "Service for shows");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(seasonTOValidator, "Validator for TO for season");
        Validators.validateArgumentNotNull(episodeTOValidator, "Validator for TO for episode");

        this.showService = showService;
        this.converter = converter;
        this.seasonTOValidator = seasonTOValidator;
        this.episodeTOValidator = episodeTOValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public EpisodeTO getEpisode(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        final List<Show> shows = showService.getAll();
        for (final Show show : shows) {
            for (final Season season : show.getSeasons()) {
                for (final Episode episode : season.getEpisodes()) {
                    if (id.equals(episode.getId())) {
                        return converter.convert(season, EpisodeTO.class);
                    }
                }

            }
        }

        return null;
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void add(final SeasonTO season, final EpisodeTO episode) {
        seasonTOValidator.validateSeasonTOWithId(season);
        episodeTOValidator.validateNewEpisodeTO(episode);
        final Show show = getShow(episode);
        final Season seasonEntity = getSeason(show, episode);
        Validators.validateExists(season, "TO for season");

        final Episode episodeEntity = converter.convert(episode, Episode.class);
        seasonEntity.getEpisodes().add(episodeEntity);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final EpisodeTO episode) {
        episodeTOValidator.validateExistingEpisodeTO(episode);
        final EpisodeTO episodeTO = getEpisode(episode.getId());
        Validators.validateExists(episodeTO, EPISODE_TO_ARGUMENT);

        final Show show = getShow(episode);
        updateEpisode(show, converter.convert(episode, Episode.class));

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final EpisodeTO episode) {
        episodeTOValidator.validateEpisodeTOWithId(episode);
        final EpisodeTO episodeTO = getEpisode(episode.getId());
        Validators.validateExists(episodeTO, EPISODE_TO_ARGUMENT);

        final Show show = getShow(episode);
        final Season season = getSeason(show, episode);
        final List<Episode> episodes = new ArrayList<>();
        for (final Episode episodeEntity : season.getEpisodes()) {
            if (!episodeEntity.getId().equals(episode.getId())) {
                episodes.add(episodeEntity);
            }
        }
        season.setEpisodes(episodes);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final EpisodeTO episode) {
        episodeTOValidator.validateEpisodeTOWithId(episode);
        final EpisodeTO episodeTO = getEpisode(episode.getId());
        Validators.validateExists(episodeTO, EPISODE_TO_ARGUMENT);

        final Show show = getShow(episode);
        final Episode newEpisode = new Episode();
        newEpisode.setNumber(episode.getNumber());
        newEpisode.setName(episode.getName());
        newEpisode.setLength(episode.getLength());
        newEpisode.setNote(episode.getNote());
        updateEpisode(show, newEpisode);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final EpisodeTO episode) {
        move(episode, true);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final EpisodeTO episode) {
        move(episode, false);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public List<EpisodeTO> findEpisodesBySeason(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
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
     * Returns show for episode.
     *
     * @param episode TO for season
     * @return show for episode
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

        throw new IllegalStateException("Unknown show");
    }

    /**
     * Returns season for episode.
     *
     * @param show show
     * @param episode TO for season
     * @return season for episode
     */
    private Season getSeason(final Show show, final Movable episode) {
        for (final Season season : show.getSeasons()) {
            for (final Episode episodeEntity : season.getEpisodes()) {
                if (episode.getId().equals(episodeEntity.getId())) {
                    return season;
                }
            }
        }

        throw new IllegalStateException("Unknown show");
    }

    /**
     * Updates episode in show.
     * @param show show
     * @param episode episode
     */
    private void updateEpisode(final Show show, final Episode episode) {
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
     * Moves episode in list one position up or down.
     *
     * @param episode TO for episode
     * @param up   if moving season up
     */
    private void move(final EpisodeTO episode, final boolean up) {
        episodeTOValidator.validateEpisodeTOWithId(episode);
        final EpisodeTO episodeTO = getEpisode(episode.getId());
        Validators.validateExists(episodeTO, EPISODE_TO_ARGUMENT);
        final Show show = getShow(episode);
        final Season season = getSeason(show, episode);
        final List<Episode> episodes = CollectionUtils.getSortedData(season.getEpisodes());
        final Episode episodeEntity = converter.convert(episode, Episode.class);
        if (up) {
            Validators.validateMoveUp(episodes, episodeEntity, EPISODE_TO_ARGUMENT);
        } else {
            Validators.validateMoveDown(episodes, episodeEntity, EPISODE_TO_ARGUMENT);
        }

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

