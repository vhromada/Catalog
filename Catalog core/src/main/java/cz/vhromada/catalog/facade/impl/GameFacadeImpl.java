package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entities.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.facade.validators.GameTOValidator;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.converters.Converter;
import cz.vhromada.validators.Validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * A class represents implementation of facade for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameFacade")
public class GameFacadeImpl implements GameFacade {

    /**
     * TO for game argument
     */
    private static final String GAME_TO_ARGUMENT = "TO for game";

    /**
     * Service for games
     */
    private CatalogService<Game> gameService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for TO for game
     */
    private GameTOValidator gameTOValidator;

    /**
     * Creates a new instance of GameFacadeImpl.
     *
     * @param gameService     service for games
     * @param converter       converter
     * @param gameTOValidator validator for TO for game
     * @throws IllegalArgumentException if service for games is null
     *                                  or converter is null
     *                                  or validator for TO for game is null
     */
    @Autowired
    public GameFacadeImpl(@Qualifier("gameService") final CatalogService<Game> gameService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final GameTOValidator gameTOValidator) {
        Validators.validateArgumentNotNull(gameService, "Service for games");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(gameTOValidator, "Validator for TO for game");

        this.gameService = gameService;
        this.converter = converter;
        this.gameTOValidator = gameTOValidator;
    }

    @Override
    public void newData() {
        gameService.newData();
    }

    @Override
    public List<GameTO> getGames() {
        return converter.convertCollection(gameService.getAll(), GameTO.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public GameTO getGame(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(gameService.get(id), GameTO.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final GameTO game) {
        gameTOValidator.validateNewGameTO(game);

        gameService.add(converter.convert(game, Game.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final GameTO game) {
        gameTOValidator.validateExistingGameTO(game);
        Validators.validateExists(gameService.get(game.getId()), GAME_TO_ARGUMENT);

        gameService.update(converter.convert(game, Game.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        final Game gameEntity = gameService.get(game.getId());
        Validators.validateExists(gameEntity, GAME_TO_ARGUMENT);

        gameService.remove(gameEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        final Game gameEntity = gameService.get(game.getId());
        Validators.validateExists(gameEntity, GAME_TO_ARGUMENT);

        gameService.duplicate(gameEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        final Game gameEntity = gameService.get(game.getId());
        Validators.validateExists(gameEntity, GAME_TO_ARGUMENT);
        final List<Game> games = gameService.getAll();
        Validators.validateMoveUp(games, gameEntity, GAME_TO_ARGUMENT);

        gameService.moveUp(gameEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        final Game gameEntity = gameService.get(game.getId());
        Validators.validateExists(gameEntity, GAME_TO_ARGUMENT);
        final List<Game> games = gameService.getAll();
        Validators.validateMoveDown(games, gameEntity, GAME_TO_ARGUMENT);

        gameService.moveDown(gameEntity);
    }

    @Override
    public void updatePositions() {
        gameService.updatePositions();
    }

    @Override
    public int getTotalMediaCount() {
        int totalMedia = 0;
        for (final Game game : gameService.getAll()) {
            totalMedia += game.getMediaCount();
        }

        return totalMedia;
    }

}
