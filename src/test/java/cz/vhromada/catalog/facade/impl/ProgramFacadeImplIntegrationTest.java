package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.persistence.EntityManager;

import cz.vhromada.catalog.CatalogConfiguration;
import cz.vhromada.catalog.CatalogTestConfiguration;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.utils.ProgramUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents integration test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CatalogConfiguration.class, CatalogTestConfiguration.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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
        final List<Program> programs = programFacade.getPrograms();

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
     * Test method for {@link ProgramFacade#add(Program)}.
     */
    @Test
    @DirtiesContext
    public void testAdd() {
        programFacade.add(ProgramUtils.newProgram(null));

        final cz.vhromada.catalog.domain.Program addedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(ProgramUtils.newProgramDomain(ProgramUtils.PROGRAMS_COUNT + 1), addedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        programFacade.add(null);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotNullId() {
        programFacade.add(ProgramUtils.newProgram(1));
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullName() {
        final Program program = ProgramUtils.newProgram(null);
        program.setName(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_EmptyName() {
        final Program program = ProgramUtils.newProgram(null);
        program.setName("");

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null URL to english Wikipedia about program.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullWikiEn() {
        final Program program = ProgramUtils.newProgram(null);
        program.setWikiEn(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null URL to czech Wikipedia about program.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullWikiCz() {
        final Program program = ProgramUtils.newProgram(null);
        program.setWikiCz(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NotPositiveMediaCount() {
        final Program program = ProgramUtils.newProgram(null);
        program.setMediaCount(0);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null other data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullOtherData() {
        final Program program = ProgramUtils.newProgram(null);
        program.setOtherData(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with program with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullNote() {
        final Program program = ProgramUtils.newProgram(null);
        program.setNote(null);

        programFacade.add(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)}.
     */
    @Test
    @DirtiesContext
    public void testUpdate() {
        final Program program = ProgramUtils.newProgram(1);

        programFacade.update(program);

        final cz.vhromada.catalog.domain.Program updatedProgram = ProgramUtils.getProgram(entityManager, 1);
        ProgramUtils.assertProgramDeepEquals(program, updatedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        programFacade.update(null);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullId() {
        programFacade.update(ProgramUtils.newProgram(null));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullName() {
        final Program program = ProgramUtils.newProgram(1);
        program.setName(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_EmptyName() {
        final Program program = ProgramUtils.newProgram(1);
        program.setName(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null URL to english Wikipedia about program.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullWikiEn() {
        final Program program = ProgramUtils.newProgram(1);
        program.setWikiEn(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null URL to czech Wikipedia about program.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullWikiCz() {
        final Program program = ProgramUtils.newProgram(1);
        program.setWikiCz(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NotPositiveMediaCount() {
        final Program program = ProgramUtils.newProgram(1);
        program.setMediaCount(0);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null other data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullOtherData() {
        final Program program = ProgramUtils.newProgram(1);
        program.setOtherData(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullNote() {
        final Program program = ProgramUtils.newProgram(1);
        program.setNote(null);

        programFacade.update(program);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with program with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_BadId() {
        programFacade.update(ProgramUtils.newProgram(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#remove(Program)}.
     */
    @Test
    @DirtiesContext
    public void testRemove() {
        programFacade.remove(ProgramUtils.newProgram(1));

        assertNull(ProgramUtils.getProgram(entityManager, 1));

        assertEquals(ProgramUtils.PROGRAMS_COUNT - 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#remove(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        programFacade.remove(null);
    }

    /**
     * Test method for {@link ProgramFacade#remove(Program)} with program with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullId() {
        programFacade.remove(ProgramUtils.newProgram(null));
    }

    /**
     * Test method for {@link ProgramFacade#remove(Program)} with program with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_BadId() {
        programFacade.remove(ProgramUtils.newProgram(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(Program)}.
     */
    @Test
    @DirtiesContext
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Program program = ProgramUtils.getProgram(ProgramUtils.PROGRAMS_COUNT);
        program.setId(ProgramUtils.PROGRAMS_COUNT + 1);

        programFacade.duplicate(ProgramUtils.newProgram(ProgramUtils.PROGRAMS_COUNT));

        final cz.vhromada.catalog.domain.Program duplicatedProgram = ProgramUtils.getProgram(entityManager, ProgramUtils.PROGRAMS_COUNT + 1);
        ProgramUtils.assertProgramDeepEquals(program, duplicatedProgram);

        assertEquals(ProgramUtils.PROGRAMS_COUNT + 1, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        programFacade.duplicate(null);
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(Program)} with program with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullId() {
        programFacade.duplicate(ProgramUtils.newProgram(null));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(Program)} with program with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_BadId() {
        programFacade.duplicate(ProgramUtils.newProgram(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)}.
     */
    @Test
    @DirtiesContext
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Program program1 = ProgramUtils.getProgram(1);
        program1.setPosition(1);
        final cz.vhromada.catalog.domain.Program program2 = ProgramUtils.getProgram(2);
        program2.setPosition(0);

        programFacade.moveUp(ProgramUtils.newProgram(2));

        ProgramUtils.assertProgramDeepEquals(program1, ProgramUtils.getProgram(entityManager, 1));
        ProgramUtils.assertProgramDeepEquals(program2, ProgramUtils.getProgram(entityManager, 2));
        for (int i = 3; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), ProgramUtils.getProgram(entityManager, i));
        }

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        programFacade.moveUp(null);
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)} with program with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullId() {
        programFacade.moveUp(ProgramUtils.newProgram(null));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NotMovableArgument() {
        programFacade.moveUp(ProgramUtils.newProgram(1));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_BadId() {
        programFacade.moveUp(ProgramUtils.newProgram(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)}.
     */
    @Test
    @DirtiesContext
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Program program1 = ProgramUtils.getProgram(1);
        program1.setPosition(1);
        final cz.vhromada.catalog.domain.Program program2 = ProgramUtils.getProgram(2);
        program2.setPosition(0);

        programFacade.moveDown(ProgramUtils.newProgram(1));

        ProgramUtils.assertProgramDeepEquals(program1, ProgramUtils.getProgram(entityManager, 1));
        ProgramUtils.assertProgramDeepEquals(program2, ProgramUtils.getProgram(entityManager, 2));
        for (int i = 3; i <= ProgramUtils.PROGRAMS_COUNT; i++) {
            ProgramUtils.assertProgramDeepEquals(ProgramUtils.getProgram(i), ProgramUtils.getProgram(entityManager, i));
        }

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        programFacade.moveDown(null);
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)} with program with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullId() {
        programFacade.moveDown(ProgramUtils.newProgram(null));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)} with not movable argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NotMovableArgument() {
        programFacade.moveDown(ProgramUtils.newProgram(ProgramUtils.PROGRAMS_COUNT));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)} with bad ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_BadId() {
        programFacade.moveDown(ProgramUtils.newProgram(Integer.MAX_VALUE));
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
        final int count = 600;

        assertEquals(count, programFacade.getTotalMediaCount());

        assertEquals(ProgramUtils.PROGRAMS_COUNT, ProgramUtils.getProgramsCount(entityManager));
    }

}
