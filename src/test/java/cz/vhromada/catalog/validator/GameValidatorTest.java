package cz.vhromada.catalog.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collections;

import cz.vhromada.catalog.entity.Game;
import cz.vhromada.catalog.utils.GameUtils;
import cz.vhromada.common.Movable;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.validator.MovableValidatorTest;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link GameValidator}.
 *
 * @author Vladimir Hromada
 */
class GameValidatorTest extends MovableValidatorTest<Game, cz.vhromada.catalog.domain.Game> {

    /**
     * Test method for {@link GameValidator#GameValidator(MovableService)} with null service for games.
     */
    @Test
    void constructor_NullGameService() {
        assertThatThrownBy(() -> new GameValidator(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link GameValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    void validate_Deep_NullName() {
        final Game game = getValidatingData(1);
        game.setName(null);

        final Result<Void> result = getMovableValidator().validate(game, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link GameValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with empty name.
     */
    @Test
    void validate_Deep_EmptyName() {
        final Game game = getValidatingData(1);
        game.setName("");

        final Result<Void> result = getMovableValidator().validate(game, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link GameValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to english
     * Wikipedia page about game.
     */
    @Test
    void validate_Deep_NullWikiEn() {
        final Game game = getValidatingData(1);
        game.setWikiEn(null);

        final Result<Void> result = getMovableValidator().validate(game, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GAME_WIKI_EN_NULL",
                "URL to english Wikipedia page about game mustn't be null.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link GameValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null URL to czech
     * Wikipedia page about game.
     */
    @Test
    void validate_Deep_NullWikiCz() {
        final Game game = getValidatingData(1);
        game.setWikiCz(null);

        final Result<Void> result = getMovableValidator().validate(game, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents()).isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GAME_WIKI_CZ_NULL",
                "URL to czech Wikipedia page about game mustn't be null.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link GameValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with not positive
     * count of media.
     */
    @Test
    void validate_Deep_NotPositiveMediaCount() {
        final Game game = getValidatingData(1);
        game.setMediaCount(0);

        final Result<Void> result = getMovableValidator().validate(game, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link GameValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null other data.
     */
    @Test
    void validate_Deep_NullOtherData() {
        final Game game = getValidatingData(1);
        game.setOtherData(null);

        final Result<Void> result = getMovableValidator().validate(game, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    /**
     * Test method for {@link GameValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null note.
     */
    @Test
    void validate_Deep_NullNote() {
        final Game game = getValidatingData(1);
        game.setNote(null);

        final Result<Void> result = getMovableValidator().validate(game, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    @Override
    protected MovableValidator<Game> getMovableValidator() {
        return new GameValidator(getMovableService());
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

}
