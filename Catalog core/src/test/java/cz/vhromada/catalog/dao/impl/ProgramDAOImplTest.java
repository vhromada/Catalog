package cz.vhromada.catalog.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ProgramUtils;
import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * A class represents test for class {@link ProgramDAOImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ProgramDAOImplTest {

    /**
     * Instance of {@link EntityManager}
     */
    @Mock
    private EntityManager entityManager;

    /**
     * Query for programs
     */
    @Mock
    private TypedQuery<Program> programsQuery;

    /**
     * Instance of {@link ProgramDAO}
     */
    private ProgramDAO programDAO;

    /**
     * Initializes DAO for programs.
     */
    @Before
    public void setUp() {
        programDAO = new ProgramDAOImpl(entityManager);
    }

    /**
     * Test method for {@link ProgramDAOImpl#ProgramDAOImpl(EntityManager)} with null entity manager.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullEntityManager() {
        new ProgramDAOImpl(null);
    }

    /**
     * Test method for {@link ProgramDAO#getPrograms()}.
     */
    @Test
    public void testGetPrograms() {
        when(entityManager.createNamedQuery(anyString(), eq(Program.class))).thenReturn(programsQuery);
        when(programsQuery.getResultList()).thenReturn(CollectionUtils.newList(ProgramUtils.newProgram(ProgramUtils.ID), ProgramUtils.newProgram(2)));

        final List<Program> programs = programDAO.getPrograms();

        ProgramUtils.assertProgramsDeepEquals(CollectionUtils.newList(ProgramUtils.newProgram(ProgramUtils.ID), ProgramUtils.newProgram(2)), programs);

        verify(entityManager).createNamedQuery(Program.SELECT_PROGRAMS, Program.class);
        verify(programsQuery).getResultList();
        verifyNoMoreInteractions(entityManager, programsQuery);
    }

    /**
     * Test method for {@link ProgramDAO#getPrograms()} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetPrograms_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).createNamedQuery(anyString(), eq(Program.class));

        programDAO.getPrograms();
    }

    /**
     * Test method for {@link ProgramDAO#getProgram(Integer)} with existing program.
     */
    @Test
    public void testGetProgram_ExistingProgram() {
        when(entityManager.find(eq(Program.class), anyInt())).thenReturn(ProgramUtils.newProgram(ProgramUtils.ID));

        final Program program = programDAO.getProgram(ProgramUtils.ID);

        ProgramUtils.assertProgramDeepEquals(ProgramUtils.newProgram(ProgramUtils.ID), program);

        verify(entityManager).find(Program.class, ProgramUtils.ID);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ProgramDAO#getProgram(Integer)} with not existing program.
     */
    @Test
    public void testGetProgram_NotExistingProgram() {
        when(entityManager.find(eq(Program.class), anyInt())).thenReturn(null);

        final Program program = programDAO.getProgram(Integer.MAX_VALUE);

        assertNull(program);

        verify(entityManager).find(Program.class, Integer.MAX_VALUE);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ProgramDAO#getProgram(Integer)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetProgram_NullArgument() {
        programDAO.getProgram(null);
    }

    /**
     * Test method for {@link ProgramDAO#getProgram(Integer)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testGetProgram_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).find(eq(Program.class), anyInt());

        programDAO.getProgram(Integer.MAX_VALUE);
    }

    /**
     * Test method for {@link ProgramDAO#add(Program)}.
     */
    @Test
    public void testAdd() {
        final Program program = ProgramUtils.newProgram(ProgramUtils.ID);
        doAnswer(setId(ProgramUtils.ID)).when(entityManager).persist(any(Program.class));

        programDAO.add(program);

        assertEquals(ProgramUtils.ID, program.getId());
        assertEquals(ProgramUtils.ID - 1, program.getPosition());

        verify(entityManager).persist(program);
        verify(entityManager).merge(program);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ProgramDAO#add(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        programDAO.add(null);
    }

    /**
     * Test method for {@link ProgramDAO#add(Program)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testAdd_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).persist(any(Program.class));

        programDAO.add(ProgramUtils.newProgram(ProgramUtils.ID));
    }

    /**
     * Test method for {@link ProgramDAO#update(Program)}.
     */
    @Test
    public void testUpdate() {
        final Program program = ProgramUtils.newProgram(ProgramUtils.ID);

        programDAO.update(program);

        verify(entityManager).merge(program);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ProgramDAO#update(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        programDAO.update(null);
    }

    /**
     * Test method for {@link ProgramDAO#update(Program)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testUpdate_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).merge(any(Program.class));

        programDAO.update(ProgramUtils.newProgram(ProgramUtils.ID));
    }

    /**
     * Test method for {@link ProgramDAO#remove(Program)} with managed program.
     */
    @Test
    public void testRemove_ManagedProgram() {
        final Program program = ProgramUtils.newProgram(ProgramUtils.ID);
        when(entityManager.contains(any(Program.class))).thenReturn(true);

        programDAO.remove(program);

        verify(entityManager).contains(program);
        verify(entityManager).remove(program);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ProgramDAO#remove(Program)} with not managed program.
     */
    @Test
    public void testRemove_NotManagedProgram() {
        final Program program = ProgramUtils.newProgram(ProgramUtils.ID);
        when(entityManager.contains(any(Program.class))).thenReturn(false);
        when(entityManager.getReference(eq(Program.class), anyInt())).thenReturn(program);

        programDAO.remove(program);

        verify(entityManager).contains(program);
        verify(entityManager).getReference(Program.class, program.getId());
        verify(entityManager).remove(program);
        verifyNoMoreInteractions(entityManager);
    }

    /**
     * Test method for {@link ProgramDAO#remove(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        programDAO.remove(null);
    }

    /**
     * Test method for {@link ProgramDAO#remove(Program)} with exception in persistence.
     */
    @Test(expected = DataStorageException.class)
    public void testRemove_PersistenceException() {
        doThrow(PersistenceException.class).when(entityManager).contains(any(Program.class));

        programDAO.remove(ProgramUtils.newProgram(ProgramUtils.ID));
    }

    /**
     * Sets ID.
     *
     * @param id ID
     * @return mocked answer
     */
    private static Answer<Void> setId(final Integer id) {
        return invocation -> {
            ((Program) invocation.getArguments()[0]).setId(id);
            return null;
        };
    }

}
