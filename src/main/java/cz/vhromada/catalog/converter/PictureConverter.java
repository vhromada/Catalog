package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.mapper.PictureMapper;
import cz.vhromada.common.converter.MovableConverter;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

/**
 * A class represents converter for picture.
 *
 * @author Vladimir Hromada
 */
@Component
public class PictureConverter implements MovableConverter<Picture, cz.vhromada.catalog.domain.Picture> {

    /**
     * Mapper for picture
     */
    private PictureMapper mapper;

    /**
     * Creates a new instance of PictureConverter.
     */
    public PictureConverter() {
        this.mapper = Mappers.getMapper(PictureMapper.class);
    }

    @Override
    public cz.vhromada.catalog.domain.Picture convert(final Picture source) {
        return mapper.map(source);
    }

    @Override
    public Picture convertBack(final cz.vhromada.catalog.domain.Picture source) {
        return mapper.mapBack(source);
    }

}
