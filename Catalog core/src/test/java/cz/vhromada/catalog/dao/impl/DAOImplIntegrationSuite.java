package cz.vhromada.catalog.dao.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents integration test suite for package cz.vhromada.catalog.dao.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieDAOImplIntegrationTest.class, GameDAOImplIntegrationTest.class, ProgramDAOImplIntegrationTest.class,
        GenreDAOImplIntegrationTest.class })
public class DAOImplIntegrationSuite {
}
