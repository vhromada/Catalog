package cz.vhromada.catalog.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.vhromada.catalog.dao.entities.Game;
import cz.vhromada.catalog.facade.GameFacade;
import cz.vhromada.catalog.facade.exceptions.FacadeOperationException;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.facade.validators.GameTOValidator;
import cz.vhromada.catalog.service.GameService;
import cz.vhromada.catalog.service.exceptions.ServiceOperationException;
import cz.vhromada.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A class represents implementation of facade for games.
 *
 * @author Vladimir Hromada
 */
@Component("gameFacade")
@Transactional
public class GameFacadeImpl implements GameFacade {

    /** Service for games field */
    private static final String GAME_SERVICE_ARGUMENT = "Service for games";

    /** Conversion service field */
    private static final String CONVERSION_SERVICE_ARGUMENT = "Conversion service";

    /** Validator for TO for game field */
    private static final String GAME_TO_VALIDATOR_ARGUMENT = "Validator for TO for game";

    /** Game argument */
    private static final String GAME_ARGUMENT = "game";

    /** TO for game argument */
    private static final String GAME_TO_ARGUMENT = "TO for game";

    /** ID argument */
    private static final String ID_ARGUMENT = "ID";

    /** Message for {@link FacadeOperationException} */
    private static final String FACADE_OPERATION_EXCEPTION_MESSAGE = "Error in working with service tier.";

    /** Message for not setting ID */
    private static final String NOT_SET_ID_EXCEPTION_MESSAGE = "Service tier doesn't set ID.";

    /** Service for games */
    private GameService gameService;

    /** Conversion service */
    private ConversionService conversionService;

    /** Validator for TO for game */
    private GameTOValidator gameTOValidator;

    /**
     * Creates a new instance of GameFacadeImpl.
     *
     * @param gameService service for games
     * @param conversionService conversion service
     * @param gameTOValidator validator for TO for game
     * @throws IllegalArgumentException if service for games is null
     *                                  or conversion service is null
     *                                  or validator for TO for game is null
     */
    @Autowired
    public GameFacadeImpl(final GameService gameService,
            @Qualifier("coreConversionService") final ConversionService conversionService,
            final GameTOValidator gameTOValidator) {
        Validators.validateArgumentNotNull(gameService, GAME_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(conversionService, CONVERSION_SERVICE_ARGUMENT);
        Validators.validateArgumentNotNull(gameTOValidator, GAME_TO_VALIDATOR_ARGUMENT);

        this.gameService = gameService;
        this.conversionService = conversionService;
        this.gameTOValidator = gameTOValidator;
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void newData() {
        try {
            gameService.newData();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GameTO> getGames() {
        try {
            final List<GameTO> games = new ArrayList<>();
            for (final Game game : gameService.getGames()) {
                games.add(conversionService.convert(game, GameTO.class));
            }
            Collections.sort(games);
            return games;
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public GameTO getGame(final Integer id) {
        Validators.validateArgumentNotNull(id, ID_ARGUMENT);

        try {
            return conversionService.convert(gameService.getGame(id), GameTO.class);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void add(final GameTO game) {
        gameTOValidator.validateNewGameTO(game);

        try {
            final Game gameEntity = conversionService.convert(game, Game.class);
            gameService.add(gameEntity);
            if (gameEntity.getId() == null) {
                throw new FacadeOperationException(NOT_SET_ID_EXCEPTION_MESSAGE);
            }
            game.setId(gameEntity.getId());
            game.setPosition(gameEntity.getPosition());
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void update(final GameTO game) {
        gameTOValidator.validateExistingGameTO(game);
        try {
            final Game gameEntity = conversionService.convert(game, Game.class);
            Validators.validateExists(gameService.exists(gameEntity), GAME_TO_ARGUMENT);

            gameService.update(gameEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void remove(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        try {
            final Game gameEntity = gameService.getGame(game.getId());
            Validators.validateExists(gameEntity, GAME_TO_ARGUMENT);

            gameService.remove(gameEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void duplicate(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        try {
            final Game oldGame = gameService.getGame(game.getId());
            Validators.validateExists(oldGame, GAME_TO_ARGUMENT);

            gameService.duplicate(oldGame);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveUp(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        try {
            final Game gameEntity = gameService.getGame(game.getId());
            Validators.validateExists(gameEntity, GAME_TO_ARGUMENT);
            final List<Game> games = gameService.getGames();
            Validators.validateMoveUp(games, gameEntity, GAME_ARGUMENT);

            gameService.moveUp(gameEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.RecordNotFoundException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void moveDown(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        try {
            final Game gameEntity = gameService.getGame(game.getId());
            Validators.validateExists(gameEntity, GAME_TO_ARGUMENT);
            final List<Game> games = gameService.getGames();
            Validators.validateMoveDown(games, gameEntity, GAME_ARGUMENT);

            gameService.moveDown(gameEntity);
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws cz.vhromada.validators.exceptions.ValidationException
     *                                  {@inheritDoc}
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(final GameTO game) {
        gameTOValidator.validateGameTOWithId(game);
        try {

            return gameService.exists(conversionService.convert(game, Game.class));
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    public void updatePositions() {
        try {
            gameService.updatePositions();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FacadeOperationException {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int getTotalMediaCount() {
        try {
            return gameService.getTotalMediaCount();
        } catch (final ServiceOperationException ex) {
            throw new FacadeOperationException(FACADE_OPERATION_EXCEPTION_MESSAGE, ex);
        }
    }

}
