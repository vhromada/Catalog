package cz.vhromada.catalog.facade.validators.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.facade.validators.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ MovieTOValidatorImplTest.class, SerieTOValidatorImplTest.class, SeasonTOValidatorImplTest.class, EpisodeTOValidatorImplTest.class,
		GameTOValidatorImplTest.class, MusicTOValidatorImplTest.class, SongTOValidatorImplTest.class, ProgramTOValidatorImplTest.class,
		BookTOValidatorImplTest.class })
public class ValidatorsSuite {
}
