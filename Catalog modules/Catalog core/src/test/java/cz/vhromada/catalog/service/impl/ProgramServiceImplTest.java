package cz.vhromada.catalog.service.impl;

import static cz.vhromada.catalog.common.TestConstants.MOVE_POSITION;
import static cz.vhromada.catalog.common.TestConstants.POSITION;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_ID;
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

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.ProgramDAO;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.dao.exceptions.DataStorageException;
import cz.vhromada.catalog.service.ProgramService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
public class ProgramServiceImplTest {

	/** Instance of {@link ProgramDAO} */
	@Mock
	private ProgramDAO programDAO;

	/** Instance of {@link Cache} */
	@Mock
	private Cache programCache;

	/** Instance of {@link ProgramService} */
	@InjectMocks
	private ProgramService programService = new ProgramServiceImpl();

	/** Test method for {@link ProgramServiceImpl#getProgramDAO()} and {@link ProgramServiceImpl#setProgramDAO(ProgramDAO)}. */
	@Test
	public void testProgramDAO() {
		final ProgramServiceImpl programService = new ProgramServiceImpl();
		programService.setProgramDAO(programDAO);
		DeepAsserts.assertEquals(programDAO, programService.getProgramDAO());
	}

	/** Test method for {@link ProgramServiceImpl#getProgramCache()} and {@link ProgramServiceImpl#setProgramCache(Cache)}. */
	@Test
	public void testProgramCache() {
		final ProgramServiceImpl programService = new ProgramServiceImpl();
		programService.setProgramCache(programCache);
		DeepAsserts.assertEquals(programCache, programService.getProgramCache());
	}

	/** Test method for {@link ProgramService#newData()} with cached programs. */
	@Test
	public void testNewDataWithCachedPrograms() {
		final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

		programService.newData();

		for (Program program : programs) {
			verify(programDAO).remove(program);
		}
		verify(programCache).get("programs");
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
		for (Program program : programs) {
			verify(programDAO).remove(program);
		}
		verify(programCache).get("programs");
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#newData()} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.newData();
	}

	/** Test method for {@link ProgramService#newData()} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.newData();
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
		verify(programCache).get("programs");
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#getPrograms()} with cached programs. */
	@Test
	public void testGetProgramsWithCachedPrograms() {
		final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

		DeepAsserts.assertEquals(programs, programService.getPrograms());

		verify(programCache).get("programs");
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
		verify(programCache).get("programs");
		verify(programCache).put("programs", programs);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#getPrograms()} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetProgramsWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.getPrograms();
	}

