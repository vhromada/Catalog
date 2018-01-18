package cz.vhromada.catalog.facade.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import cz.vhromada.catalog.entity.Program;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.facade.ProgramFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.ProgramUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link ProgramFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class ProgramFacadeImplTest extends AbstractParentFacadeTest<Program, cz.vhromada.catalog.domain.Program> {

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for games.
     */
    @Test
    void constructor_NullProgramService() {
        assertThrows(IllegalArgumentException.class, () -> new ProgramFacadeImpl(null, getConverter(), getCatalogValidator()));
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThrows(IllegalArgumentException.class, () -> new ProgramFacadeImpl(getCatalogService(), null, getCatalogValidator()));
    }

    /**
     * Test method for {@link ProgramFacadeImpl#ProgramFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for game.
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

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(program1, program2));

        final Result<Integer> result = ((ProgramFacade) getCatalogParentFacade()).getTotalMediaCount();

        assertNotNull(result);
        assertAll(
            () -> assertEquals(Status.OK, result.getStatus()),
            () -> assertEquals(Integer.valueOf(expectedCount), result.getData()),
            () -> assertTrue(result.getEvents().isEmpty())
        );

        verify(getCatalogService()).getAll();
        verifyNoMoreInteractions(getCatalogService());
        verifyZeroInteractions(getConverter(), getCatalogValidator());
    }

    @Override
    protected CatalogParentFacade<Program> getCatalogParentFacade() {
        return new ProgramFacadeImpl(getCatalogService(), getConverter(), getCatalogValidator());
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
