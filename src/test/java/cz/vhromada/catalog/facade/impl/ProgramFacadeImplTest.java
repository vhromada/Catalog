package cz.vhromada.catalog.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.common.ProgramUtils;
import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.validator.ProgramValidator;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.exceptions.RecordNotFoundException;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class ProgramFacadeImplTest {

    /**
     * Instance of {@link CatalogService}
     */
    @Mock
    private CatalogService<cz.vhromada.catalog.domain.Program> programService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link ProgramValidator}
     */
    @Mock
    private ProgramValidator programValidator;

    /**
     * Instance of {@link ProgramFacade}
     */
    private ProgramFacade programFacade;

    /**
     * Initializes facade for programs.
     */
    @Before
    public void setUp() {
        programFacade = new ProgramFacadeImpl(programService, converter, programValidator);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, ProgramValidator)} with null service for programs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullProgramService() {
        new ProgramFacadeImpl(null, converter, programValidator);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, ProgramValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new ProgramFacadeImpl(programService, null, programValidator);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, ProgramValidator)} with null validator for TO for program.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullProgramTOValidator() {
        new ProgramFacadeImpl(programService, converter, null);
    }

    /**
     * Test method for {@link ProgramFacade#newData()}.
     */
    @Test
    public void testNewData() {
        programFacade.newData();

        verify(programService).newData();
        verifyNoMoreInteractions(programService);
        verifyZeroInteractions(converter, programValidator);
    }

    /**
     * Test method for {@link ProgramFacade#getPrograms()}.
     */
    @Test
    public void testGetPrograms() {
        final List<cz.vhromada.catalog.domain.Program> programList = CollectionUtils.newList(ProgramUtils.newProgram(1), ProgramUtils.newProgram(2));
        final List<Program> expectedPrograms = CollectionUtils.newList(ProgramUtils.newProgramTO(1), ProgramUtils.newProgramTO(2));

        when(programService.getAll()).thenReturn(programList);
        when(converter.convertCollection(anyListOf(cz.vhromada.catalog.domain.Program.class), eq(Program.class))).thenReturn(expectedPrograms);

        final List<Program> programs = programFacade.getPrograms();

        assertNotNull(programs);
        assertEquals(expectedPrograms, programs);

        verify(programService).getAll();
        verify(converter).convertCollection(programList, Program.class);
        verifyNoMoreInteractions(programService, converter);
        verifyZeroInteractions(programValidator);
    }

    /**
     * Test method for {@link ProgramFacade#getProgram(Integer)} with existing program.
     */
    @Test
    public void testGetProgram_ExistingProgram() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(1);
        final Program expectedProgram = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(converter.convert(any(cz.vhromada.catalog.domain.Program.class), eq(Program.class))).thenReturn(expectedProgram);

        final Program program = programFacade.getProgram(1);

        assertNotNull(program);
        assertEquals(expectedProgram, program);

        verify(programService).get(1);
        verify(converter).convert(programEntity, Program.class);
        verifyNoMoreInteractions(programService, converter);
        verifyZeroInteractions(programValidator);
    }

    /**
     * Test method for {@link ProgramFacade#getProgram(Integer)} with not existing program.
     */
    @Test
    public void testGetProgram_NotExistingProgram() {
        when(programService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(cz.vhromada.catalog.domain.Program.class), eq(Program.class))).thenReturn(null);

        assertNull(programFacade.getProgram(Integer.MAX_VALUE));

        verify(programService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, Program.class);
        verifyNoMoreInteractions(programService, converter);
        verifyZeroInteractions(programValidator);
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
    public void testAdd() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(null);
        final Program program = ProgramUtils.newProgramTO(null);

        when(converter.convert(any(Program.class), eq(cz.vhromada.catalog.domain.Program.class))).thenReturn(programEntity);

        programFacade.add(program);

        verify(programService).add(programEntity);
        verify(converter).convert(program, cz.vhromada.catalog.domain.Program.class);
        verify(programValidator).validateNewProgram(program);
        verifyNoMoreInteractions(programService, converter, programValidator);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programValidator).validateNewProgram(any(Program.class));

        programFacade.add(null);
    }

    /**
     * Test method for {@link ProgramFacade#add(Program)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadArgument() {
        doThrow(ValidationException.class).when(programValidator).validateNewProgram(any(Program.class));

        programFacade.add(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)}.
     */
    @Test
    public void testUpdate() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(1);
        final Program program = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(converter.convert(any(Program.class), eq(cz.vhromada.catalog.domain.Program.class))).thenReturn(programEntity);

        programFacade.update(program);

        verify(programService).get(1);
        verify(programService).update(programEntity);
        verify(converter).convert(program, cz.vhromada.catalog.domain.Program.class);
        verify(programValidator).validateExistingProgram(program);
        verifyNoMoreInteractions(programService, converter, programValidator);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programValidator).validateExistingProgram(any(Program.class));

        programFacade.update(null);
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(programValidator).validateExistingProgram(any(Program.class));

        programFacade.update(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#update(Program)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.update(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#remove(Program)}.
     */
    @Test
    public void testRemove() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(1);
        final Program program = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);

        programFacade.remove(program);

        verify(programService).get(1);
        verify(programService).remove(programEntity);
        verify(programValidator).validateProgramWithId(program);
        verifyNoMoreInteractions(programService, programValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link ProgramFacade#remove(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programValidator).validateProgramWithId(any(Program.class));

        programFacade.remove(null);
    }

    /**
     * Test method for {@link ProgramFacade#remove(Program)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(programValidator).validateProgramWithId(any(Program.class));

        programFacade.remove(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#remove(Program)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.remove(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(Program)}.
     */
    @Test
    public void testDuplicate() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(1);
        final Program program = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);

        programFacade.duplicate(program);

        verify(programService).get(1);
        verify(programService).duplicate(programEntity);
        verify(programValidator).validateProgramWithId(program);
        verifyNoMoreInteractions(programService, programValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programValidator).validateProgramWithId(any(Program.class));

        programFacade.duplicate(null);
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(Program)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(programValidator).validateProgramWithId(any(Program.class));

        programFacade.duplicate(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(Program)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.duplicate(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)}.
     */
    @Test
    public void testMoveUp() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(2);
        final List<cz.vhromada.catalog.domain.Program> programs = CollectionUtils.newList(ProgramUtils.newProgram(1), programEntity);
        final Program program = ProgramUtils.newProgramTO(2);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(programService.getAll()).thenReturn(programs);

        programFacade.moveUp(program);

        verify(programService).get(2);
        verify(programService).getAll();
        verify(programService).moveUp(programEntity);
        verify(programValidator).validateProgramWithId(program);
        verifyNoMoreInteractions(programService, programValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programValidator).validateProgramWithId(any(Program.class));

        programFacade.moveUp(null);
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(programValidator).validateProgramWithId(any(Program.class));

        programFacade.moveUp(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.moveUp(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(Program)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Program> programs = CollectionUtils.newList(programEntity, ProgramUtils.newProgram(1));
        final Program program = ProgramUtils.newProgramTO(Integer.MAX_VALUE);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(programService.getAll()).thenReturn(programs);

        programFacade.moveUp(program);
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)}.
     */
    @Test
    public void testMoveDown() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(1);
        final List<cz.vhromada.catalog.domain.Program> programs = CollectionUtils.newList(programEntity, ProgramUtils.newProgram(2));
        final Program program = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(programService.getAll()).thenReturn(programs);

        programFacade.moveDown(program);

        verify(programService).get(1);
        verify(programService).getAll();
        verify(programService).moveDown(programEntity);
        verify(programValidator).validateProgramWithId(program);
        verifyNoMoreInteractions(programService, programValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programValidator).validateProgramWithId(any(Program.class));

        programFacade.moveDown(null);
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(programValidator).validateProgramWithId(any(Program.class));

        programFacade.moveDown(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.moveDown(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(Program)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        final cz.vhromada.catalog.domain.Program programEntity = ProgramUtils.newProgram(Integer.MAX_VALUE);
        final List<cz.vhromada.catalog.domain.Program> programs = CollectionUtils.newList(ProgramUtils.newProgram(1), programEntity);
        final Program program = ProgramUtils.newProgramTO(Integer.MAX_VALUE);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(programService.getAll()).thenReturn(programs);

        programFacade.moveDown(program);
    }

    /**
     * Test method for {@link ProgramFacade#updatePositions()}.
     */
    @Test
    public void testUpdatePositions() {
        programFacade.updatePositions();

        verify(programService).updatePositions();
        verifyNoMoreInteractions(programService);
        verifyZeroInteractions(converter, programValidator);
    }

    /**
     * Test method for {@link ProgramFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        final cz.vhromada.catalog.domain.Program program1 = ProgramUtils.newProgram(1);
        final cz.vhromada.catalog.domain.Program program2 = ProgramUtils.newProgram(2);
        final int expectedCount = program1.getMediaCount() + program2.getMediaCount();

        when(programService.getAll()).thenReturn(CollectionUtils.newList(program1, program2));

        assertEquals(expectedCount, programFacade.getTotalMediaCount());

        verify(programService).getAll();
        verifyNoMoreInteractions(programService);
        verifyZeroInteractions(converter, programValidator);
    }

}
