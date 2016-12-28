package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.catalog.utils.ShowUtils;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Show} and {@link Show}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
public class ShowConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity.
     */
    @Test
    public void testConvertShowDomain() {
        final cz.vhromada.catalog.domain.Show showDomain = ShowUtils.newShowDomain(1);
        final Show show = converter.convert(showDomain, Show.class);

        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from domain to entity with null argument.
     */
    @Test
    public void testConvertShowDomain_NullArgument() {
        assertNull(converter.convert(null, Show.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertShow() {
        final Show show = ShowUtils.newShow(1);
        final cz.vhromada.catalog.domain.Show showDomain = converter.convert(show, cz.vhromada.catalog.domain.Show.class);

        assertNotNull(show);
        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertShow_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Show.class));
    }

}
