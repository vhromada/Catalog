package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link cz.vhromada.catalog.facade.converters.MediumToIntegerConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class MediumToIntegerConverterSpringTest {

    /** Instance of {@link ConversionService} */
    @Autowired
    private ConversionService conversionService;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Test method for {@link cz.vhromada.catalog.facade.converters.MediumToIntegerConverter#convert(Medium)}. */
    @Test
    public void testConvert() {
        final Medium medium = objectGenerator.generate(Medium.class);
        final Integer length = conversionService.convert(medium, Integer.class);
        DeepAsserts.assertNotNull(length);
        DeepAsserts.assertEquals(medium.getLength(), length);
    }

    /** Test method for {@link cz.vhromada.catalog.facade.converters.MediumToIntegerConverter#convert(Medium)} with null argument. */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(conversionService.convert(null, Integer.class));
    }

}
