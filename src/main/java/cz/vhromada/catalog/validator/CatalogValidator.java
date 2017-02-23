package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Result;

/**
 * An interface represents validator for catalog.
 *
 * @param <T> type of data
 * @author Vladimir Hromada
 */
public interface CatalogValidator<T extends Movable> {

    /**
     * Validates data.
     *
     * @param data            validating data
     * @param validationTypes types of validation
     * @return result with validation errors
     */
    Result<Void> validate(T data, ValidationType... validationTypes);

}
