package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link MediumToIntegerConverter}.
 *
 * @author Vladimir Hromada
 */
public class MediumToIntegerConverterTest {

	/** Instance of {@link MediumToIntegerConverter} */
	private MediumToIntegerConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new MediumToIntegerConverter();
	}

	/** Test method for {@link MediumToIntegerConverter#convert(Medium)}. */
	@Test
	public void testConvert() {
		final Medium medium = EntityGenerator.createMedium();
		final Integer length = converter.convert(medium);
		DeepAsserts.assertNotNull(length);
		DeepAsserts.assertEquals(medium.getLength(), length);
	}

	/** Test method for {@link MediumToIntegerConverter#convert(Medium)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
