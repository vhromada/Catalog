package cz.vhromada.catalog.facade.converters.spring;

import static cz.vhromada.catalog.commons.TestConstants.LENGTH;
import static org.junit.Assert.assertNull;

import cz.vhromada.catalog.dao.entities.Medium;
import cz.vhromada.catalog.facade.converters.IntegerToMediumConverter;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link IntegerToMediumConverter} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeConvertersContext.xml")
public class IntegerToMediumConverterSpringTest {

	/** Instance of {@link ConversionService} */
	@Autowired
	private ConversionService conversionService;

	/** Test method for {@link IntegerToMediumConverter#convert(Integer)}. */
	@Test
	public void testConvert() {
		final Medium medium = conversionService.convert(LENGTH, Medium.class);
		DeepAsserts.assertNotNull(medium, "id");
		DeepAsserts.assertEquals(LENGTH, medium.getLength());
	}

	/** Test method for {@link IntegerToMediumConverter#convert(Integer)} with null argument. */
	@Test
	public void testConvertWithNullArgument() {
		assertNull(conversionService.convert(null, Medium.class));
	}

}
