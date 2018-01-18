package cz.vhromada.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Program;
import cz.vhromada.catalog.utils.ProgramUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link ProgramRepository}.
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
class ProgramRepositoryIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link ProgramRepository}
     */
    @Autowired
    private ProgramRepository programRepository;

    /**
     * Test method for get programs.
     */
    @Test
    void getPrograms() {
        final List<Program> programs = programRepository.findAll();

        ProgramUtils.assertProgramsDeepEquals(ProgramUtils.getPrograms(), programs);

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for get program.
     */
    @Test
    void getProgram() {
        for (int i = 1; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            final Program program = programRepository.findById(i).orElse(null);

            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), program);
        }

        assertFalse(programRepository.findById(Integer.MAX_VALUE).isPresent());

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for add program.
     */
    @Test
    void add() {
        final Program program = ProgramUtils.newProgramDomain(null);

        programRepository.save(program);

        assertEquals(Integer.valueOf(ProgramUtils.PROGRAMS_COUNT + 1), program.getId());

        final Program addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        final Program expectedAddProgram = ProgramUtils.newProgramDomain(null);
        expectedAddProgram.setId(ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(expectedAddProgram, addedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for update program.
     */
    @Test
    void update() {
        final Program program = ProgramUtils.updateProgram(entityManager, 1);

        programRepository.save(program);

        final Program updatedProgram = ProgramUtils.getProgram(entityManager, 1);
        final Program expectedUpdatedProgram = ProgramUtils.getProgram(1);
        ProgramUtils.updateProgram(expectedUpdatedProgram);
        expectedUpdatedProgram.setPosition(ProgramUtils.POSITION);
        ProgramUtils.assertProgramDeepEquals(expectedUpdatedProgram, updatedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for remove program.
     */
    @Test
    void remove() {
        programRepository.delete(ProgramUtils.getProgram(entityManager, 1));

        assertNull(ProgramUtils.getProgram(entityManager, 1));

        assertEquals(ProgramUtils.PROGRAMS_COUNT - 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for remove all programs.
     */
    @Test
    void removeAll() {
        programRepository.deleteAll();

        assertEquals(0, ProgramUtils.getProgramsCount(entityManager));
    }

}
