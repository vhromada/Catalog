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
     * Show argument
     */
    private static final String SHOW_ARGUMENT = "show";

    /**
     * Season argument
     */
    private static final String SEASON_ARGUMENT = "season";

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
        Validators.validateArgumentNotNull(showService, "Service for shows");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(showValidator, "Validator for show");
        Validators.validateArgumentNotNull(seasonValidator, "Validator for season");

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
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(getSeasonEntity(id), Season.class);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void add(final Show show, final Season season) {
        showValidator.validateShowWithId(show);
        seasonValidator.validateNewSeason(season);
        final cz.vhromada.catalog.domain.Show showEntity = showService.get(show.getId());
        Validators.validateExists(showEntity, SHOW_ARGUMENT);

        final cz.vhromada.catalog.domain.Season seasonEntity = converter.convert(season, cz.vhromada.catalog.domain.Season.class);
        seasonEntity.setPosition(Integer.MAX_VALUE);
        seasonEntity.setEpisodes(new ArrayList<>());
        showEntity.getSeasons().add(seasonEntity);

        showService.update(showEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final Season season) {
        seasonValidator.validateExistingSeason(season);
        final cz.vhromada.catalog.domain.Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonEntity = getSeason(season.getId(), show);
        Validators.validateExists(seasonEntity, SEASON_ARGUMENT);
        assert seasonEntity != null;

        seasonEntity.setNumber(season.getNumber());
        seasonEntity.setStartYear(season.getStartYear());
        seasonEntity.setEndYear(season.getEndYear());
        seasonEntity.setLanguage(season.getLanguage());
        seasonEntity.setSubtitles(new ArrayList<>(season.getSubtitles()));
        seasonEntity.setNote(season.getNote());
        seasonEntity.setPosition(season.getPosition());

        updateSeason(show, seasonEntity);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final Season season) {
        seasonValidator.validateSeasonWithId(season);
        final cz.vhromada.catalog.domain.Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonEntity = getSeason(season.getId(), show);
        Validators.validateExists(seasonEntity, SEASON_ARGUMENT);
        assert seasonEntity != null;

        final List<cz.vhromada.catalog.domain.Season> seasons = show.getSeasons().stream().filter(seasonValue -> !seasonValue.getId().equals(season.getId()))
                .collect(Collectors.toList());
        show.setSeasons(seasons);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final Season season) {
        seasonValidator.validateSeasonWithId(season);
        final cz.vhromada.catalog.domain.Show show = getShow(season);
        final cz.vhromada.catalog.domain.Season seasonEntity = getSeason(season.getId(), show);
        Validators.validateExists(seasonEntity, SEASON_ARGUMENT);
        assert seasonEntity != null;

        final cz.vhromada.catalog.domain.Season newSeason = CatalogUtils.duplicateSeason(seasonEntity);
        show.getSeasons().add(newSeason);

        showService.update(show);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final Season season) {
        move(season, true);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final Season season) {
        move(season, false);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public List<Season> findSeasonsByShow(final Show show) {
        showValidator.validateShowWithId(show);
        final cz.vhromada.catalog.domain.Show showEntity = showService.get(show.getId());
        Validators.validateExists(showEntity, SHOW_ARGUMENT);

        return CollectionUtils.getSortedData(converter.convertCollection(showEntity.getSeasons(), Season.class));
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
        for (final cz.vhromada.catalog.domain.Season seasonEntity : show.getSeasons()) {
            if (seasonEntity.equals(season)) {
                seasons.add(season);
            } else {
                seasons.add(seasonEntity);
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
    private cz.vhromada.catalog.domain.Season getSeasonEntity(final Integer id) {
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
            for (final cz.vhromada.catalog.domain.Season seasonEntity : show.getSeasons()) {
                if (season.getId().equals(seasonEntity.getId())) {
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
        final cz.vhromada.catalog.domain.Season seasonEntity = getSeason(season.getId(), show);
        Validators.validateExists(seasonEntity, SEASON_ARGUMENT);
        assert seasonEntity != null;
        final List<cz.vhromada.catalog.domain.Season> seasons = CollectionUtils.getSortedData(show.getSeasons());
        if (up) {
            Validators.validateMoveUp(seasons, seasonEntity, SEASON_ARGUMENT);
        } else {
            Validators.validateMoveDown(seasons, seasonEntity, SEASON_ARGUMENT);
        }

        final int index = seasons.indexOf(seasonEntity);
        final cz.vhromada.catalog.domain.Season other = seasons.get(up ? index - 1 : index + 1);
        final int position = seasonEntity.getPosition();
        seasonEntity.setPosition(other.getPosition());
        other.setPosition(position);

        updateSeason(show, seasonEntity);
        updateSeason(show, other);

        showService.update(show);
    }

}
