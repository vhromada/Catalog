package cz.vhromada.catalog.validators.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.validators.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieTOValidatorImplTest.class, ShowTOValidatorImplTest.class, SeasonTOValidatorImplTest.class, EpisodeTOValidatorImplTest.class,
        GameTOValidatorImplTest.class, MusicTOValidatorImplTest.class, SongTOValidatorImplTest.class, ProgramTOValidatorImplTest.class,
        GenreTOValidatorImplTest.class })
public class ValidatorsSuite {
}
