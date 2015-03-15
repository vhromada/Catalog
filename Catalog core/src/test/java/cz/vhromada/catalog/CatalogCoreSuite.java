package cz.vhromada.catalog;

import cz.vhromada.catalog.dao.impl.DAOImplSuite;
import cz.vhromada.catalog.facade.FacadeSuite;
import cz.vhromada.catalog.service.impl.ServiceImplSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for all tests.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ DAOImplSuite.class, ServiceImplSuite.class, FacadeSuite.class })
public class CatalogCoreSuite {
}
