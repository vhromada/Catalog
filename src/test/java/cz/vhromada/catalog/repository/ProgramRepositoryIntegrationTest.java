package cz.vhromada.catalog.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.domain.Program;
import cz.vhromada.catalog.utils.ProgramUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link ProgramRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CatalogTestConfiguration.class)
@Transactional
@Rollback
public class ProgramRepositoryIntegrationTest {

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
    public void getPrograms() {
        final List<Program> programs = programRepository.findAll(new Sort("position", "id"));

        ProgramUtils.assertProgramsDeepEquals(ProgramUtils.getPrograms(), programs);

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for get program.
     */
    @Test
    public void getProgram() {
        for (int i = 1; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            final Program program = programRepository.findOne(i);

            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), program);
        }

        assertThat(programRepository.findOne(Integer.MAX_VALUE), is(nullValue()));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for add program.
     */
    @Test
    public void add() {
        final Program program = ProgramUtils.newProgramDomain(null);

        programRepository.saveAndFlush(program);

        assertThat(program.getId(), is(notNullValue()));
        assertThat(program.getId(), is(ProgramUtils.PROGRAMS_COUNT + 1));

        final Program addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        final Program expectedAddProgram = ProgramUtils.newProgramDomain(null);
        expectedAddProgram.setId(ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(expectedAddProgram, addedProgram);

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT + 1));
    }

    /**
     * Test method for update program.
     */
    @Test
    public void update() {
        final Program program = ProgramUtils.updateProgram(entityManager, 1);

        programRepository.saveAndFlush(program);

        final Program updatedProgram = ProgramUtils.getProgram(entityManager, 1);
        final Program expectedUpdatedProgram = ProgramUtils.getProgram(1);
        ProgramUtils.updateProgram(expectedUpdatedProgram);
        expectedUpdatedProgram.setPosition(ProgramUtils.POSITION);
        ProgramUtils.assertProgramDeepEquals(expectedUpdatedProgram, updatedProgram);

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for remove program.
     */
    @Test
    public void remove() {
        programRepository.delete(ProgramUtils.getProgram(entityManager, 1));

        assertThat(ProgramUtils.getProgram(entityManager, 1), is(nullValue()));

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(ProgramUtils.PROGRAMS_COUNT - 1));
    }

    /**
     * Test method for remove all programs.
     */
    @Test
    public void removeAll() {
        programRepository.deleteAll();

        assertThat(ProgramUtils.getProgramsCount(entityManager), is(0));
    }

}
