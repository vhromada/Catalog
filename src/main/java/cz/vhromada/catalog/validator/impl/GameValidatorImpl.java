package cz.vhromada.catalog.validator.impl;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * A class represents implementation of validator for game.
 *
 * @author Vladimir Hromada
 */
@Component("gameValidator")
public class GameValidatorImpl extends AbstractCatalogValidator<Game, cz.vhromada.catalog.domain.Game> {

    /**
     * Creates a new instance of GameValidatorImpl.
     *
     * @param gameService service for games
     * @throws IllegalArgumentException if service for games is null
     */
    @Autowired
    public GameValidatorImpl(final CatalogService<cz.vhromada.catalog.domain.Game> gameService) {
        super("Game", gameService);
    }

    /**
     * Validates game deeply.
     * <br/>
     * Validation errors:
     * <ul>
     * <li>Name is null</li>
     * <li>Name is empty string</li>
     * <li>URL to english Wikipedia page about game is null</li>
     * <li>URL to czech Wikipedia page about game is null</li>
     * <li>Count of media isn't positive number</li>
     * <li>Other data is null</li>
     * <li>Note is null</li>
     * </ul>
     *
     * @param data   validating game
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Game data, final Result<Void> result) {
        if (data.getName() == null) {
            result.addEvent(new Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null."));
        } else if (StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getName().trim())) {
            result.addEvent(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string."));
        }
        if (data.getWikiEn() == null) {
            result.addEvent(new Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null."));
        }
        if (data.getWikiCz() == null) {
            result.addEvent(new Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null."));
        }
        if (data.getMediaCount() <= 0) {
            result.addEvent(new Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."));
        }
        if (data.getOtherData() == null) {
            result.addEvent(new Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null."));
        }
        if (data.getNote() == null) {
            result.addEvent(new Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null."));
        }
    }

}
