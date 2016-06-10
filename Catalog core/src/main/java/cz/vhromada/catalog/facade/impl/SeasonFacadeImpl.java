package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.entities.Episode;
import cz.vhromada.catalog.entities.Season;
import cz.vhromada.catalog.entities.Show;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.facade.to.SeasonTO;
import cz.vhromada.catalog.facade.to.ShowTO;
import cz.vhromada.catalog.facade.validators.SeasonTOValidator;
import cz.vhromada.catalog.facade.validators.ShowTOValidator;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonFacade")
public class SeasonFacadeImpl implements SeasonFacade {

    /**
     * TO for show argument
     */
    private static final String SHOW_TO_ARGUMENT = "TO for show";

    /**
     * TO for season argument
     */
    private static final String SEASON_TO_ARGUMENT = "TO for season";

    /**
     * Service for shows
     */
    private CatalogService<Show> showService;

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
     * @param converter         converter
     * @param showTOValidator   validator for TO for show
     * @param seasonTOValidator validator for TO for season
     * @throws IllegalArgumentException if service for shows is null
     *                                  or converter is null
     *                                  or validator for TO for show is null
     *                                  or validator for TO for season is null
     */
    @Autowired
    public SeasonFacadeImpl(final CatalogService<Show> showService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final ShowTOValidator showTOValidator,
            final SeasonTOValidator seasonTOValidator) {
        Validators.validateArgumentNotNull(showService, "Service for shows");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(showTOValidator, "Validator for TO for show");
        Validators.validateArgumentNotNull(seasonTOValidator, "Validator for TO for season");

        this.showService = showService;
        this.converter = converter;
        this.showTOValidator = showTOValidator;
        this.seasonTOValidator = seasonTOValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public SeasonTO getSeason(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        final List<Show> shows = showService.getAll();
        for (final Show show : shows) {
            for (final Season season : show.getSeasons()) {
                if (id.equals(season.getId())) {
                    return converter.convert(season, SeasonTO.class);
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
    public void add(final ShowTO show, final SeasonTO season) {
        showTOValidator.validateShowTOWithId(show);
        seasonTOValidator.validateNewSeasonTO(season);
        final Show showEntity = showService.get(show.getId());
        Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);

        final Season seasonEntity = converter.convert(season, Season.class);
        showEntity.getSeasons().add(seasonEntity);

        showService.update(showEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final SeasonTO season) {
        seasonTOValidator.validateExistingSeasonTO(season);
        final SeasonTO seasonTO = getSeason(season.getId());
        Validators.validateExists(seasonTO, SEASON_TO_ARGUMENT);

        final Show show = getShow(season);
        updateSeason(show, converter.convert(season, Season.class));

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        final SeasonTO seasonTO = getSeason(season.getId());
        Validators.validateExists(seasonTO, SEASON_TO_ARGUMENT);

        final Show show = getShow(season);
        final List<Season> seasons = new ArrayList<>();
        for (final Season seasonEntity : show.getSeasons()) {
            if (!seasonEntity.getId().equals(season.getId())) {
                seasons.add(seasonEntity);
            }
        }
        show.setSeasons(seasons);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        final SeasonTO seasonTO = getSeason(season.getId());
        Validators.validateExists(seasonTO, SEASON_TO_ARGUMENT);

        final Show show = getShow(season);
        final Season newSeason = new Season();
        newSeason.setNumber(season.getNumber());
        newSeason.setStartYear(season.getStartYear());
        newSeason.setEndYear(season.getEndYear());
        newSeason.setLanguage(season.getLanguage());
        newSeason.setSubtitles(new ArrayList<>(season.getSubtitles()));
        newSeason.setNote(season.getNote());
        final List<Episode> newEpisodes = new ArrayList<>();
        //TODO vladimir.hromada 10.06.2016: get episodes
        for (final Episode episode : newEpisodes) {
            final Episode newEpisode = new Episode();
            newEpisode.setNumber(episode.getNumber());
            newEpisode.setName(episode.getName());
            newEpisode.setLength(episode.getLength());
            newEpisode.setNote(episode.getNote());
            newEpisodes.add(newEpisode);
        }
        newSeason.setEpisodes(newEpisodes);
        show.getSeasons().add(newSeason);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final SeasonTO season) {
        move(season, true);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final SeasonTO season) {
        move(season, false);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public List<SeasonTO> findSeasonsByShow(final ShowTO show) {
        showTOValidator.validateShowTOWithId(show);
        final Show showEntity = showService.get(show.getId());
        Validators.validateExists(showEntity, SHOW_TO_ARGUMENT);

        return CollectionUtils.getSortedData(converter.convertCollection(showEntity.getSeasons(), SeasonTO.class));
    }

    /**
     * Returns show for season.
     *
     * @param season TO for season
     * @return show for season
     */
    private Show getShow(final SeasonTO season) {
        for (final Show show : showService.getAll()) {
            for (final Season seasonEntity : show.getSeasons()) {
                if (season.getId().equals(seasonEntity.getId())) {
                    return show;
                }
            }
        }

        throw new IllegalStateException("Unknown show");
    }

    /**
     * Updates season in show.
     *
     * @param show   TO for show
     * @param season TO for season
     */
    private void updateSeason(final Show show, final Season season) {
        final List<Season> seasons = new ArrayList<>();
        for (final Season seasonEntity : show.getSeasons()) {
            if (seasonEntity.equals(season)) {
                seasons.add(season);
            } else {
                seasons.add(seasonEntity);
            }
        }
        show.setSeasons(seasons);
    }

    /**
     * Moves season in list one position up or down.
     *
     * @param season TO for season
     * @param up     if moving season up
     */
    private void move(final SeasonTO season, final boolean up) {
        seasonTOValidator.validateSeasonTOWithId(season);
        final SeasonTO seasonTO = getSeason(season.getId());
        Validators.validateExists(seasonTO, SEASON_TO_ARGUMENT);
        final Show show = getShow(season);
        final List<Season> seasons = CollectionUtils.getSortedData(show.getSeasons());
        final Season seasonEntity = converter.convert(season, Season.class);
        if (up) {
            Validators.validateMoveUp(seasons, seasonEntity, SEASON_TO_ARGUMENT);
        } else {
            Validators.validateMoveDown(seasons, seasonEntity, SEASON_TO_ARGUMENT);
        }

        final int index = seasons.indexOf(seasonEntity);
        final Season other = seasons.get(up ? index - 1 : index + 1);
        final int position = seasonEntity.getPosition();
        seasonEntity.setPosition(other.getPosition());
        other.setPosition(position);

        updateSeason(show, seasonEntity);
        updateSeason(show, other);

        showService.update(show);
    }

}
