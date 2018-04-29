package cz.vhromada.catalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.vhromada.catalog.domain.Episode;
import cz.vhromada.catalog.domain.Season;
import cz.vhromada.catalog.domain.Show;
import cz.vhromada.catalog.repository.ShowRepository;
import cz.vhromada.catalog.utils.CatalogUtils;
import cz.vhromada.common.service.AbstractMovableService;
import cz.vhromada.common.utils.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents service for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showService")
public class ShowService extends AbstractMovableService<Show> {

    /**
     * Creates a new instance of ShowService.
     *
     * @param showRepository repository for shows
     * @param cache          cache
     * @throws IllegalArgumentException if repository for shows is null
     *                                  or cache is null
     */
    @Autowired
    public ShowService(final ShowRepository showRepository, @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
        super(showRepository, cache, "shows");
    }

    @Override
    protected Show getCopy(final Show data) {
        final Show newShow = new Show();
        newShow.setCzechName(data.getCzechName());
        newShow.setOriginalName(data.getOriginalName());
        newShow.setCsfd(data.getCsfd());
        newShow.setImdbCode(data.getImdbCode());
        newShow.setWikiEn(data.getWikiEn());
        newShow.setWikiCz(data.getWikiCz());
        newShow.setPicture(data.getPicture());
        newShow.setNote(data.getNote());
        newShow.setPosition(data.getPosition());
        newShow.setGenres(new ArrayList<>(data.getGenres()));
        newShow.setSeasons(data.getSeasons().stream().map(CatalogUtils::duplicateSeason).collect(Collectors.toList()));

        return newShow;
    }

    @Override
    protected void updatePositions(final List<Show> data) {
        for (int i = 0; i < data.size(); i++) {
            final Show show = data.get(i);
            show.setPosition(i);
            if (show.getSeasons() == null) {
                show.setSeasons(new ArrayList<>());
            }
            final List<Season> seasons = CollectionUtils.getSortedData(show.getSeasons());
            for (int j = 0; j < seasons.size(); j++) {
                final Season season = seasons.get(j);
                season.setPosition(j);
                if (season.getEpisodes() == null) {
                    season.setEpisodes(new ArrayList<>());
                }
                final List<Episode> episodes = CollectionUtils.getSortedData(season.getEpisodes());
                for (int k = 0; k < episodes.size(); k++) {
                    final Episode episode = episodes.get(k);
                    episode.setPosition(k);
                }
            }
        }
    }

}
