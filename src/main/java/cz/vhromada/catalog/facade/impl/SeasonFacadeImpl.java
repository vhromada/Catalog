package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.entity.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.SeasonFacade;
import cz.vhromada.catalog.utils.CatalogUtils;
import cz.vhromada.common.facade.AbstractMovableChildFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.utils.CollectionUtils;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonFacade")
public class SeasonFacadeImpl extends AbstractMovableChildFacade<Season, cz.vhromada.catalog.domain.Season, Show, cz.vhromada.catalog.domain.Show>
    implements SeasonFacade {

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
    public SeasonFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Show> showService, final Converter converter,
        final MovableValidator<Show> showValidator, final MovableValidator<Season> seasonValidator) {
        super(showService, converter, showValidator, seasonValidator);
    }

    @Override
    protected cz.vhromada.catalog.domain.Season getDomainData(final Integer id) {
        final List<cz.vhromada.catalog.domain.Show> shows = getMovableService().getAll();
        for (final cz.vhromada.catalog.domain.Show show : shows) {
            for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
                if (id.equals(season.getId())) {
                    return season;
                }
            }
        }

        return null;
    }

    @Override
    protected List<cz.vhromada.catalog.domain.Season> getDomainList(final Show parent) {
        return getMovableService().get(parent.getId()).getSeasons();
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getForAdd(final Show parent, final cz.vhromada.catalog.domain.Season data) {
        final cz.vhromada.catalog.domain.Show show = getMovableService().get(parent.getId());
        if (show.getSeasons() == null) {
            show.setSeasons(new ArrayList<>());
        }
        show.getSeasons().add(data);

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getForUpdate(final Season data) {
        final cz.vhromada.catalog.domain.Show show = getShow(data);
        final cz.vhromada.catalog.domain.Season season = getDataForUpdate(data);
        season.setEpisodes(getSeason(data.getId(), show).getEpisodes());

        updateSeason(show, season);

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getForRemove(final Season data) {
        final cz.vhromada.catalog.domain.Show show = getShow(data);

        final List<cz.vhromada.catalog.domain.Season> seasons = show.getSeasons().stream()
            .filter(seasonValue -> !seasonValue.getId().equals(data.getId()))
            .collect(Collectors.toList());
        show.setSeasons(seasons);

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getForDuplicate(final Season data) {
        final cz.vhromada.catalog.domain.Show show = getShow(data);
        final cz.vhromada.catalog.domain.Season seasonDomain = getSeason(data.getId(), show);

        final cz.vhromada.catalog.domain.Season newSeason = CatalogUtils.duplicateSeason(seasonDomain);
        show.getSeasons().add(newSeason);

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getForMove(final Season data, final boolean up) {
        final cz.vhromada.catalog.domain.Show show = getShow(data);
        final cz.vhromada.catalog.domain.Season season = getSeason(data.getId(), show);
        final List<cz.vhromada.catalog.domain.Season> seasons = CollectionUtils.getSortedData(show.getSeasons());

        final int index = seasons.indexOf(season);
        final cz.vhromada.catalog.domain.Season other = seasons.get(up ? index - 1 : index + 1);
        final int position = season.getPosition();
        season.setPosition(other.getPosition());
        other.setPosition(position);

        updateSeason(show, season);
        updateSeason(show, other);

        return show;
    }

    @Override
    protected Class<Season> getEntityClass() {
        return Season.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Season> getDomainClass() {
        return cz.vhromada.catalog.domain.Season.class;
    }

    /**
     * Returns show for season.
     *
     * @param season season
     * @return show for season
     */
    private cz.vhromada.catalog.domain.Show getShow(final Season season) {
        for (final cz.vhromada.catalog.domain.Show show : getMovableService().getAll()) {
            for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
                if (season.getId().equals(seasonDomain.getId())) {
                    return show;
                }
            }
        }

        throw new IllegalStateException("Unknown season.");
    }

    /**
     * Returns season with ID.
     *
     * @param id   ID
     * @param show show
     * @return season with ID
     */
    private static cz.vhromada.catalog.domain.Season getSeason(final Integer id, final cz.vhromada.catalog.domain.Show show) {
        for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
            if (id.equals(season.getId())) {
                return season;
            }
        }

        throw new IllegalStateException("Unknown season.");
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

}
