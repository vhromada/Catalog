package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SeasonService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for seasons.
 *
 * @author Vladimir Hromada
 */
@Component("seasonService")
public class SeasonServiceImpl extends AbstractSerieService implements SeasonService {

    /** DAO for seasons field */
    private static final String SEASON_DAO_FIELD = "DAO for seasons";

    /** DAO for episodes field */
    private static final String EPISODE_DAO_FIELD = "DAO for episodes";

    /** Serie argument */
    private static final String SERIE_ARGUMENT = "Serie";

    /** Season argument */
    private static final String SEASON_ARGUMENT = "Season";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link ServiceOperationException} */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /** DAO for seasons */
    @Autowired
    private SeasonDAO seasonDAO;

    /** DAO for episodes */
    @Autowired
    private EpisodeDAO episodeDAO;

    /**
     * Returns DAO for seasons.
     *
     * @return DAO for seasons
     */
    public SeasonDAO getSeasonDAO() {
        return seasonDAO;
    }

    /**
     * Sets a new value to DAO for seasons.
     *
     * @param seasonDAO new value
     */
    public void setSeasonDAO(final SeasonDAO seasonDAO) {
        this.seasonDAO = seasonDAO;
    }

    /**
     * Returns DAO for episodes.
     *
     * @return DAO for episodes
     */
    public EpisodeDAO getEpisodeDAO() {
        return episodeDAO;
    }

    /**
     * Sets a new value to DAO for episodes.
     *
     * @param episodeDAO new value
     */
    public void setEpisodeDAO(final EpisodeDAO episodeDAO) {
        this.episodeDAO = episodeDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Season getSeason(final Integer id) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedSeason(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Season season) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            seasonDAO.add(season);
            addSeasonToCache(season);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Season season) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            seasonDAO.update(season);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or DAO for episodes isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Season season) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        Validators.validateFieldNotNull(episodeDAO, EPISODE_DAO_FIELD);
        validateSerieCacheNotNull();
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
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or DAO for episodes isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Season season) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        Validators.validateFieldNotNull(episodeDAO, EPISODE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            final Season newSeason = createSeason(season);
            newSeason.setSerie(season.getSerie());
            seasonDAO.add(newSeason);
            newSeason.setPosition(season.getPosition());
            seasonDAO.update(newSeason);

            for (final Episode episode : getCachedEpisodes(season, false)) {
                final Episode newEpisode = createEpisode(episode);
                newEpisode.setSeason(newSeason);
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
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Season season) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            final List<Season> seasons = getCachedSeasons(season.getSerie(), false);
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
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Season season) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            final List<Season> seasons = getCachedSeasons(season.getSerie(), false);
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
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Season season) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            return getCachedSeason(season.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for seasons isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Season> findSeasonsBySerie(final Serie serie) {
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

        try {
            return getCachedSeasons(serie, true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<Serie> getDAOSeries() {
        return null;
    }

    @Override
    protected List<Season> getDAOSeasons(final Serie serie) {
        return seasonDAO.findSeasonsBySerie(serie);
    }

    @Override
    protected List<Episode> getDAOEpisodes(final Season season) {
        return episodeDAO.findEpisodesBySeason(season);
    }

    @Override
    protected Serie getDAOSerie(final Integer id) {
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
