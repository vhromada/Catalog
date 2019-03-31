package cz.vhromada.catalog.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.vhromada.catalog.domain.Program;
import cz.vhromada.catalog.repository.ProgramRepository;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.common.repository.MovableRepository;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.service.MovableServiceTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.Cache;

/**
 * A class represents test for class {@link ProgramService}.
 *
 * @author Vladimir Hromada
 */
class ProgramServiceTest extends MovableServiceTest<Program> {

    /**
     * Instance of {@link ProgramRepository}
     */
    @Mock
    private ProgramRepository repository;

    /**
     * Test method for {@link ProgramService#ProgramService(ProgramRepository, Cache)} with null repository for programs.
     */
    @Test
    void constructor_NullProgramRepository() {
        assertThatThrownBy(() -> new ProgramService(null, getCache())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ProgramService#ProgramService(ProgramRepository, Cache)} with null cache.
     */
    @Test
    void constructor_NullCache() {
        assertThatThrownBy(() -> new ProgramService(repository, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Override
    protected MovableRepository<Program> getRepository() {
        return repository;
    }

    @Override
    protected MovableService<Program> getService() {
        return new ProgramService(repository, getCache());
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
