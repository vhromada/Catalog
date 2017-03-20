package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
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

import org.junit.Test;

/**
 * A class represents test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
public class GameFacadeImplTest extends AbstractParentFacadeTest<Game, cz.vhromada.catalog.domain.Game> {

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null service for games.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGameService() {
        new GameFacadeImpl(null, getConverter(), getCatalogValidator());
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null converter.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullConverter() {
        new GameFacadeImpl(getCatalogService(), null, getCatalogValidator());
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(CatalogService, Converter, CatalogValidator)} with null validator for game.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor_NullGameValidator() {
        new GameFacadeImpl(getCatalogService(), getConverter(), null);
    }

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    public void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.newGameDomain(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.newGameDomain(2);
        final int expectedCount = game1.getMediaCount() + game2.getMediaCount();

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(game1, game2));

        final Result<Integer> result = ((GameFacade) getCatalogParentFacade()).getTotalMediaCount();

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.OK));
        assertThat(result.getData(), is(expectedCount));
        assertThat(result.getEvents().isEmpty(), is(true));

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
