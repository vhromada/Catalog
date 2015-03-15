package cz.vhromada.catalog.facade.converters;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Medium;

import org.dozer.CustomConverter;
import org.dozer.Mapper;
import org.dozer.MapperAware;

/**
 * A class represents converter between media.
 *
 * @author Vladimir Hromada
 */
public class MediaConverter implements MapperAware, CustomConverter {

    /**
     * Mapper
     */
    private Mapper mapper;

    @Override
    public Object convert(final Object existingDestinationFieldValue, final Object sourceFieldValue, final Class<?> destinationClass,
            final Class<?> sourceClass) {
        if (sourceFieldValue == null) {
            return null;
        }

        final List sourceList = (List) sourceFieldValue;

        return convert(sourceList, sourceList.get(0) instanceof Integer ? Medium.class : Integer.class);
    }

    @Override
    public void setMapper(final Mapper mapper) {
        this.mapper = mapper;
    }

    private <T> List<T> convert(final List source, final Class<T> destinationClass) {
        final List<T> result = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            final Object object = source.get(i);
            if (object == null) {
                result.add(null);
            } else {
                final T convertedObject = mapper.map(object, destinationClass);
                if (destinationClass == Medium.class) {
                    ((Medium) convertedObject).setNumber(i + 1);
                }
                result.add(convertedObject);
            }
        }

        return result;
    }

}
