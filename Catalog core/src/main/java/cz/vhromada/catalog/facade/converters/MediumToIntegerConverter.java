package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.dao.entities.Medium;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A class represents converter from entity medium to integer.
 *
 * @author Vladimir Hromada
 */
@Component("mediumToIntegerConverter")
public class MediumToIntegerConverter implements Converter<Medium, Integer> {

    @Override
    public Integer convert(final Medium source) {
        if (source == null) {
            return null;
        }

        return source.getLength();
    }

}
