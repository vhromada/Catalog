package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.ProgramUtils;
import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents integration test for class {@link ProgramDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
@Rollback
public class ProgramDAOImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    private EntityManager entityManager;

    /**
     * Instance of {@link ProgramDAO}
     */
    @Autowired
    private ProgramDAO programDAO;

    /**
     * Test method for {@link ProgramDAO#getPrograms()}.
     */
    @Test
    public void testGetPrograms() {
        final List<Program> programs = programDAO.getPrograms();

        ProgramUtils.assertProgramsDeepEquals(ProgramUtils.getPrograms(), programs);

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramDAO#getProgram(Integer)}.
     */
    @Test
    public void testGetProgram() {
        for (int i = 1; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            final Program program = programDAO.getProgram(i);

            assertNotNull(program);
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), program);
        }

        assertNull(programDAO.getProgram(Integer.MAX_VALUE));

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramDAO#add(Program)}.
     */
    @Test
    public void testAdd() {
        final Program program = ProgramUtils.newProgram(null);

        programDAO.add(program);

        assertNotNull(program.getId());
        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, program.getId().intValue());
        assertEquals(ProgramUtils.PROGRAMS_COUNT, program.getPosition());

        final Program addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(ProgramUtils.newProgram(ProgramUtils.PROGRAMS_COUNT + 1), addedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramDAO#update(Program)}.
     */
    @Test
    public void testUpdate() {
        final Program program = ProgramUtils.updateProgram(1, entityManager);

        programDAO.update(program);

        final Program updatedProgram = ProgramUtils.getProgram(entityManager, 1);
        final Program expectedUpdatedProgram = ProgramUtils.getProgram(1);
        ProgramUtils.updateProgram(expectedUpdatedProgram);
        expectedUpdatedProgram.setPosition(ProgramUtils.POSITION);
        ProgramUtils.assertProgramDeepEquals(expectedUpdatedProgram, updatedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramDAO#remove(Program)}.
     */
    @Test
    public void testRemove() {
        programDAO.remove(ProgramUtils.getProgram(entityManager, 1));

        assertNull(ProgramUtils.getProgram(entityManager, 1));

        assertEquals(ProgramUtils.PROGRAMS_COUNT - 1, ProgramUtils.getProgramsCount(entityManager));
    }

}
