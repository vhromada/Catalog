package cz.vhromada.catalog.facade.converters;

import static cz.vhromada.catalog.commons.TestConstants.ID;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link ProgramTOToProgramConverter}.
 *
 * @author Vladimir Hromada
 */
public class ProgramTOToProgramConverterTest {

	/** Instance of {@link ProgramTOToProgramConverter} */
	private ProgramTOToProgramConverter converter;

	/** Initializes converter. */
	@Before
	public void setUp() {
		converter = new ProgramTOToProgramConverter();
	}

	/** Test method for {@link ProgramTOToProgramConverter#convert(ProgramTO)}. */
	@Test
	public void testConvert() {
		final ProgramTO programTO = ToGenerator.createProgram(ID);
		final Program program = converter.convert(programTO);
		DeepAsserts.assertNotNull(program);
		DeepAsserts.assertEquals(programTO, program, "additionalData");
	}

	/** Test method for {@link ProgramTOToProgramConverter#convert(ProgramTO)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(converter.convert(null));
	}

}
