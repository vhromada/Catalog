package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.test.DeepAsserts;

import org.dozer.MappingException;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MediumToIntegerConverter}.
 *
 * @author Vladimir Hromada
 */
public class MediumToIntegerConverterTest extends ObjectGeneratorTest {

    /**
     * Instance of {@link MediumToIntegerConverter}
     */
    private MediumToIntegerConverter converter;

    /**
     * Initializes converter.
     */
    @Before
    public void setUp() {
        converter = new MediumToIntegerConverter();
    }

    /**
     * Test method for {@link MediumToIntegerConverter#convert(Object, Object, Class, Class)} with integer argument.
     */
    @Test
    public void testConvertWithIntegerArgument() {
        final int length = generate(Integer.class);
        final Medium medium = (Medium) converter.convert(null, length, Medium.class, Integer.class);
        DeepAsserts.assertNotNull(medium, "id");
        DeepAsserts.assertEquals(length, medium.getLength());
    }

    /**
     * Test method for {@link MediumToIntegerConverter#convert(Object, Object, Class, Class)} with medium argument.
     */
    @Test
    public void testConvertWithMediumArgument() {
        final Medium medium = generate(Medium.class);
        final Integer length = (Integer) converter.convert(null, medium, Integer.class, Medium.class);
        DeepAsserts.assertNotNull(length);
        DeepAsserts.assertEquals(medium.getLength(), length);
    }

    /**
     * Test method for {@link MediumToIntegerConverter#convert(Object, Object, Class, Class)} with null argument.
     */
    @Test
    public void testConvertWithNullArgument() {
        assertNull(converter.convert(null, null, null, null));
    }

    /**
     * Test method for {@link MediumToIntegerConverter#convert(Object, Object, Class, Class)} with bad argument.
     */
    @Test(expected = MappingException.class)
    public void testConvertWithBadArgument() {
        assertNull(converter.convert(null, "Test", Integer.class, Medium.class));
    }

}
