package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.EpisodeService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for episodes.
 *
 * @author Vladimir Hromada
 */
@Component("episodeService")
public class EpisodeServiceImpl extends AbstractSerieService implements EpisodeService {

    /** DAO for episodes field */
    private static final String EPISODE_DAO_ARGUMENT = "DAO for episodes";

    /** Season argument */
    private static final String SEASON_ARGUMENT = "Season";

    /** Episode argument */
    private static final String EPISODE_ARGUMENT = "Episode";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link ServiceOperationException} */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /** DAO for episodes */
    @Autowired
    private EpisodeDAO episodeDAO;

    /**
     * Creates a new instance of EpisodeServiceImpl.
     *
     * @param episodeDAO DAO for episodes
     * @param serieCache cache for series
     * @throws IllegalArgumentException if DAO for episodes is null
     *                                  or cache for series is null
     */
    @Autowired
    public EpisodeServiceImpl(final EpisodeDAO episodeDAO,
            @Value("#{cacheManager.getCache('serieCache')}") final Cache serieCache) {
        super(serieCache);

        Validators.validateArgumentNotNull(episodeDAO, EPISODE_DAO_ARGUMENT);

        this.episodeDAO = episodeDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Episode getEpisode(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedEpisode(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            episodeDAO.add(episode);
            addEpisodeToCache(episode);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            episodeDAO.update(episode);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            episodeDAO.remove(episode);
            removeEpisodeFromCache(episode);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            final Episode newEpisode = new Episode();
            newEpisode.setNumber(episode.getNumber());
            newEpisode.setName(episode.getName());
            newEpisode.setLength(episode.getLength());
            newEpisode.setNote(episode.getNote());
            newEpisode.setSeason(episode.getSeason());
            episodeDAO.add(newEpisode);
            newEpisode.setPosition(episode.getPosition());
            episodeDAO.update(newEpisode);
            addEpisodeToCache(newEpisode);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            final List<Episode> episodes = getCachedEpisodes(episode.getSeason(), false);
            final Episode otherEpisode = episodes.get(episodes.indexOf(episode) - 1);
            switchPosition(episode, otherEpisode);
            episodeDAO.update(episode);
            episodeDAO.update(otherEpisode);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            final List<Episode> episodes = getCachedEpisodes(episode.getSeason(), false);
            final Episode otherEpisode = episodes.get(episodes.indexOf(episode) + 1);
            switchPosition(episode, otherEpisode);
            episodeDAO.update(episode);
            episodeDAO.update(otherEpisode);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Episode episode) {
        Validators.validateArgumentNotNull(episode, EPISODE_ARGUMENT);

        try {
            return getCachedEpisode(episode.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Episode> findEpisodesBySeason(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            return getCachedEpisodes(season, true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Time getTotalLengthBySeason(final Season season) {
        Validators.validateArgumentNotNull(season, SEASON_ARGUMENT);

        try {
            int sum = 0;
            for (final Episode episode : getCachedEpisodes(season, true)) {
                sum += episode.getLength();
            }
            return new Time(sum);
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
        return null;
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
        return null;
    }

    @Override
    protected Episode getDAOEpisode(final Integer id) {
        return episodeDAO.getEpisode(id);
    }

    /**
     * Switch position of episodes.
     *
     * @param episode1 1st episode
     * @param episode2 2nd episode
     */
    private static void switchPosition(final Episode episode1, final Episode episode2) {
        final int position = episode1.getPosition();
        episode1.setPosition(episode2.getPosition());
        episode2.setPosition(position);
    }

}

