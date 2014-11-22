package cz.vhromada.catalog.facade.impl.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.impl.spring.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieFacadeImplSpringTest.class, SerieFacadeImplSpringTest.class, SeasonFacadeImplSpringTest.class, EpisodeFacadeImplSpringTest.class,
        GameFacadeImplSpringTest.class, MusicFacadeImplSpringTest.class, SongFacadeImplSpringTest.class, ProgramFacadeImplSpringTest.class,
        BookCategoryFacadeImplSpringTest.class, BookFacadeImplSpringTest.class, GenreFacadeImplSpringTest.class })
public class FacadeImplSpringSuite {
}
