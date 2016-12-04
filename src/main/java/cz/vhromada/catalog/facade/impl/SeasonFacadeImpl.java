package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.util.CatalogUtils;
import cz.vhromada.catalog.util.CollectionUtils;
import cz.vhromada.catalog.validator.SeasonValidator;
import cz.vhromada.catalog.validator.ShowValidator;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of facade for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonFacade")
public class SeasonFacadeImpl implements SeasonFacade {

    /**
     * Message for not existing season
     */
    private static final String NOT_EXISTING_SEASON_MESSAGE = "Season doesn't exist.";

    /**
     * Message for not movable season
     */
    private static final String NOT_MOVABLE_SEASON_MESSAGE = "ID isn't valid - season can't be moved.";

    /**
     * Message for not existing show
     */
    private static final String NOT_EXISTING_SHOW_MESSAGE = "Show doesn't exist.";

    /**
     * Service for shows
     */
    private CatalogService<cz.vhromada.catalog.domain.Show> showService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for show
     */
    private ShowValidator showValidator;

    /**
     * Validator for season
     */
    private SeasonValidator seasonValidator;

    /**
     * Creates a new instance of SeasonFacadeImpl.
     *
     * @param showService     service for shows
     * @param converter       converter
     * @param showValidator   validator for show
     * @param seasonValidator validator for season
     * @throws IllegalArgumentException if service for shows is null
     *                                  or converter is null
     *                                  or validator for show is null
     *                                  or validator for season is null
     */
    @Autowired
    public SeasonFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Show> showService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final ShowValidator showValidator,
            final SeasonValidator seasonValidator) {
        Assert.notNull(showService, "Service for shows mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(showValidator, "Validator for show mustn't be null.");
        Assert.notNull(seasonValidator, "Validator for season mustn't be null.");

        this.showService = showService;
        this.converter = converter;
        this.showValidator = showValidator;
        this.seasonValidator = seasonValidator;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Season getSeason(final Integer id) {
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(getSeasonDomain(id), Season.class);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void add(final Show show, final Season season) {
        showValidator.validateShowWithId(show);
        seasonValidator.validateNewSeason(season);
        final cz.vhromada.catalog.domain.Show showDomain = showService.get(show.getId());
        if (showDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SHOW_MESSAGE);
        }

        final cz.vhromada.catalog.domain.Season seasonDomain = converter.convert(season, cz.vhromada.catalog.domain.Season.class);
        seasonDomain.setPosition(Integer.MAX_VALUE);
        seasonDomain.setEpisodes(new ArrayList<>());
        showDomain.getSeasons().add(seasonDomain);

        showService.update(showDomain);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void update(final Season season) {
        seasonValidator.validateExistingSeason(season);
        final cz.vhromada.catalog.domain.Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonDomain = getSeason(season.getId(), show);
        if (seasonDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SEASON_MESSAGE);
        }

        seasonDomain.setNumber(season.getNumber());
        seasonDomain.setStartYear(season.getStartYear());
        seasonDomain.setEndYear(season.getEndYear());
        seasonDomain.setLanguage(season.getLanguage());
        seasonDomain.setSubtitles(new ArrayList<>(season.getSubtitles()));
        seasonDomain.setNote(season.getNote());
        seasonDomain.setPosition(season.getPosition());

        updateSeason(show, seasonDomain);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void remove(final Season season) {
        seasonValidator.validateSeasonWithId(season);
        final cz.vhromada.catalog.domain.Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonDomain = getSeason(season.getId(), show);
        if (seasonDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SEASON_MESSAGE);
        }

        final List<cz.vhromada.catalog.domain.Season> seasons = show.getSeasons().stream().filter(seasonValue -> !seasonValue.getId().equals(season.getId()))
                .collect(Collectors.toList());
        show.setSeasons(seasons);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void duplicate(final Season season) {
        seasonValidator.validateSeasonWithId(season);
        final cz.vhromada.catalog.domain.Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonDomain = getSeason(season.getId(), show);
        if (seasonDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SEASON_MESSAGE);
        }

        final cz.vhromada.catalog.domain.Season newSeason = CatalogUtils.duplicateSeason(seasonDomain);
        show.getSeasons().add(newSeason);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void moveUp(final Season season) {
        move(season, true);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public void moveDown(final Season season) {
        move(season, false);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     */
    @Override
    public List<Season> findSeasonsByShow(final Show show) {
        showValidator.validateShowWithId(show);
        final cz.vhromada.catalog.domain.Show showDomain = showService.get(show.getId());
        if (showDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SHOW_MESSAGE);
        }

        return CollectionUtils.getSortedData(converter.convertCollection(showDomain.getSeasons(), Season.class));
    }

    /**
     * Returns season with ID.
     *
     * @param id   ID
     * @param show show
     * @return season with ID
     */
    private static cz.vhromada.catalog.domain.Season getSeason(final Integer id, final cz.vhromada.catalog.domain.Show show) {
        if (show == null) {
            return null;
        }

        for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
            if (id.equals(season.getId())) {
                return season;
            }
        }

        return null;
    }

    /**
     * Updates season in show.
     *
     * @param show   show
     * @param season season
     */
    private static void updateSeason(final cz.vhromada.catalog.domain.Show show, final cz.vhromada.catalog.domain.Season season) {
        final List<cz.vhromada.catalog.domain.Season> seasons = new ArrayList<>();
        for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
            if (seasonDomain.equals(season)) {
                seasons.add(season);
            } else {
                seasons.add(seasonDomain);
            }
        }
        show.setSeasons(seasons);
    }

    /**
     * Returns season with ID.
     *
     * @param id ID
     * @return season with ID
     */
    private cz.vhromada.catalog.domain.Season getSeasonDomain(final Integer id) {
        final List<cz.vhromada.catalog.domain.Show> shows = showService.getAll();
        for (final cz.vhromada.catalog.domain.Show show : shows) {
            for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
                if (id.equals(season.getId())) {
                    return season;
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
    private cz.vhromada.catalog.domain.Show getShow(final Season season) {
        for (final cz.vhromada.catalog.domain.Show show : showService.getAll()) {
            for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
                if (season.getId().equals(seasonDomain.getId())) {
                    return show;
                }
            }
        }

        return null;
    }

    /**
     * Moves season in list one position up or down.
     *
     * @param season season
     * @param up     true if moving season up
     */
    private void move(final Season season, final boolean up) {
        seasonValidator.validateSeasonWithId(season);
        final cz.vhromada.catalog.domain.Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonDomain = getSeason(season.getId(), show);
        if (seasonDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_SEASON_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Season> seasons = CollectionUtils.getSortedData(show.getSeasons());
        if (up) {
            if (seasons.indexOf(seasonDomain) <= 0) {
                throw new IllegalArgumentException(NOT_MOVABLE_SEASON_MESSAGE);
            }
        } else if (seasons.indexOf(seasonDomain) >= seasons.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_SEASON_MESSAGE);
        }

        final int index = seasons.indexOf(seasonDomain);
        final cz.vhromada.catalog.domain.Season other = seasons.get(up ? index - 1 : index + 1);
        final int position = seasonDomain.getPosition();
        seasonDomain.setPosition(other.getPosition());
        other.setPosition(position);

        updateSeason(show, seasonDomain);
        updateSeason(show, other);

        showService.update(show);
    }

}
