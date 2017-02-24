package cz.vhromada.catalog.facade.impl;

import java.util.List;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.converters.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A class represents implementation of facade for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameFacade")
public class GameFacadeImpl implements GameFacade {

    /**
     * Message for not existing game
     */
    private static final String NOT_EXISTING_GAME_MESSAGE = "Game doesn't exist.";

    /**
     * Message for not movable game
     */
    private static final String NOT_MOVABLE_GAME_MESSAGE = "ID isn't valid - game can't be moved.";

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
    private CatalogValidator<Game> gameValidator;

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
    public GameFacadeImpl(final CatalogService<cz.vhromada.catalog.domain.Game> gameService,
            final Converter converter,
            final CatalogValidator<Game> gameValidator) {
        Assert.notNull(gameService, "Service for games mustn't be null.");
        Assert.notNull(converter, "Converter mustn't be null.");
        Assert.notNull(gameValidator, "Validator for game mustn't be null.");

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
        Assert.notNull(id, "ID mustn't be null.");

        return converter.convert(gameService.get(id), Game.class);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void add(final Game game) {
        gameValidator.validate(game, ValidationType.NEW, ValidationType.DEEP);

        gameService.add(converter.convert(game, cz.vhromada.catalog.domain.Game.class));
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void update(final Game game) {
        gameValidator.validate(game, ValidationType.EXISTS, ValidationType.DEEP);
        if (gameService.get(game.getId()) == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GAME_MESSAGE);
        }

        gameService.update(converter.convert(game, cz.vhromada.catalog.domain.Game.class));
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void remove(final Game game) {
        gameValidator.validate(game, ValidationType.EXISTS);
        final cz.vhromada.catalog.domain.Game gameDomain = gameService.get(game.getId());
        if (gameDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GAME_MESSAGE);
        }

        gameService.remove(gameDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void duplicate(final Game game) {
        gameValidator.validate(game, ValidationType.EXISTS);
        final cz.vhromada.catalog.domain.Game gameDomain = gameService.get(game.getId());
        if (gameDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GAME_MESSAGE);
        }

        gameService.duplicate(gameDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveUp(final Game game) {
        gameValidator.validate(game, ValidationType.EXISTS, ValidationType.UP);
        final cz.vhromada.catalog.domain.Game gameDomain = gameService.get(game.getId());
        if (gameDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GAME_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Game> games = gameService.getAll();
        if (games.indexOf(gameDomain) <= 0) {
            throw new IllegalArgumentException(NOT_MOVABLE_GAME_MESSAGE);
        }

        gameService.moveUp(gameDomain);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void moveDown(final Game game) {
        gameValidator.validate(game, ValidationType.EXISTS, ValidationType.DOWN);
        final cz.vhromada.catalog.domain.Game gameDomain = gameService.get(game.getId());
        if (gameDomain == null) {
            throw new IllegalArgumentException(NOT_EXISTING_GAME_MESSAGE);
        }
        final List<cz.vhromada.catalog.domain.Game> games = gameService.getAll();
        if (games.indexOf(gameDomain) >= games.size() - 1) {
            throw new IllegalArgumentException(NOT_MOVABLE_GAME_MESSAGE);
        }

        gameService.moveDown(gameDomain);
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
