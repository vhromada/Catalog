package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.commons.ProgramUtils;
import cz.vhromada.catalog.entities.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testFacadeContext.xml")
public class ProgramFacadeImplIntegrationTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private EntityManager entityManager;

    /**
     * Instance of {@link ProgramFacade}
     */
    @Autowired
    private ProgramFacade programFacade;

    /**
     * Test method for {@link ProgramFacade#newData()}.
     */
    @Test
    @DirtiesContext
    public void testNewData() {
        programFacade.newData();

        assertEquals(0, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#getPrograms()}.
     */
    @Test
    public void testGetPrograms() {
        final List<ProgramTO> programs = programFacade.getPrograms();

        ProgramUtils.assertProgramListDeepEquals(programs, ProgramUtils.getPrograms());

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#getProgram(Integer)}.
     */
    @Test
    public void testGetProgram() {
        for (int i = 1; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(programFacade.getProgram(i), ProgramUtils.getProgram(i));
        }

        assertNull(programFacade.getProgram(Integer.MAX_VALUE));

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#getProgram(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetProgram_NullArgument() {
        programFacade.getProgram(null);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        programFacade.add(ProgramUtils.newProgramTO(null));

        final Program addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(ProgramUtils.newProgramTO(ProgramUtils.PROGRAMS_COUNT + 1), addedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        programFacade.add(null);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with program with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotNullId() {
        programFacade.add(ProgramUtils.newProgramTO(1));
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullName() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setName(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_EmptyName() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setName("");

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with program with null URL to english Wikipedia about program.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullWikiEn() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setWikiEn(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with program with null URL to czech Wikipedia about program.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullWikiCz() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setWikiCz(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NotPositiveMediaCount() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setMediaCount(0);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullOtherData() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setOtherData(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_NullNote() {
        final ProgramTO program = ProgramUtils.newProgramTO(null);
        program.setNote(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);

        programFacade.update(program);

        final Program updatedProgram = ProgramUtils.getProgram(entityManager, 1);
        ProgramUtils.assertProgramDeepEquals(program, updatedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        programFacade.update(null);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullId() {
        programFacade.update(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with null name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullName() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setName(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_EmptyName() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setName(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with null URL to english Wikipedia about program.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullWikiEn() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setWikiEn(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with null URL to czech Wikipedia about program.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullWikiCz() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setWikiCz(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NotPositiveMediaCount() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setMediaCount(0);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullOtherData() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setOtherData(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with null note.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_NullNote() {
        final ProgramTO program = ProgramUtils.newProgramTO(1);
        program.setNote(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with program with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_BadId() {
        programFacade.update(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#remove(ProgramTO)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        programFacade.remove(ProgramUtils.newProgramTO(1));

        assertNull(ProgramUtils.getProgram(entityManager, 1));

        assertEquals(ProgramUtils.PROGRAMS_COUNT - 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#remove(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        programFacade.remove(null);
    }

    /**
     * Test method for {@link ProgramFacade#remove(ProgramTO)} with program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_NullId() {
        programFacade.remove(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#remove(ProgramTO)} with program with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_BadId() {
        programFacade.remove(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(ProgramTO)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final Program program = ProgramUtils.getProgram(ProgramUtils.PROGRAMS_COUNT);
        program.setId(ProgramUtils.PROGRAMS_COUNT + 1);

        programFacade.duplicate(ProgramUtils.newProgramTO(ProgramUtils.PROGRAMS_COUNT));

        final Program duplicatedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(program, duplicatedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        programFacade.duplicate(null);
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(ProgramTO)} with program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_NullId() {
        programFacade.duplicate(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(ProgramTO)} with program with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_BadId() {
        programFacade.duplicate(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final Program program1 = ProgramUtils.getProgram(1);
        program1.setPosition(1);
        final Program program2 = ProgramUtils.getProgram(2);
        program2.setPosition(0);

        programFacade.moveUp(ProgramUtils.newProgramTO(2));

        ProgramUtils.assertProgramDeepEquals(program1, ProgramUtils.getProgram(entityManager, 1));
        ProgramUtils.assertProgramDeepEquals(program2, ProgramUtils.getProgram(entityManager, 2));
        for (int i = 3; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), ProgramUtils.getProgram(entityManager, i));
        }

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        programFacade.moveUp(null);
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)} with program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NullId() {
        programFacade.moveUp(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        programFacade.moveUp(ProgramUtils.newProgramTO(1));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_BadId() {
        programFacade.moveUp(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final Program program1 = ProgramUtils.getProgram(1);
        program1.setPosition(1);
        final Program program2 = ProgramUtils.getProgram(2);
        program2.setPosition(0);

        programFacade.moveDown(ProgramUtils.newProgramTO(1));

        ProgramUtils.assertProgramDeepEquals(program1, ProgramUtils.getProgram(entityManager, 1));
        ProgramUtils.assertProgramDeepEquals(program2, ProgramUtils.getProgram(entityManager, 2));
        for (int i = 3; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), ProgramUtils.getProgram(entityManager, i));
        }

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        programFacade.moveDown(null);
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)} with program with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NullId() {
        programFacade.moveDown(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        programFacade.moveDown(ProgramUtils.newProgramTO(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)} with bad ID.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_BadId() {
        programFacade.moveDown(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#updatePositions()}.
     */
    @Test
    @DirtiesContext
    public void testUpdatePositions() {
        programFacade.updatePositions();

        for (int i = 1; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), ProgramUtils.getProgram(entityManager, i));
        }

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        assertEquals(600, programFacade.getTotalMediaCount());

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

}
