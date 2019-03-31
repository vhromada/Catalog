package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableParentFacadeTest;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.validation.result.Result;
import cz.vhromada.validation.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class ProgramFacadeImplTest extends MovableParentFacadeTest<Program, cz.vhromada.catalog.domain.Program> {

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(MovableService, MovableConverter, MovableValidator)} with null service for programs.
     */
    @Test
    void constructor_NullProgramService() {
        assertThatThrownBy(() -> new ProgramFacadeImpl(null, getConverter(), getValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(MovableService, MovableConverter, MovableValidator)} with null converter for programs.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new ProgramFacadeImpl(getService(), null, getValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(MovableService, MovableConverter, MovableValidator)} with null validator for program.
     */
    @Test
    void constructor_NullProgramValidator() {
    }

    /**
     * Test method for {@link ProgramFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Program program1 = ProgramUtils.newProgramDomain(1);
        final cz.vhromada.catalog.domain.Program program2 = ProgramUtils.newProgramDomain(2);
        final int expectedCount = program1.getMediaCount() + program2.getMediaCount();

        when(getService().getAll()).thenReturn(List.of(program1, program2));

        final Result<Integer> result = ((ProgramFacade) getFacade()).getTotalMediaCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(expectedCount);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getService()).getAll();
        verifyNoMoreInteractions(getService());
        verifyZeroInteractions(getConverter(), getValidator());
    }

    @Override
    protected MovableParentFacade<Program> getFacade() {
        return new ProgramFacadeImpl(getService(), getConverter(), getValidator());
    }

    @Override
    protected Program newEntity(final Integer id) {
        return ProgramUtils.newProgram(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Program newDomain(final Integer id) {
        return ProgramUtils.newProgramDomain(id);
    }

    @Override
    protected Class<Program> getEntityClass() {
        return Program.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Program> getDomainClass() {
        return cz.vhromada.catalog.domain.Program.class;
    }

}
