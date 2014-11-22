package cz.vhromada.catalog.dao.impl.spring;

import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.SpringEntitiesUtils;
import cz.vhromada.catalog.commons.SpringUtils;
import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.generator.ObjectGenerator;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents test for class {@link cz.vhromada.catalog.dao.impl.ProgramDAOImpl} with Spring framework.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testDAOContext.xml")
@Transactional
public class ProgramDAOImplSpringTest {

    /** Instance of {@link EntityManager} */
    @Autowired
    private EntityManager entityManager;

    /** Instance of {@link ProgramDAO} */
    @Autowired
    private ProgramDAO programDAO;

    /** Instance of {@link ObjectGenerator} */
    @Autowired
    private ObjectGenerator objectGenerator;

    /** Restarts sequence. */
    @Before
    public void setUp() {
        entityManager.createNativeQuery("ALTER SEQUENCE programs_sq RESTART WITH 4").executeUpdate();
    }

    /** Test method for {@link ProgramDAO#getPrograms()}. */
    @Test
    public void testGetPrograms() {
        DeepAsserts.assertEquals(SpringEntitiesUtils.getPrograms(), programDAO.getPrograms());
        DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
    }

    /** Test method for {@link ProgramDAO#getProgram(Integer)}. */
    @Test
    public void testGetProgram() {
        for (int i = 1; i <= SpringUtils.PROGRAMS_COUNT; i++) {
            DeepAsserts.assertEquals(SpringEntitiesUtils.getProgram(i), programDAO.getProgram(i));
        }

        assertNull(programDAO.getProgram(Integer.MAX_VALUE));

        DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
    }

    /** Test method for {@link ProgramDAO#add(Program)}. */
    @Test
    public void testAdd() {
        final Program program = SpringEntitiesUtils.newProgram(objectGenerator);

        programDAO.add(program);

        DeepAsserts.assertNotNull(program.getId());
        DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT + 1, program.getId());
        DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, program.getPosition());
        final Program addedProgram = SpringUtils.getProgram(entityManager, SpringUtils.PROGRAMS_COUNT + 1);
        DeepAsserts.assertEquals(program, addedProgram);
        DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT + 1, SpringUtils.getProgramsCount(entityManager));
    }

    /** Test method for {@link ProgramDAO#update(Program)}. */
    @Test
    public void testUpdate() {
        final Program program = SpringEntitiesUtils.updateProgram(1, objectGenerator, entityManager);

        programDAO.update(program);

        final Program updatedProgram = SpringUtils.getProgram(entityManager, 1);
        DeepAsserts.assertEquals(program, updatedProgram);
        DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT, SpringUtils.getProgramsCount(entityManager));
    }

    /** Test method for {@link ProgramDAO#remove(Program)}. */
    @Test
    public void testRemove() {
        programDAO.remove(SpringUtils.getProgram(entityManager, 1));

        assertNull(SpringUtils.getProgram(entityManager, 1));
        DeepAsserts.assertEquals(SpringUtils.PROGRAMS_COUNT - 1, SpringUtils.getProgramsCount(entityManager));
    }

}
