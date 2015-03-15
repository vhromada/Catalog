package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Medium;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

/**
 * A class represents converter between entity medium and integer.
 *
 * @author Vladimir Hromada
 */
public class MediumToIntegerConverter implements CustomConverter {

    @Override
    public Object convert(final Object existingDestinationFieldValue, final Object sourceFieldValue, final Class<?> destinationClass,
            final Class<?> sourceClass) {
        if (sourceFieldValue == null) {
            return null;
        }
        if (sourceFieldValue instanceof Medium && sourceClass == Medium.class && destinationClass == Integer.class) {
            return convertMedium((Medium) sourceFieldValue);
        } else if (sourceFieldValue instanceof Integer && sourceClass == Integer.class && destinationClass == Medium.class) {
            return convertInteger((Integer) sourceFieldValue);
        } else {
            throw new MappingException("Converter MediumToIntegerConverter used incorrectly. Arguments passed in were:" + existingDestinationFieldValue
                    + " and " + sourceFieldValue);
        }
    }

    private static Integer convertMedium(final Medium source) {
        return source.getLength();
    }

    private static Medium convertInteger(final Integer source) {
        final Medium medium = new Medium();
        medium.setLength(source);

        return medium;
    }

}
