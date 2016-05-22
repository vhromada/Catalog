package cz.vhromada.catalog.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.repository.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieRepositoryTest.class, ShowRepositoryTest.class, SeasonRepositoryTest.class, EpisodeRepositoryTest.class, GameRepositoryTest.class,
        MusicRepositoryTest.class, SongRepositoryTest.class, ProgramRepositoryTest.class, GenreRepositoryTest.class })
public class RepositorySuite {
}
