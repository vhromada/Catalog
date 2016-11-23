package cz.vhromada.catalog.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.repository.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieRepositoryIntegrationTest.class, ShowRepositoryIntegrationTest.class, GameRepositoryIntegrationTest.class,
        MusicRepositoryIntegrationTest.class, ProgramRepositoryIntegrationTest.class, GenreRepositoryIntegrationTest.class })
public class RepositorySuite {
}
