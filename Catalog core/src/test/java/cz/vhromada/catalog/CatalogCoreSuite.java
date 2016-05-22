package cz.vhromada.catalog;

import cz.vhromada.catalog.repository.RepositorySuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//import cz.vhromada.catalog.facade.FacadeSuite;
//import cz.vhromada.catalog.service.impl.ServiceImplSuite;

/**
 * A class represents test suite for all tests.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ RepositorySuite.class/*, ServiceImplSuite.class, FacadeSuite.class*/ })
public class CatalogCoreSuite {
}
