package cz.vhromada.catalog.facade.validators.impl;

import cz.vhromada.catalog.commons.ObjectGeneratorTest;
import cz.vhromada.catalog.commons.ToGenerator;
import cz.vhromada.catalog.facade.to.GameTO;
import cz.vhromada.catalog.facade.validators.GameTOValidator;
import cz.vhromada.validators.exceptions.ValidationException;

import org.junit.Before;
import org.junit.Test;

/**
 * A class represents test for class {@link GameTOValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class GameTOValidatorImplTest extends ObjectGeneratorTest {

    /** Instance of {@link GameTOValidator} */
    private GameTOValidator gameTOValidator;

    /** Initializes validator for TO for game. */
    @Before
    public void setUp() {
        gameTOValidator = new GameTOValidatorImpl();
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNewGameTOWithNullArgument() {
        gameTOValidator.validateNewGameTO(null);
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with not null ID. */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTOWithNotNullId() {
        gameTOValidator.validateNewGameTO(ToGenerator.newGameWithId(getObjectGenerator()));
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null name. */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTOWithNullName() {
        final GameTO game = ToGenerator.newGame(getObjectGenerator());
        game.setName(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with empty string as name. */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTOWithEmptyName() {
        final GameTO game = ToGenerator.newGame(getObjectGenerator());
        game.setName("");

        gameTOValidator.validateNewGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null URL to english Wikipedia page about game is null. */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTOWithNullWikiEn() {
        final GameTO game = ToGenerator.newGame(getObjectGenerator());
        game.setWikiEn(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null URL to czech Wikipedia page about game is null. */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTOWithNullWikiCz() {
        final GameTO game = ToGenerator.newGame(getObjectGenerator());
        game.setWikiCz(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with not positive count of media. */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTOWithNotPositiveMediaCount() {
        final GameTO game = ToGenerator.newGame(getObjectGenerator());
        game.setMediaCount(0);

        gameTOValidator.validateNewGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null other data. */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTOWithNullOtherData() {
        final GameTO game = ToGenerator.newGame(getObjectGenerator());
        game.setOtherData(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateNewGameTO(GameTO)} with TO for game with null note. */
    @Test(expected = ValidationException.class)
    public void testValidateNewGameTOWithNullNote() {
        final GameTO game = ToGenerator.newGame(getObjectGenerator());
        game.setNote(null);

        gameTOValidator.validateNewGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateExistingGameTOWithNullArgument() {
        gameTOValidator.validateExistingGameTO(null);
    }

    /** Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null ID. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTOWithNullId() {
        gameTOValidator.validateExistingGameTO(ToGenerator.newGame(getObjectGenerator()));
    }

    /** Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null name. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTOWithNullName() {
        final GameTO game = ToGenerator.newGameWithId(getObjectGenerator());
        game.setName(null);

        gameTOValidator.validateExistingGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with empty string as name. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTOWithEmptyName() {
        final GameTO game = ToGenerator.newGameWithId(getObjectGenerator());
        game.setName("");

        gameTOValidator.validateExistingGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null URL to Wikipedia page about game is null. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTOWithNullWiki() {
        final GameTO game = ToGenerator.newGameWithId(getObjectGenerator());
        game.setWikiCz(null);

        gameTOValidator.validateExistingGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with not positive count of media. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTOWithNotPositiveMediaCount() {
        final GameTO game = ToGenerator.newGameWithId(getObjectGenerator());
        game.setMediaCount(0);

        gameTOValidator.validateExistingGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null other data. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTOWithNullOtherData() {
        final GameTO game = ToGenerator.newGameWithId(getObjectGenerator());
        game.setOtherData(null);

        gameTOValidator.validateExistingGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateExistingGameTO(GameTO)} with TO for game with null note. */
    @Test(expected = ValidationException.class)
    public void testValidateExistingGameTOWithNullNote() {
        final GameTO game = ToGenerator.newGameWithId(getObjectGenerator());
        game.setNote(null);

        gameTOValidator.validateExistingGameTO(game);
    }

    /** Test method for {@link GameTOValidator#validateGameTOWithId(GameTO)} with null argument. */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateGameTOWithIdWithNullArgument() {
        gameTOValidator.validateGameTOWithId(null);
    }

    /** Test method for {@link GameTOValidator#validateGameTOWithId(GameTO)} with TO for game with null ID. */
    @Test(expected = ValidationException.class)
    public void testValidateGameTOWithIdWithNullId() {
        gameTOValidator.validateGameTOWithId(ToGenerator.newGame(getObjectGenerator()));
    }

}
