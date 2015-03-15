package cz.vhromada.catalog.facade.converters;

import cz.vhromada.catalog.facade.converters.spring.ConvertersSpringSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.converters.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MediumToIntegerConverterTest.class, MediaConverterTest.class, ConvertersSpringSuite.class })
public class ConvertersSuite {
}
