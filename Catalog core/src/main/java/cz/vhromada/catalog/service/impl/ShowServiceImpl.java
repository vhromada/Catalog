package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.entities.Episode;
import cz.vhromada.catalog.entities.Season;
import cz.vhromada.catalog.entities.Show;
import cz.vhromada.catalog.repository.ShowRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for shows.
 *
 * @author Vladimir Hromada
 */
@Component("showService")
public class ShowServiceImpl extends AbstractCatalogService<Show> {

    /**
     * Creates a new instance of ShowServiceImpl.
     *
     * @param showRepository repository for shows
     * @param cache          cache
     * @throws IllegalArgumentException if repository for shows is null
     *                                  or cache is null
     */
    @Autowired
    public ShowServiceImpl(final ShowRepository showRepository,
            @Value("#{cacheManager.getCache('catalogCache')}") final Cache cache) {
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
        newShow.setGenres(new ArrayList<>(data.getGenres()));
        final List<Season> newSeasons = new ArrayList<>();
        for (final Season season : data.getSeasons()) {
            final Season newSeason = new Season();
            newSeason.setNumber(season.getNumber());
            newSeason.setStartYear(season.getStartYear());
            newSeason.setEndYear(season.getEndYear());
            newSeason.setLanguage(season.getLanguage());
            newSeason.setSubtitles(new ArrayList<>(season.getSubtitles()));
            newSeason.setNote(season.getNote());
            final List<Episode> newEpisodes = new ArrayList<>();
            for (final Episode episode : season.getEpisodes()) {
                final Episode newEpisode = new Episode();
                newEpisode.setNumber(episode.getNumber());
                newEpisode.setName(episode.getName());
                newEpisode.setLength(episode.getLength());
                newEpisode.setNote(episode.getNote());
                newEpisodes.add(newEpisode);
            }
            newSeason.setEpisodes(newEpisodes);
            newSeasons.add(newSeason);
        }
        newShow.setSeasons(newSeasons);

        return newShow;
    }

    @Override
    protected void updatePositions(final List<Show> data) {
        for (int i = 0; i < data.size(); i++) {
            final Show show = data.get(i);
            show.setPosition(i);
            final List<Season> seasons = show.getSeasons();
            for (int j = 0; j < seasons.size(); j++) {
                final Season season = seasons.get(j);
                season.setPosition(j);
                final List<Episode> episodes = season.getEpisodes();
                for (int k = 0; k < episodes.size(); k++) {
                    final Episode episode = episodes.get(k);
                    episode.setPosition(k);
                }
            }
        }
    }

}
