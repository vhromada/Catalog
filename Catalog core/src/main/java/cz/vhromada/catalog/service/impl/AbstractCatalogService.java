package cz.vhromada.catalog.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.commons.Movable;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.validators.Validators;

import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * An abstract class represents service for data.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
@Transactional
public abstract class AbstractCatalogService<T extends Movable> implements CatalogService<T> {

    /**
     * Data argument
     */
    private static final String DATA_ARGUMENT = "Data";

    /**
     * Repository for data
     */
    private JpaRepository<T, Integer> repository;

    /**
     * Cache for data
     */
    private Cache cache;

    /**
     * Cache key
     */
    private String key;

    /**
     * Creates a new instance of AbstractCatalogService.
     *
     * @param repository repository for data
     * @param cache      cache for data
     * @param key        cache key
     * @throws IllegalArgumentException if repository for data is null
     *                                  or cache for data is null
     *                                  or cache key is null
     */
    public AbstractCatalogService(final JpaRepository<T, Integer> repository, final Cache cache, final String key) {
        Validators.validateArgumentNotNull(repository, "Repository");
        Validators.validateArgumentNotNull(cache, "Cache");
        Validators.validateArgumentNotNull(key, "Cache key");

        this.repository = repository;
        this.cache = cache;
        this.key = key;
    }

    @Override
    public void newData() {
        repository.deleteAll();

        cache.clear();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll() {
        return getSortedData(getCachedData(true));
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public T get(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        final List<T> data = getCachedData(true);
        for (final T item : data) {
            if (id.equals(item.getId())) {
                return item;
            }
        }

        return null;
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void add(final T data) {
        Validators.validateArgumentNotNull(data, DATA_ARGUMENT);

        final T savedData = repository.save(data);
        savedData.setPosition(savedData.getId() - 1);
        repository.save(savedData);

        final List<T> dataList = getCachedData(false);
        dataList.add(savedData);
        cache.put(key, dataList);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void update(final T data) {
        Validators.validateArgumentNotNull(data, DATA_ARGUMENT);

        final T savedData = repository.save(data);

        final List<T> dataList = getCachedData(false);
        updateItem(dataList, savedData);
        cache.put(key, dataList);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void remove(final T data) {
        Validators.validateArgumentNotNull(data, DATA_ARGUMENT);

        repository.delete(data);

        final List<T> dataList = getCachedData(false);
        dataList.remove(data);
        cache.put(key, dataList);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void duplicate(final T data) {
        Validators.validateArgumentNotNull(data, DATA_ARGUMENT);

        final T savedDataCopy = repository.save(getCopy(data));

        final List<T> dataList = getCachedData(false);
        dataList.add(savedDataCopy);
        cache.put(key, dataList);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveUp(final T data) {
        move(data, true);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveDown(final T data) {
        move(data, false);
    }

    @Override
    public void updatePositions() {
        final List<T> data = getCachedData(false);
        for (int i = 0; i < data.size(); i++) {
            final T item = data.get(i);
            item.setPosition(i);
        }

        final List<T> savedData = repository.save(data);

        cache.put(key, savedData);
    }

    /**
     * Returns copy of data.
     *
     * @param data data
     * @return copy of data
     */
    protected abstract T getCopy(final T data);

    /**
     * Returns list of data.
     *
     * @param cached true if returned data from repository should be cached
     * @return list of data
     */
    private List<T> getCachedData(final boolean cached) {
        final Cache.ValueWrapper cacheValue = cache.get(key);
        if (cacheValue == null) {
            final List<T> data = repository.findAll();
            if (cached) {
                cache.put(key, data);
            }

            return data;
        }

        @SuppressWarnings("unchecked")
        final List<T> data = (List<T>) cacheValue.get();
        return data;
    }

    /**
     * Returns sorted data.
     *
     * @param data data for sorting
     * @return sorted data
     */
    private List<T> getSortedData(final List<T> data) {
        final List<T> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData, (o1, o2) -> {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }

            final int result = Integer.compare(o1.getPosition(), o2.getPosition());
            if (result == 0) {
                return Integer.compare(o1.getId(), o2.getId());
            }
            return result;
        });

        return sortedData;
    }

    /**
     * Moves data in list one position up or down.
     *
     * @param data data
     * @param up   if moving data up
     * @throws IllegalArgumentException if data is null
     */
    private void move(final T data, final boolean up) {
        Validators.validateArgumentNotNull(data, DATA_ARGUMENT);

        final List<T> dataList = getSortedData(getCachedData(false));
        final int index = dataList.indexOf(data);
        final T other = dataList.get(up ? index - 1 : index + 1);
        final int position = data.getPosition();
        data.setPosition(other.getPosition());
        other.setPosition(position);

        final T savedData = repository.save(data);
        final T savedOther = repository.save(other);

        updateItem(dataList, savedData);
        updateItem(dataList, savedOther);
        cache.put(key, dataList);
    }

    /**
     * Updates item if list of data.
     *
     * @param data list of data
     * @param item updating item
     */
    private void updateItem(final List<T> data, final T item) {
        final int index = data.indexOf(item);
        data.remove(item);
        data.add(index, item);
    }

}
