package cz.vhromada.catalog.facade.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.utils.CollectionUtils;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A class represents test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
@RunWith(MockitoJUnitRunner.class)
public class GameFacadeImplTest extends AbstractParentFacadeTest<Game, cz.vhromada.catalog.domain.Game> {

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    public void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.newGameDomain(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.newGameDomain(2);
        final int expectedCount = game1.getMediaCount() + game2.getMediaCount();

        when(getCatalogService().getAll()).thenReturn(CollectionUtils.newList(game1, game2));

        final Result<Integer> result = ((GameFacade) getParentCatalogFacade()).getTotalMediaCount();

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
    protected AbstractParentCatalogFacade<Game, cz.vhromada.catalog.domain.Game> getParentCatalogFacade() {
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
