package cz.vhromada.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT);
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

        assertThat(programRepository.findById(Integer.MAX_VALUE).isPresent()).isFalse();

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT);
    }

    /**
     * Test method for add program.
     */
    @Test
    void add() {
        final Program program = ProgramUtils.newProgramDomain(null);

        programRepository.save(program);

        assertThat(program.getId()).isEqualTo(ProgramUtils.PROGRAMS_COUNT + 1);

        final Program addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        final Program expectedAddProgram = ProgramUtils.newProgramDomain(null);
        expectedAddProgram.setId(ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(expectedAddProgram, addedProgram);

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT + 1);
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

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT);
    }

    /**
     * Test method for remove program.
     */
    @Test
    void remove() {
        programRepository.delete(ProgramUtils.getProgram(entityManager, 1));

        assertThat(ProgramUtils.getProgram(entityManager, 1)).isNull();

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(ProgramUtils.PROGRAMS_COUNT - 1);
    }

    /**
     * Test method for remove all programs.
     */
    @Test
    void removeAll() {
        programRepository.deleteAll();

        assertThat(ProgramUtils.getProgramsCount(entityManager)).isEqualTo(0);
    }

}
