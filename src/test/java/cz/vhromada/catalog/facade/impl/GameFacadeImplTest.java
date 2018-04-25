package cz.vhromada.catalog.facade.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.common.facade.MovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.facade.MovableParentFacadeTest;
import cz.vhromada.common.utils.CollectionUtils;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.converter.Converter;
import cz.vhromada.result.Result;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link GameFacadeImpl}.
 *
 * @author Vladimir Hromada
 */
class GameFacadeImplTest extends MovableParentFacadeTest<Game, cz.vhromada.catalog.domain.Game> {

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(MovableService, Converter, MovableValidator)} with null service for games.
     */
    @Test
    void constructor_NullGameService() {
        assertThatThrownBy(() -> new GameFacadeImpl(null, getConverter(), getMovableValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(MovableService, Converter, MovableValidator)} with null converter.
     */
    @Test
    void constructor_NullConverter() {
        assertThatThrownBy(() -> new GameFacadeImpl(getMovableService(), null, getMovableValidator())).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GameFacadeImpl#GameFacadeImpl(MovableService, Converter, MovableValidator)} with null validator for game.
     */
    @Test
    void constructor_NullGameValidator() {
        assertThatThrownBy(() -> new GameFacadeImpl(getMovableService(), getConverter(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GameFacade#getTotalMediaCount()}.
     */
    @Test
    void getTotalMediaCount() {
        final cz.vhromada.catalog.domain.Game game1 = GameUtils.newGameDomain(1);
        final cz.vhromada.catalog.domain.Game game2 = GameUtils.newGameDomain(2);
        final int expectedCount = game1.getMediaCount() + game2.getMediaCount();

        when(getMovableService().getAll()).thenReturn(CollectionUtils.newList(game1, game2));

        final Result<Integer> result = ((GameFacade) getMovableParentFacade()).getTotalMediaCount();

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.OK);
            softly.assertThat(result.getData()).isEqualTo(expectedCount);
            softly.assertThat(result.getEvents()).isEmpty();
        });

        verify(getMovableService()).getAll();
        verifyNoMoreInteractions(getMovableService());
        verifyZeroInteractions(getConverter(), getMovableValidator());
    }

    @Override
    protected MovableParentFacade<Game> getMovableParentFacade() {
        return new GameFacadeImpl(getMovableService(), getConverter(), getMovableValidator());
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
