package cz.vhromada.catalog.converter;

import static org.assertj.core.api.Assertions.assertThat;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.utils.PictureUtils;
import cz.vhromada.converter.Converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Picture} and {@link Picture}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class PictureConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertPictureDomain() {
        final cz.vhromada.catalog.domain.Picture pictureDomain = PictureUtils.newPictureDomain(1);
        final Picture picture = converter.convert(pictureDomain, Picture.class);

        PictureUtils.assertPictureDeepEquals(picture, pictureDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null picture.
     */
    @Test
    void convertPictureDomain_NullPicture() {
        assertThat(converter.convert(null, Picture.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertPicture() {
        final Picture picture = PictureUtils.newPicture(1);
        final cz.vhromada.catalog.domain.Picture pictureDomain = converter.convert(picture, cz.vhromada.catalog.domain.Picture.class);

        PictureUtils.assertPictureDeepEquals(picture, pictureDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null picture.
     */
    @Test
    void convertPicture_NullPicture() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Picture.class));
    }

}
