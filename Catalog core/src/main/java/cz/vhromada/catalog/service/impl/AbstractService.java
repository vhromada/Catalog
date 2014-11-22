package cz.vhromada.catalog.service.impl;

import java.util.List;

import org.springframework.cache.Cache;

/**
 * An abstract class represents service.
 *
 * @param <T> type of objects
 * @author Vladimir Hromada
 */
public abstract class AbstractService<T> {

    /**
     * Returns list of cached objects.
     *
     * @param cache  cache
     * @param key    cache key
     * @param cached true if returned data from DAO should be cached
     * @return list of cached objects
     */
    protected List<T> getCachedObjects(final Cache cache, final String key, final boolean cached) {
        final CacheValue<List<T>> cachedData = getObjectFromCache(cache, key);
        if (cachedData == null) {
            final List<T> data = getData();
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
     * @param cache  cache
     * @param key    cache key
     * @param id     ID
     * @param cached true if returned data from DAO should be cached
     * @return cached object with ID or null if there isn't such object
     */
    protected T getCachedObject(final Cache cache, final String key, final Integer id, final boolean cached) {
        final CacheValue<T> cachedData = getObjectFromCache(cache, key + id);
        if (cachedData == null) {
            final T data = getData(id);
            if (cached) {
                cache.put(key + id, data);
            }
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
    protected void addObjectToListCache(final Cache cache, final String key, final T object) {
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
    protected void addObjectToCache(final Cache cache, final String key, final T object) {
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
    protected void removeObjectFromCache(final Cache cache, final String key, final T object) {
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
     * @return data from DAO tier
     */
    protected abstract List<T> getData();

    /**
     * Returns data with ID from DAO tier.
     *
     * @param id ID
     * @return data with ID from DAO tier
     */
    protected abstract T getData(final Integer id);

    /**
     * Returns value for cache.
     *
     * @param cache cache
     * @param key   key
     * @param <U>   type of returned value
     * @return value for cache
     */
    protected <U> CacheValue<U> getObjectFromCache(final Cache cache, final Object key) {
        final Cache.ValueWrapper cacheValue = cache.get(key);
        if (cacheValue == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final U value = (U) cacheValue.get();
        return new CacheValue<>(value);
    }

    /**
     * A class represents value in cache.
     *
     * @param <T> type of cached value
     */
    //TODO vhromada 22.11.2014: checkstyle
    protected static class CacheValue<T> {

        /** Value */
        private T value;

        /**
         * Creates a new instance of CacheValue.
         *
         * @param value value
         */
        public CacheValue(final T value) {
            this.value = value;
        }

        /**
         * Returns value.
         *
         * @return value
         */
        public T getValue() {
            return value;
        }

    }

}
