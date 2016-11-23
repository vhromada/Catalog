package cz.vhromada.catalog.service.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.catalog.service.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ AbstractCatalogServiceTest.class, MovieServiceImplTest.class, ShowServiceImplTest.class, GameServiceImplTest.class,
        MusicServiceImplTest.class, ProgramServiceImplTest.class, GenreServiceImplTest.class })
public class ServiceImplSuite {
}
