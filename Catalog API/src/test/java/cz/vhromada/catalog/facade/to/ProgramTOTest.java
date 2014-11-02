package cz.vhromada.catalog.facade.to;

import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link ProgramTO}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testGeneratorContext.xml")
public class ProgramTOTest {

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link ProgramTO#getAdditionalData()}. */
	@Test
	public void testGetAdditionalData() {
		final ProgramTO program = new ProgramTO();
		final String otherData = objectGenerator.generate(String.class);

		program.setCrack(true);
		DeepAsserts.assertEquals("Crack", program.getAdditionalData());

		program.setSerialKey(true);
		DeepAsserts.assertEquals("Crack, serial key", program.getAdditionalData());

		program.setOtherData(otherData);
		DeepAsserts.assertEquals("Crack, serial key, " + otherData, program.getAdditionalData());

		clearAdditionalData(program);
		program.setSerialKey(true);
		DeepAsserts.assertEquals("Serial key", program.getAdditionalData());

		clearAdditionalData(program);
		program.setOtherData(otherData);
		DeepAsserts.assertEquals(otherData, program.getAdditionalData());
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
