package cz.vhromada.catalog.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.ProgramUtils;
import cz.vhromada.catalog.dao.entities.Program;

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
 * A class represents test for class {@link ProgramRepository}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testRepositoryContext.xml")
@Transactional
@Rollback
public class ProgramRepositoryTest {

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
    public void testGetPrograms() {
        final List<Program> programs = programRepository.findAll(new Sort("position", "id"));

        ProgramUtils.assertProgramsDeepEquals(ProgramUtils.getPrograms(), programs);

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for get program.
     */
    @Test
    public void testGetProgram() {
        for (int i = 1; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            final Program program = programRepository.findOne(i);

            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), program);
        }

        assertNull(programRepository.findOne(Integer.MAX_VALUE));

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for add program.
     */
    @Test
    public void testAdd() {
        final Program program = ProgramUtils.newProgram(null);

        programRepository.saveAndFlush(program);

        assertNotNull(program.getId());
        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, program.getId().intValue());

        final Program addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        final Program expectedAddProgram = ProgramUtils.newProgram(null);
        expectedAddProgram.setId(ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(expectedAddProgram, addedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for update program.
     */
    @Test
    public void testUpdate() {
        final Program program = ProgramUtils.updateProgram(entityManager, 1);

        programRepository.saveAndFlush(program);

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
    public void testRemove() {
        programRepository.delete(ProgramUtils.getProgram(entityManager, 1));

        assertNull(ProgramUtils.getProgram(entityManager, 1));

        assertEquals(ProgramUtils.PROGRAMS_COUNT - 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for remove all programs.
     */
    @Test
//    @Ignore
    public void testRemoveAll() {
        programRepository.deleteAllInBatch();

        assertEquals(0, ProgramUtils.getProgramsCount(entityManager));
    }

}
