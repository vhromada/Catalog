package cz.vhromada.catalog.facade;

import cz.vhromada.catalog.facade.impl.FacadeImplSuite;
import cz.vhromada.catalog.facade.validators.impl.ValidatorsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ FacadeImplSuite.class, ValidatorsSuite.class })
public class FacadeSuite {
}
