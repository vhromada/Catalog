package cz.vhromada.catalog.facade.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ EpisodeFacadeImplTest.class, GameFacadeImplTest.class, SongFacadeImplTest.class, ProgramFacadeImplTest.class, GenreFacadeImplTest.class })
public class FacadeImplSuite {
}
