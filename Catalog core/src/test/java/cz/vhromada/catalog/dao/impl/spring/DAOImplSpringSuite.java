package cz.vhromada.catalog.dao.impl.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.dao.impl.spring.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieDAOImplSpringTest.class, ShowDAOImplSpringTest.class, SeasonDAOImplSpringTest.class, EpisodeDAOImplSpringTest.class,
        GameDAOImplSpringTest.class, MusicDAOImplSpringTest.class, SongDAOImplSpringTest.class, ProgramDAOImplSpringTest.class,
        BookCategoryDAOImplSpringTest.class, BookDAOImplSpringTest.class, GenreDAOImplSpringTest.class })
public class DAOImplSpringSuite {
}
