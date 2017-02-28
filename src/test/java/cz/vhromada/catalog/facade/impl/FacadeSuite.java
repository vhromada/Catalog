package cz.vhromada.catalog.facade.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ AbstractCatalogParentFacadeTest.class, AbstractCatalogChildFacadeTest.class, MovieFacadeImplTest.class, ShowFacadeImplTest.class,
        SeasonFacadeImplTest.class, EpisodeFacadeImplTest.class, GameFacadeImplTest.class, MusicFacadeImplTest.class, SongFacadeImplTest.class,
        ProgramFacadeImplTest.class, GenreFacadeImplTest.class, MovieFacadeImplIntegrationTest.class, ShowFacadeImplIntegrationTest.class,
        SeasonFacadeImplIntegrationTest.class, EpisodeFacadeImplIntegrationTest.class, GameFacadeImplIntegrationTest.class,
        MusicFacadeImplIntegrationTest.class, SongFacadeImplIntegrationTest.class, ProgramFacadeImplIntegrationTest.class,
        GenreFacadeImplIntegrationTest.class })
public class FacadeSuite {
}
