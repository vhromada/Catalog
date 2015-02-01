package cz.vhromada.catalog.service.domain;

/**
 * A class represents value in cache.
 *
 * @param <T> type of cached value
 */
public final class CacheValue<T> {

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
