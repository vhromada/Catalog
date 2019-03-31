package cz.vhromada.catalog.converter;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.ShowUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Show} and {@link Show}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
class ShowConverterTest {

    /**
     * Instance of {@link ShowConverter}
     */
    @Autowired
    private ShowConverter converter;

    /**
     * Test method for {@link ShowConverter#convert(Show)}.
     */
    @Test
    void convert() {
        final Show show = ShowUtils.newShow(1);
        final cz.vhromada.catalog.domain.Show showDomain = converter.convert(show);

        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

    /**
     * Test method for {@link ShowConverter#convertBack(cz.vhromada.catalog.domain.Show)}.
     */
    @Test
    void convertBack() {
        final cz.vhromada.catalog.domain.Show showDomain = ShowUtils.newShowDomain(1);
        final Show show = converter.convertBack(showDomain);

        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

}
