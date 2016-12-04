package cz.vhromada.catalog;

import cz.vhromada.catalog.common.CommonSuite;
import cz.vhromada.catalog.converter.ConverterSuite;
import cz.vhromada.catalog.facade.impl.FacadeSuite;
import cz.vhromada.catalog.repository.RepositorySuite;
import cz.vhromada.catalog.service.impl.ServiceSuite;
import cz.vhromada.catalog.validator.impl.ValidatorsSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for all tests.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ CommonSuite.class, ConverterSuite.class, FacadeSuite.class, RepositorySuite.class, ServiceSuite.class, ValidatorsSuite.class })
public class CatalogSuite {
}
