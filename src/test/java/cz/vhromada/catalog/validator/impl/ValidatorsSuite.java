package cz.vhromada.catalog.validator.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.validator.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ AbstractCatalogValidatorTest.class, MovieValidatorImplTest.class, ShowValidatorImplTest.class, SeasonValidatorImplTest.class,
        EpisodeValidatorImplTest.class, GameValidatorImplTest.class, MusicValidatorImplTest.class, SongValidatorImplTest.class, ProgramValidatorImplTest.class,
        GenreValidatorImplTest.class })
public class ValidatorsSuite {
}
