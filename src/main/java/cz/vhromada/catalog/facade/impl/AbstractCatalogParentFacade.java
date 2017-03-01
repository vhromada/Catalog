package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converters.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.springframework.util.Assert;

/**
 * An abstract class facade for catalog for parent data.
 *
 * @param <T> type of entity data
 * @param <U> type of domain data
 * @author Vladimir Hromada
 */
public abstract class AbstractCatalogParentFacade<T extends Movable, U extends Movable> implements CatalogParentFacade<T> {

    /**
     * Service for catalog
     */
    private final CatalogService<U> catalogService;

    /**
     * Converter
     */
    private final Converter converter;

    /**
     * Validator for catalog
     */
    private final CatalogValidator<T> catalogValidator;

    /**
     * Creates a new instance of AbstractCatalogParentFacade.
     *
     * @param catalogService   service for catalog
     * @param converter        converter
     * @param catalogValidator validator for catalog
     * @throws IllegalArgumentException if service for catalog is null
     *                                  or converter is null
     *                                  or validator for catalog is null
     */
    public AbstractCatalogParentFacade(final CatalogService<U> catalogService, final Converter converter, final CatalogValidator<T> catalogValidator) {
        Assert.notNull(catalogService, "Service for catalog mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(catalogValidator, "Validator for catalog mustn't be null.");

        this.catalogService = catalogService;
        this.converter = converter;
        this.catalogValidator = catalogValidator;
    }

    /**
     * Creates new data.
     *
     * @return result
     */
    @Override
    public Result<Void> newData() {
        catalogService.newData();

        return new Result<>();
    }

    /**
     * Returns list of data.
     *
     * @return result with list of data
     */
    @Override
    public Result<List<T>> getAll() {
        return Result.of(converter.convertCollection(catalogService.getAll(), getEntityClass()));
    }

    /**
     * Returns data with ID or null if there aren't such data.
     * <br>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * </ul>
     *
     * @param id ID
     * @return result with data or validation errors
     */
    @Override
    public Result<T> get(final Integer id) {
        if (id == null) {
            return Result.error("ID_NULL", "ID mustn't be null.");
        }

        return Result.of(converter.convert(catalogService.get(id), getEntityClass()));
    }

    /**
     * Adds data. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Data is null</li>
     * <li>ID isn't null</li>
     * <li>Deep data validation errors</li>
     * </ul>
     *
     * @param data data
     * @return result with validation errors
     */
    @Override
    public Result<Void> add(final T data) {
        final Result<Void> result = catalogValidator.validate(data, ValidationType.NEW, ValidationType.DEEP);

        if (Status.OK == result.getStatus()) {
            catalogService.add(getDataForAdd(data));
        }

        return result;
    }

    /**
     * Updates data.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Data is null</li>
     * <li>ID is null</li>
     * <li>Deep data validation errors</li>
     * <li>Data doesn't exist in data storage</li>
     * </ul>
     *
     * @param data new value of data
     * @return result with validation errors
     */
    @Override
    public Result<Void> update(final T data) {
        final Result<Void> result = catalogValidator.validate(data, ValidationType.EXISTS, ValidationType.DEEP);

        if (Status.OK == result.getStatus()) {
            catalogService.update(getDataForUpdate(data));
        }

        return result;
    }

    /**
     * Removes data.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Data is null</li>
     * <li>ID is null</li>
     * <li>Data doesn't exist in data storage</li>
     * </ul>
     *
     * @param data data
     * @return result with validation errors
     */
    @Override
    public Result<Void> remove(final T data) {
        final Result<Void> result = catalogValidator.validate(data, ValidationType.EXISTS);

        if (Status.OK == result.getStatus()) {
            catalogService.remove(catalogService.get(data.getId()));
        }

        return result;
    }

    /**
     * Duplicates data.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Data is null</li>
     * <li>ID is null</li>
     * <li>Data doesn't exist in data storage</li>
     * </ul>
     *
     * @param data data
     * @return result with validation errors
     */
    @Override
    public Result<Void> duplicate(final T data) {
        final Result<Void> result = catalogValidator.validate(data, ValidationType.EXISTS);

        if (Status.OK == result.getStatus()) {
            catalogService.duplicate(catalogService.get(data.getId()));
        }

        return result;
    }

    /**
     * Moves data in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Data is null</li>
     * <li>ID is null</li>
     * <li>Data can't be moved up</li>
     * <li>Data doesn't exist in data storage</li>
     * </ul>
     *
     * @param data data
     * @return result with validation errors
     */
    @Override
    public Result<Void> moveUp(final T data) {
        final Result<Void> result = catalogValidator.validate(data, ValidationType.EXISTS, ValidationType.UP);

        if (Status.OK == result.getStatus()) {
            catalogService.moveUp(catalogService.get(data.getId()));
        }

        return result;
    }

    /**
     * Moves data in list one position up.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Data is null</li>
     * <li>ID is null</li>
     * <li>Data can't be moved down</li>
     * <li>Data doesn't exist in data storage</li>
     * </ul>
     *
     * @param data data
     * @return result with validation errors
     */
    @Override
    public Result<Void> moveDown(final T data) {
        final Result<Void> result = catalogValidator.validate(data, ValidationType.EXISTS, ValidationType.DOWN);

        if (Status.OK == result.getStatus()) {
            catalogService.moveDown(catalogService.get(data.getId()));
        }

        return result;
    }

    /**
     * Updates positions.
     *
     * @return result
     */
    @Override
    public Result<Void> updatePositions() {
        catalogService.updatePositions();

        return new Result<>();
    }

    /**
     * Returns service for catalog.
     *
     * @return service for catalog
     */
    protected CatalogService<U> getCatalogService() {
        return catalogService;
    }

    /**
     * Returns data for add.
     *
     * @param data data
     * @return data for add
     */
    protected U getDataForAdd(final T data) {
        return converter.convert(data, getDomainClass());
    }

    /**
     * Returns data for update.
     *
     * @param data data
     * @return data for update
     */
    protected U getDataForUpdate(final T data) {
        return converter.convert(data, getDomainClass());
    }

    /**
     * Returns entity class.
     *
     * @return entity class.
     */
    protected abstract Class<T> getEntityClass();

    /**
     * Returns domain class.
     *
     * @return domain class.
     */
    protected abstract Class<U> getDomainClass();

}
