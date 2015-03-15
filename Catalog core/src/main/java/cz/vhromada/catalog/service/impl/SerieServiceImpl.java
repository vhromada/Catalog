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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of service for series.
 *
 * @author Vladimir Hromada
 */
@Component("serieService")
public class SerieServiceImpl extends AbstractSerieService implements SerieService {

    /**
     * DAO for series field
     */
    private static final String SERIE_DAO_ARGUMENT = "DAO for series";

    /**
     * DAO for seasons field
     */
    private static final String SEASON_DAO_ARGUMENT = "DAO for seasons";

    /**
     * DAO for episodes field
     */
    private static final String EPISODE_DAO_ARGUMENT = "DAO for episodes";

    /**
     * Serie argument
     */
    private static final String SERIE_ARGUMENT = "Serie";

    /**
     * ID argument
     */
    private static final String ID_ARGUMENT = "ID";

    /**
     * Message for {@link ServiceOperationException}
     */
    private static final String SERVICE_OPERATION_EXCEPTION_MESSAGE = "Error in working with DAO tier.";

    /**
     * DAO for series
     */
    private SerieDAO serieDAO;

    /**
     * DAO for seasons
     */
    private SeasonDAO seasonDAO;

    /**
     * DAO for episodes
     */
    private EpisodeDAO episodeDAO;

    /**
     * Creates a new instance of SerieServiceImpl.
     *
     * @param serieDAO   DAO for series
     * @param seasonDAO  DAO for seasons
     * @param episodeDAO DAO for episodes
     * @param serieCache cache for series
     * @throws IllegalArgumentException if DAO for series is null
     *                                  or DAO for seasons is null
     *                                  or DAO for episodes is null
     *                                  or cache for series is null
     */
    @Autowired
    public SerieServiceImpl(final SerieDAO serieDAO,
            final SeasonDAO seasonDAO,
            final EpisodeDAO episodeDAO,
            @Value("#{cacheManager.getCache('serieCache')}") final Cache serieCache) {
        super(serieCache);

        Validators.validateArgumentNotNull(serieDAO, SERIE_DAO_ARGUMENT);
        Validators.validateArgumentNotNull(seasonDAO, SEASON_DAO_ARGUMENT);
        Validators.validateArgumentNotNull(episodeDAO, EPISODE_DAO_ARGUMENT);

        this.serieDAO = serieDAO;
        this.seasonDAO = seasonDAO;
        this.episodeDAO = episodeDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
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
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public List<Serie> getSeries() {
        try {
            return getCachedSeries(true);
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
    public Serie getSerie(final Integer id) {
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
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void add(final Serie serie) {
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
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void update(final Serie serie) {
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
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void remove(final Serie serie) {
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
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final Serie serie) {
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
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final Serie serie) {
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
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final Serie serie) {
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
     * @throws IllegalArgumentException  {@inheritDoc}
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public boolean exists(final Serie serie) {
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
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
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
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public Time getTotalLength() {
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
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getSeasonsCount() {
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
     * @throws ServiceOperationException {@inheritDoc}
     */
    @Override
    public int getEpisodesCount() {
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
