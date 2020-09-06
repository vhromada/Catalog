package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.domain.Show
import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.validator.MovableValidatorTest
import com.github.vhromada.common.validator.MovableValidator
import com.github.vhromada.common.validator.ValidationType
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [EpisodeValidator].
 *
 * @author Vladimir Hromada
 */
class EpisodeValidatorTest : MovableValidatorTest<Episode, Show>() {

    /**
     * Test method for [EpisodeValidator.validate] with [ValidationType.DEEP] with data with null number of episode.
     */
    @Test
    fun validateDeepNullNumber() {
        val episode = getValidatingData(1)
                .copy(number = null)

        val result = getValidator().validate(episode, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "EPISODE_NUMBER_NULL", "Number of episode mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [EpisodeValidator.validate] with [ValidationType.DEEP] with data with not positive number of episode.
     */
    @Test
    fun validateDeepNotPositiveNumber() {
        val episode = getValidatingData(1)
                .copy(number = 0)

        val result = getValidator().validate(episode, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [EpisodeValidator.validate] with [ValidationType.DEEP] with data with null name.
     */
    @Test
    fun validateDeepNullName() {
        val episode = getValidatingData(1)
                .copy(name = null)

        val result = getValidator().validate(episode, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [EpisodeValidator.validate] with [ValidationType.DEEP] with data with empty name.
     */
    @Test
    fun validateDeepEmptyName() {
        val episode = getValidatingData(1)
                .copy(name = "")

        val result = getValidator().validate(episode, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [EpisodeValidator.validate] with [ValidationType.DEEP] with data with null length of episode.
     */
    @Test
    fun validateDeepNullLength() {
        val episode = getValidatingData(1)
                .copy(length = null)

        val result = getValidator().validate(episode, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "EPISODE_LENGTH_NULL", "Length of episode mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [EpisodeValidator.validate] with [ValidationType.DEEP] with data with negative length of episode.
     */
    @Test
    fun validateDeepNegativeLength() {
        val episode = getValidatingData(1)
                .copy(length = -1)

        val result = getValidator().validate(episode, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [EpisodeValidator.validate] with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validateDeepNullNote() {
        val episode = getValidatingData(1)
                .copy(note = null)

        val result = getValidator().validate(episode, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<Episode> {
        return EpisodeValidator(service)
    }

    override fun getValidatingData(id: Int?): Episode {
        return EpisodeUtils.newEpisode(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Episode {
        return EpisodeUtils.newEpisode(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Episode): Show {
        return ShowUtils.newShowWithSeasons(validatingData.id)
    }

    override fun getItem1(): Show {
        return ShowUtils.newShowDomain(null)
    }

    override fun getItem2(): Show {
        return ShowUtils.newShowDomain(null)
    }

    override fun getName(): String {
        return "Episode"
    }

    override fun initExistsMock(validatingData: Episode, exists: Boolean) {
        val show = if (exists) ShowUtils.newShowWithSeasons(validatingData.id) else ShowUtils.newShowDomain(Int.MAX_VALUE)

        whenever(service.getAll()).thenReturn(listOf(show))
    }

    override fun verifyExistsMock(validatingData: Episode) {
        verify(service).getAll()
        verifyNoMoreInteractions(service)
    }

    override fun initMovingMock(validatingData: Episode, up: Boolean, valid: Boolean) {
        val episodes = if (up && valid || !up && !valid) {
            listOf(EpisodeUtils.newEpisodeDomain(1), EpisodeUtils.newEpisodeDomain(validatingData.id))
        } else {
            listOf(EpisodeUtils.newEpisodeDomain(validatingData.id), EpisodeUtils.newEpisodeDomain(Int.MAX_VALUE))
        }
        val season = SeasonUtils.newSeasonDomain(1)
                .copy(episodes = episodes)
        val show = ShowUtils.newShowDomain(1)
                .copy(seasons = listOf(season))

        whenever(service.getAll()).thenReturn(listOf(show))
    }

    override fun verifyMovingMock(validatingData: Episode) {
        verify(service, times(2)).getAll()
        verifyNoMoreInteractions(service)
    }

}
