package cz.vhromada.catalog.service.impl;

import java.util.List;

import cz.vhromada.catalog.service.domain.CacheValue;
import org.springframework.cache.Cache;

/**
 * An abstract class represents service.
 *
 * @param <S> type of objects
 * @param <T> type of inner objects
 * @author Vladimir Hromada
 */
public abstract class AbstractInnerService<S, T> extends AbstractService<S> {

    /**
     * Returns list of cached objects.
     *
     * @param cache  cache
     * @param key    cache key
     * @param cached true if returned data from DAO should be cached
     * @param parent parent
     * @return list of cached objects
     */
    protected List<T> getCachedInnerObjects(final Cache cache, final String key, final boolean cached, final S parent) {
        final CacheValue<List<T>> cachedData = getObjectFromCache(cache, key);
        if (cachedData == null) {
            final List<T> data = getInnerData(parent);
            if (cached) {
                cache.put(key, data);
            }
            return data;
        }
        return cachedData.getValue();
    }

    /**
     * Returns cached object with ID or null if there isn't such object.
     *
     * @param cache cache
     * @param key   cache key
     * @param id    ID
     * @return cached object with ID or null if there isn't such object
     */
    protected T getCachedInnerObject(final Cache cache, final String key, final Integer id) {
        final CacheValue<T> cachedData = getObjectFromCache(cache, key + id);
        if (cachedData == null) {
            final T data = getInnerData(id);
            cache.put(key + id, data);
            return data;
        }
        return cachedData.getValue();
    }

    /**
     * Adds object to list in cache.
     *
     * @param cache  cache
     * @param key    cache key
     * @param object object
     */
    protected void addInnerObjectToListCache(final Cache cache, final String key, final T object) {
        final CacheValue<List<T>> cacheData = getObjectFromCache(cache, key);
        if (cacheData != null) {
            final List<T> data = cacheData.getValue();
            data.add(object);
            cache.put(key, data);
        }
    }

    /**
     * Adds object to cache.
     *
     * @param cache  cache
     * @param key    cache key
     * @param object object
     */
    protected void addInnerObjectToCache(final Cache cache, final String key, final T object) {
        final CacheValue<T> cacheData = getObjectFromCache(cache, key);
        if (cacheData != null) {
            cache.put(key, object);
        }
    }

    /**
     * Remove object from cache.
     *
     * @param cache  cache
     * @param key    cache key
     * @param object object
     */
    protected void removeInnerObjectFromCache(final Cache cache, final String key, final T object) {
        final CacheValue<List<T>> cacheData = getObjectFromCache(cache, key);
        if (cacheData != null) {
            final List<T> data = cacheData.getValue();
            data.remove(object);
            cache.put(key, data);
        }
    }

    /**
     * Returns data from DAO tier.
     *
     * @param parent parent
     * @return data from DAO tier
     */
    protected abstract List<T> getInnerData(final S parent);

    /**
     * Returns data with ID from DAO tier.
     *
     * @param id ID
     * @return data with ID from DAO tier
     */
    protected abstract T getInnerData(final Integer id);

}
