package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.commons.ProgramUtils;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.repository.ProgramRepository;
import cz.vhromada.catalog.service.CatalogService;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A class represents test for class {@link ProgramServiceImpl}.
 *
 * @author Vladimir Hromada
 */
public class ProgramServiceImplTest extends AbstractServiceTest<Program> {

    /**
     * Instance of {@link ProgramRepository}
     */
    @Mock
    private ProgramRepository programRepository;

    /**
     * Test method for {@link ProgramServiceImpl#ProgramServiceImpl(ProgramRepository, Cache)} with null repository for programs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProgramDAO() {
        new ProgramServiceImpl(null, getCache());
    }

    /**
     * Test method for {@link ProgramServiceImpl#ProgramServiceImpl(ProgramRepository, Cache)} with null cache for programs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProgramCache() {
        new ProgramServiceImpl(programRepository, null);
    }

    @Override
    protected JpaRepository<Program, Integer> getRepository() {
        return programRepository;
    }

    @Override
    protected CatalogService<Program> getCatalogService() {
        return new ProgramServiceImpl(programRepository, getCache());
    }

    @Override
    protected String getCacheKey() {
        return "programs";
    }

    @Override
    protected Program getItem1() {
        return ProgramUtils.newProgram(1);
    }

    @Override
    protected Program getItem2() {
        return ProgramUtils.newProgram(2);
    }

    @Override
    protected Program getAddItem() {
        return ProgramUtils.newProgram(null);
    }

    @Override
    protected Program getCopyItem() {
        final Program program = ProgramUtils.newProgram(null);
        program.setPosition(0);

        return program;
    }

    @Override
    protected Class<Program> getItemClass() {
        return Program.class;
    }

    @Override
    protected void assertDataDeepEquals(final Program expected, final Program actual) {
        ProgramUtils.assertProgramDeepEquals(expected, actual);
    }

}
