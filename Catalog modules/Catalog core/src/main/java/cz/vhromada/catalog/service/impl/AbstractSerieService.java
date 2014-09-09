package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.dao.entities.Episode;
import cz.vhromada.catalog.dao.entities.Season;
import cz.vhromada.catalog.dao.entities.Serie;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;

/**
 * An abstract class represents service with serie cache.
 *
 * @author Vladimir Hromada
 */
public abstract class AbstractSerieService extends AbstractInnerService<Serie, Season> {

	/** Cache for series */
	@Value("#{cacheManager.getCache('serieCache')}")
	private Cache serieCache;

	/**
	 * Returns cache for series.
	 *
	 * @return cache for series
	 */
	public Cache getSerieCache() {
		return serieCache;
	}

	/**
	 * Sets a new value to cache for series.
	 *
	 * @param serieCache new value
	 */
	public void setSerieCache(final Cache serieCache) {
		this.serieCache = serieCache;
	}

	/**
	 * Validates if cache for series is set.
	 *
	 * @throws IllegalStateException if cache for series is null
	 */
	protected void validateSerieCacheNotNull() {
		Validators.validateFieldNotNull(serieCache, "Cache for series");
	}

	/** Remove all mappings from the cache for series. */
	protected void clearCache() {
		serieCache.clear();
	}

	/**
	 * Returns list of series.
	 *
	 * @param cached true if returned data from DAO should be cached
	 * @return list of series
	 */
	protected List<Serie> getCachedSeries(final boolean cached) {
		return getCachedObjects(serieCache, "series", cached);
	}

	/**
	 * Returns list of seasons for specified serie.
	 *
	 * @param serie  serie
	 * @param cached true if returned data from DAO should be cached
	 * @return list of seasons for specified serie
	 */
	protected List<Season> getCachedSeasons(final Serie serie, final boolean cached) {
		return getCachedInnerObjects(serieCache, "seasons" + serie.getId(), cached, serie);
	}

	/**
	 * Returns list of episodes for specified season.
	 *
	 * @param season season
	 * @param cached true if returned data from DAO should be cached
	 * @return list of episodes for specified season
	 */
	protected List<Episode> getCachedEpisodes(final Season season, final boolean cached) {
		final String key = "episodes" + season.getId();
		final CacheValue<List<Episode>> cachedData = getObjectFromCache(serieCache, key);
		if (cachedData == null) {
			final List<Episode> data = getDAOEpisodes(season);
			if (cached) {
				serieCache.put(key, data);
			}
			return data;
		}
		return cachedData.getValue();
	}

	/**
	 * Returns serie with ID or null if there isn't such serie.
	 *
	 * @param id ID
	 * @return serie with ID or null if there isn't such serie
	 */
	protected Serie getCachedSerie(final Integer id) {
		return getCachedObject(serieCache, "serie", id, true);
	}

	/**
	 * Returns season with ID or null if there isn't such season.
	 *
	 * @param id ID
	 * @return season with ID or null if there isn't such season
	 */
	protected Season getCachedSeason(final Integer id) {
		return getCachedInnerObject(serieCache, "season", id);
	}

	/**
	 * Returns episode with ID or null if there isn't such episode.
	 *
	 * @param id ID
	 * @return episode with ID or null if there isn't such episode
	 */
	protected Episode getCachedEpisode(final Integer id) {
		final String key = "episode" + id;
		final CacheValue<Episode> cachedData = getObjectFromCache(serieCache, key);
		if (cachedData == null) {
			final Episode data = getDAOEpisode(id);
			serieCache.put(key, data);
			return data;
		}
		return cachedData.getValue();
	}

	/**
	 * Adds serie to cache.
	 *
	 * @param serie serie
	 */
	protected void addSerieToCache(final Serie serie) {
		addObjectToListCache(serieCache, "series", serie);
		addObjectToCache(serieCache, "serie" + serie.getId(), serie);
	}

	/**
	 * Adds season to cache.
	 *
	 * @param season season
	 */
	protected void addSeasonToCache(final Season season) {
		addInnerObjectToListCache(serieCache, "seasons" + season.getSerie().getId(), season);
		addInnerObjectToCache(serieCache, "season" + season.getId(), season);
	}

	/**
	 * Adds episode to cache.
	 *
	 * @param episode episode
	 */
	protected void addEpisodeToCache(final Episode episode) {
		final String keyList = "episodes" + episode.getSeason().getId();
		final CacheValue<List<Episode>> cacheDataList = getObjectFromCache(serieCache, keyList);
		if (cacheDataList != null) {
			final List<Episode> data = cacheDataList.getValue();
			data.add(episode);
			serieCache.put(keyList, data);
		}
		final String keyItem = "episode" + episode.getId();
		final CacheValue<Episode> cacheData = getObjectFromCache(serieCache, keyItem);
		if (cacheData != null) {
			serieCache.put(keyItem, episode);
		}
	}

	/**
	 * Removes episode from cache.
	 *
	 * @param episode episode
	 */
	protected void removeEpisodeFromCache(final Episode episode) {
		final String key = "episodes" + episode.getSeason().getId();
		final CacheValue<List<Episode>> cacheData = getObjectFromCache(serieCache, key);
		if (cacheData != null) {
			final List<Episode> data = cacheData.getValue();
			data.remove(episode);
			serieCache.put(key, data);
		}
		serieCache.evict("episode" + episode.getId());
	}

	@Override
	protected List<Serie> getData() {
		return getDAOSeries();
	}

	@Override
	protected Serie getData(final Integer id) {
		return getDAOSerie(id);
	}

	@Override
	protected List<Season> getInnerData(final Serie parent) {
		return getDAOSeasons(parent);
	}

	@Override
	protected Season getInnerData(final Integer id) {
		return getDAOSeason(id);
	}

	/**
	 * Returns list of series from DAO tier.
	 *
	 * @return list of series from DAO tier
	 */
	protected abstract List<Serie> getDAOSeries();

	/**
	 * Returns list of seasons for specified serie from DAO tier.
	 *
	 * @param serie serie
	 * @return list of seasons for specified serie from DAO tier
	 */
	protected abstract List<Season> getDAOSeasons(final Serie serie);

	/**
	 * Returns list of episodes for specified season from DAO tier.
	 *
	 * @param season season
	 * @return list of episodes for specified season from DAO tier
	 */
	protected abstract List<Episode> getDAOEpisodes(final Season season);

	/**
	 * Returns serie with ID from DAO tier.
	 *
	 * @param id ID
	 * @return serie with ID from DAO tier
	 */
	protected abstract Serie getDAOSerie(final Integer id);

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
