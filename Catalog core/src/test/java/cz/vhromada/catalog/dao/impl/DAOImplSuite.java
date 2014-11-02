package cz.vhromada.catalog.dao.impl;

import cz.vhromada.catalog.dao.impl.spring.DAOImplSpringSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.dao.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieDAOImplTest.class, SerieDAOImplTest.class, SeasonDAOImplTest.class, EpisodeDAOImplTest.class, GameDAOImplTest.class,
		MusicDAOImplTest.class, SongDAOImplTest.class, ProgramDAOImplTest.class, BookCategoryDAOImplTest.class, BookDAOImplTest.class, GenreDAOImplTest.class,
		DAOImplSpringSuite.class })
public class DAOImplSuite {
}
