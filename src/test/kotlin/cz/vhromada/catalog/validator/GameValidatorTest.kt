package cz.vhromada.catalog.validator

import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import cz.vhromada.catalog.entity.Game
import cz.vhromada.catalog.utils.GameUtils
import cz.vhromada.common.test.validator.MovableValidatorTest
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.common.validator.ValidationType
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Severity
import cz.vhromada.validation.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [GameValidator].
 *
 * @author Vladimir Hromada
 */
class GameValidatorTest : MovableValidatorTest<Game, cz.vhromada.catalog.domain.Game>() {

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null name.
     */
    @Test
    fun validate_Deep_NullName() {
        val game = getValidatingData(1)
                .copy(name = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NAME_NULL", "Name mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with empty name.
     */
    @Test
    fun validate_Deep_EmptyName() {
        val game = getValidatingData(1)
                .copy(name = "")

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null URL to english Wikipedia page about game.
     */
    @Test
    fun validate_Deep_NullWikiEn() {
        val game = getValidatingData(1)
                .copy(wikiEn = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null URL to czech Wikipedia page about game.
     */
    @Test
    fun validate_Deep_NullWikiCz() {
        val game = getValidatingData(1)
                .copy(wikiCz = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about game mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null count of media.
     */
    @Test
    fun validate_Deep_NullMediaCount() {
        val game = getValidatingData(1)
                .copy(mediaCount = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with not positive count of media.
     */
    @Test
    fun validate_Deep_NotPositiveMediaCount() {
        val game = getValidatingData(1)
                .copy(mediaCount = 0)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null other data.
     */
    @Test
    fun validate_Deep_NullOtherData() {
        val game = getValidatingData(1)
                .copy(otherData = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validate_Deep_NullNote() {
        val game = getValidatingData(1)
                .copy(note = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GAME_NOTE_NULL", "Note mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<Game> {
        return GameValidator(service)
    }

    override fun getValidatingData(id: Int?): Game {
        return GameUtils.newGame(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Game {
        return GameUtils.newGame(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Game): cz.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(validatingData.id)
    }

    override fun getItem1(): cz.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(1)
    }

    override fun getItem2(): cz.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(2)
    }

    override fun getName(): String {
        return "Game"
    }

}