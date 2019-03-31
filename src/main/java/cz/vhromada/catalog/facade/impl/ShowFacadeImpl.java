package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;

import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.facade.ShowFacade;
import cz.vhromada.common.Time;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.AbstractMovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.validation.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * A class represents implementation of facade for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showFacade")
public class ShowFacadeImpl extends AbstractMovableParentFacade<Show, cz.vhromada.catalog.domain.Show> implements ShowFacade {

    /**
     * Creates a new instance of ShowFacadeImpl.
     *
     * @param showService   service for shows
     * @param converter     converter for shows
     * @param showValidator validator for show
     * @throws IllegalArgumentException if service for shows is null
     *                                  or converter for shows is null
     *                                  or validator for show is null
     */
    @Autowired
    public ShowFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Show> showService,
        final MovableConverter<Show, cz.vhromada.catalog.domain.Show> converter, final MovableValidator<Show> showValidator) {
        super(showService, converter, showValidator);
    }

    @Override
    public Result<Time> getTotalLength() {
        int totalLength = 0;
        for (final cz.vhromada.catalog.domain.Show show : getService().getAll()) {
            if (!CollectionUtils.isEmpty(show.getSeasons())) {
                for (final Season season : show.getSeasons()) {
                    if (!CollectionUtils.isEmpty(season.getEpisodes())) {
                        for (final Episode episode : season.getEpisodes()) {
                            totalLength += episode.getLength();
                        }
                    }
                }
            }
        }

        return Result.of(new Time(totalLength));
    }

    @Override
    public Result<Integer> getSeasonsCount() {
        int seasonsCount = 0;
        for (final cz.vhromada.catalog.domain.Show show : getService().getAll()) {
            if (!CollectionUtils.isEmpty(show.getSeasons())) {
                seasonsCount += show.getSeasons().size();
            }
        }

        return Result.of(seasonsCount);
    }

    @Override
    public Result<Integer> getEpisodesCount() {
        int episodesCount = 0;
        for (final cz.vhromada.catalog.domain.Show show : getService().getAll()) {
            if (!CollectionUtils.isEmpty(show.getSeasons())) {
                for (final Season season : show.getSeasons()) {
                    if (!CollectionUtils.isEmpty(season.getEpisodes())) {
                        episodesCount += season.getEpisodes().size();
                    }
                }
            }
        }

        return Result.of(episodesCount);
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getDataForAdd(final Show data) {
        final cz.vhromada.catalog.domain.Show show = super.getDataForAdd(data);
        show.setSeasons(new ArrayList<>());

        return show;
    }

    @Override
    protected cz.vhromada.catalog.domain.Show getDataForUpdate(final Show data) {
        final cz.vhromada.catalog.domain.Show show = super.getDataForUpdate(data);
        show.setSeasons(getService().get(data.getId()).getSeasons());

        return show;
    }

}
