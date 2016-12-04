package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.GameUtils;
import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.validator.GameValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GameValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class GameValidatorImplTest {

    /**
     * Instance of {@link GameValidator}
     */
    private GameValidator gameValidator;

    /**
     * Initializes validator for TO for game.
     */
    @Before
    public void setUp() {
        gameValidator = new GameValidatorImpl();
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGameTO_NullArgument() {
        gameValidator.validateNewGame(null);
    }

    /**
     * Test method for {@link GameValidator#validateNewGameTO(GameTO)} with TO for game with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NotNullId() {
        gameValidator.validateNewGame(GameUtils.newGameTO(1));
    }

    /**
     * Test method for {@link GameValidator#validateNewGameTO(GameTO)} with TO for game with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullName() {
        final Game game = GameUtils.newGameTO(null);
        game.setName(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGameTO(GameTO)} with TO for game with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_EmptyName() {
        final Game game = GameUtils.newGameTO(null);
        game.setName("");

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGameTO(GameTO)} with TO for game with null URL to english Wikipedia page about game is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullWikiEn() {
        final Game game = GameUtils.newGameTO(null);
        game.setWikiEn(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGameTO(GameTO)} with TO for game with null URL to czech Wikipedia page about game is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullWikiCz() {
        final Game game = GameUtils.newGameTO(null);
        game.setWikiCz(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGameTO(GameTO)} with TO for game with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NotPositiveMediaCount() {
        final Game game = GameUtils.newGameTO(null);
        game.setMediaCount(0);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGameTO(GameTO)} with TO for game with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullOtherData() {
        final Game game = GameUtils.newGameTO(null);
        game.setOtherData(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGameTO(GameTO)} with TO for game with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullNote() {
        final Game game = GameUtils.newGameTO(null);
        game.setNote(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGameTO_NullArgument() {
        gameValidator.validateExistingGame(null);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGameTO(GameTO)} with TO for game with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullId() {
        gameValidator.validateExistingGame(GameUtils.newGameTO(null));
    }

    /**
     * Test method for {@link GameValidator#validateExistingGameTO(GameTO)} with TO for game with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullName() {
        final Game game = GameUtils.newGameTO(1);
        game.setName(null);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGameTO(GameTO)} with TO for game with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_EmptyName() {
        final Game game = GameUtils.newGameTO(1);
        game.setName("");

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGameTO(GameTO)} with TO for game with null URL to Wikipedia page about game is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullWiki() {
        final Game game = GameUtils.newGameTO(1);
        game.setWikiCz(null);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGameTO(GameTO)} with TO for game with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NotPositiveMediaCount() {
        final Game game = GameUtils.newGameTO(1);
        game.setMediaCount(0);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGameTO(GameTO)} with TO for game with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullOtherData() {
        final Game game = GameUtils.newGameTO(1);
        game.setOtherData(null);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGameTO(GameTO)} with TO for game with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullNote() {
        final Game game = GameUtils.newGameTO(1);
        game.setNote(null);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateGameWithId(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGameTOWithId_NullArgument() {
        gameValidator.validateGameWithId(null);
    }

    /**
     * Test method for {@link GameValidator#validateGameWithId(GameTO)} with TO for game with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateGameTOWithId_NullId() {
        gameValidator.validateGameWithId(new Game());
    }

}
