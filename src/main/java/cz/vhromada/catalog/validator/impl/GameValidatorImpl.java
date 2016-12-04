package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.validator.GameValidator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for game.
 *
 * @author Vladimir Hromada
 */
@Component("gameValidator")
public class GameValidatorImpl implements GameValidator {

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateNewGame(final Game game) {
        validateGame(game);
        Assert.isNull(game.getId(), "ID must be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateExistingGame(final Game game) {
        validateGame(game);
        Assert.notNull(game.getId(), "ID mustn't be null.");
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void validateGameWithId(final Game game) {
        Assert.notNull(game, "Game mustn't be null.");
        Assert.notNull(game.getId(), "ID mustn't be null.");
    }

    /**
     * Validates game.
     *
     * @param game game
     * @throws IllegalArgumentException if game is null
     *                                  or name is null
     *                                  or name is empty string
     *                                  or URL to english Wikipedia page about game is null
     *                                  or URL to czech Wikipedia page about game is null
     *                                  or count of media isn't positive number
     *                                  or other data is null
     *                                  or note is null
     */
    private static void validateGame(final Game game) {
        Assert.notNull(game, "Game mustn't be null.");
        Assert.notNull(game.getName(), "Name mustn't be null");
        Assert.isTrue(!StringUtils.isEmpty(game.getName()) && !StringUtils.isEmpty(game.getName().trim()), "Name mustn't be empty string.");
        Assert.notNull(game.getWikiEn(), "URL to english Wikipedia page about game mustn't be null.");
        Assert.notNull(game.getWikiCz(), "URL to czech Wikipedia page about game mustn't be null.");
        Assert.isTrue(game.getMediaCount() > 0, "Count of media must be positive number.");
        Assert.notNull(game.getOtherData(), "Other data mustn't be null.");
        Assert.notNull(game.getNote(), "Note mustn't be null.");
    }

}
