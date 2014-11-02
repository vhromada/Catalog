package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.facade.impl.spring.FacadeImplSpringSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieFacadeImplTest.class, SerieFacadeImplTest.class, SeasonFacadeImplTest.class, EpisodeFacadeImplTest.class, GameFacadeImplTest.class,
		MusicFacadeImplTest.class, SongFacadeImplTest.class, ProgramFacadeImplTest.class, BookCategoryFacadeImplTest.class, BookFacadeImplTest.class,
		GenreFacadeImplTest.class, FacadeImplSpringSuite.class })
public class FacadeImplSuite {
}
