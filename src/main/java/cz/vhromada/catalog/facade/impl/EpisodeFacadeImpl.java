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
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeFacade")
public class EpisodeFacadeImpl extends AbstractCatalogChildFacade<Episode, cz.vhromada.catalog.domain.Episode, Season, Show>
        implements EpisodeFacade {

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
            final CatalogValidator<Season> seasonValidator,
            final CatalogValidator<Episode> episodeValidator) {
        super(showService, converter, seasonValidator, episodeValidator);
    }

    @Override
    protected cz.vhromada.catalog.domain.Episode getDomainData(final Integer id) {
        final List<Show> shows = getCatalogService().getAll();
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

    @Override
    protected List<cz.vhromada.catalog.domain.Episode> getDomainList(final Season parent) {
        final List<Show> shows = getCatalogService().getAll();
        for (final Show show : shows) {
            for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
                if (parent.getId().equals(seasonDomain.getId())) {
                    return seasonDomain.getEpisodes();
                }
            }
        }

        return null;
    }

    @Override
    protected Show getForAdd(final Season parent, final cz.vhromada.catalog.domain.Episode data) {
        final Show show = getShow(parent);

        getSeason(show, parent).getEpisodes().add(data);

        return show;
    }

    @Override
    protected Show getForUpdate(final Episode data) {
        final Show show = getShow(data);

        updateEpisode(show, getDataForUpdate(data));

        return show;
    }

    @Override
    protected Show getForRemove(final Episode data) {
        final Show show = getShow(data);
        final cz.vhromada.catalog.domain.Season season = getSeason(show, data);
        final List<cz.vhromada.catalog.domain.Episode> episodes = season.getEpisodes().stream()
                .filter(episodeValue -> !episodeValue.getId().equals(data.getId()))
                .collect(Collectors.toList());
        season.setEpisodes(episodes);

        return show;
    }

    @Override
    protected Show getForDuplicate(final Episode data) {
        final Show show = getShow(data);

        final cz.vhromada.catalog.domain.Episode episodeDomain = getEpisode(data.getId(), show);
        final cz.vhromada.catalog.domain.Episode newEpisode = CatalogUtils.duplicateEpisode(episodeDomain);
        final cz.vhromada.catalog.domain.Season seasonDomain = getSeason(show, data);
        seasonDomain.getEpisodes().add(newEpisode);

        return show;
    }

    @Override
    protected Show getForMove(final Episode data, final boolean up) {
        final Show show = getShow(data);
        final cz.vhromada.catalog.domain.Episode episodeDomain = getEpisode(data.getId(), show);
        final cz.vhromada.catalog.domain.Season season = getSeason(show, data);
        final List<cz.vhromada.catalog.domain.Episode> episodes = CollectionUtils.getSortedData(season.getEpisodes());
        final int index = episodes.indexOf(episodeDomain);
        final cz.vhromada.catalog.domain.Episode other = episodes.get(up ? index - 1 : index + 1);
        final int position = episodeDomain.getPosition();
        episodeDomain.setPosition(other.getPosition());
        other.setPosition(position);

        updateEpisode(show, episodeDomain);
        updateEpisode(show, other);

        return show;
    }

    @Override
    protected Class<Episode> getEntityClass() {
        return Episode.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Episode> getDomainClass() {
        return cz.vhromada.catalog.domain.Episode.class;
    }

    /**
     * Returns show for season.
     *
     * @param season season
     * @return show for season
     */
    private Show getShow(final Season season) {
        for (final Show show : getCatalogService().getAll()) {
            for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
                if (season.getId().equals(seasonDomain.getId())) {
                    return show;
                }
            }
        }

        throw new IllegalStateException("Unknown season.");
    }

    /**
     * Returns show for episode.
     *
     * @param episode episode
     * @return show for episode
     */
    private Show getShow(final Episode episode) {
        for (final Show show : getCatalogService().getAll()) {
            for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
                for (final cz.vhromada.catalog.domain.Episode episodeDomain : season.getEpisodes()) {
                    if (episode.getId().equals(episodeDomain.getId())) {
                        return show;
                    }
                }
            }
        }

        throw new IllegalStateException("Unknown episode.");
    }

    /**
     * Returns season for show.
     *
     * @param show   show
     * @param season season
     * @return season for show
     */
    private static cz.vhromada.catalog.domain.Season getSeason(final Show show, final Season season) {
        for (final cz.vhromada.catalog.domain.Season seasonDomain : show.getSeasons()) {
            if (season.getId().equals(seasonDomain.getId())) {
                return seasonDomain;
            }
        }

        throw new IllegalStateException("Unknown season");
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

        throw new IllegalStateException("Unknown episode");
    }


    /**
     * Returns episode with ID.
     *
     * @param id   ID
     * @param show show
     * @return episode with ID
     */
    private static cz.vhromada.catalog.domain.Episode getEpisode(final Integer id, final Show show) {
        for (final cz.vhromada.catalog.domain.Season season : show.getSeasons()) {
            for (final cz.vhromada.catalog.domain.Episode episode : season.getEpisodes()) {
                if (id.equals(episode.getId())) {
                    return episode;
                }
            }
        }

        throw new IllegalStateException("Unknown episode");
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

}
