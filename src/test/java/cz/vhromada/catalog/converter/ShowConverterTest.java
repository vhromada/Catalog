package cz.vhromada.catalog.converter;

import static org.junit.jupiter.api.Assertions.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.converter.Converter;

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
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    void convertShowDomain() {
        final cz.vhromada.catalog.domain.Show showDomain = ShowUtils.newShowDomain(1);
        final Show show = converter.convert(showDomain, Show.class);

        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null show.
     */
    @Test
    void convertShowDomain_NullShow() {
        assertNull(converter.convert(null, Show.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    void convertShow() {
        final Show show = ShowUtils.newShow(1);
        final cz.vhromada.catalog.domain.Show showDomain = converter.convert(show, cz.vhromada.catalog.domain.Show.class);

        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null show.
     */
    @Test
    void convertShow_NullShow() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Show.class));
    }

}
