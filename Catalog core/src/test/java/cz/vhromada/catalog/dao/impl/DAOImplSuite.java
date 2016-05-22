package cz.vhromada.catalog.dao.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.dao.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieDAOImplTest.class, GameDAOImplTest.class, MusicDAOImplTest.class, SongDAOImplTest.class, ProgramDAOImplTest.class,
        GenreDAOImplTest.class })
public class DAOImplSuite {
}
