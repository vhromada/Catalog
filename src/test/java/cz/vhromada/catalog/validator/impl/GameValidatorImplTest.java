package cz.vhromada.catalog.validator.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verifyZeroInteractions;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.Test;

/**
 * A class represents test for class {@link GameValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
public class GameValidatorImplTest extends AbstractValidatorTest<Game, cz.vhromada.catalog.domain.Game> {

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    public void validate_Deep_NullName() {
        final Game game = getValidatingData();
        game.setName(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertNotNull(result);
        assertNotNull(result.getEvents());
        assertEquals(Status.ERROR, result.getStatus());
        assertEquals(1, result.getEvents().size());
        assertEquals(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string."), result.getEvents().get(0));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    public void validate_Deep_EmptyName() {
        final Game game = getValidatingData();
        game.setName("");

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertNotNull(result);
        assertNotNull(result.getEvents());
        assertEquals(Status.ERROR, result.getStatus());
        assertEquals(1, result.getEvents().size());
        assertEquals(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string."), result.getEvents().get(0));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about game.
     */
    @Test
    public void validate_Deep_NullWikiEn() {
        final Game game = getValidatingData();
        game.setWikiEn(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertNotNull(result);
        assertNotNull(result.getEvents());
        assertEquals(Status.ERROR, result.getStatus());
        assertEquals(1, result.getEvents().size());
        assertEquals(new Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null."), result.getEvents().get(0));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about game.
     */
    @Test
    public void validate_Deep_NullWikiCz() {
        final Game game = getValidatingData();
        game.setWikiCz(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertNotNull(result);
        assertNotNull(result.getEvents());
        assertEquals(Status.ERROR, result.getStatus());
        assertEquals(1, result.getEvents().size());
        assertEquals(new Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null."), result.getEvents().get(0));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * count of media.
     */
    @Test
    public void validate_Deep_NotPositiveMediaCount() {
        final Game game = getValidatingData();
        game.setMediaCount(0);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertNotNull(result);
        assertNotNull(result.getEvents());
        assertEquals(Status.ERROR, result.getStatus());
        assertEquals(1, result.getEvents().size());
        assertEquals(new Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number."), result.getEvents().get(0));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null other data.
     */
    @Test
    public void validate_Deep_NullOtherData() {
        final Game game = getValidatingData();
        game.setOtherData(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertNotNull(result);
        assertNotNull(result.getEvents());
        assertEquals(Status.ERROR, result.getStatus());
        assertEquals(1, result.getEvents().size());
        assertEquals(new Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null."), result.getEvents().get(0));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    public void validate_Deep_NullNote() {
        final Game game = getValidatingData();
        game.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertNotNull(result);
        assertNotNull(result.getEvents());
        assertEquals(Status.ERROR, result.getStatus());
        assertEquals(1, result.getEvents().size());
        assertEquals(new Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null."), result.getEvents().get(0));

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Game> getCatalogValidator() {
        return new GameValidatorImpl(getCatalogService());
    }

    @Override
    protected Game getValidatingData() {
        return GameUtils.newGame(null);
    }

    @Override
    protected cz.vhromada.catalog.domain.Game getRepositoryData() {
        return GameUtils.newGameDomain(null);
    }

    @Override
    protected cz.vhromada.catalog.domain.Game getItem1() {
        return GameUtils.newGameDomain(1);
    }

    @Override
    protected cz.vhromada.catalog.domain.Game getItem2() {
        return GameUtils.newGameDomain(2);
    }

    @Override
    protected String getName() {
        return "Game";
    }

    @Override
    protected String getPrefix() {
        return "GAME";
    }

}
