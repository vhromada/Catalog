package cz.vhromada.catalog.facade.impl;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.common.converter.MovableConverter;
import cz.vhromada.common.facade.AbstractMovableParentFacade;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.validation.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameFacade")
public class GameFacadeImpl extends AbstractMovableParentFacade<Game, cz.vhromada.catalog.domain.Game> implements GameFacade {

    /**
     * Creates a new instance of GameFacadeImpl.
     *
     * @param gameService   service for games
     * @param converter     converter for games
     * @param gameValidator validator for game
     * @throws IllegalArgumentException if service for games is null
     *                                  or converter for games is null
     *                                  or validator for game is null
     */
    @Autowired
    public GameFacadeImpl(final MovableService<cz.vhromada.catalog.domain.Game> gameService,
        final MovableConverter<Game, cz.vhromada.catalog.domain.Game> converter, final MovableValidator<Game> gameValidator) {
        super(gameService, converter, gameValidator);
    }

    @Override
    public Result<Integer> getTotalMediaCount() {
        int totalMedia = 0;
        for (final cz.vhromada.catalog.domain.Game game : getService().getAll()) {
            totalMedia += game.getMediaCount();
        }

        return Result.of(totalMedia);
    }

}
