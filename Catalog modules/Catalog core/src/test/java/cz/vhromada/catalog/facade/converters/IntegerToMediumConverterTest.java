package cz.vhromada.catalog.facade.converters;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link IntegerToMediumConverter}.
 *
 * @author Vladimir Hromada
 */
public class IntegerToMediumConverterTest extends ObjectGeneratorTest {

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
		final int length = generate(Integer.class);
		final Medium medium = converter.convert(length);
		DeepAsserts.assertNotNull(medium, "id");
		DeepAsserts.assertEquals(length, medium.getLength());
	}

	/** Test method for {@link IntegerToMediumConverter#convert(Integer)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
