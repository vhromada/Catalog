package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Show;
import cz.vhromada.catalog.service.domain.CacheValue;
import cz.vhromada.validators.Validators;

import org.springframework.cache.Cache;

/**
 * An abstract class represents service with show cache.
 *
 * @author Vladimir Hromada
 */
public abstract class AbstractShowService extends AbstractInnerService<Show, Season> {

    /**
     * Cache for shows argument
     */
    private static final String SHOW_CACHE_ARGUMENT = "Cache for shows";

    /**
     * Cache key for list of shows
     */
    private static final String SHOWS_CACHE_KEY = "shows";

    /**
     * Cache key for book show
     */
    private static final String SHOW_CACHE_KEY = "show";

    /**
     * Cache key for list of seasons
     */
    private static final String SEASONS_CACHE_KEY = "seasons";

    /**
     * Cache key for season
     */
    private static final String SEASON_CACHE_KEY = "season";

    /**
     * Cache key for list of episodes
     */
    private static final String EPISODES_CACHE_KEY = "episodes";

    /**
     * Cache key for episode
     */
    private static final String EPISODE_CACHE_KEY = "episode";

    /**
     * Cache for shows
     */
    private Cache showCache;

    /**
     * Creates a new instance of AbstractShowService.
     *
     * @param showCache cache for shows
     * @throws IllegalArgumentException if cache for shows is null
     */
    public AbstractShowService(final Cache showCache) {
        Validators.validateArgumentNotNull(showCache, SHOW_CACHE_ARGUMENT);

        this.showCache = showCache;
    }

    /**
     * Remove all mappings from the cache for shows.
     */
    protected void clearCache() {
        showCache.clear();
    }

    /**
     * Returns list of shows.
     *
     * @param cached true if returned data from DAO should be cached
     * @return list of shows
     */
    protected List<Show> getCachedShows(final boolean cached) {
        return getCachedObjects(showCache, SHOWS_CACHE_KEY, cached);
    }

    /**
     * Returns list of seasons for specified show.
     *
     * @param show   show
     * @param cached true if returned data from DAO should be cached
     * @return list of seasons for specified show
     */
    protected List<Season> getCachedSeasons(final Show show, final boolean cached) {
        return getCachedInnerObjects(showCache, SEASONS_CACHE_KEY + show.getId(), cached, show);
    }

    /**
     * Returns list of episodes for specified season.
     *
     * @param season season
     * @param cached true if returned data from DAO should be cached
     * @return list of episodes for specified season
     */
    protected List<Episode> getCachedEpisodes(final Season season, final boolean cached) {
        final String key = EPISODES_CACHE_KEY + season.getId();
        final CacheValue<List<Episode>> cachedData = getObjectFromCache(showCache, key);
        if (cachedData == null) {
            final List<Episode> data = getDAOEpisodes(season);
            if (cached) {
                showCache.put(key, data);
            }
            return data;
        }
        return cachedData.getValue();
    }

    /**
     * Returns show with ID or null if there isn't such show.
     *
     * @param id ID
     * @return show with ID or null if there isn't such show
     */
    protected Show getCachedShow(final Integer id) {
        return getCachedObject(showCache, SHOW_CACHE_KEY, id, true);
    }

    /**
     * Returns season with ID or null if there isn't such season.
     *
     * @param id ID
     * @return season with ID or null if there isn't such season
     */
    protected Season getCachedSeason(final Integer id) {
        return getCachedInnerObject(showCache, SEASON_CACHE_KEY, id);
    }

    /**
     * Returns episode with ID or null if there isn't such episode.
     *
     * @param id ID
     * @return episode with ID or null if there isn't such episode
     */
    protected Episode getCachedEpisode(final Integer id) {
        final String key = EPISODE_CACHE_KEY + id;
        final CacheValue<Episode> cachedData = getObjectFromCache(showCache, key);
        if (cachedData == null) {
            final Episode data = getDAOEpisode(id);
            showCache.put(key, data);
            return data;
        }
        return cachedData.getValue();
    }

    /**
     * Adds show to cache.
     *
     * @param show show
     */
    protected void addShowToCache(final Show show) {
        addObjectToListCache(showCache, SHOWS_CACHE_KEY, show);
        addObjectToCache(showCache, SHOW_CACHE_KEY + show.getId(), show);
    }

    /**
     * Adds season to cache.
     *
     * @param season season
     */
    protected void addSeasonToCache(final Season season) {
//        addInnerObjectToListCache(showCache, SEASONS_CACHE_KEY + season.getShow().getId(), season);
        addInnerObjectToCache(showCache, SEASON_CACHE_KEY + season.getId(), season);
    }

    /**
     * Adds episode to cache.
     *
     * @param episode episode
     */
    protected void addEpisodeToCache(final Episode episode) {
        final String keyList = null;//EPISODES_CACHE_KEY + episode.getSeason().getId();
        final CacheValue<List<Episode>> cacheDataList = getObjectFromCache(showCache, keyList);
        if (cacheDataList != null) {
            final List<Episode> data = cacheDataList.getValue();
            data.add(episode);
            showCache.put(keyList, data);
        }
        final String keyItem = EPISODE_CACHE_KEY + episode.getId();
        final CacheValue<Episode> cacheData = getObjectFromCache(showCache, keyItem);
        if (cacheData != null) {
            showCache.put(keyItem, episode);
        }
    }

    /**
     * Removes episode from cache.
     *
     * @param episode episode
     */
    protected void removeEpisodeFromCache(final Episode episode) {
        final String key = null;//EPISODES_CACHE_KEY + episode.getSeason().getId();
        final CacheValue<List<Episode>> cacheData = getObjectFromCache(showCache, key);
        if (cacheData != null) {
            final List<Episode> data = cacheData.getValue();
            data.remove(episode);
            showCache.put(key, data);
        }
        showCache.evict(EPISODE_CACHE_KEY + episode.getId());
    }

    @Override
    protected List<Show> getData() {
        return getDAOShows();
    }

    @Override
    protected Show getData(final Integer id) {
        return getDAOShow(id);
    }

    @Override
    protected List<Season> getInnerData(final Show parent) {
        return getDAOSeasons(parent);
    }

    @Override
    protected Season getInnerData(final Integer id) {
        return getDAOSeason(id);
    }

    /**
     * Returns list of shows from DAO tier.
     *
     * @return list of shows from DAO tier
     */
    protected abstract List<Show> getDAOShows();

    /**
     * Returns list of seasons for specified show from DAO tier.
     *
     * @param show show
     * @return list of seasons for specified show from DAO tier
     */
    protected abstract List<Season> getDAOSeasons(final Show show);

    /**
     * Returns list of episodes for specified season from DAO tier.
     *
     * @param season season
     * @return list of episodes for specified season from DAO tier
     */
    protected abstract List<Episode> getDAOEpisodes(final Season season);

    /**
     * Returns show with ID from DAO tier.
     *
     * @param id ID
     * @return show with ID from DAO tier
     */
    protected abstract Show getDAOShow(final Integer id);

    /**
     * Returns season with ID from DAO tier.
     *
     * @param id ID
     * @return season with ID from DAO tier
     */
    protected abstract Season getDAOSeason(final Integer id);

    /**
     * Returns episode with ID from DAO tier.
     *
     * @param id ID
     * @return episode with ID from DAO tier
     */
    protected abstract Episode getDAOEpisode(final Integer id);

}
