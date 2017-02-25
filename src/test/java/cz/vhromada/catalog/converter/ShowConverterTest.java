package cz.vhromada.catalog.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

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
        assertThat(converter.convert(null, Show.class), is(nullValue()));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain.
     */
    @Test
    public void testConvertShow() {
        final Show show = ShowUtils.newShow(1);
        final cz.vhromada.catalog.domain.Show showDomain = converter.convert(show, cz.vhromada.catalog.domain.Show.class);

        assertThat(showDomain, is(notNullValue()));
        ShowUtils.assertShowDeepEquals(show, showDomain);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to domain with null argument.
     */
    @Test
    public void testConvertShow_NullArgument() {
        assertThat(converter.convert(null, cz.vhromada.catalog.domain.Show.class), is(nullValue()));
    }

}
