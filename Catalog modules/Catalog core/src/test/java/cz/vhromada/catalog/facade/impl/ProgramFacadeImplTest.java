package cz.vhromada.catalog.facade.impl;

import static cz.vhromada.catalog.common.TestConstants.ADD_ID;
import static cz.vhromada.catalog.common.TestConstants.ADD_POSITION;
import static cz.vhromada.catalog.common.TestConstants.COUNT;
import static cz.vhromada.catalog.common.TestConstants.PRIMARY_ID;
import static cz.vhromada.catalog.common.TestConstants.SECONDARY_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.EntityGenerator;
import cz.vhromada.catalog.common.ToGenerator;
import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.dao.entities.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.ProgramTO;
import cz.vhromada.catalog.facade.validators.ProgramTOValidator;
import cz.vhromada.catalog.service.ProgramService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.test.DeepAsserts;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.convert.ConversionService;

/**
 * A class represents test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ProgramFacadeImplTest {

	/** Instance of {@link ProgramService} */
	@Mock
	private ProgramService programService;

	/** Instance of {@link ConversionService} */
	@Mock
	private ConversionService conversionService;

	/** Instance of {@link ProgramTOValidator} */
	@Mock
	private ProgramTOValidator programTOValidator;

	/** Instance of {@link ProgramFacade} */
	@InjectMocks
	private ProgramFacade programFacade = new ProgramFacadeImpl();

	/** Test method for {@link ProgramFacadeImpl#getProgramService()} and {@link ProgramFacadeImpl#setProgramService(ProgramService)}. */
	@Test
	public void testProgramService() {
		final ProgramFacadeImpl programFacade = new ProgramFacadeImpl();
		programFacade.setProgramService(programService);
		DeepAsserts.assertEquals(programService, programFacade.getProgramService());
	}

	/** Test method for {@link ProgramFacadeImpl#getConversionService()} and {@link ProgramFacadeImpl#setConversionService(ConversionService)}. */
	@Test
	public void testConversionService() {
		final ProgramFacadeImpl programFacade = new ProgramFacadeImpl();
		programFacade.setConversionService(conversionService);
		DeepAsserts.assertEquals(conversionService, programFacade.getConversionService());
	}

	/** Test method for {@link ProgramFacadeImpl#getProgramTOValidator()} and {@link ProgramFacadeImpl#setProgramTOValidator(ProgramTOValidator)}. */
	@Test
	public void testProgramTOValidator() {
		final ProgramFacadeImpl programFacade = new ProgramFacadeImpl();
		programFacade.setProgramTOValidator(programTOValidator);
		DeepAsserts.assertEquals(programTOValidator, programFacade.getProgramTOValidator());
	}

	/** Test method for {@link ProgramFacade#newData()}. */
	@Test
	public void testNewData() {
		programFacade.newData();

		verify(programService).newData();
		verifyNoMoreInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#newData()} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testNewDataWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.newData();
	}

	/** Test method for {@link ProgramFacade#newData()} with exception in service tier. */
	@Test
	public void testNewDataWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(programService).newData();

		try {
			programFacade.newData();
			fail("Can't create new data with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).newData();
		verifyNoMoreInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#getPrograms()}. */
	@Test
	public void testGetPrograms() {
		final List<Program> programs = CollectionUtils.newList(EntityGenerator.createProgram(PRIMARY_ID), EntityGenerator.createProgram(SECONDARY_ID));
		final List<ProgramTO> programsList = CollectionUtils.newList(ToGenerator.createProgram(PRIMARY_ID), ToGenerator.createProgram(SECONDARY_ID));
		when(programService.getPrograms()).thenReturn(programs);
		for (int i = 0; i < programs.size(); i++) {
			final Program program = programs.get(i);
			when(conversionService.convert(program, ProgramTO.class)).thenReturn(programsList.get(i));
		}

		DeepAsserts.assertEquals(programsList, programFacade.getPrograms());

		verify(programService).getPrograms();
		for (Program program : programs) {
			verify(conversionService).convert(program, ProgramTO.class);
		}
		verifyNoMoreInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#getPrograms()} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetProgramsWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.getPrograms();
	}

	/** Test method for {@link ProgramFacade#getPrograms()} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetProgramsWithNotSetConversionService() {
		((ProgramFacadeImpl) programFacade).setConversionService(null);
		programFacade.getPrograms();
	}

	/** Test method for {@link ProgramFacade#getPrograms()} with exception in service tier. */
	@Test
	public void testGetProgramsWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(programService).getPrograms();

		try {
			programFacade.getPrograms();
			fail("Can't get programs with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).getPrograms();
		verifyNoMoreInteractions(programService);
		verifyZeroInteractions(conversionService);
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)} with existing program. */
	@Test
	public void testGetProgramWithExistingProgram() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		when(programService.getProgram(anyInt())).thenReturn(program);
		when(conversionService.convert(any(Program.class), eq(ProgramTO.class))).thenReturn(programTO);

		DeepAsserts.assertEquals(programTO, programFacade.getProgram(PRIMARY_ID));

		verify(programService).getProgram(PRIMARY_ID);
		verify(conversionService).convert(program, ProgramTO.class);
		verifyNoMoreInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)} with not existing program. */
	@Test
	public void testGetProgramWithNotExistingProgram() {
		when(programService.getProgram(anyInt())).thenReturn(null);
		when(conversionService.convert(any(Program.class), eq(ProgramTO.class))).thenReturn(null);

		assertNull(programFacade.getProgram(Integer.MAX_VALUE));

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(conversionService).convert(null, ProgramTO.class);
		verifyNoMoreInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetProgramWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.getProgram(Integer.MAX_VALUE);
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testGetProgramWithNotSetConversionService() {
		((ProgramFacadeImpl) programFacade).setConversionService(null);
		programFacade.getProgram(Integer.MAX_VALUE);
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)} with null argument. */
	@Test
	public void testGetProgramWithNullArgument() {
		try {
			programFacade.getProgram(null);
			fail("Can't get program with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verifyZeroInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#getProgram(Integer)} with exception in service tier. */
	@Test
	public void testGetProgramWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

		try {
			programFacade.getProgram(Integer.MAX_VALUE);
			fail("Can't get program with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verifyNoMoreInteractions(programService);
		verifyZeroInteractions(conversionService);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)}. */
	@Test
	public void testAdd() {
		final Program program = EntityGenerator.createProgram();
		final ProgramTO programTO = ToGenerator.createProgram();
		doAnswer(setProgramIdAndPosition(ADD_ID, ADD_POSITION)).when(programService).add(any(Program.class));
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		programFacade.add(programTO);
		DeepAsserts.assertEquals(ADD_ID, program.getId());
		DeepAsserts.assertEquals(ADD_POSITION, program.getPosition());

		verify(programService).add(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateNewProgramTO(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.add(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetConversionService() {
		((ProgramFacadeImpl) programFacade).setConversionService(null);
		programFacade.add(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with not set validator for TO for program. */
	@Test(expected = IllegalStateException.class)
	public void testAddWithNotSetProgramTOValidator() {
		((ProgramFacadeImpl) programFacade).setProgramTOValidator(null);
		programFacade.add(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with null argument. */
	@Test
	public void testAddWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(programTOValidator).validateNewProgramTO(any(ProgramTO.class));

		try {
			programFacade.add(null);
			fail("Can't add program with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(programTOValidator).validateNewProgramTO(null);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with argument with bad data. */
	@Test
	public void testAddWithBadArgument() {
		final ProgramTO program = ToGenerator.createProgram();
		doThrow(ValidationException.class).when(programTOValidator).validateNewProgramTO(any(ProgramTO.class));

		try {
			programFacade.add(program);
			fail("Can't add program with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programTOValidator).validateNewProgramTO(program);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with service tier not setting ID. */
	@Test
	public void testAddWithNotServiceTierSettingID() {
		final Program program = EntityGenerator.createProgram();
		final ProgramTO programTO = ToGenerator.createProgram();
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		try {
			programFacade.add(programTO);
			fail("Can't add program with service tier not setting ID.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).add(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateNewProgramTO(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#add(ProgramTO)} with exception in service tier. */
	@Test
	public void testAddWithServiceTierException() {
		final Program program = EntityGenerator.createProgram();
		final ProgramTO programTO = ToGenerator.createProgram();
		doThrow(ServiceOperationException.class).when(programService).add(any(Program.class));
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		try {
			programFacade.add(programTO);
			fail("Can't add program with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).add(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateNewProgramTO(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)}. */
	@Test
	public void testUpdate() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		when(programService.exists(any(Program.class))).thenReturn(true);
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		programFacade.update(programTO);

		verify(programService).exists(program);
		verify(programService).update(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateExistingProgramTO(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.update(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetConversionService() {
		((ProgramFacadeImpl) programFacade).setConversionService(null);
		programFacade.update(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with not set validator for TO for program. */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithNotSetProgramTOValidator() {
		((ProgramFacadeImpl) programFacade).setProgramTOValidator(null);
		programFacade.update(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with null argument. */
	@Test
	public void testUpdateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(programTOValidator).validateExistingProgramTO(any(ProgramTO.class));

		try {
			programFacade.update(null);
			fail("Can't update program with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(programTOValidator).validateExistingProgramTO(null);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with argument with bad data. */
	@Test
	public void testUpdateWithBadArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(programTOValidator).validateExistingProgramTO(any(ProgramTO.class));

		try {
			programFacade.update(program);
			fail("Can't update program with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programTOValidator).validateExistingProgramTO(program);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with not existing argument. */
	@Test
	public void testUpdateWithNotExistingArgument() {
		final Program program = EntityGenerator.createProgram(Integer.MAX_VALUE);
		final ProgramTO programTO = ToGenerator.createProgram(Integer.MAX_VALUE);
		when(programService.exists(any(Program.class))).thenReturn(false);
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		try {
			programFacade.update(programTO);
			fail("Can't update program with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(programService).exists(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateExistingProgramTO(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#update(ProgramTO)} with exception in service tier. */
	@Test
	public void testUpdateWithServiceTierException() {
		final Program program = EntityGenerator.createProgram(Integer.MAX_VALUE);
		final ProgramTO programTO = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(programService).exists(any(Program.class));
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		try {
			programFacade.update(programTO);
			fail("Can't update program with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).exists(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateExistingProgramTO(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)}. */
	@Test
	public void testRemove() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		when(programService.getProgram(anyInt())).thenReturn(program);

		programFacade.remove(programTO);

		verify(programService).getProgram(PRIMARY_ID);
		verify(programService).remove(program);
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.remove(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with not set validator for TO for program. */
	@Test(expected = IllegalStateException.class)
	public void testRemoveWithNotSetProgramTOValidator() {
		((ProgramFacadeImpl) programFacade).setProgramTOValidator(null);
		programFacade.remove(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with null argument. */
	@Test
	public void testRemoveWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.remove(null);
			fail("Can't remove program with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(null);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with argument with bad data. */
	@Test
	public void testRemoveWithBadArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.remove(program);
			fail("Can't remove program with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with not existing argument. */
	@Test
	public void testRemoveWithNotExistingArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		when(programService.getProgram(anyInt())).thenReturn(null);

		try {
			programFacade.remove(program);
			fail("Can't remove program with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#remove(ProgramTO)} with exception in service tier. */
	@Test
	public void testRemoveWithServiceTierException() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

		try {
			programFacade.remove(program);
			fail("Can't remove program with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)}. */
	@Test
	public void testDuplicate() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		when(programService.getProgram(anyInt())).thenReturn(program);

		programFacade.duplicate(programTO);

		verify(programService).getProgram(PRIMARY_ID);
		verify(programService).duplicate(program);
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.duplicate(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with not set validator for TO for program. */
	@Test(expected = IllegalStateException.class)
	public void testDuplicateWithNotSetProgramTOValidator() {
		((ProgramFacadeImpl) programFacade).setProgramTOValidator(null);
		programFacade.duplicate(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with null argument. */
	@Test
	public void testDuplicateWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.duplicate(null);
			fail("Can't duplicate program with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(null);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with argument with bad data. */
	@Test
	public void testDuplicateWithBadArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.duplicate(program);
			fail("Can't duplicate program with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with not existing argument. */
	@Test
	public void testDuplicateWithNotExistingArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		when(programService.getProgram(anyInt())).thenReturn(null);

		try {
			programFacade.duplicate(program);
			fail("Can't duplicate program with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with exception in service tier. */
	@Test
	public void testDuplicateWithServiceTierException() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

		try {
			programFacade.duplicate(program);
			fail("Can't duplicate program with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)}. */
	@Test
	public void testMoveUp() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final List<Program> programs = CollectionUtils.newList(mock(Program.class), program);
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		when(programService.getProgram(anyInt())).thenReturn(program);
		when(programService.getPrograms()).thenReturn(programs);

		programFacade.moveUp(programTO);

		verify(programService).getProgram(PRIMARY_ID);
		verify(programService).getPrograms();
		verify(programService).moveUp(program);
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.moveUp(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not set validator for TO for program. */
	@Test(expected = IllegalStateException.class)
	public void testMoveUpWithNotSetProgramTOValidator() {
		((ProgramFacadeImpl) programFacade).setProgramTOValidator(null);
		programFacade.moveUp(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with null argument. */
	@Test
	public void testMoveUpWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.moveUp(null);
			fail("Can't move up program with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(null);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with argument with bad data. */
	@Test
	public void testMoveUpWithBadArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.moveUp(program);
			fail("Can't move up program with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not existing argument. */
	@Test
	public void testMoveUpWithNotExistingArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		when(programService.getProgram(anyInt())).thenReturn(null);

		try {
			programFacade.moveUp(program);
			fail("Can't move up program with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not moveable argument. */
	@Test
	public void testMoveUpWithNotMoveableArgument() {
		final Program program = EntityGenerator.createProgram(Integer.MAX_VALUE);
		final List<Program> programs = CollectionUtils.newList(program, mock(Program.class));
		final ProgramTO programTO = ToGenerator.createProgram(Integer.MAX_VALUE);
		when(programService.getProgram(anyInt())).thenReturn(program);
		when(programService.getPrograms()).thenReturn(programs);

		try {
			programFacade.moveUp(programTO);
			fail("Can't move up program with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programService).getPrograms();
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with exception in service tier. */
	@Test
	public void testMoveUpWithServiceTierException() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

		try {
			programFacade.moveUp(program);
			fail("Can't move up program with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)}. */
	@Test
	public void testMoveDown() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final List<Program> programs = CollectionUtils.newList(program, mock(Program.class));
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		when(programService.getProgram(anyInt())).thenReturn(program);
		when(programService.getPrograms()).thenReturn(programs);

		programFacade.moveDown(programTO);

		verify(programService).getProgram(PRIMARY_ID);
		verify(programService).getPrograms();
		verify(programService).moveDown(program);
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.moveDown(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not set validator for TO for program. */
	@Test(expected = IllegalStateException.class)
	public void testMoveDownWithNotSetProgramTOValidator() {
		((ProgramFacadeImpl) programFacade).setProgramTOValidator(null);
		programFacade.moveDown(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with null argument. */
	@Test
	public void testMoveDownWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.moveDown(null);
			fail("Can't move down program with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(null);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with argument with bad data. */
	@Test
	public void testMoveDownWithBadArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.moveDown(program);
			fail("Can't move down program with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not existing argument. */
	@Test
	public void testMoveDownWithNotExistingArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		when(programService.getProgram(anyInt())).thenReturn(null);

		try {
			programFacade.moveDown(program);
			fail("Can't move down program with not thrown RecordNotFoundException for not existing argument.");
		} catch (final RecordNotFoundException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not moveable argument. */
	@Test
	public void testMoveDownWithNotMoveableArgument() {
		final Program program = EntityGenerator.createProgram(Integer.MAX_VALUE);
		final List<Program> programs = CollectionUtils.newList(mock(Program.class), program);
		final ProgramTO programTO = ToGenerator.createProgram(Integer.MAX_VALUE);
		when(programService.getProgram(anyInt())).thenReturn(program);
		when(programService.getPrograms()).thenReturn(programs);

		try {
			programFacade.moveDown(programTO);
			fail("Can't move down program with not thrown ValidationException for not moveable argument.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programService).getPrograms();
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with exception in service tier. */
	@Test
	public void testMoveDownWithServiceTierException() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

		try {
			programFacade.moveDown(program);
			fail("Can't move down program with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).getProgram(Integer.MAX_VALUE);
		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with existing program. */
	@Test
	public void testExistsWithExistingProgram() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		when(programService.exists(any(Program.class))).thenReturn(true);
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		assertTrue(programFacade.exists(programTO));

		verify(programService).exists(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with not existing program. */
	@Test
	public void testExistsWithNotExistingProgram() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		when(programService.exists(any(Program.class))).thenReturn(false);
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		assertFalse(programFacade.exists(programTO));

		verify(programService).exists(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.exists(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with not set conversion service. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetConversionService() {
		((ProgramFacadeImpl) programFacade).setConversionService(null);
		programFacade.exists(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with not set validator for TO for program. */
	@Test(expected = IllegalStateException.class)
	public void testExistsWithNotSetProgramTOValidator() {
		((ProgramFacadeImpl) programFacade).setProgramTOValidator(null);
		programFacade.exists(mock(ProgramTO.class));
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with null argument. */
	@Test
	public void testExistsWithNullArgument() {
		doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.exists(null);
			fail("Can't exists program with not thrown IllegalArgumentException for null argument.");
		} catch (final IllegalArgumentException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(null);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with argument with bad data. */
	@Test
	public void testExistsWithBadArgument() {
		final ProgramTO program = ToGenerator.createProgram(Integer.MAX_VALUE);
		doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

		try {
			programFacade.exists(program);
			fail("Can't exists program with not thrown ValidationException for argument with bad data.");
		} catch (final ValidationException ex) {
			// OK
		}

		verify(programTOValidator).validateProgramTOWithId(program);
		verifyNoMoreInteractions(programTOValidator);
		verifyZeroInteractions(programService, conversionService);
	}

	/** Test method for {@link ProgramFacade#exists(ProgramTO)} with exception in service tier. */
	@Test
	public void testExistsWithServiceTierException() {
		final Program program = EntityGenerator.createProgram(PRIMARY_ID);
		final ProgramTO programTO = ToGenerator.createProgram(PRIMARY_ID);
		doThrow(ServiceOperationException.class).when(programService).exists(any(Program.class));
		when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

		try {
			programFacade.exists(programTO);
			fail("Can't exists program with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).exists(program);
		verify(conversionService).convert(programTO, Program.class);
		verify(programTOValidator).validateProgramTOWithId(programTO);
		verifyNoMoreInteractions(programService, conversionService, programTOValidator);
	}

	/** Test method for {@link ProgramFacade#updatePositions()}. */
	@Test
	public void testUpdatePositions() {
		programFacade.updatePositions();

		verify(programService).updatePositions();
		verifyNoMoreInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#updatePositions()} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testUpdatePositionsWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.updatePositions();
	}

	/** Test method for {@link ProgramFacade#updatePositions()} with exception in service tier. */
	@Test
	public void testUpdatePositionsWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(programService).updatePositions();

		try {
			programFacade.updatePositions();
			fail("Can't update positions with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).updatePositions();
		verifyNoMoreInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#getTotalMediaCount()}. */
	@Test
	public void testGetTotalMediaCount() {
		when(programService.getTotalMediaCount()).thenReturn(COUNT);

		DeepAsserts.assertEquals(COUNT, programFacade.getTotalMediaCount());

		verify(programService).getTotalMediaCount();
		verifyNoMoreInteractions(programService);
	}

	/** Test method for {@link ProgramFacade#getTotalMediaCount()} with not set service for programs. */
	@Test(expected = IllegalStateException.class)
	public void testGetTotalMediaCountWithNotSetProgramService() {
		((ProgramFacadeImpl) programFacade).setProgramService(null);
		programFacade.getTotalMediaCount();
	}

	/** Test method for {@link ProgramFacade#getTotalMediaCount()} with exception in service tier. */
	@Test
	public void testGetTotalMediaCountWithServiceTierException() {
		doThrow(ServiceOperationException.class).when(programService).getTotalMediaCount();

		try {
			programFacade.getTotalMediaCount();
			fail("Can't get total media count with not thrown FacadeOperationException for service tier exception.");
		} catch (final FacadeOperationException ex) {
			// OK
		}

		verify(programService).getTotalMediaCount();
		verifyNoMoreInteractions(programService);
	}

	/**
	 * Sets program's ID and position.
	 *
	 * @param id       ID
	 * @param position position
	 * @return mocked answer
	 */
	private Answer<Void> setProgramIdAndPosition(final Integer id, final int position) {
		return new Answer<Void>() {

			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Program program = (Program) invocation.getArguments()[0];
				program.setId(id);
				program.setPosition(position);
				return null;
			}

		};
	}

}
