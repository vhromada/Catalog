package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.commons.TestConstants.LENGTH;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link IntegerToMediumConverter}.
 *
 * @author Vladimir Hromada
 */
public class IntegerToMediumConverterTest {

	/** Instance of {@link IntegerToMediumConverter} */
	private IntegerToMediumConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new IntegerToMediumConverter();
	}

	/** Test method for {@link IntegerToMediumConverter#convert(Integer)}. */
	@Test
	public void testConvert() {
		final Medium medium = converter.convert(LENGTH);
		DeepAsserts.assertNotNull(medium, "id");
		DeepAsserts.assertEquals(LENGTH, medium.getLength());
	}

	/** Test method for {@link IntegerToMediumConverter#convert(Integer)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
