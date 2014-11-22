package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Medium;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from integer to entity medium.
 *
 * @author Vladimir Hromada
 */
@Component("integerToMediumConverter")
public class IntegerToMediumConverter implements Converter<Integer, Medium> {

    @Override
    public Medium convert(final Integer source) {
        if (source == null) {
            return null;
        }

        final Medium medium = new Medium();
        medium.setLength(source);
        return medium;
    }

}
