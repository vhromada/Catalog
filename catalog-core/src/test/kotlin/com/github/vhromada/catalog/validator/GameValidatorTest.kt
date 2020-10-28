package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Game
import com.github.vhromada.catalog.utils.GameUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.validator.MovableValidatorTest
import com.github.vhromada.common.validator.AbstractMovableValidator
import com.github.vhromada.common.validator.MovableValidator
import com.github.vhromada.common.validator.ValidationType
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [GameValidator].
 *
 * @author Vladimir Hromada
 */
class GameValidatorTest : MovableValidatorTest<Game, com.github.vhromada.catalog.domain.Game>() {

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null name.
     */
    @Test
    fun validateDeepNullName() {
        val game = getValidatingData(1)
                .copy(name = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_NULL", "Name mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with empty name.
     */
    @Test
    fun validateDeepEmptyName() {
        val game = getValidatingData(1)
                .copy(name = "")

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null URL to english Wikipedia page about game.
     */
    @Test
    fun validateDeepNullWikiEn() {
        val game = getValidatingData(1)
                .copy(wikiEn = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_WIKI_EN_NULL", "URL to english Wikipedia page about game mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null URL to czech Wikipedia page about game.
     */
    @Test
    fun validateDeepNullWikiCz() {
        val game = getValidatingData(1)
                .copy(wikiCz = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about game mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null count of media.
     */
    @Test
    fun validateDeepNullMediaCount() {
        val game = getValidatingData(1)
                .copy(mediaCount = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_MEDIA_COUNT_NULL", "Count of media mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with not positive count of media.
     */
    @Test
    fun validateDeepNotPositiveMediaCount() {
        val game = getValidatingData(1)
                .copy(mediaCount = 0)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_MEDIA_COUNT_NOT_POSITIVE", "Count of media must be positive number.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null format.
     */
    @Test
    fun validateDeepNullFormat() {
        val game = getValidatingData(1)
                .copy(format = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_FORMAT_NULL", "Format mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null other data.
     */
    @Test
    fun validateDeepNullOtherData() {
        val game = getValidatingData(1)
                .copy(otherData = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_OTHER_DATA_NULL", "Other data mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validateDeepNullNote() {
        val game = getValidatingData(1)
                .copy(note = null)

        val result = getValidator().validate(game, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NOTE_NULL", "Note mustn't be null.")))
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

    override fun getRepositoryData(validatingData: Game): com.github.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(validatingData.id)
    }

    override fun getItem1(): com.github.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(1)
    }

    override fun getItem2(): com.github.vhromada.catalog.domain.Game {
        return GameUtils.newGameDomain(2)
    }

    override fun getName(): String {
        return "Game"
    }

}
