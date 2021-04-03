package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.MusicUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [MusicValidator].
 *
 * @author Vladimir Hromada
 */
class MusicValidatorTest {

    /**
     * Instance of [MusicValidator]
     */
    private lateinit var validator: MusicValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = MusicValidator()
    }

    /**
     * Test method for [MusicValidator.validate] with correct new music.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = MusicUtils.newMusic(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [MusicValidator.validate] with null new music.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NULL", message = "Music mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val music = MusicUtils.newMusic(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val music = MusicUtils.newMusic(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_POSITION_NOT_NULL", message = "Position must be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with null name.
     */
    @Test
    fun validateNewNullName() {
        val music = MusicUtils.newMusic(id = null)
            .copy(name = null)

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with empty name.
     */
    @Test
    fun validateNewEmptyName() {
        val music = MusicUtils.newMusic(id = null)
            .copy(name = "")

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with null URL to english Wikipedia page about music.
     */
    @Test
    fun validateNewNullWikiEn() {
        val music = MusicUtils.newMusic(id = null)
            .copy(wikiEn = null)

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_WIKI_EN_NULL", message = "URL to english Wikipedia page about music mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with null URL to czech Wikipedia page about music.
     */
    @Test
    fun validateNewNullWikiCz() {
        val music = MusicUtils.newMusic(id = null)
            .copy(wikiCz = null)

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about music mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with null count of media.
     */
    @Test
    fun validateNewNullMediaCount() {
        val music = MusicUtils.newMusic(id = null)
            .copy(mediaCount = null)

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with  not positive count of media.
     */
    @Test
    fun validateNewNotPositiveMediaCount() {
        val music = MusicUtils.newMusic(id = null)
            .copy(mediaCount = 0)

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with new music with null note.
     */
    @Test
    fun validateNewNullNote() {
        val music = MusicUtils.newMusic(id = null)
            .copy(note = null)

        val result = validator.validate(data = music, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with with update correct music.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = MusicUtils.newMusic(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [MusicValidator.validate] with null update music.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NULL", message = "Music mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(id = null)

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(position = null)

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_POSITION_NULL", message = "Position mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with null name.
     */
    @Test
    fun validateUpdateNullName() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(name = null)

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with empty name.
     */
    @Test
    fun validateUpdateEmptyName() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(name = "")

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with null URL to english Wikipedia page about music.
     */
    @Test
    fun validateUpdateNullWikiEn() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(wikiEn = null)

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_WIKI_EN_NULL", message = "URL to english Wikipedia page about music mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with null URL to czech Wikipedia page about music.
     */
    @Test
    fun validateUpdateNullWikiCz() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(wikiCz = null)

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about music mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with null count of media.
     */
    @Test
    fun validateUpdateNullMediaCount() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(mediaCount = null)

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NULL", message = "Count of media mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with not positive count of media.
     */
    @Test
    fun validateUpdateNotPositiveMediaCount() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(mediaCount = 0)

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_MEDIA_COUNT_NOT_POSITIVE", message = "Count of media must be positive number.")))
        }
    }

    /**
     * Test method for [MusicValidator.validate] with update music with null note.
     */
    @Test
    fun validateUpdateNullNote() {
        val music = MusicUtils.newMusic(id = 1)
            .copy(note = null)

        val result = validator.validate(data = music, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [MusicValidator.validateExists] with correct music.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(MusicUtils.newMusicDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [MusicValidator.validateExists] with invalid music.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOT_EXIST", message = "Music doesn't exist.")))
        }
    }

    /**
     * Test method for [MusicValidator.validateMovingData] with correct up music.
     */
    @Test
    fun validateMovingDataUp() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        val result = validator.validateMovingData(data = musicList[1], list = musicList, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [MusicValidator.validateMovingData] with with invalid up music.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        val result = validator.validateMovingData(data = musicList[0], list = musicList, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOT_MOVABLE", message = "Music can't be moved up.")))
        }
    }

    /**
     * Test method for [MusicValidator.validateMovingData] with correct down music.
     */
    @Test
    fun validateMovingDataDown() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        val result = validator.validateMovingData(data = musicList[0], list = musicList, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [MusicValidator.validateMovingData] with with invalid down music.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val musicList = listOf(MusicUtils.newMusicDomain(id = 1), MusicUtils.newMusicDomain(id = 2))

        val result = validator.validateMovingData(data = musicList[1], list = musicList, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MUSIC_NOT_MOVABLE", message = "Music can't be moved down.")))
        }
    }

}
