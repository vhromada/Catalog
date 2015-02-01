package cz.vhromada.catalog.facade.impl;

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

import cz.vhromada.catalog.commons.CollectionUtils;
import cz.vhromada.catalog.commons.ObjectGeneratorTest;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class ProgramFacadeImplTest extends ObjectGeneratorTest {

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
    private ProgramFacade programFacade;

    /** Initializes facade for programs. */
    @Before
    public void setUp() {
        programFacade = new ProgramFacadeImpl(programService, conversionService, programTOValidator);
    }

    /** Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(ProgramService, ConversionService, ProgramTOValidator)} with null service for programs. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProgramService() {
        new ProgramFacadeImpl(null, conversionService, programTOValidator);
    }

    /** Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(ProgramService, ConversionService, ProgramTOValidator)} with null conversion service. */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConversionService() {
        new ProgramFacadeImpl(programService, null, programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(ProgramService, ConversionService, ProgramTOValidator)} with null validator for
     * TO for program.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProgramTOValidator() {
        new ProgramFacadeImpl(programService, conversionService, null);
    }

    /** Test method for {@link ProgramFacade#newData()}. */
    @Test
    public void testNewData() {
        programFacade.newData();

        verify(programService).newData();
        verifyNoMoreInteractions(programService);
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
        final List<Program> programs = CollectionUtils.newList(generate(Program.class), generate(Program.class));
        final List<ProgramTO> programsList = CollectionUtils.newList(generate(ProgramTO.class), generate(ProgramTO.class));
        when(programService.getPrograms()).thenReturn(programs);
        for (int i = 0; i < programs.size(); i++) {
            final Program program = programs.get(i);
            when(conversionService.convert(program, ProgramTO.class)).thenReturn(programsList.get(i));
        }

        DeepAsserts.assertEquals(programsList, programFacade.getPrograms());

        verify(programService).getPrograms();
        for (final Program program : programs) {
            verify(conversionService).convert(program, ProgramTO.class);
        }
        verifyNoMoreInteractions(programService, conversionService);
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
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(program);
        when(conversionService.convert(any(Program.class), eq(ProgramTO.class))).thenReturn(programTO);

        DeepAsserts.assertEquals(programTO, programFacade.getProgram(programTO.getId()));

        verify(programService).getProgram(programTO.getId());
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
        final Program program = generate(Program.class);
        program.setId(null);
        final ProgramTO programTO = generate(ProgramTO.class);
        programTO.setId(null);
        final int id = generate(Integer.class);
        final int position = generate(Integer.class);
        doAnswer(setProgramIdAndPosition(id, position)).when(programService).add(any(Program.class));
        when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

        programFacade.add(programTO);
        DeepAsserts.assertEquals(id, program.getId());
        DeepAsserts.assertEquals(position, program.getPosition());

        verify(programService).add(program);
        verify(conversionService).convert(programTO, Program.class);
        verify(programTOValidator).validateNewProgramTO(programTO);
        verifyNoMoreInteractions(programService, conversionService, programTOValidator);
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
        final ProgramTO program = generate(ProgramTO.class);
        program.setId(null);
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
        final Program program = generate(Program.class);
        program.setId(null);
        final ProgramTO programTO = generate(ProgramTO.class);
        programTO.setId(null);
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
        final Program program = generate(Program.class);
        program.setId(null);
        final ProgramTO programTO = generate(ProgramTO.class);
        programTO.setId(null);
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
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.exists(any(Program.class))).thenReturn(true);
        when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

        programFacade.update(programTO);

        verify(programService).exists(program);
        verify(programService).update(program);
        verify(conversionService).convert(programTO, Program.class);
        verify(programTOValidator).validateExistingProgramTO(programTO);
        verifyNoMoreInteractions(programService, conversionService, programTOValidator);
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
        final ProgramTO program = generate(ProgramTO.class);
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
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
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
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
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
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(program);

        programFacade.remove(programTO);

        verify(programService).getProgram(programTO.getId());
        verify(programService).remove(program);
        verify(programTOValidator).validateProgramTOWithId(programTO);
        verifyNoMoreInteractions(programService, programTOValidator);
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
        final ProgramTO program = generate(ProgramTO.class);
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
        final ProgramTO program = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(null);

        try {
            programFacade.remove(program);
            fail("Can't remove program with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(programService).getProgram(program.getId());
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#remove(ProgramTO)} with exception in service tier. */
    @Test
    public void testRemoveWithServiceTierException() {
        final ProgramTO program = generate(ProgramTO.class);
        doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

        try {
            programFacade.remove(program);
            fail("Can't remove program with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(programService).getProgram(program.getId());
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#duplicate(ProgramTO)}. */
    @Test
    public void testDuplicate() {
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(program);

        programFacade.duplicate(programTO);

        verify(programService).getProgram(programTO.getId());
        verify(programService).duplicate(program);
        verify(programTOValidator).validateProgramTOWithId(programTO);
        verifyNoMoreInteractions(programService, programTOValidator);
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
        final ProgramTO program = generate(ProgramTO.class);
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
        final ProgramTO program = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(null);

        try {
            programFacade.duplicate(program);
            fail("Can't duplicate program with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(programService).getProgram(program.getId());
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#duplicate(ProgramTO)} with exception in service tier. */
    @Test
    public void testDuplicateWithServiceTierException() {
        final ProgramTO program = generate(ProgramTO.class);
        doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

        try {
            programFacade.duplicate(program);
            fail("Can't duplicate program with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(programService).getProgram(program.getId());
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#moveUp(ProgramTO)}. */
    @Test
    public void testMoveUp() {
        final Program program = generate(Program.class);
        final List<Program> programs = CollectionUtils.newList(mock(Program.class), program);
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(program);
        when(programService.getPrograms()).thenReturn(programs);

        programFacade.moveUp(programTO);

        verify(programService).getProgram(programTO.getId());
        verify(programService).getPrograms();
        verify(programService).moveUp(program);
        verify(programTOValidator).validateProgramTOWithId(programTO);
        verifyNoMoreInteractions(programService, programTOValidator);
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
        final ProgramTO program = generate(ProgramTO.class);
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
        final ProgramTO program = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(null);

        try {
            programFacade.moveUp(program);
            fail("Can't move up program with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(programService).getProgram(program.getId());
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not moveable argument. */
    @Test
    public void testMoveUpWithNotMoveableArgument() {
        final Program program = generate(Program.class);
        final List<Program> programs = CollectionUtils.newList(program, mock(Program.class));
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(program);
        when(programService.getPrograms()).thenReturn(programs);

        try {
            programFacade.moveUp(programTO);
            fail("Can't move up program with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(programService).getProgram(programTO.getId());
        verify(programService).getPrograms();
        verify(programTOValidator).validateProgramTOWithId(programTO);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#moveUp(ProgramTO)} with exception in service tier. */
    @Test
    public void testMoveUpWithServiceTierException() {
        final ProgramTO program = generate(ProgramTO.class);
        doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

        try {
            programFacade.moveUp(program);
            fail("Can't move up program with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(programService).getProgram(program.getId());
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#moveDown(ProgramTO)}. */
    @Test
    public void testMoveDown() {
        final Program program = generate(Program.class);
        final List<Program> programs = CollectionUtils.newList(program, mock(Program.class));
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(program);
        when(programService.getPrograms()).thenReturn(programs);

        programFacade.moveDown(programTO);

        verify(programService).getProgram(programTO.getId());
        verify(programService).getPrograms();
        verify(programService).moveDown(program);
        verify(programTOValidator).validateProgramTOWithId(programTO);
        verifyNoMoreInteractions(programService, programTOValidator);
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
        final ProgramTO program = generate(ProgramTO.class);
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
        final ProgramTO program = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(null);

        try {
            programFacade.moveDown(program);
            fail("Can't move down program with not thrown RecordNotFoundException for not existing argument.");
        } catch (final RecordNotFoundException ex) {
            // OK
        }

        verify(programService).getProgram(program.getId());
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not moveable argument. */
    @Test
    public void testMoveDownWithNotMoveableArgument() {
        final Program program = generate(Program.class);
        final List<Program> programs = CollectionUtils.newList(mock(Program.class), program);
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.getProgram(anyInt())).thenReturn(program);
        when(programService.getPrograms()).thenReturn(programs);

        try {
            programFacade.moveDown(programTO);
            fail("Can't move down program with not thrown ValidationException for not moveable argument.");
        } catch (final ValidationException ex) {
            // OK
        }

        verify(programService).getProgram(programTO.getId());
        verify(programService).getPrograms();
        verify(programTOValidator).validateProgramTOWithId(programTO);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#moveDown(ProgramTO)} with exception in service tier. */
    @Test
    public void testMoveDownWithServiceTierException() {
        final ProgramTO program = generate(ProgramTO.class);
        doThrow(ServiceOperationException.class).when(programService).getProgram(anyInt());

        try {
            programFacade.moveDown(program);
            fail("Can't move down program with not thrown FacadeOperationException for service tier exception.");
        } catch (final FacadeOperationException ex) {
            // OK
        }

        verify(programService).getProgram(program.getId());
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
    }

    /** Test method for {@link ProgramFacade#exists(ProgramTO)} with existing program. */
    @Test
    public void testExistsWithExistingProgram() {
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
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
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
        when(programService.exists(any(Program.class))).thenReturn(false);
        when(conversionService.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(program);

        assertFalse(programFacade.exists(programTO));

        verify(programService).exists(program);
        verify(conversionService).convert(programTO, Program.class);
        verify(programTOValidator).validateProgramTOWithId(programTO);
        verifyNoMoreInteractions(programService, conversionService, programTOValidator);
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
        final ProgramTO program = generate(ProgramTO.class);
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
        final Program program = generate(Program.class);
        final ProgramTO programTO = generate(ProgramTO.class);
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
        final int count = generate(Integer.class);
        when(programService.getTotalMediaCount()).thenReturn(count);

        DeepAsserts.assertEquals(count, programFacade.getTotalMediaCount());

        verify(programService).getTotalMediaCount();
        verifyNoMoreInteractions(programService);
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
    private static Answer<Void> setProgramIdAndPosition(final Integer id, final int position) {
        return new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) {
                final Program program = (Program) invocation.getArguments()[0];
                program.setId(id);
                program.setPosition(position);
                return null;
            }

        };
    }

}
