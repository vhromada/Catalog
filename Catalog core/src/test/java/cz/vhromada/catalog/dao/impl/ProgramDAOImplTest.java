package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link ProgramDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ProgramDAOImplTest extends ObjectGeneratorTest {

    /** Instance of {@link EntityManager} */
    @Mock
    private EntityManager entityManager;

    /** Query for programs */
    @Mock
    private TypedQuery<Program> programsQuery;

    /** Instance of {@link ProgramDAO} */
    private ProgramDAO programDAO;

    /** Initializes DAO for programs. */
    @Before
    public void setUp() {
        programDAO = new ProgramDAOImpl(entityManager);
    }

    /** Test method for {@link ProgramDAOImpl#ProgramDAOImpl(EntityManager)} with null entity manager. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullEntityManager() {
        new ProgramDAOImpl(null);
    }

    /** Test method for {@link ProgramDAO#getPrograms()}. */
    @Test
    public void testGetPrograms() {
        final List<Program> programs = CollectionUtils.newList(generate(Program.class), generate(Program.class));
        when(entityManager.createNamedQuery(anyString(), eq(Program.class))).thenReturn(programsQuery);
        when(programsQuery.getResultList()).thenReturn(programs);

        DeepAsserts.assertEquals(programs, programDAO.getPrograms());

        verify(entityManager).createNamedQuery(Program.SELECT_PROGRAMS, Program.class);
        verify(programsQuery).getResultList();
        verifyNoMoreInteractions(entityManager, programsQuery);
    }

    /** Test method for {@link ProgramDAO#getPrograms()} with exception in persistence. */
    @Test
    public void testGetProgramsWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Program.class));

        try {
            programDAO.getPrograms();
            fail("Can't get programs with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).createNamedQuery(Program.SELECT_PROGRAMS, Program.class);
        verifyNoMoreInteractions(entityManager);
        verifyZeroInteractions(programsQuery);
    }

    /** Test method for {@link ProgramDAO#getProgram(Integer)} with existing program. */
    @Test
    public void testGetProgramWithExistingProgram() {
        final int id = generate(Integer.class);
        final Program program = mock(Program.class);
        when(entityManager.find(eq(Program.class), anyInt())).thenReturn(program);

        DeepAsserts.assertEquals(program, programDAO.getProgram(id));

        verify(entityManager).find(Program.class, id);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#getProgram(Integer)} with not existing program. */
    @Test
    public void testGetProgramWithNotExistingProgram() {
        when(entityManager.find(eq(Program.class), anyInt())).thenReturn(null);

        assertNull(programDAO.getProgram(Integer.MAX_VALUE));

        verify(entityManager).find(Program.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#getProgram(Integer)} with null argument. */
    @Test
    public void testGetProgramWithNullArgument() {
        try {
            programDAO.getProgram(null);
            fail("Can't get program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#getProgram(Integer)} with exception in persistence. */
    @Test
    public void testGetProgramWithPersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Program.class), anyInt());

        try {
            programDAO.getProgram(Integer.MAX_VALUE);
            fail("Can't get program with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).find(Program.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#add(Program)}. */
    @Test
    public void testAdd() {
        final Program program = generate(Program.class);
        final int id = generate(Integer.class);
        doAnswer(setId(id)).when(entityManager).persist(any(Program.class));

        programDAO.add(program);
        DeepAsserts.assertEquals(id, program.getId());
        DeepAsserts.assertEquals(id - 1, program.getPosition());

        verify(entityManager).persist(program);
        verify(entityManager).merge(program);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#add(Program)} with null argument. */
    @Test
    public void testAddWithNullArgument() {
        try {
            programDAO.add(null);
            fail("Can't add program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#add(Program)} with exception in persistence. */
    @Test
    public void testAddWithPersistenceException() {
        final Program program = generate(Program.class);
        doThrow(PersistenceException.class).when(entityManager).persist(any(Program.class));

        try {
            programDAO.add(program);
            fail("Can't add program with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).persist(program);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#update(Program)}. */
    @Test
    public void testUpdate() {
        final Program program = generate(Program.class);

        programDAO.update(program);

        verify(entityManager).merge(program);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#update(Program)} with null argument. */
    @Test
    public void testUpdateWithNullArgument() {
        try {
            programDAO.update(null);
            fail("Can't update program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#update(Program)} with exception in persistence. */
    @Test
    public void testUpdateWithPersistenceException() {
        final Program program = generate(Program.class);
        doThrow(PersistenceException.class).when(entityManager).merge(any(Program.class));

        try {
            programDAO.update(program);
            fail("Can't update program with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).merge(program);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#remove(Program)} with managed program. */
    @Test
    public void testRemoveWithManagedProgram() {
        final Program program = generate(Program.class);
        when(entityManager.contains(any(Program.class))).thenReturn(true);

        programDAO.remove(program);

        verify(entityManager).contains(program);
        verify(entityManager).remove(program);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#remove(Program)} with not managed program. */
    @Test
    public void testRemoveWithNotManagedProgram() {
        final Program program = generate(Program.class);
        when(entityManager.contains(any(Program.class))).thenReturn(false);
        when(entityManager.getReference(eq(Program.class), anyInt())).thenReturn(program);

        programDAO.remove(program);

        verify(entityManager).contains(program);
        verify(entityManager).getReference(Program.class, program.getId());
        verify(entityManager).remove(program);
        verifyNoMoreInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#remove(Program)} with null argument. */
    @Test
    public void testRemoveWithNullArgument() {
        try {
            programDAO.remove(null);
            fail("Can't remove program with null argument.");
        } catch (final IllegalArgumentException ex) {
            // OK
        }

        verifyZeroInteractions(entityManager);
    }

    /** Test method for {@link ProgramDAO#remove(Program)} with exception in persistence. */
    @Test
    public void testRemoveWithPersistenceException() {
        final Program program = generate(Program.class);
        doThrow(PersistenceException.class).when(entityManager).contains(any(Program.class));

        try {
            programDAO.remove(program);
            fail("Can't remove program with not thrown DataStorageException for exception in persistence.");
        } catch (final DataStorageException ex) {
            // OK
        }

        verify(entityManager).contains(program);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                ((Program) invocation.getArguments()[0]).setId(id);
                return null;
            }

        };
    }

}
