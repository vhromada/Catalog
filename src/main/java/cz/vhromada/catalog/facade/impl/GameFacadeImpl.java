package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.GameValidator;
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
     * Game argument
     */
    private static final String GAME_ARGUMENT = "game";

    /**
     * Service for games
     */
    private CatalogService<cz.vhromada.catalog.domain.Game> gameService;

    /**
     * Converter
     */
    private Converter converter;

    /**
     * Validator for game
     */
    private GameValidator gameValidator;

    /**
     * Creates a new instance of GameFacadeImpl.
     *
     * @param gameService   service for games
     * @param converter     converter
     * @param gameValidator validator for game
     * @throws IllegalArgumentException if service for games is null
     *                                  or converter is null
     *                                  or validator for game is null
     */
    @Autowired
    public GameFacadeImpl(@Qualifier("gameService") final CatalogService<cz.vhromada.catalog.domain.Game> gameService,
            @Qualifier("catalogDozerConverter") final Converter converter,
            final GameValidator gameValidator) {
        Validators.validateArgumentNotNull(gameService, "Service for games");
        Validators.validateArgumentNotNull(converter, "Converter");
        Validators.validateArgumentNotNull(gameValidator, "Validator for game");

        this.gameService = gameService;
        this.converter = converter;
        this.gameValidator = gameValidator;
    }

    @Override
    public void newData() {
        gameService.newData();
    }

    @Override
    public List<Game> getGames() {
        return converter.convertCollection(gameService.getAll(), Game.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Game getGame(final Integer id) {
        Validators.validateArgumentNotNull(id, "ID");

        return converter.convert(gameService.get(id), Game.class);
    }

    /**
     * @throws IllegalArgumentException                              {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException {@inheritDoc}
     */
    @Override
    public void add(final Game game) {
        gameValidator.validateNewGame(game);

        gameService.add(converter.convert(game, cz.vhromada.catalog.domain.Game.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void update(final Game game) {
        gameValidator.validateExistingGame(game);
        Validators.validateExists(gameService.get(game.getId()), GAME_ARGUMENT);

        gameService.update(converter.convert(game, cz.vhromada.catalog.domain.Game.class));
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void remove(final Game game) {
        gameValidator.validateGameWith(game);
        final cz.vhromada.catalog.domain.Game gameEntity = gameService.get(game.getId());
        Validators.validateExists(gameEntity, GAME_ARGUMENT);

        gameService.remove(gameEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void duplicate(final Game game) {
        gameValidator.validateGameWith(game);
        final cz.vhromada.catalog.domain.Game gameEntity = gameService.get(game.getId());
        Validators.validateExists(gameEntity, GAME_ARGUMENT);

        gameService.duplicate(gameEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveUp(final Game game) {
        gameValidator.validateGameWith(game);
        final cz.vhromada.catalog.domain.Game gameEntity = gameService.get(game.getId());
        Validators.validateExists(gameEntity, GAME_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Game> games = gameService.getAll();
        Validators.validateMoveUp(games, gameEntity, GAME_ARGUMENT);

        gameService.moveUp(gameEntity);
    }

    /**
     * @throws IllegalArgumentException                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException     {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException {@inheritDoc}
     */
    @Override
    public void moveDown(final Game game) {
        gameValidator.validateGameWith(game);
        final cz.vhromada.catalog.domain.Game gameEntity = gameService.get(game.getId());
        Validators.validateExists(gameEntity, GAME_ARGUMENT);
        final List<cz.vhromada.catalog.domain.Game> games = gameService.getAll();
        Validators.validateMoveDown(games, gameEntity, GAME_ARGUMENT);

        gameService.moveDown(gameEntity);
    }

    @Override
    public void updatePositions() {
        gameService.updatePositions();
    }

    @Override
    public int getTotalMediaCount() {
        int totalMedia = 0;
        for (final cz.vhromada.catalog.domain.Game game : gameService.getAll()) {
            totalMedia += game.getMediaCount();
        }

        return totalMedia;
    }

}
