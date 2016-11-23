package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.CatalogUtils;
import cz.vhromada.catalog.common.CollectionUtils;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.entity.SeasonTO;
import cz.vhromada.catalog.entity.ShowTO;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validators.SeasonTOValidator;
import cz.vhromada.catalog.validators.ShowTOValidator;
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

        return converter.convert(getSeasonEntity(id), SeasonTO.class);
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
    public void update(final SeasonTO season) {
        seasonTOValidator.validateExistingSeasonTO(season);
        final Show show = getShow(season);
        final Season seasonEntity = getSeason(season.getId(), show);
        Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
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
    public void remove(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        final Show show = getShow(season);
        final Season seasonEntity = getSeason(season.getId(), show);
        Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
        assert seasonEntity != null;

        final List<Season> seasons = show.getSeasons().stream().filter(seasonValue -> !seasonValue.getId().equals(season.getId()))
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
    public void duplicate(final SeasonTO season) {
        seasonTOValidator.validateSeasonTOWithId(season);
        final Show show = getShow(season);
        final Season seasonEntity = getSeason(season.getId(), show);
        Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
        assert seasonEntity != null;

        final Season newSeason = CatalogUtils.duplicateSeason(seasonEntity);
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
     * Returns season with ID.
     *
     * @param id   ID
     * @param show show
     * @return season with ID
     */
    private static Season getSeason(final Integer id, final Show show) {
        if (show == null) {
            return null;
        }

        for (final Season season : show.getSeasons()) {
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
    private static void updateSeason(final Show show, final Season season) {
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
     * Returns season with ID.
     *
     * @param id ID
     * @return season with ID
     */
    private Season getSeasonEntity(final Integer id) {
        final List<Show> shows = showService.getAll();
        for (final Show show : shows) {
            for (final Season season : show.getSeasons()) {
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

        return null;
    }

    /**
     * Moves TO for season in list one position up or down.
     *
     * @param season TO for season
     * @param up     true if moving TO for season up
     */
    private void move(final SeasonTO season, final boolean up) {
        seasonTOValidator.validateSeasonTOWithId(season);
        final Show show = getShow(season);
        final Season seasonEntity = getSeason(season.getId(), show);
        Validators.validateExists(seasonEntity, SEASON_TO_ARGUMENT);
        assert seasonEntity != null;
        final List<Season> seasons = CollectionUtils.getSortedData(show.getSeasons());
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
