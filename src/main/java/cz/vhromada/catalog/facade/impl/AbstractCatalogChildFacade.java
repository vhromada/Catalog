package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.facade.CatalogChildFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converters.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.springframework.util.Assert;

/**
 * An abstract class facade for catalog for child data.
 *
 * @param <S> type of child entity data
 * @param <T> type of child domain data
 * @param <U> type of parent entity data
 * @param <V> type of domain repository data
 * @author Vladimir Hromada
 */
public abstract class AbstractCatalogChildFacade<S extends Movable, T extends Movable, U extends Movable, V extends Movable>
        implements CatalogChildFacade<S, U> {

    /**
     * Service for catalog
     */
    private CatalogService<V> catalogService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for catalog for parent data
     */
    private CatalogValidator<U> parentCatalogValidator;

    /**
     * Validator for catalog for child data
     */
    private CatalogValidator<S> childCatalogValidator;

    /**
     * Creates a new instance of AbstractCatalogChildFacade.
     *
     * @param catalogService         service for catalog
     * @param converter              converter
     * @param parentCatalogValidator validator for catalog for parent data
     * @param childCatalogValidator  validator for catalog for child data
     * @throws IllegalArgumentException if service for catalog is null
     *                                  or converter is null
     *                                  or validator for catalog for parent data is null
     *                                  or validator for catalog for child data is null
     */
    public AbstractCatalogChildFacade(final CatalogService<V> catalogService, final Converter converter, final CatalogValidator<U> parentCatalogValidator,
            final CatalogValidator<S> childCatalogValidator) {
        Assert.notNull(catalogService, "Service for catalog mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(parentCatalogValidator, "Validator for catalog for parent data mustn't be null.");
        Assert.notNull(childCatalogValidator, "Validator for catalog for child data mustn't be null.");

        this.catalogService = catalogService;
        this.converter = converter;
        this.parentCatalogValidator = parentCatalogValidator;
        this.childCatalogValidator = childCatalogValidator;
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
    public Result<S> get(final Integer id) {
        if (id == null) {
            return Result.error("ID_NULL", "ID mustn't be null.");
        }

        return Result.of(converter.convert(getDomainData(id), getEntityClass()));
    }

    /**
     * Adds data. Sets new ID and position.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Data is null</li>
     * <li>ID isn't null</li>
     * <li>Deep data validation errors</li>
     * <li>Parent is null</li>
     * <li>Parent ID is null</li>
     * <li>Parent doesn't exist in data storage</li>
     * </ul>
     *
     * @param parent parent
     * @param data   data
     * @return result with validation errors
     */
    public Result<Void> add(final U parent, final S data) {
        final Result<Void> result = parentCatalogValidator.validate(parent, ValidationType.EXISTS);
        result.addEvents(childCatalogValidator.validate(data, ValidationType.NEW, ValidationType.DEEP).getEvents());

        if (Status.OK.equals(result.getStatus())) {
            catalogService.update(getForAdd(parent, getDataForAdd(data)));
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
    public Result<Void> update(final S data) {
        final Result<Void> result = childCatalogValidator.validate(data, ValidationType.EXISTS, ValidationType.DEEP);

        if (Status.OK.equals(result.getStatus())) {
            catalogService.update(getForUpdate(data));
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
    public Result<Void> remove(final S data) {
        final Result<Void> result = childCatalogValidator.validate(data, ValidationType.EXISTS);

        if (Status.OK.equals(result.getStatus())) {
            catalogService.update(getForRemove(data));
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
    public Result<Void> duplicate(final S data) {
        final Result<Void> result = childCatalogValidator.validate(data, ValidationType.EXISTS);

        if (Status.OK.equals(result.getStatus())) {
            catalogService.update(getForDuplicate(data));
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
    public Result<Void> moveUp(final S data) {
        final Result<Void> result = childCatalogValidator.validate(data, ValidationType.EXISTS, ValidationType.UP);

        if (Status.OK.equals(result.getStatus())) {
            catalogService.update(getForMove(data, true));
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
    public Result<Void> moveDown(final S data) {
        final Result<Void> result = childCatalogValidator.validate(data, ValidationType.EXISTS, ValidationType.DOWN);

        if (Status.OK.equals(result.getStatus())) {
            catalogService.update(getForMove(data, false));
        }

        return result;
    }

    /**
     * Returns list of child for specified parent.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Parent is null</li>
     * <li>ID is null</li>
     * <li>Parent doesn't exist in data storage</li>
     * </ul>
     *
     * @param parent parent
     * @return result with list of data
     */
    public Result<List<S>> find(final U parent) {
        final Result<Void> validationResult = parentCatalogValidator.validate(parent, ValidationType.EXISTS);

        if (Status.OK.equals(validationResult.getStatus())) {
            return Result.of(CollectionUtils.getSortedData(converter.convertCollection(getDomainList(parent), getEntityClass())));
        }


        final Result<List<S>> result = new Result<>();
        result.addEvents(validationResult.getEvents());

        return result;
    }

    /**
     * Returns service for catalog.
     *
     * @return service for catalog
     */
    protected CatalogService<V> getCatalogService() {
        return catalogService;
    }

    /**
     * Returns data for add.
     *
     * @param data data
     * @return data for add
     */
    protected T getDataForAdd(final S data) {
        final T updatedData = converter.convert(data, getDomainClass());
        updatedData.setPosition(Integer.MAX_VALUE);

        return updatedData;
    }

    /**
     * Returns data for add.
     *
     * @param data data
     * @return data for add
     */
    protected T getDataForUpdate(final S data) {
        return converter.convert(data, getDomainClass());
    }

    /**
     * Returns domain data with specified ID.
     *
     * @param id ID
     * @return domain data with specified ID
     */
    protected abstract T getDomainData(Integer id);

    /**
     * Returns data for specified parent.
     *
     * @param parent parent
     * @return data for specified parent
     */
    protected abstract List<T> getDomainList(U parent);

    /**
     * Returns data for add.
     *
     * @param parent parent
     * @param data   data
     * @return data for add
     */
    protected abstract V getForAdd(U parent, T data);

    /**
     * Returns data for update.
     *
     * @param data data
     * @return data for update
     */
    protected abstract V getForUpdate(S data);

    /**
     * Returns data for remove.
     *
     * @param data data
     * @return data for remove
     */
    protected abstract V getForRemove(S data);

    /**
     * Returns data for duplicate.
     *
     * @param data data
     * @return data for duplicate
     */
    protected abstract V getForDuplicate(S data);

    /**
     * Returns data for duplicate.
     *
     * @param data data
     * @param up   true if moving data up
     * @return data for duplicate
     */
    protected abstract V getForMove(S data, boolean up);

    /**
     * Returns entity class.
     *
     * @return entity class.
     */
    protected abstract Class<S> getEntityClass();

    /**
     * Returns domain class.
     *
     * @return domain class.
     */
    protected abstract Class<T> getDomainClass();

}
