package cz.vhromada.catalog.facade.to;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.service.to.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieTOTest.class, SerieTOTest.class, SeasonTOTest.class, GameTOTest.class, ProgramTOTest.class })
public class ToSuite {
}
