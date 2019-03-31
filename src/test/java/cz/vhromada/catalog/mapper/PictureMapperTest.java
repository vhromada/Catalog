package cz.vhromada.catalog.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.utils.PictureUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

/**
 * A class represents test for mapper between {@link cz.vhromada.catalog.domain.Picture} and {@link Picture}.
 *
 * @author Vladimir Hromada
 */
class PictureMapperTest {

    private PictureMapper mapper;

    /**
     * Initializes mapper.
     */
    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PictureMapper.class);
    }

    /**
     * Test method for {@link PictureMapper#map(Picture)}.
     */
    @Test
    void map() {
        final Picture picture = PictureUtils.newPicture(1);
        final cz.vhromada.catalog.domain.Picture pictureDomain = mapper.map(picture);

        PictureUtils.assertPictureDeepEquals(picture, pictureDomain);
    }

    /**
     * Test method for {@link PictureMapper#map(Picture)} with null picture.
     */
    @Test
    void map_NullPicture() {
        assertThat(mapper.map(null)).isNull();
    }


    /**
     * Test method for {@link PictureMapper#mapBack(cz.vhromada.catalog.domain.Picture)}.
     */
    @Test
    void mapBack() {
        final cz.vhromada.catalog.domain.Picture pictureDomain = PictureUtils.newPictureDomain(1);
        final Picture picture = mapper.mapBack(pictureDomain);

        PictureUtils.assertPictureDeepEquals(picture, pictureDomain);
    }

    /**
     * Test method for {@link PictureMapper#mapBack(cz.vhromada.catalog.domain.Picture)} with null picture.
     */
    @Test
    void mapBack_NullPicture() {
        assertThat(mapper.mapBack(null)).isNull();
    }

}
