package cz.vhromada.catalog.service.impl.spring;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.service.impl.spring.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieServiceImplSpringTest.class, SerieServiceImplSpringTest.class, SeasonServiceImplSpringTest.class, EpisodeServiceImplSpringTest.class,
        GameServiceImplSpringTest.class, MusicServiceImplSpringTest.class, SongServiceImplSpringTest.class, ProgramServiceImplSpringTest.class,
        BookCategoryServiceImplSpringTest.class, BookServiceImplSpringTest.class, GenreServiceImplSpringTest.class })
public class ServiceImplSpringSuite {
}
