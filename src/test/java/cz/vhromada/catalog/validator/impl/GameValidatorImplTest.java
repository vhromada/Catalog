package cz.vhromada.catalog.validator.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
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
        final Game game = getValidatingData(1);
        game.setName(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    public void validate_Deep_EmptyName() {
        final Game game = getValidatingData(1);
        game.setName("");

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about game.
     */
    @Test
    public void validate_Deep_NullWikiEn() {
        final Game game = getValidatingData(1);
        game.setWikiEn(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about game.
     */
    @Test
    public void validate_Deep_NullWikiCz() {
        final Game game = getValidatingData(1);
        game.setWikiCz(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_WIKI_CZ_NULL", "URL to czech Wikipedia page about game mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * count of media.
     */
    @Test
    public void validate_Deep_NotPositiveMediaCount() {
        final Game game = getValidatingData(1);
        game.setMediaCount(0);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null other data.
     */
    @Test
    public void validate_Deep_NullOtherData() {
        final Game game = getValidatingData(1);
        game.setOtherData(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    /**
     * Test method for {@link GameValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    public void validate_Deep_NullNote() {
        final Game game = getValidatingData(1);
        game.setNote(null);

        final Result<Void> result = getCatalogValidator().validate(game, ValidationType.DEEP);

        assertThat(result, is(notNullValue()));
        assertThat(result.getEvents(), is(notNullValue()));
        assertThat(result.getStatus(), is(Status.ERROR));
        assertThat(result.getEvents().size(), is(1));
        assertThat(result.getEvents().get(0), is(new Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")));

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Game> getCatalogValidator() {
        return new GameValidatorImpl(getCatalogService());
    }

    @Override
    protected Game getValidatingData(final Integer id) {
        return GameUtils.newGame(id);
    }

    @Override
    protected cz.vhromada.catalog.domain.Game getRepositoryData(final Game validatingData) {
        return GameUtils.newGameDomain(validatingData.getId());
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
