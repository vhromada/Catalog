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

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.CatalogParentFacade;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class GameFacadeImplTest extends AbstractParentFacadeTest<Game, cz.vhromada.catalog.domain.Game> {

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for games.
     */
    @Test
    void constructor_NullGameService() {
        assertThrows(IllegalArgumentException.class, () -> new GameFacadeImpl(null, getConverter(), getCatalogValidator()));
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThrows(IllegalArgumentException.class, () -> new GameFacadeImpl(getCatalogService(), null, getCatalogValidator()));
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for game.
     */
    @Test
    void constructor_NullGameValidator() {
        assertThrows(IllegalArgumentException.class, () -> new GameFacadeImpl(getCatalogService(), getConverter(), null));
    }

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.newGameDomain(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.newGameDomain(2);
        final int expectedCount = game1.getMediaCount() + game2.getMediaCount();

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(game1, game2));

        final Result<Integer> result = ((GameFacade) getCatalogParentFacade()).getTotalMediaCount();

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
    protected CatalogParentFacade<Game> getCatalogParentFacade() {
        return new GameFacadeImpl(getCatalogService(), getConverter(), getCatalogValidator());
    }

    @Override
    protected Game newEntity(final Integer id) {
        return GameUtils.newGame(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Game newDomain(final Integer id) {
        return GameUtils.newGameDomain(id);
    }

    @Override
    protected Class<Game> getEntityClass() {
        return Game.class;
    }

    @Override
    protected Class<cz.vhromada.catalog.domain.Game> getDomainClass() {
        return cz.vhromada.catalog.domain.Game.class;
    }

}
