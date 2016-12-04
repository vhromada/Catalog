package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.catalog.validator.GameValidator;

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
     * Initializes validator for game.
     */
    @Before
    public void setUp() {
        gameValidator = new GameValidatorImpl();
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_NullArgument() {
        gameValidator.validateNewGame(null);
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with game with not null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_NotNullId() {
        gameValidator.validateNewGame(GameUtils.newGame(1));
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with game with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_NullName() {
        final Game game = GameUtils.newGame(null);
        game.setName(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with game with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_EmptyName() {
        final Game game = GameUtils.newGame(null);
        game.setName("");

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with game with null URL to english Wikipedia page about game is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_NullWikiEn() {
        final Game game = GameUtils.newGame(null);
        game.setWikiEn(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with game with null URL to czech Wikipedia page about game is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_NullWikiCz() {
        final Game game = GameUtils.newGame(null);
        game.setWikiCz(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with game with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_NotPositiveMediaCount() {
        final Game game = GameUtils.newGame(null);
        game.setMediaCount(0);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with game with null other data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_NullOtherData() {
        final Game game = GameUtils.newGame(null);
        game.setOtherData(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateNewGame(Game)} with game with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGame_NullNote() {
        final Game game = GameUtils.newGame(null);
        game.setNote(null);

        gameValidator.validateNewGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGame_NullArgument() {
        gameValidator.validateExistingGame(null);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with game with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGame_NullId() {
        gameValidator.validateExistingGame(GameUtils.newGame(null));
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with game with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGame_NullName() {
        final Game game = GameUtils.newGame(1);
        game.setName(null);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with game with empty string as name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGame_EmptyName() {
        final Game game = GameUtils.newGame(1);
        game.setName("");

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with game with null URL to Wikipedia page about game is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGame_NullWiki() {
        final Game game = GameUtils.newGame(1);
        game.setWikiCz(null);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with game with not positive count of media.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGame_NotPositiveMediaCount() {
        final Game game = GameUtils.newGame(1);
        game.setMediaCount(0);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with game with null other data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGame_NullOtherData() {
        final Game game = GameUtils.newGame(1);
        game.setOtherData(null);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateExistingGame(Game)} with game with null note.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGame_NullNote() {
        final Game game = GameUtils.newGame(1);
        game.setNote(null);

        gameValidator.validateExistingGame(game);
    }

    /**
     * Test method for {@link GameValidator#validateGameWithId(Game)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGameWithId_NullArgument() {
        gameValidator.validateGameWithId(null);
    }

    /**
     * Test method for {@link GameValidator#validateGameWithId(Game)} with game with null ID.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGameWithId_NullId() {
        gameValidator.validateGameWithId(new Game());
    }

}
