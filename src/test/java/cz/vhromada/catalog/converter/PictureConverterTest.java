package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.utils.PictureUtils;

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
     * Instance of {@link PictureConverter}
     */
    @Autowired
    private PictureConverter converter;

    /**
     * Test method for {@link PictureConverter#convert(Picture)}.
     */
    @Test
    void convert() {
        final Picture picture = PictureUtils.newPicture(1);
        final cz.vhromada.catalog.domain.Picture pictureDomain = converter.convert(picture);

        PictureUtils.assertPictureDeepEquals(picture, pictureDomain);
    }

    /**
     * Test method for {@link PictureConverter#convertBack(cz.vhromada.catalog.domain.Picture)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Picture pictureDomain = PictureUtils.newPictureDomain(1);
        final Picture picture = converter.convertBack(pictureDomain);

        PictureUtils.assertPictureDeepEquals(picture, pictureDomain);
    }

}
