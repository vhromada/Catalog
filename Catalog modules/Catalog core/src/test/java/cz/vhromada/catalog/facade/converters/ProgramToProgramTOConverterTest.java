package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.common.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link ProgramToProgramTOConverter}.
 *
 * @author Vladimir Hromada
 */
public class ProgramToProgramTOConverterTest {

	/** Instance of {@link ProgramToProgramTOConverter} */
	private ProgramToProgramTOConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new ProgramToProgramTOConverter();
	}

	/** Test method for {@link ProgramToProgramTOConverter#convert(Program)}. */
	@Test
	public void testConvert() {
		final Program program = EntityGenerator.createProgram(ID);
		final ProgramTO programTO = converter.convert(program);
		DeepAsserts.assertNotNull(programTO);
		DeepAsserts.assertEquals(program, programTO, "additionalData");
	}

	/** Test method for {@link ProgramToProgramTOConverter#convert(Program)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
