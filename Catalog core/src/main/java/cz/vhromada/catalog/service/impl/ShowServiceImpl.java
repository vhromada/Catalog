package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.ShowDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.ShowService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;

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
public class ShowServiceImpl extends AbstractShowService implements ShowService {

    /**
     * DAO for shows field
     */
    private static final String SHOW_DAO_ARGUMENT = "DAO for shows";

    /**
     * DAO for seasons field
     */
    private static final String SEASON_DAO_ARGUMENT = "DAO for seasons";

    /**
     * DAO for episodes field
     */
    private static final String EPISODE_DAO_ARGUMENT = "DAO for episodes";

    /**
     * Show argument
     */
    private static final String SHOW_ARGUMENT = "Show";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link ServiceOperationException}
     */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /**
     * DAO for shows
     */
    private ShowDAO showDAO;

    /**
     * DAO for seasons
     */
    private SeasonDAO seasonDAO;

    /**
     * DAO for episodes
     */
    private EpisodeDAO episodeDAO;

    /**
     * Creates a new instance of ShowServiceImpl.
     *
     * @param showDAO    DAO for shows
     * @param seasonDAO  DAO for seasons
     * @param episodeDAO DAO for episodes
     * @param showCache  cache for shows
     * @throws IllegalArgumentException if DAO for shows is null
     *                                  or DAO for seasons is null
     *                                  or DAO for episodes is null
     *                                  or cache for shows is null
     */
    @Autowired
    public ShowServiceImpl(final ShowDAO showDAO,
            final SeasonDAO seasonDAO,
            final EpisodeDAO episodeDAO,
            @Value("#{cacheManager.getCache('showCache')}") final Cache showCache) {
        super(showCache);

        Validators.validateArgumentNotNull(showDAO, SHOW_DAO_ARGUMENT);
        Validators.validateArgumentNotNull(seasonDAO, SEASON_DAO_ARGUMENT);
        Validators.validateArgumentNotNull(episodeDAO, EPISODE_DAO_ARGUMENT);

        this.showDAO = showDAO;
        this.seasonDAO = seasonDAO;
        this.episodeDAO = episodeDAO;
    }

    /**
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            for (final Show show : getCachedShows(false)) {
                removeShow(show);
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Show> getShows() {
        try {
            return getCachedShows(true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Show getShow(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedShow(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            showDAO.add(show);
            addShowToCache(show);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            showDAO.update(show);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            removeShow(show);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            final Show newShow = createShow(show);
            showDAO.add(newShow);
            newShow.setPosition(show.getPosition());
            showDAO.update(newShow);

            for (final Season season : getCachedSeasons(show, false)) {
                final Season newSeason = createSeason(season);
//                newSeason.setShow(newShow);
                seasonDAO.add(newSeason);
                newSeason.setPosition(season.getPosition());
                seasonDAO.update(newSeason);

                for (final Episode episode : getCachedEpisodes(season, false)) {
                    final Episode newEpisode = createEpisode(episode);
//                    newEpisode.setSeason(newSeason);
                    episodeDAO.add(newEpisode);
                    newEpisode.setPosition(episode.getPosition());
                    episodeDAO.update(newEpisode);
                }
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            final List<Show> shows = getCachedShows(false);
            final Show otherShow = shows.get(shows.indexOf(show) - 1);
            switchPosition(show, otherShow);
            showDAO.update(show);
            showDAO.update(otherShow);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            final List<Show> shows = getCachedShows(false);
            final Show otherShow = shows.get(shows.indexOf(show) + 1);
            switchPosition(show, otherShow);
            showDAO.update(show);
            showDAO.update(otherShow);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            return getCachedShow(show.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            final List<Show> shows = getCachedShows(false);
            for (int i = 0; i < shows.size(); i++) {
                final Show show = shows.get(i);
                show.setPosition(i);
                showDAO.update(show);
                final List<Season> seasons = getCachedSeasons(show, false);
                for (int j = 0; j < seasons.size(); j++) {
                    final Season season = seasons.get(j);
                    season.setPosition(j);
                    seasonDAO.update(season);
                    final List<Episode> episodes = getCachedEpisodes(season, false);
                    for (int k = 0; k < episodes.size(); k++) {
                        final Episode episode = episodes.get(k);
                        episode.setPosition(k);
                        episodeDAO.update(episode);
                    }
                }
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Time getTotalLength() {
        try {
            int sum = 0;
            for (final Show show : getCachedShows(true)) {
                for (final Season season : getCachedSeasons(show, true)) {
                    for (final Episode episode : getCachedEpisodes(season, true)) {
                        sum += episode.getLength();
                    }
                }
            }
            return new Time(sum);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getSeasonsCount() {
        try {
            int sum = 0;
            for (final Show show : getCachedShows(true)) {
                sum += getCachedSeasons(show, true).size();
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getEpisodesCount() {
        try {
            int sum = 0;
            for (final Show show : getCachedShows(true)) {
                for (final Season season : getCachedSeasons(show, true)) {
                    sum += getCachedEpisodes(season, true).size();
                }
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<Show> getDAOShows() {
        return showDAO.getShows();
    }

    @Override
    protected List<Season> getDAOSeasons(final Show show) {
        return seasonDAO.findSeasonsByShow(show);
    }

    @Override
    protected List<Episode> getDAOEpisodes(final Season season) {
        return episodeDAO.findEpisodesBySeason(season);
    }

    @Override
    protected Show getDAOShow(final Integer id) {
        return showDAO.getShow(id);
    }

    @Override
    protected Season getDAOSeason(final Integer id) {
        return seasonDAO.getSeason(id);
    }

    @Override
    protected Episode getDAOEpisode(final Integer id) {
        return episodeDAO.getEpisode(id);
    }

    /**
     * Removes show.
     *
     * @param show show
     */
    private void removeShow(final Show show) {
        for (final Season season : getCachedSeasons(show, false)) {
            for (final Episode episode : getCachedEpisodes(season, false)) {
                episodeDAO.remove(episode);
            }
            seasonDAO.remove(season);
        }
        showDAO.remove(show);
    }

    /**
     * Creates new copy of show from another show.
     *
     * @param show show
     * @return new copy of show
     */
    private static Show createShow(final Show show) {
        final Show newShow = new Show();
        newShow.setCzechName(show.getCzechName());
        newShow.setOriginalName(show.getOriginalName());
        newShow.setCsfd(show.getCsfd());
        newShow.setImdbCode(show.getImdbCode());
        newShow.setWikiEn(show.getWikiEn());
        newShow.setWikiCz(show.getWikiCz());
        newShow.setPicture(show.getPicture());
        newShow.setNote(show.getNote());
        newShow.setGenres(new ArrayList<>(show.getGenres()));
        return newShow;
    }

    /**
     * Creates new copy of season from another season.
     *
     * @param season copying season
     * @return new copy of season
     */
    private static Season createSeason(final Season season) {
        final Season newSeason = new Season();
        newSeason.setNumber(season.getNumber());
        newSeason.setStartYear(season.getStartYear());
        newSeason.setEndYear(season.getEndYear());
        newSeason.setLanguage(season.getLanguage());
        newSeason.setSubtitles(new ArrayList<>(season.getSubtitles()));
        newSeason.setNote(season.getNote());
        return newSeason;
    }

    /**
     * Creates new copy of episode from another episode.
     *
     * @param episode copying episode
     * @return new copy of episode
     */
    private static Episode createEpisode(final Episode episode) {
        final Episode newEpisode = new Episode();
        newEpisode.setNumber(episode.getNumber());
        newEpisode.setName(episode.getName());
        newEpisode.setLength(episode.getLength());
        newEpisode.setNote(episode.getNote());
        return newEpisode;
    }

    /**
     * Switch position of shows.
     *
     * @param show1 1st show
     * @param show2 2nd show
     */
    private static void switchPosition(final Show show1, final Show show2) {
        final int position = show1.getPosition();
        show1.setPosition(show2.getPosition());
        show2.setPosition(position);
    }

}