	/** Test method for {@link ProgramService#getPrograms()} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetProgramsWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.getPrograms();
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
		verify(programCache).get("programs");
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#getProgram(Integer)} with cached existing program. */
	@Test
	public void testGetProgramWithCachedExistingProgram() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(program));

		DeepAsserts.assertEquals(program, programService.getProgram(PRIMARY_ID));

		verify(programCache).get("program" + PRIMARY_ID);
		verifyNoMoreInteractions(programCache);
		verifyZeroInteractions(programDAO);
	}

	/** Test method for {@link ProgramService#getProgram(Integer)} with cached not existing program. */
	@Test
	public void testGetProgramWithCachedNotExistingProgram() {
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertNull(programService.getProgram(PRIMARY_ID));

		verify(programCache).get("program" + PRIMARY_ID);
		verifyNoMoreInteractions(programCache);
		verifyZeroInteractions(programDAO);
	}

	/** Test method for {@link ProgramService#getProgram(Integer)} with not cached existing program. */
	@Test
	public void testGetProgramWithNotCachedExistingProgram() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		when(programDAO.getProgram(anyInt())).thenReturn(program);
		when(programCache.get(anyString())).thenReturn(null);

		DeepAsserts.assertEquals(program, programService.getProgram(PRIMARY_ID));

		verify(programDAO).getProgram(PRIMARY_ID);
		verify(programCache).get("program" + PRIMARY_ID);
		verify(programCache).put("program" + PRIMARY_ID, program);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#getProgram(Integer)} with not cached not existing program. */
	@Test
	public void testGetProgramWithNotCachedNotExistingProgram() {
		when(programDAO.getProgram(anyInt())).thenReturn(null);
		when(programCache.get(anyString())).thenReturn(null);

		assertNull(programService.getProgram(PRIMARY_ID));

		verify(programDAO).getProgram(PRIMARY_ID);
		verify(programCache).get("program" + PRIMARY_ID);
		verify(programCache).put("program" + PRIMARY_ID, null);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#getProgram(Integer)} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetProgramWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.getProgram(Integer.MAX_VALUE);
	}

	/** Test method for {@link ProgramService#getProgram(Integer)} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetProgramWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.getProgram(Integer.MAX_VALUE);
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
		verify(programCache).get("program" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#add(Program)} with cached programs. */
	@Test
	public void testAddWithCachedPrograms() {
		final Program program = EntityGenerator.createProgram();
		final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
		final List<Program> programsList = new ArrayList<>(programs);
		programsList.add(program);
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

		programService.add(program);

		verify(programDAO).add(program);
		verify(programCache).get("programs");
		verify(programCache).get("program" + null);
		verify(programCache).put("programs", programsList);
		verify(programCache).put("program" + null, program);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#add(Program)} with not cached programs. */
	@Test
	public void testAddWithNotCachedPrograms() {
		final Program program = EntityGenerator.createProgram();
		when(programCache.get(anyString())).thenReturn(null);

		programService.add(program);

		verify(programDAO).add(program);
		verify(programCache).get("programs");
		verify(programCache).get("program" + null);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#add(Program)} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.add(mock(Program.class));
	}

	/** Test method for {@link ProgramService#add(Program)} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.add(mock(Program.class));
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
		final Program program = EntityGenerator.createProgram();
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
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);

		programService.update(program);

		verify(programDAO).update(program);
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#update(Program)} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.update(mock(Program.class));
	}

	/** Test method for {@link ProgramService#update(Program)} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.update(mock(Program.class));
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
		final Program program = EntityGenerator.createProgram(Integer.MAX_VALUE);
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
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final List<Program> programs = CollectionUtils.newList(mock(Program.class), mock(Program.class));
		final List<Program> programsList = new ArrayList<>(programs);
		programsList.add(program);
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programsList));

		programService.remove(program);

		verify(programDAO).remove(program);
		verify(programCache).get("programs");
		verify(programCache).put("programs", programs);
		verify(programCache).evict(PRIMARY_ID);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#remove(Program)} with not cached programs. */
	@Test
	public void testRemoveWithNotCachedPrograms() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		when(programCache.get(anyString())).thenReturn(null);

		programService.remove(program);

		verify(programDAO).remove(program);
		verify(programCache).get("programs");
		verify(programCache).evict(PRIMARY_ID);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#remove(Program)} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.remove(mock(Program.class));
	}

	/** Test method for {@link ProgramService#remove(Program)} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.remove(mock(Program.class));
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
		final Program program = EntityGenerator.createProgram(Integer.MAX_VALUE);
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
		programService.duplicate(EntityGenerator.createProgram(PRIMARY_ID));

		verify(programDAO).add(any(Program.class));
		verify(programDAO).update(any(Program.class));
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#duplicate(Program)} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.duplicate(mock(Program.class));
	}

	/** Test method for {@link ProgramService#duplicate(Program)} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.duplicate(mock(Program.class));
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
			programService.duplicate(EntityGenerator.createProgram(Integer.MAX_VALUE));
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
		final Program program1 = EntityGenerator.createProgram(PRIMARY_ID);
		program1.setPosition(MOVE_POSITION);
		final Program program2 = EntityGenerator.createProgram(SECONDARY_ID);
		final List<Program> programs = CollectionUtils.newList(program1, program2);
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

		programService.moveUp(program2);
		DeepAsserts.assertEquals(POSITION, program1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, program2.getPosition());

		verify(programDAO).update(program1);
		verify(programDAO).update(program2);
		verify(programCache).get("programs");
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#moveUp(Program)} with not cached programs. */
	@Test
	public void testMoveUpWithNotCachedPrograms() {
		final Program program1 = EntityGenerator.createProgram(PRIMARY_ID);
		program1.setPosition(MOVE_POSITION);
		final Program program2 = EntityGenerator.createProgram(SECONDARY_ID);
		final List<Program> programs = CollectionUtils.newList(program1, program2);
		when(programDAO.getPrograms()).thenReturn(programs);
		when(programCache.get(anyString())).thenReturn(null);

		programService.moveUp(program2);
		DeepAsserts.assertEquals(POSITION, program1.getPosition());
		DeepAsserts.assertEquals(MOVE_POSITION, program2.getPosition());

		verify(programDAO).update(program1);
		verify(programDAO).update(program2);
		verify(programDAO).getPrograms();
		verify(programCache).get("programs");
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#moveUp(Program)} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.moveUp(mock(Program.class));
	}

	/** Test method for {@link ProgramService#moveUp(Program)} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.moveUp(mock(Program.class));
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
			programService.moveUp(EntityGenerator.createProgram(Integer.MAX_VALUE));
			fail("Can't move up program with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(programDAO).getPrograms();
		verify(programCache).get("programs");
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#moveDown(Program)} with cached programs. */
	@Test
	public void testMoveDownWithCachedPrograms() {
		final Program program1 = EntityGenerator.createProgram(PRIMARY_ID);
		final Program program2 = EntityGenerator.createProgram(SECONDARY_ID);
		program2.setPosition(MOVE_POSITION);
		final List<Program> programs = CollectionUtils.newList(program1, program2);
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

		programService.moveDown(program1);
		DeepAsserts.assertEquals(MOVE_POSITION, program1.getPosition());
		DeepAsserts.assertEquals(POSITION, program2.getPosition());

		verify(programDAO).update(program1);
		verify(programDAO).update(program2);
		verify(programCache).get("programs");
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#moveDown(Program)} with not cached programs. */
	@Test
	public void testMoveDownWithNotCachedPrograms() {
		final Program program1 = EntityGenerator.createProgram(PRIMARY_ID);
		final Program program2 = EntityGenerator.createProgram(SECONDARY_ID);
		program2.setPosition(MOVE_POSITION);
		final List<Program> programs = CollectionUtils.newList(program1, program2);
		when(programDAO.getPrograms()).thenReturn(programs);
		when(programCache.get(anyString())).thenReturn(null);

		programService.moveDown(program1);
		DeepAsserts.assertEquals(MOVE_POSITION, program1.getPosition());
		DeepAsserts.assertEquals(POSITION, program2.getPosition());

		verify(programDAO).update(program1);
		verify(programDAO).update(program2);
		verify(programDAO).getPrograms();
		verify(programCache).get("programs");
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#moveDown(Program)} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.moveDown(mock(Program.class));
	}

	/** Test method for {@link ProgramService#moveDown(Program)} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.moveDown(mock(Program.class));
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
			programService.moveDown(EntityGenerator.createProgram(Integer.MAX_VALUE));
			fail("Can't move down program with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(programDAO).getPrograms();
		verify(programCache).get("programs");
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#exists(Program)} with cached existing program. */
	@Test
	public void testExistsWithCachedExistingProgram() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(program));

		assertTrue(programService.exists(program));

		verify(programCache).get("program" + PRIMARY_ID);
		verifyNoMoreInteractions(programCache);
		verifyZeroInteractions(programDAO);
	}

	/** Test method for {@link ProgramService#exists(Program)} with cached not existing program. */
	@Test
	public void testExistsWithCachedNotExistingProgram() {
		final Program program = EntityGenerator.createProgram(Integer.MAX_VALUE);
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(null));

		assertFalse(programService.exists(program));

		verify(programCache).get("program" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(programCache);
		verifyZeroInteractions(programDAO);
	}

	/** Test method for {@link ProgramService#exists(Program)} with not cached existing program. */
	@Test
	public void testExistsWithNotCachedExistingProgram() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		when(programDAO.getProgram(anyInt())).thenReturn(program);
		when(programCache.get(anyString())).thenReturn(null);

		assertTrue(programService.exists(program));

		verify(programDAO).getProgram(PRIMARY_ID);
		verify(programCache).get("program" + PRIMARY_ID);
		verify(programCache).put("program" + PRIMARY_ID, program);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#exists(Program)} with not cached not existing program. */
	@Test
	public void testExistsWithNotCachedNotExistingProgram() {
		when(programDAO.getProgram(anyInt())).thenReturn(null);
		when(programCache.get(anyString())).thenReturn(null);

		assertFalse(programService.exists(EntityGenerator.createProgram(Integer.MAX_VALUE)));

		verify(programDAO).getProgram(Integer.MAX_VALUE);
		verify(programCache).get("program" + Integer.MAX_VALUE);
		verify(programCache).put("program" + Integer.MAX_VALUE, null);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#exists(Program)} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.exists(mock(Program.class));
	}

	/** Test method for {@link ProgramService#exists(Program)} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.exists(mock(Program.class));
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
		doThrow(DataStorageException.class).when(programDAO).getProgram(anyInt());
		when(programCache.get(anyString())).thenReturn(null);

		try {
			programService.exists(EntityGenerator.createProgram(Integer.MAX_VALUE));
			fail("Can't exists program with not thrown ServiceOperationException for DAO tier exception.");
		} catch (final ServiceOperationException ex) {
			// OK
		}

		verify(programDAO).getProgram(Integer.MAX_VALUE);
		verify(programCache).get("program" + Integer.MAX_VALUE);
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#updatePositions()} with cached programs. */
	@Test
	public void testUpdatePositionsWithCachedPrograms() {
		final List<Program> programs = CollectionUtils.newList(EntityGenerator.createProgram(PRIMARY_ID), EntityGenerator.createProgram(SECONDARY_ID));
		when(programCache.get(anyString())).thenReturn(new SimpleValueWrapper(programs));

		programService.updatePositions();

		for (int i = 0; i < programs.size(); i++) {
			final Program program = programs.get(i);
			DeepAsserts.assertEquals(i, program.getPosition());
			verify(programDAO).update(program);
		}
		verify(programCache).get("programs");
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#updatePositions()} with not cached programs. */
	@Test
	public void testUpdatePositionsWithNotCachedPrograms() {
		final List<Program> programs = CollectionUtils.newList(EntityGenerator.createProgram(PRIMARY_ID), EntityGenerator.createProgram(SECONDARY_ID));
		when(programDAO.getPrograms()).thenReturn(programs);
		when(programCache.get(anyString())).thenReturn(null);

		programService.updatePositions();

		verify(programDAO).getPrograms();
		for (int i = 0; i < programs.size(); i++) {
			final Program program = programs.get(i);
			DeepAsserts.assertEquals(i, program.getPosition());
			verify(programDAO).update(program);
		}
		verify(programCache).get("programs");
		verify(programCache).clear();
		verifyNoMoreInteractions(programDAO, programCache);
	}

	/** Test method for {@link ProgramService#updatePositions()} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.updatePositions();
	}

	/** Test method for {@link ProgramService#updatePositions()} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.updatePositions();
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
		verify(programCache).get("programs");
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

		verify(programCache).get("programs");
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
		verify(programCache).get("programs");
		verify(programCache).put("programs", programs);
		verify(program1).getMediaCount();
		verify(program2).getMediaCount();
		verify(program3).getMediaCount();
		verifyNoMoreInteractions(programDAO, programCache, program1, program2, program3);
	}

	/** Test method for {@link ProgramService#getTotalMediaCount()} with not set DAO for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetProgramDAO() {
		((ProgramServiceImpl) programService).setProgramDAO(null);
		programService.getTotalMediaCount();
	}

	/** Test method for {@link ProgramService#getTotalMediaCount()} with not set cache for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetProgramCache() {
		((ProgramServiceImpl) programService).setProgramCache(null);
		programService.getTotalMediaCount();
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
		verify(programCache).get("programs");
		verifyNoMoreInteractions(programDAO, programCache);
	}

}
