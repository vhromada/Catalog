package cz.vhromada.catalog.facade.converters.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.converters.spring.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MediumToIntegerConverterSpringTest.class, IntegerToMediaConverterSpringTest.class })
public class ConvertersSpringSuite {
}
