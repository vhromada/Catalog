package cz.vhromada.catalog.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.repository.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieRepositoryIntegrationTest.class, ShowRepositoryIntegrationTest.class, SeasonRepositoryIntegrationTest.class,
        EpisodeRepositoryIntegrationTest.class, GameRepositoryIntegrationTest.class, MusicRepositoryIntegrationTest.class, SongRepositoryIntegrationTest.class,
        ProgramRepositoryIntegrationTest.class, GenreRepositoryIntegrationTest.class })
public class RepositorySuite {
}
