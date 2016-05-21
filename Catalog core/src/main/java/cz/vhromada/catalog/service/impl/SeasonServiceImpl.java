package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonService")
public class SeasonServiceImpl extends AbstractShowService implements SeasonService {

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
     * Season argument
     */
    private static final String SEASON_ARGUMENT = "Season";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link ServiceOperationException}
     */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /**
     * DAO for seasons
     */
    private SeasonDAO seasonDAO;

    /**
     * DAO for episodes
     */
    private EpisodeDAO episodeDAO;

    /**
     * Creates a new instance of SeasonServiceImpl.
     *
     * @param seasonDAO  DAO for seasons
     * @param episodeDAO DAO for episodes
     * @param showCache  cache for shows
     * @throws IllegalArgumentException if DAO for seasons is null
     *                                  or DAO for episodes is null
     *                                  or cache for shows is null
     */
    @Autowired
    public SeasonServiceImpl(final SeasonDAO seasonDAO,
            final EpisodeDAO episodeDAO,
            @Value("#{cacheManager.getCache('showCache')}") final Cache showCache) {
        super(showCache);

        Validators.validateArgumentNotNull(seasonDAO, SEASON_DAO_ARGUMENT);
        Validators.validateArgumentNotNull(episodeDAO, EPISODE_DAO_ARGUMENT);

        this.seasonDAO = seasonDAO;
        this.episodeDAO = episodeDAO;
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Season getSeason(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedSeason(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            seasonDAO.add(season);
            addSeasonToCache(season);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            seasonDAO.update(season);
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
    public void remove(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            for (final Episode episode : getCachedEpisodes(season, false)) {
                episodeDAO.remove(episode);
            }
            seasonDAO.remove(season);
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
    public void duplicate(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            final Season newSeason = createSeason(season);
            newSeason.setShow(season.getShow());
            seasonDAO.add(newSeason);
            newSeason.setPosition(season.getPosition());
            seasonDAO.update(newSeason);

            for (final Episode episode : getCachedEpisodes(season, false)) {
                final Episode newEpisode = createEpisode(episode);
//                newEpisode.setSeason(newSeason);
                episodeDAO.add(newEpisode);
                newEpisode.setPosition(episode.getPosition());
                episodeDAO.update(newEpisode);
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
    public void moveUp(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            final List<Season> seasons = null;//getCachedSeasons(season.getShow(), false);
            final Season otherSeason = seasons.get(seasons.indexOf(season) - 1);
            switchPosition(season, otherSeason);
            seasonDAO.update(season);
            seasonDAO.update(otherSeason);
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
    public void moveDown(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            final List<Season> seasons = null;//getCachedSeasons(season.getShow(), false);
            final Season otherSeason = seasons.get(seasons.indexOf(season) + 1);
            switchPosition(season, otherSeason);
            seasonDAO.update(season);
            seasonDAO.update(otherSeason);
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
    public boolean exists(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            return getCachedSeason(season.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Season> findSeasonsByShow(final Show show) {
        Validators.validateArgumentNotNull(show, SHOW_ARGUMENT);

        try {
            return getCachedSeasons(show, true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<Show> getDAOShows() {
        return null;
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
        return null;
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
     * Switch position of seasons.
     *
     * @param season1 1st season
     * @param season2 2nd season
     */
    private static void switchPosition(final Season season1, final Season season2) {
        final int position = season1.getPosition();
        season1.setPosition(season2.getPosition());
        season2.setPosition(position);
    }

}
