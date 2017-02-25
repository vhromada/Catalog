package cz.vhromada.catalog.validator.impl;

import java.util.List;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.util.Assert;

/**
 * An abstract class represents validator for data.
 *
 * @param <T> type of entity data
 * @param <U> type of domain data
 * @author Vladimir Hromada
 */
public abstract class AbstractCatalogValidator<T extends Movable, U extends Movable> implements CatalogValidator<T> {

    /**
     * Name of entity
     */
    private String name;

    /**
     * Prefix for validation keys
     */
    private String prefix;

    /**
     * Creates a new instance of AbstractCatalogValidator.
     *
     * @param name name of entity
     * @throws IllegalArgumentException if name of entity is null
     */
    public AbstractCatalogValidator(final String name) {
        Assert.notNull(name, "Name of entity mustn't be null.");

        this.name = name;
        this.prefix = name.toUpperCase();
    }

    @Override
    public Result<Void> validate(final T data, final ValidationType... validationTypes) {
        if (data == null) {
            return Result.error(prefix + "_NULL", name + " mustn't be null.");
        }

        final Result<Void> result = new Result<>();
        final List<ValidationType> validationTypeList = CollectionUtils.newList(validationTypes);
        if (validationTypeList.contains(ValidationType.NEW)) {
            validateNewData(data, result);
        }
        if (validationTypeList.contains(ValidationType.EXISTS)) {
            validateExistingData(data, result);
        }
        if (validationTypeList.contains(ValidationType.DEEP)) {
            validateDataDeep(data, result);
        }
        if (validationTypeList.contains(ValidationType.UP)) {
            validateMovingData(data, result, true);
        }
        if (validationTypeList.contains(ValidationType.DOWN)) {
            validateMovingData(data, result, false);
        }

        return result;
    }

    /**
     * Returns data from repository.
     *
     * @param data data
     * @return data from repository
     */
    protected abstract U getData(T data);

    /**
     * Returns list of data from repository.
     *
     * @param data data
     * @return list of data from repository
     */
    protected abstract List<U> getList(T data);

    /**
     * Validates data deeply.
     *
     * @param data   validating data
     * @param result result with validation errors
     */
    protected abstract void validateDataDeep(T data, Result<Void> result);

    /**
     * Validates new data.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>ID isn't null</li>
     * </ul>
     *
     * @param data   validating data
     * @param result result with validation errors
     */
    private void validateNewData(final T data, final Result<Void> result) {
        if (data.getId() != null) {
            result.addEvent(new Event(Severity.ERROR, prefix + "_ID_NOT_NULL", "ID must be null."));
        }
    }

    /**
     * Validates existing data.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>ID is null</li>
     * <li>Data doesn't exist</li>
     * </ul>
     *
     * @param data   validating data
     * @param result result with validation errors
     */
    private void validateExistingData(final T data, final Result<Void> result) {
        if (data.getId() == null) {
            result.addEvent(new Event(Severity.ERROR, prefix + "_ID_NULL", "ID mustn't be null."));
        } else if (getData(data) == null) {
            result.addEvent(new Event(Severity.ERROR, prefix + "_NOT_EXIST", name + " doesn't exist."));
        }
    }

    /**
     * Validates moving data.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>Not movable</li>
     * </ul>
     *
     * @param data   validating data
     * @param result result with validation errors
     * @param up     true if data is moving up
     */
    private void validateMovingData(final T data, final Result<Void> result, final boolean up) {
        final List<U> list = getList(data);
        final int index = list.indexOf(getData(data));
        if (up) {
            if (index <= 0) {
                result.addEvent(new Event(Severity.ERROR, prefix + "_NOT_MOVABLE", name + " can't be moved up."));
            }
        } else if (index < 0 || index >= list.size() - 1) {
            result.addEvent(new Event(Severity.ERROR, prefix + "_NOT_MOVABLE", name + " can't be moved down."));
        }
    }

}
