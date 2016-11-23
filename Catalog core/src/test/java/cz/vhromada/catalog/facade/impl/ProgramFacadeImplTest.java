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

import cz.vhromada.catalog.common.CollectionUtils;
import cz.vhromada.catalog.common.ProgramUtils;
import cz.vhromada.catalog.domain.Program;
import cz.vhromada.catalog.entity.ProgramTO;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validators.ProgramTOValidator;
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
    private CatalogService<Program> programService;

    /**
     * Instance of {@link Converter}
     */
    @Mock
    private Converter converter;

    /**
     * Instance of {@link ProgramTOValidator}
     */
    @Mock
    private ProgramTOValidator programTOValidator;

    /**
     * Instance of {@link ProgramFacade}
     */
    private ProgramFacade programFacade;

    /**
     * Initializes facade for programs.
     */
    @Before
    public void setUp() {
        programFacade = new ProgramFacadeImpl(programService, converter, programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, ProgramTOValidator)} with null service for programs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullProgramService() {
        new ProgramFacadeImpl(null, converter, programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, ProgramTOValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullConverter() {
        new ProgramFacadeImpl(programService, null, programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, ProgramTOValidator)} with null validator for TO for program.
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
        verifyZeroInteractions(converter, programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacade#getPrograms()}.
     */
    @Test
    public void testGetPrograms() {
        final List<Program> programList = CollectionUtils.newList(ProgramUtils.newProgram(1), ProgramUtils.newProgram(2));
        final List<ProgramTO> expectedPrograms = CollectionUtils.newList(ProgramUtils.newProgramTO(1), ProgramUtils.newProgramTO(2));

        when(programService.getAll()).thenReturn(programList);
        when(converter.convertCollection(anyListOf(Program.class), eq(ProgramTO.class))).thenReturn(expectedPrograms);

        final List<ProgramTO> programs = programFacade.getPrograms();

        assertNotNull(programs);
        assertEquals(expectedPrograms, programs);

        verify(programService).getAll();
        verify(converter).convertCollection(programList, ProgramTO.class);
        verifyNoMoreInteractions(programService, converter);
        verifyZeroInteractions(programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacade#getProgram(Integer)} with existing program.
     */
    @Test
    public void testGetProgram_ExistingProgram() {
        final Program programEntity = ProgramUtils.newProgram(1);
        final ProgramTO expectedProgram = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(converter.convert(any(Program.class), eq(ProgramTO.class))).thenReturn(expectedProgram);

        final ProgramTO program = programFacade.getProgram(1);

        assertNotNull(program);
        assertEquals(expectedProgram, program);

        verify(programService).get(1);
        verify(converter).convert(programEntity, ProgramTO.class);
        verifyNoMoreInteractions(programService, converter);
        verifyZeroInteractions(programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacade#getProgram(Integer)} with not existing program.
     */
    @Test
    public void testGetProgram_NotExistingProgram() {
        when(programService.get(anyInt())).thenReturn(null);
        when(converter.convert(any(Program.class), eq(ProgramTO.class))).thenReturn(null);

        assertNull(programFacade.getProgram(Integer.MAX_VALUE));

        verify(programService).get(Integer.MAX_VALUE);
        verify(converter).convert(null, ProgramTO.class);
        verifyNoMoreInteractions(programService, converter);
        verifyZeroInteractions(programTOValidator);
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
    public void testAdd() {
        final Program programEntity = ProgramUtils.newProgram(null);
        final ProgramTO program = ProgramUtils.newProgramTO(null);

        when(converter.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(programEntity);

        programFacade.add(program);

        verify(programService).add(programEntity);
        verify(converter).convert(program, Program.class);
        verify(programTOValidator).validateNewProgramTO(program);
        verifyNoMoreInteractions(programService, converter, programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdd_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programTOValidator).validateNewProgramTO(any(ProgramTO.class));

        programFacade.add(null);
    }

    /**
     * Test method for {@link ProgramFacade#add(ProgramTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testAdd_BadArgument() {
        doThrow(ValidationException.class).when(programTOValidator).validateNewProgramTO(any(ProgramTO.class));

        programFacade.add(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)}.
     */
    @Test
    public void testUpdate() {
        final Program programEntity = ProgramUtils.newProgram(1);
        final ProgramTO program = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(converter.convert(any(ProgramTO.class), eq(Program.class))).thenReturn(programEntity);

        programFacade.update(program);

        verify(programService).get(1);
        verify(programService).update(programEntity);
        verify(converter).convert(program, Program.class);
        verify(programTOValidator).validateExistingProgramTO(program);
        verifyNoMoreInteractions(programService, converter, programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programTOValidator).validateExistingProgramTO(any(ProgramTO.class));

        programFacade.update(null);
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testUpdate_BadArgument() {
        doThrow(ValidationException.class).when(programTOValidator).validateExistingProgramTO(any(ProgramTO.class));

        programFacade.update(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#update(ProgramTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testUpdate_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.update(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#remove(ProgramTO)}.
     */
    @Test
    public void testRemove() {
        final Program programEntity = ProgramUtils.newProgram(1);
        final ProgramTO program = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);

        programFacade.remove(program);

        verify(programService).get(1);
        verify(programService).remove(programEntity);
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link ProgramFacade#remove(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

        programFacade.remove(null);
    }

    /**
     * Test method for {@link ProgramFacade#remove(ProgramTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testRemove_BadArgument() {
        doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

        programFacade.remove(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#remove(ProgramTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testRemove_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.remove(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(ProgramTO)}.
     */
    @Test
    public void testDuplicate() {
        final Program programEntity = ProgramUtils.newProgram(1);
        final ProgramTO program = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);

        programFacade.duplicate(program);

        verify(programService).get(1);
        verify(programService).duplicate(programEntity);
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicate_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

        programFacade.duplicate(null);
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(ProgramTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testDuplicate_BadArgument() {
        doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

        programFacade.duplicate(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#duplicate(ProgramTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testDuplicate_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.duplicate(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)}.
     */
    @Test
    public void testMoveUp() {
        final Program programEntity = ProgramUtils.newProgram(2);
        final List<Program> programs = CollectionUtils.newList(ProgramUtils.newProgram(1), programEntity);
        final ProgramTO program = ProgramUtils.newProgramTO(2);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(programService.getAll()).thenReturn(programs);

        programFacade.moveUp(program);

        verify(programService).get(2);
        verify(programService).getAll();
        verify(programService).moveUp(programEntity);
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUp_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

        programFacade.moveUp(null);
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_BadArgument() {
        doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

        programFacade.moveUp(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveUp_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.moveUp(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveUp(ProgramTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveUp_NotMovableArgument() {
        final Program programEntity = ProgramUtils.newProgram(Integer.MAX_VALUE);
        final List<Program> programs = CollectionUtils.newList(programEntity, ProgramUtils.newProgram(1));
        final ProgramTO program = ProgramUtils.newProgramTO(Integer.MAX_VALUE);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(programService.getAll()).thenReturn(programs);

        programFacade.moveUp(program);
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)}.
     */
    @Test
    public void testMoveDown() {
        final Program programEntity = ProgramUtils.newProgram(1);
        final List<Program> programs = CollectionUtils.newList(programEntity, ProgramUtils.newProgram(2));
        final ProgramTO program = ProgramUtils.newProgramTO(1);

        when(programService.get(anyInt())).thenReturn(programEntity);
        when(programService.getAll()).thenReturn(programs);

        programFacade.moveDown(program);

        verify(programService).get(1);
        verify(programService).getAll();
        verify(programService).moveDown(programEntity);
        verify(programTOValidator).validateProgramTOWithId(program);
        verifyNoMoreInteractions(programService, programTOValidator);
        verifyZeroInteractions(converter);
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDown_NullArgument() {
        doThrow(IllegalArgumentException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

        programFacade.moveDown(null);
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)} with argument with bad data.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_BadArgument() {
        doThrow(ValidationException.class).when(programTOValidator).validateProgramTOWithId(any(ProgramTO.class));

        programFacade.moveDown(ProgramUtils.newProgramTO(null));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not existing argument.
     */
    @Test(expected = RecordNotFoundException.class)
    public void testMoveDown_NotExistingArgument() {
        when(programService.get(anyInt())).thenReturn(null);

        programFacade.moveDown(ProgramUtils.newProgramTO(Integer.MAX_VALUE));
    }

    /**
     * Test method for {@link ProgramFacade#moveDown(ProgramTO)} with not movable argument.
     */
    @Test(expected = ValidationException.class)
    public void testMoveDown_NotMovableArgument() {
        final Program programEntity = ProgramUtils.newProgram(Integer.MAX_VALUE);
        final List<Program> programs = CollectionUtils.newList(ProgramUtils.newProgram(1), programEntity);
        final ProgramTO program = ProgramUtils.newProgramTO(Integer.MAX_VALUE);

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
        verifyZeroInteractions(converter, programTOValidator);
    }

    /**
     * Test method for {@link ProgramFacade#getTotalMediaCount()}.
     */
    @Test
    public void testGetTotalMediaCount() {
        final Program program1 = ProgramUtils.newProgram(1);
        final Program program2 = ProgramUtils.newProgram(2);
        final int expectedCount = program1.getMediaCount() + program2.getMediaCount();

        when(programService.getAll()).thenReturn(CollectionUtils.newList(program1, program2));

        assertEquals(expectedCount, programFacade.getTotalMediaCount());

        verify(programService).getAll();
        verifyNoMoreInteractions(programService);
        verifyZeroInteractions(converter, programTOValidator);
    }

}
