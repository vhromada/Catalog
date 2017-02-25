package cz.vhromada.catalog.service.impl;

import cz.vhromada.catalog.domain.Program;
import cz.vhromada.catalog.repository.ProgramRepository;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.ProgramUtils;

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
    public void constructor_NullProgramRepository() {
        new ProgramServiceImpl(null, getCache());
    }

    /**
     * Test method for {@link ProgramServiceImpl#ProgramServiceImpl(ProgramRepository, Cache)} with null cache.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullCache() {
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
        return ProgramUtils.newProgramDomain(1);
    }

    @Override
    protected Program getItem2() {
        return ProgramUtils.newProgramDomain(2);
    }

    @Override
    protected Program getAddItem() {
        return ProgramUtils.newProgramDomain(null);
    }

    @Override
    protected Program getCopyItem() {
        final Program program = ProgramUtils.newProgramDomain(null);
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
