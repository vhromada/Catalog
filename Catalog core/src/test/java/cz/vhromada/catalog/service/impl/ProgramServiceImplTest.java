package cz.vhromada.catalog.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.ProgramService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * A class represents test for class {@link ProgramServiceImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ProgramServiceImplTest extends ObjectGeneratorTest {

    /** Cache key for list of programs */
    private static final String PROGRAMS_CACHE_KEY = "programs";

    /** Cache key for program */
    private static final String PROGRAM_CACHE_KEY = "program";

    /** Instance of {@link ProgramDAO} */
    @Mock
    private ProgramDAO programDAO;

    /** Instance of {@link Cache} */
    @Mock
    private Cache programCache;

    /** Instance of {@link ProgramService} */
    private ProgramService programService;

    /** Initializes service for programs. */
    @Before
    public void setUp() {
        programService = new ProgramServiceImpl(programDAO, programCache);
    }

    /** Test method for {@link ProgramServiceImpl#ProgramServiceImpl(ProgramDAO, Cache)} with null DAO for programs. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProgramDAO() {
        new ProgramServiceImpl(null, programCache);
    }

    /** Test method for {@link ProgramServiceImpl#ProgramServiceImpl(ProgramDAO, Cache)} with null cache for programs. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProgramCache() {
        new ProgramServiceImpl(programDAO, null);
    }

    /** Test method for {@link ProgramService#newData()} with cached programs. */
    @Test
    public void testNewDataWithCachedPrograms() {
        final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

        programService.newData();

        for (final Program program : programs) {
            verify(programDAO).remove(program);
        }
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#newData()} with not cached programs. */
    @Test
    public void testNewDataWithNotCachedPrograms() {
        final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
        when(programDAO.getPrograms()).thenReturn(programs);
        when(programCache.get(anyString())).thenReturn(null);

        programService.newData();

        verify(programDAO).getPrograms();
        for (final Program program : programs) {
            verify(programDAO).remove(program);
        }
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#newData()} with exception in DAO tier. */
    @Test
    public void testNewDataWithDAOTierException() {
        doThrow(DataStorageException.class).when(programDAO).getPrograms();
        when(programCache.get(anyString())).thenReturn(null);

        try {
            programService.newData();
            fail("Can't create new data with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#getPrograms()} with cached programs. */
    @Test
    public void testGetProgramsWithCachedPrograms() {
        final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

        DeepAsserts.assertEquals(programs, programService.getPrograms());

        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verifyNoMoreInteractions(programCache);
        verifyZeroInteractions(programDAO);
    }

    /** Test method for {@link ProgramService#getPrograms()} with not cached programs. */
    @Test
    public void testGetProgramsWithNotCachedPrograms() {
        final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
        when(programDAO.getPrograms()).thenReturn(programs);
        when(programCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(programs, programService.getPrograms());

        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).put(PROGRAMS_CACHE_KEY, programs);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#getPrograms()} with exception in DAO tier. */
    @Test
    public void testGetProgramsWithDAOTierException() {
        doThrow(DataStorageException.class).when(programDAO).getPrograms();
        when(programCache.get(anyString())).thenReturn(null);

        try {
            programService.getPrograms();
            fail("Can't get programs with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#getProgram(Integer)} with cached existing program. */
    @Test
    public void testGetProgramWithCachedExistingProgram() {
        final Program program = generate(Program.class);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(program));

        DeepAsserts.assertEquals(program, programService.getProgram(program.getId()));

        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verifyNoMoreInteractions(programCache);
        verifyZeroInteractions(programDAO);
    }

    /** Test method for {@link ProgramService#getProgram(Integer)} with cached not existing program. */
    @Test
    public void testGetProgramWithCachedNotExistingProgram() {
        final int id = generate(Integer.class);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertNull(programService.getProgram(id));

        verify(programCache).get(PROGRAM_CACHE_KEY + id);
        verifyNoMoreInteractions(programCache);
        verifyZeroInteractions(programDAO);
    }

    /** Test method for {@link ProgramService#getProgram(Integer)} with not cached existing program. */
    @Test
    public void testGetProgramWithNotCachedExistingProgram() {
        final Program program = generate(Program.class);
        when(programDAO.getProgram(anyInt())).thenReturn(program);
        when(programCache.get(anyString())).thenReturn(null);

        DeepAsserts.assertEquals(program, programService.getProgram(program.getId()));

        verify(programDAO).getProgram(program.getId());
        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verify(programCache).put(PROGRAM_CACHE_KEY + program.getId(), program);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#getProgram(Integer)} with not cached not existing program. */
    @Test
    public void testGetProgramWithNotCachedNotExistingProgram() {
        final int id = generate(Integer.class);
        when(programDAO.getProgram(anyInt())).thenReturn(null);
        when(programCache.get(anyString())).thenReturn(null);

        assertNull(programService.getProgram(id));

        verify(programDAO).getProgram(id);
        verify(programCache).get(PROGRAM_CACHE_KEY + id);
        verify(programCache).put(PROGRAM_CACHE_KEY + id, null);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#getProgram(Integer)} with null argument. */
    @Test
    public void testGetProgramWithNullArgument() {
        try {
            programService.getProgram(null);
            fail("Can't get program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#getProgram(Integer)} with exception in DAO tier. */
    @Test
    public void testGetProgramWithDAOTierException() {
        doThrow(DataStorageException.class).when(programDAO).getProgram(anyInt());
        when(programCache.get(anyString())).thenReturn(null);

        try {
            programService.getProgram(Integer.MAX_VALUE);
            fail("Can't get program with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).getProgram(Integer.MAX_VALUE);
        verify(programCache).get(PROGRAM_CACHE_KEY + Integer.MAX_VALUE);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#add(Program)} with cached programs. */
    @Test
    public void testAddWithCachedPrograms() {
        final Program program = generate(Program.class);
        final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
        final List<Program> programsList = new ArrayList<>(programs);
        programsList.add(program);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

        programService.add(program);

        verify(programDAO).add(program);
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verify(programCache).put(PROGRAMS_CACHE_KEY, programsList);
        verify(programCache).put(PROGRAM_CACHE_KEY + program.getId(), program);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#add(Program)} with not cached programs. */
    @Test
    public void testAddWithNotCachedPrograms() {
        final Program program = generate(Program.class);
        when(programCache.get(anyString())).thenReturn(null);

        programService.add(program);

        verify(programDAO).add(program);
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#add(Program)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        try {
            programService.add(null);
            fail("Can't add program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#add(Program)} with exception in DAO tier. */
    @Test
    public void testAddWithDAOTierException() {
        final Program program = generate(Program.class);
        doThrow(DataStorageException.class).when(programDAO).add(any(Program.class));

        try {
            programService.add(program);
            fail("Can't add program with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).add(program);
        verifyNoMoreInteractions(programDAO);
        verifyZeroInteractions(programCache);
    }

    /** Test method for {@link ProgramService#update(Program)}. */
    @Test
    public void testUpdate() {
        final Program program = generate(Program.class);

        programService.update(program);

        verify(programDAO).update(program);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#update(Program)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            programService.update(null);
            fail("Can't update program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#update(Program)} with exception in DAO tier. */
    @Test
    public void testUpdateWithDAOTierException() {
        final Program program = generate(Program.class);
        doThrow(DataStorageException.class).when(programDAO).update(any(Program.class));

        try {
            programService.update(program);
            fail("Can't update program with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).update(program);
        verifyNoMoreInteractions(programDAO);
        verifyZeroInteractions(programCache);
    }

    /** Test method for {@link ProgramService#remove(Program)} with cached programs. */
    @Test
    public void testRemoveWithCachedPrograms() {
        final Program program = generate(Program.class);
        final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
        final List<Program> programsList = new ArrayList<>(programs);
        programsList.add(program);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programsList));

        programService.remove(program);

        verify(programDAO).remove(program);
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).put(PROGRAMS_CACHE_KEY, programs);
        verify(programCache).evict(program.getId());
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#remove(Program)} with not cached programs. */
    @Test
    public void testRemoveWithNotCachedPrograms() {
        final Program program = generate(Program.class);
        when(programCache.get(anyString())).thenReturn(null);

        programService.remove(program);

        verify(programDAO).remove(program);
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).evict(program.getId());
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#remove(Program)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            programService.remove(null);
            fail("Can't remove program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#remove(Program)} with exception in DAO tier. */
    @Test
    public void testRemoveWithDAOTierException() {
        final Program program = generate(Program.class);
        doThrow(DataStorageException.class).when(programDAO).remove(any(Program.class));

        try {
            programService.remove(program);
            fail("Can't remove program with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).remove(program);
        verifyNoMoreInteractions(programDAO);
        verifyZeroInteractions(programCache);
    }

    /** Test method for {@link ProgramService#duplicate(Program)}. */
    @Test
    public void testDuplicate() {
        programService.duplicate(generate(Program.class));

        verify(programDAO).add(any(Program.class));
        verify(programDAO).update(any(Program.class));
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#duplicate(Program)} with null argument. */
    @Test
    public void testDuplicateWithNullArgument() {
        try {
            programService.duplicate(null);
            fail("Can't duplicate program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#duplicate(Program)} with exception in DAO tier. */
    @Test
    public void testDuplicateWithDAOTierException() {
        doThrow(DataStorageException.class).when(programDAO).add(any(Program.class));

        try {
            programService.duplicate(generate(Program.class));
            fail("Can't duplicate program with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).add(any(Program.class));
        verifyNoMoreInteractions(programDAO);
        verifyZeroInteractions(programCache);
    }

    /** Test method for {@link ProgramService#moveUp(Program)} with cached programs. */
    @Test
    public void testMoveUpWithCachedPrograms() {
        final Program program1 = generate(Program.class);
        final int position1 = program1.getPosition();
        final Program program2 = generate(Program.class);
        final int position2 = program2.getPosition();
        final List<Program> programs = CollectionUtils.newList(program1, program2);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

        programService.moveUp(program2);
        DeepAsserts.assertEquals(position2, program1.getPosition());
        DeepAsserts.assertEquals(position1, program2.getPosition());

        verify(programDAO).update(program1);
        verify(programDAO).update(program2);
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#moveUp(Program)} with not cached programs. */
    @Test
    public void testMoveUpWithNotCachedPrograms() {
        final Program program1 = generate(Program.class);
        final int position1 = program1.getPosition();
        final Program program2 = generate(Program.class);
        final int position2 = program2.getPosition();
        final List<Program> programs = CollectionUtils.newList(program1, program2);
        when(programDAO.getPrograms()).thenReturn(programs);
        when(programCache.get(anyString())).thenReturn(null);

        programService.moveUp(program2);
        DeepAsserts.assertEquals(position2, program1.getPosition());
        DeepAsserts.assertEquals(position1, program2.getPosition());

        verify(programDAO).update(program1);
        verify(programDAO).update(program2);
        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#moveUp(Program)} with null argument. */
    @Test
    public void testMoveUpWithNullArgument() {
        try {
            programService.moveUp(null);
            fail("Can't move up program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#moveUp(Program)} with exception in DAO tier. */
    @Test
    public void testMoveUpWithDAOTierException() {
        doThrow(DataStorageException.class).when(programDAO).getPrograms();
        when(programCache.get(anyString())).thenReturn(null);

        try {
            programService.moveUp(generate(Program.class));
            fail("Can't move up program with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#moveDown(Program)} with cached programs. */
    @Test
    public void testMoveDownWithCachedPrograms() {
        final Program program1 = generate(Program.class);
        final int position1 = program1.getPosition();
        final Program program2 = generate(Program.class);
        final int position2 = program2.getPosition();
        final List<Program> programs = CollectionUtils.newList(program1, program2);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

        programService.moveDown(program1);
        DeepAsserts.assertEquals(position2, program1.getPosition());
        DeepAsserts.assertEquals(position1, program2.getPosition());

        verify(programDAO).update(program1);
        verify(programDAO).update(program2);
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#moveDown(Program)} with not cached programs. */
    @Test
    public void testMoveDownWithNotCachedPrograms() {
        final Program program1 = generate(Program.class);
        final int position1 = program1.getPosition();
        final Program program2 = generate(Program.class);
        final int position2 = program2.getPosition();
        final List<Program> programs = CollectionUtils.newList(program1, program2);
        when(programDAO.getPrograms()).thenReturn(programs);
        when(programCache.get(anyString())).thenReturn(null);

        programService.moveDown(program1);
        DeepAsserts.assertEquals(position2, program1.getPosition());
        DeepAsserts.assertEquals(position1, program2.getPosition());

        verify(programDAO).update(program1);
        verify(programDAO).update(program2);
        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#moveDown(Program)} with null argument. */
    @Test
    public void testMoveDownWithNullArgument() {
        try {
            programService.moveDown(null);
            fail("Can't move down program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#moveDown(Program)} with exception in DAO tier. */
    @Test
    public void testMoveDownWithDAOTierException() {
        doThrow(DataStorageException.class).when(programDAO).getPrograms();
        when(programCache.get(anyString())).thenReturn(null);

        try {
            programService.moveDown(generate(Program.class));
            fail("Can't move down program with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#exists(Program)} with cached existing program. */
    @Test
    public void testExistsWithCachedExistingProgram() {
        final Program program = generate(Program.class);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(program));

        assertTrue(programService.exists(program));

        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verifyNoMoreInteractions(programCache);
        verifyZeroInteractions(programDAO);
    }

    /** Test method for {@link ProgramService#exists(Program)} with cached not existing program. */
    @Test
    public void testExistsWithCachedNotExistingProgram() {
        final Program program = generate(Program.class);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

        assertFalse(programService.exists(program));

        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verifyNoMoreInteractions(programCache);
        verifyZeroInteractions(programDAO);
    }

    /** Test method for {@link ProgramService#exists(Program)} with not cached existing program. */
    @Test
    public void testExistsWithNotCachedExistingProgram() {
        final Program program = generate(Program.class);
        when(programDAO.getProgram(anyInt())).thenReturn(program);
        when(programCache.get(anyString())).thenReturn(null);

        assertTrue(programService.exists(program));

        verify(programDAO).getProgram(program.getId());
        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verify(programCache).put(PROGRAM_CACHE_KEY + program.getId(), program);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#exists(Program)} with not cached not existing program. */
    @Test
    public void testExistsWithNotCachedNotExistingProgram() {
        final Program program = generate(Program.class);
        when(programDAO.getProgram(anyInt())).thenReturn(null);
        when(programCache.get(anyString())).thenReturn(null);

        assertFalse(programService.exists(program));

        verify(programDAO).getProgram(program.getId());
        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verify(programCache).put(PROGRAM_CACHE_KEY + program.getId(), null);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#exists(Program)} with null argument. */
    @Test
    public void testExistsWithNullArgument() {
        try {
            programService.exists(null);
            fail("Can't exists program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#exists(Program)} with exception in DAO tier. */
    @Test
    public void testExistsWithDAOTierException() {
        final Program program = generate(Program.class);
        doThrow(DataStorageException.class).when(programDAO).getProgram(anyInt());
        when(programCache.get(anyString())).thenReturn(null);

        try {
            programService.exists(program);
            fail("Can't exists program with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).getProgram(program.getId());
        verify(programCache).get(PROGRAM_CACHE_KEY + program.getId());
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#updatePositions()} with cached programs. */
    @Test
    public void testUpdatePositionsWithCachedPrograms() {
        final List<Program> programs = CollectionUtils.newList(generate(Program.class), generate(Program.class));
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

        programService.updatePositions();

        for (int i = 0; i < programs.size(); i++) {
            final Program program = programs.get(i);
            DeepAsserts.assertEquals(i, program.getPosition());
            verify(programDAO).update(program);
        }
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#updatePositions()} with not cached programs. */
    @Test
    public void testUpdatePositionsWithNotCachedPrograms() {
        final List<Program> programs = CollectionUtils.newList(generate(Program.class), generate(Program.class));
        when(programDAO.getPrograms()).thenReturn(programs);
        when(programCache.get(anyString())).thenReturn(null);

        programService.updatePositions();

        verify(programDAO).getPrograms();
        for (int i = 0; i < programs.size(); i++) {
            final Program program = programs.get(i);
            DeepAsserts.assertEquals(i, program.getPosition());
            verify(programDAO).update(program);
        }
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).clear();
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#updatePositions()} with exception in DAO tier. */
    @Test
    public void testUpdatePositionsWithDAOTierException() {
        doThrow(DataStorageException.class).when(programDAO).getPrograms();
        when(programCache.get(anyString())).thenReturn(null);

        try {
            programService.updatePositions();
            fail("Can't update positions with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verifyNoMoreInteractions(programDAO, programCache);
    }

    /** Test method for {@link ProgramService#getTotalMediaCount()} with cached programs. */
    @Test
    public void testGetTotalMediaCountWithCachedPrograms() {
        final Program program1 = mock(Program.class);
        final Program program2 = mock(Program.class);
        final Program program3 = mock(Program.class);
        final List<Program> programs = CollectionUtils.newList(program1, program2, program3);
        when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));
        when(program1.getMediaCount()).thenReturn(1);
        when(program2.getMediaCount()).thenReturn(2);
        when(program3.getMediaCount()).thenReturn(3);

        DeepAsserts.assertEquals(6, programService.getTotalMediaCount());

        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(program1).getMediaCount();
        verify(program2).getMediaCount();
        verify(program3).getMediaCount();
        verifyNoMoreInteractions(programCache, program1, program2, program3);
        verifyZeroInteractions(programDAO);
    }

    /** Test method for {@link ProgramService#getTotalMediaCount()} with not cached programs. */
    @Test
    public void testGetTotalMediaCountWithNotCachedPrograms() {
        final Program program1 = mock(Program.class);
        final Program program2 = mock(Program.class);
        final Program program3 = mock(Program.class);
        final List<Program> programs = CollectionUtils.newList(program1, program2, program3);
        when(programDAO.getPrograms()).thenReturn(programs);
        when(programCache.get(anyString())).thenReturn(null);
        when(program1.getMediaCount()).thenReturn(1);
        when(program2.getMediaCount()).thenReturn(2);
        when(program3.getMediaCount()).thenReturn(3);

        DeepAsserts.assertEquals(6, programService.getTotalMediaCount());

        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verify(programCache).put(PROGRAMS_CACHE_KEY, programs);
        verify(program1).getMediaCount();
        verify(program2).getMediaCount();
        verify(program3).getMediaCount();
        verifyNoMoreInteractions(programDAO, programCache, program1, program2, program3);
    }

    /** Test method for {@link ProgramService#getTotalMediaCount()} with exception in DAO tier. */
    @Test
    public void testGetTotalMediaCountWithDAOTierException() {
        doThrow(DataStorageException.class).when(programDAO).getPrograms();
        when(programCache.get(anyString())).thenReturn(null);

        try {
            programService.getTotalMediaCount();
            fail("Can't get total media count with not thrown ServiceOperationException for DAO tier exception.");
        } catch (final ServiceOperationException ex) {
            // OK
        }

        verify(programDAO).getPrograms();
        verify(programCache).get(PROGRAMS_CACHE_KEY);
        verifyNoMoreInteractions(programDAO, programCache);
    }

}
