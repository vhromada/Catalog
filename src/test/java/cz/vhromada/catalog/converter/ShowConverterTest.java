package cz.vhromada.catalog.converter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.ShowUtils;
import cz.vhromada.catalog.entity.Show;
import cz.vhromada.converters.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for converter between {@link cz.vhromada.catalog.domain.Show} and {@link Show}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:catalogDozerMappingContext.xml")
public class ShowConverterTest {

    /**
     * Instance of {@link Converter}
     */
    @Autowired
    @Qualifier("catalogDozerConverter")
    private Converter converter;

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO.
     */
    @Test
    public void testConvertShow() {
        final cz.vhromada.catalog.domain.Show show = ShowUtils.newShow(1);
        final Show showTO = converter.convert(show, Show.class);

        ShowUtils.assertShowDeepEquals(showTO, show);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from entity to TO with null argument.
     */
    @Test
    public void testConvertShow_NullArgument() {
        assertNull(converter.convert(null, Show.class));
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity.
     */
    @Test
    public void testConvertShowTO() {
        final Show showTO = ShowUtils.newShowTO(1);
        final cz.vhromada.catalog.domain.Show show = converter.convert(showTO, cz.vhromada.catalog.domain.Show.class);

        assertNotNull(show);
        ShowUtils.assertShowDeepEquals(showTO, show);
    }

    /**
     * Test method for {@link Converter#convert(Object, Class)} from TO to entity with null argument.
     */
    @Test
    public void testConvertShowTO_NullArgument() {
        assertNull(converter.convert(null, cz.vhromada.catalog.domain.Show.class));
    }

}
