package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.common.GameUtils;
import cz.vhromada.catalog.entity.GameTO;
import cz.vhromada.catalog.validator.GameTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GameTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class GameTOValidatorImplTest {

    /**
     * Instance of {@link GameTOValidator}
     */
    private GameTOValidator gameTOValidator;

    /**
     * Initializes validator for TO for game.
     */
    @Before
    public void setUp() {
        gameTOValidator = new GameTOValidatorImpl();
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGameTO_NullArgument() {
        gameTOValidator.validateNewGameTO(null);
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with not null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NotNullId() {
        gameTOValidator.validateNewGameTO(GameUtils.newGameTO(1));
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullName() {
        final GameTO game = GameUtils.newGameTO(null);
        game.setName(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_EmptyName() {
        final GameTO game = GameUtils.newGameTO(null);
        game.setName("");

        gameTOValidator.validateNewGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null URL to english Wikipedia page about game is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullWikiEn() {
        final GameTO game = GameUtils.newGameTO(null);
        game.setWikiEn(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null URL to czech Wikipedia page about game is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullWikiCz() {
        final GameTO game = GameUtils.newGameTO(null);
        game.setWikiCz(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NotPositiveMediaCount() {
        final GameTO game = GameUtils.newGameTO(null);
        game.setMediaCount(0);

        gameTOValidator.validateNewGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullOtherData() {
        final GameTO game = GameUtils.newGameTO(null);
        game.setOtherData(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTO_NullNote() {
        final GameTO game = GameUtils.newGameTO(null);
        game.setNote(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGameTO_NullArgument() {
        gameTOValidator.validateExistingGameTO(null);
    }

    /**
     * Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullId() {
        gameTOValidator.validateExistingGameTO(GameUtils.newGameTO(null));
    }

    /**
     * Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullName() {
        final GameTO game = GameUtils.newGameTO(1);
        game.setName(null);

        gameTOValidator.validateExistingGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with empty string as name.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_EmptyName() {
        final GameTO game = GameUtils.newGameTO(1);
        game.setName("");

        gameTOValidator.validateExistingGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null URL to Wikipedia page about game is null.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullWiki() {
        final GameTO game = GameUtils.newGameTO(1);
        game.setWikiCz(null);

        gameTOValidator.validateExistingGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with not positive count of media.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NotPositiveMediaCount() {
        final GameTO game = GameUtils.newGameTO(1);
        game.setMediaCount(0);

        gameTOValidator.validateExistingGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null other data.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullOtherData() {
        final GameTO game = GameUtils.newGameTO(1);
        game.setOtherData(null);

        gameTOValidator.validateExistingGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null note.
     */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTO_NullNote() {
        final GameTO game = GameUtils.newGameTO(1);
        game.setNote(null);

        gameTOValidator.validateExistingGameTO(game);
    }

    /**
     * Test method for {@link GameTOValidator#validateGameTOWithId(GameTO)} with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGameTOWithId_NullArgument() {
        gameTOValidator.validateGameTOWithId(null);
    }

    /**
     * Test method for {@link GameTOValidator#validateGameTOWithId(GameTO)} with TO for game with null ID.
     */
    @Test(expected = ValidationException.class)
    public void testValidateGameTOWithId_NullId() {
        gameTOValidator.validateGameTOWithId(new GameTO());
    }

}
