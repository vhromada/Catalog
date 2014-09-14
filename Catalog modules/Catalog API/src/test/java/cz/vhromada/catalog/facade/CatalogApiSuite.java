package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.facade.to.ToSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for all tests.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(ToSuite.class)
public class CatalogApiSuite {
}
