package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.Time;
import cz.vhromada.catalog.dao.EpisodeDAO;
import cz.vhromada.catalog.dao.SeasonDAO;
import cz.vhromada.catalog.dao.SerieDAO;
import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.SerieService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for series.
 *
 * @author Vladimir Hromada
 */
@Component("serieService")
public class SerieServiceImpl extends AbstractSerieService implements SerieService {

    /** DAO for series field */
    private static final String SERIE_DAO_FIELD = "DAO for series";

    /** DAO for seasons field */
    private static final String SEASON_DAO_FIELD = "DAO for seasons";

    /** DAO for episodes field */
    private static final String EPISODE_DAO_FIELD = "DAO for episodes";

    /** Serie argument */
    private static final String SERIE_ARGUMENT = "Serie";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link ServiceOperationException} */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /** DAO for series */
    @Autowired
    private SerieDAO serieDAO;

    /** DAO for seasons */
    @Autowired
    private SeasonDAO seasonDAO;

    /** DAO for episodes */
    @Autowired
    private EpisodeDAO episodeDAO;

    /**
     * Returns DAO for series.
     *
     * @return DAO for series
     */
    public SerieDAO getSerieDAO() {
        return serieDAO;
    }

    /**
     * Sets a new value to DAO for series.
     *
     * @param serieDAO new value
     */
    public void setSerieDAO(final SerieDAO serieDAO) {
        this.serieDAO = serieDAO;
    }

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
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or DAO for seasons isn't set
     *                                   or DAO for episodes isn't set
     *                                   or cache for series isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        Validators.validateFieldNotNull(episodeDAO, EPISODE_DAO_FIELD);
        validateSerieCacheNotNull();

        try {
            for (final Serie serie : getCachedSeries(false)) {
                removeSerie(serie);
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or cache for series isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Serie> getSeries() {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        validateSerieCacheNotNull();

        try {
            return getCachedSeries(true);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Serie getSerie(final Integer id) {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return getCachedSerie(id);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Serie serie) {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

        try {
            serieDAO.add(serie);
            addSerieToCache(serie);
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Serie serie) {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

        try {
            serieDAO.update(serie);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or DAO for seasons isn't set
     *                                   or DAO for episodes isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Serie serie) {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        Validators.validateFieldNotNull(episodeDAO, EPISODE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

        try {
            removeSerie(serie);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or DAO for seasons isn't set
     *                                   or DAO for episodes isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Serie serie) {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        Validators.validateFieldNotNull(episodeDAO, EPISODE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

        try {
            final Serie newSerie = createSerie(serie);
            serieDAO.add(newSerie);
            newSerie.setPosition(serie.getPosition());
            serieDAO.update(newSerie);

            for (final Season season : getCachedSeasons(serie, false)) {
                final Season newSeason = createSeason(season);
                newSeason.setSerie(newSerie);
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
            }
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Serie serie) {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

        try {
            final List<Serie> series = getCachedSeries(false);
            final Serie otherSerie = series.get(series.indexOf(serie) - 1);
            switchPosition(serie, otherSerie);
            serieDAO.update(serie);
            serieDAO.update(otherSerie);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Serie serie) {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

        try {
            final List<Serie> series = getCachedSeries(false);
            final Serie otherSerie = series.get(series.indexOf(serie) + 1);
            switchPosition(serie, otherSerie);
            serieDAO.update(serie);
            serieDAO.update(otherSerie);
            clearCache();
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or cache for series isn't set
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Serie serie) {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        validateSerieCacheNotNull();
        Validators.validateArgumentNotNull(serie, SERIE_ARGUMENT);

        try {
            return getCachedSerie(serie.getId()) != null;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or DAO for seasons isn't set
     *                                   or DAO for episodes isn't set
     *                                   or cache for series isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        Validators.validateFieldNotNull(episodeDAO, EPISODE_DAO_FIELD);
        validateSerieCacheNotNull();

        try {
            final List<Serie> series = getCachedSeries(false);
            for (int i = 0; i < series.size(); i++) {
                final Serie serie = series.get(i);
                serie.setPosition(i);
                serieDAO.update(serie);
                final List<Season> seasons = getCachedSeasons(serie, false);
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
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or DAO for seasons isn't set
     *                                   or DAO for episodes isn't set
     *                                   or cache for series isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Time getTotalLength() {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        Validators.validateFieldNotNull(episodeDAO, EPISODE_DAO_FIELD);
        validateSerieCacheNotNull();

        try {
            int sum = 0;
            for (final Serie serie : getCachedSeries(true)) {
                for (final Season season : getCachedSeasons(serie, true)) {
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
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or DAO for seasons isn't set
     *                                   or cache for series isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getSeasonsCount() {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        validateSerieCacheNotNull();

        try {
            int sum = 0;
            for (final Serie serie : getCachedSeries(true)) {
                sum += getCachedSeasons(serie, true).size();
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException     if DAO for series isn't set
     *                                   or DAO for seasons isn't set
     *                                   or DAO for episodes isn't set
     *                                   or cache for series isn't set
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getEpisodesCount() {
        Validators.validateFieldNotNull(serieDAO, SERIE_DAO_FIELD);
        Validators.validateFieldNotNull(seasonDAO, SEASON_DAO_FIELD);
        Validators.validateFieldNotNull(episodeDAO, EPISODE_DAO_FIELD);
        validateSerieCacheNotNull();

        try {
            int sum = 0;
            for (final Serie serie : getCachedSeries(true)) {
                for (final Season season : getCachedSeasons(serie, true)) {
                    sum += getCachedEpisodes(season, true).size();
                }
            }
            return sum;
        } catch (final DataStorageException ex) {
            throw new ServiceOperationException(SERVICE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    @Override
    protected List<Serie> getDAOSeries() {
        return serieDAO.getSeries();
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
        return serieDAO.getSerie(id);
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
     * Removes serie.
     *
     * @param serie serie
     */
    private void removeSerie(final Serie serie) {
        for (final Season season : getCachedSeasons(serie, false)) {
            for (final Episode episode : getCachedEpisodes(season, false)) {
                episodeDAO.remove(episode);
            }
            seasonDAO.remove(season);
        }
        serieDAO.remove(serie);
    }

    /**
     * Creates new copy of serie from another serie.
     *
     * @param serie serie
     * @return new copy of serie
     */
    private static Serie createSerie(final Serie serie) {
        final Serie newSerie = new Serie();
        newSerie.setCzechName(serie.getCzechName());
        newSerie.setOriginalName(serie.getOriginalName());
        newSerie.setCsfd(serie.getCsfd());
        newSerie.setImdbCode(serie.getImdbCode());
        newSerie.setWikiEn(serie.getWikiEn());
        newSerie.setWikiCz(serie.getWikiCz());
        newSerie.setPicture(serie.getPicture());
        newSerie.setNote(serie.getNote());
        newSerie.setGenres(new ArrayList<>(serie.getGenres()));
        return newSerie;
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
     * Switch position of series.
     *
     * @param serie1 1st serie
     * @param serie2 2nd serie
     */
    private static void switchPosition(final Serie serie1, final Serie serie2) {
        final int position = serie1.getPosition();
        serie1.setPosition(serie2.getPosition());
        serie2.setPosition(position);
    }

}
