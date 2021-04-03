package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [EpisodeValidator].
 *
 * @author Vladimir Hromada
 */
class EpisodeValidatorTest {

    /**
     * Instance of [EpisodeValidator]
     */
    private lateinit var validator: EpisodeValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = EpisodeValidator()
    }

    /**
     * Test method for [EpisodeValidator.validate] with correct new episode.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = EpisodeUtils.newEpisode(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with null new episode.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NULL", message = "Episode mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_POSITION_NOT_NULL", message = "Position must be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with null number of episode.
     */
    @Test
    fun validateNewNullNumber() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(number = null)

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NULL", message = "Number of episode mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with not positive number of episode.
     */
    @Test
    fun validateNewNotPositiveNumber() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(number = 0)

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NOT_POSITIVE", message = "Number of episode must be positive number.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with null name.
     */
    @Test
    fun validateNewNullName() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(name = null)

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with empty name.
     */
    @Test
    fun validateNewEmptyName() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(name = "")

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with null length of episode.
     */
    @Test
    fun validateNewNullLength() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(length = null)

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NULL", message = "Length of episode mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with negative length of episode.
     */
    @Test
    fun validateNewNegativeLength() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(length = -1)

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NEGATIVE", message = "Length of episode mustn't be negative number.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with new episode with null note.
     */
    @Test
    fun validateNewNullNote() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(note = null)

        val result = validator.validate(data = episode, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with with update correct episode.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = EpisodeUtils.newEpisode(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with null update episode.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NULL", message = "Episode mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(id = null)

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(position = null)

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_POSITION_NULL", message = "Position mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with null number of episode.
     */
    @Test
    fun validateUpdateNullNumber() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(number = null)

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NULL", message = "Number of episode mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with not positive number of episode.
     */
    @Test
    fun validateUpdateNotPositiveNumber() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(number = 0)

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NOT_POSITIVE", message = "Number of episode must be positive number.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with null name.
     */
    @Test
    fun validateUpdateNullName() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(name = null)

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with empty name.
     */
    @Test
    fun validateUpdateEmptyName() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(name = "")

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with null length of episode.
     */
    @Test
    fun validateUpdateNullLength() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(length = null)

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NULL", message = "Length of episode mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with negative length of episode.
     */
    @Test
    fun validateUpdateNegativeLength() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(length = -1)

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NEGATIVE", message = "Length of episode mustn't be negative number.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validate] with update episode with null note.
     */
    @Test
    fun validateUpdateNullNote() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(note = null)

        val result = validator.validate(data = episode, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validateExists] with correct episode.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(EpisodeUtils.newEpisodeDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [EpisodeValidator.validateExists] with invalid episode.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOT_EXIST", message = "Episode doesn't exist.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validateMovingData] with correct up episode.
     */
    @Test
    fun validateMovingDataUp() {
        val episodes = listOf(EpisodeUtils.newEpisodeDomain(id = 1), EpisodeUtils.newEpisodeDomain(id = 2))

        val result = validator.validateMovingData(data = episodes[1], list = episodes, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [EpisodeValidator.validateMovingData] with with invalid up episode.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val episodes = listOf(EpisodeUtils.newEpisodeDomain(id = 1), EpisodeUtils.newEpisodeDomain(id = 2))

        val result = validator.validateMovingData(data = episodes[0], list = episodes, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOT_MOVABLE", message = "Episode can't be moved up.")))
        }
    }

    /**
     * Test method for [EpisodeValidator.validateMovingData] with correct down episode.
     */
    @Test
    fun validateMovingDataDown() {
        val episodes = listOf(EpisodeUtils.newEpisodeDomain(id = 1), EpisodeUtils.newEpisodeDomain(id = 2))

        val result = validator.validateMovingData(data = episodes[0], list = episodes, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [EpisodeValidator.validateMovingData] with with invalid down episode.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val episodes = listOf(EpisodeUtils.newEpisodeDomain(id = 1), EpisodeUtils.newEpisodeDomain(id = 2))

        val result = validator.validateMovingData(data = episodes[1], list = episodes, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOT_MOVABLE", message = "Episode can't be moved down.")))
        }
    }

}
