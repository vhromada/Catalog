package cz.vhromada.catalog.facade.converters.spring;

import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.converters.ProgramToProgramTOConverter;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link ProgramToProgramTOConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class ProgramToProgramTOConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Instance of {@link ObjectGenerator} */
	@Autowired
	private ObjectGenerator objectGenerator;

	/** Test method for {@link ProgramToProgramTOConverter#convert(Program)}. */
	@Test
	public void testConvert() {
		final Program program = objectGenerator.generate(Program.class);
		final ProgramTO programTO = conversionService.convert(program, ProgramTO.class);
		DeepAsserts.assertNotNull(programTO);
		DeepAsserts.assertEquals(program, programTO, "additionalData");
	}

	/** Test method for {@link ProgramToProgramTOConverter#convert(Program)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, ProgramTO.class));
	}

}
