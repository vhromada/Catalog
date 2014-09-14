package cz.vhromada.catalog.facade.to;

import static cz.vhromada.catalog.common.TestConstants.OTHER_DATA;

import cz.vhromada.test.DeepAsserts;
import org.junit.Test;

/**
 * A class represents test for class {@link ProgramTO}.
 *
 * @author Vladimir Hromada
 */
public class ProgramTOTest {

	/** Test method for {@link ProgramTO#getAdditionalData()}. */
	@Test
	public void testGetAdditionalData() {
		final ProgramTO program = new ProgramTO();
		program.setCrack(true);
		DeepAsserts.assertEquals("Crack", program.getAdditionalData());
		program.setSerialKey(true);
		DeepAsserts.assertEquals("Crack, serial key", program.getAdditionalData());
		program.setOtherData(OTHER_DATA);
		DeepAsserts.assertEquals("Crack, serial key, " + OTHER_DATA, program.getAdditionalData());
		clearAdditionalData(program);
		program.setSerialKey(true);
		DeepAsserts.assertEquals("Serial key", program.getAdditionalData());
		clearAdditionalData(program);
		program.setOtherData(OTHER_DATA);
		DeepAsserts.assertEquals(OTHER_DATA, program.getAdditionalData());
	}

	/**
	 * Clear additional data.
	 *
	 * @param program TO for program
	 */
	private void clearAdditionalData(final ProgramTO program) {
		program.setCrack(false);
		program.setSerialKey(false);
		program.setOtherData(null);
	}

}
