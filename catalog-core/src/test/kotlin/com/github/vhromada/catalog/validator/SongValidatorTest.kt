package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.SongUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [SongValidator].
 *
 * @author Vladimir Hromada
 */
class SongValidatorTest {

    /**
     * Instance of [SongValidator]
     */
    private lateinit var validator: SongValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = SongValidator()
    }

    /**
     * Test method for [SongValidator.validate] with correct new song.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = SongUtils.newSong(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SongValidator.validate] with null new song.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NULL", message = "Song mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with new song with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val song = SongUtils.newSong(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = song, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with new song with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val song = SongUtils.newSong(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = validator.validate(data = song, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_POSITION_NOT_NULL", message = "Position must be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with new song with null name.
     */
    @Test
    fun validateNewNullName() {
        val song = SongUtils.newSong(id = null)
            .copy(name = null)

        val result = validator.validate(data = song, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with new song with empty name.
     */
    @Test
    fun validateNewEmptyName() {
        val song = SongUtils.newSong(id = null)
            .copy(name = "")

        val result = validator.validate(data = song, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with new song with null length of song.
     */
    @Test
    fun validateNewNullLength() {
        val song = SongUtils.newSong(id = null)
            .copy(length = null)

        val result = validator.validate(data = song, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_LENGTH_NULL", message = "Length of song mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with new song with negative length of song.
     */
    @Test
    fun validateNewNegativeLength() {
        val song = SongUtils.newSong(id = null)
            .copy(length = -1)

        val result = validator.validate(data = song, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_LENGTH_NEGATIVE", message = "Length of song mustn't be negative number.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with new song with null note.
     */
    @Test
    fun validateNewNullNote() {
        val song = SongUtils.newSong(id = null)
            .copy(note = null)

        val result = validator.validate(data = song, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with with update correct song.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = SongUtils.newSong(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SongValidator.validate] with null update song.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NULL", message = "Song mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with update song with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val song = SongUtils.newSong(id = 1)
            .copy(id = null)

        val result = validator.validate(data = song, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with update song with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val song = SongUtils.newSong(id = 1)
            .copy(position = null)

        val result = validator.validate(data = song, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_POSITION_NULL", message = "Position mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with update song with null name.
     */
    @Test
    fun validateUpdateNullName() {
        val song = SongUtils.newSong(id = 1)
            .copy(name = null)

        val result = validator.validate(data = song, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with update song with empty name.
     */
    @Test
    fun validateUpdateEmptyName() {
        val song = SongUtils.newSong(id = 1)
            .copy(name = "")

        val result = validator.validate(data = song, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with update song with null length of song.
     */
    @Test
    fun validateUpdateNullLength() {
        val song = SongUtils.newSong(id = 1)
            .copy(length = null)

        val result = validator.validate(data = song, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_LENGTH_NULL", message = "Length of song mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with update song with negative length of song.
     */
    @Test
    fun validateUpdateNegativeLength() {
        val song = SongUtils.newSong(id = 1)
            .copy(length = -1)

        val result = validator.validate(data = song, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_LENGTH_NEGATIVE", message = "Length of song mustn't be negative number.")))
        }
    }

    /**
     * Test method for [SongValidator.validate] with update song with null note.
     */
    @Test
    fun validateUpdateNullNote() {
        val song = SongUtils.newSong(id = 1)
            .copy(note = null)

        val result = validator.validate(data = song, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOTE_NULL", message = "Note mustn't be null.")))
        }
    }

    /**
     * Test method for [SongValidator.validateExists] with correct song.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(SongUtils.newSongDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SongValidator.validateExists] with invalid song.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOT_EXIST", message = "Song doesn't exist.")))
        }
    }

    /**
     * Test method for [SongValidator.validateMovingData] with correct up song.
     */
    @Test
    fun validateMovingDataUp() {
        val songs = listOf(SongUtils.newSongDomain(id = 1), SongUtils.newSongDomain(id = 2))

        val result = validator.validateMovingData(data = songs[1], list = songs, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SongValidator.validateMovingData] with with invalid up song.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val songs = listOf(SongUtils.newSongDomain(id = 1), SongUtils.newSongDomain(id = 2))

        val result = validator.validateMovingData(data = songs[0], list = songs, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOT_MOVABLE", message = "Song can't be moved up.")))
        }
    }

    /**
     * Test method for [SongValidator.validateMovingData] with correct down song.
     */
    @Test
    fun validateMovingDataDown() {
        val songs = listOf(SongUtils.newSongDomain(id = 1), SongUtils.newSongDomain(id = 2))

        val result = validator.validateMovingData(data = songs[0], list = songs, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [SongValidator.validateMovingData] with with invalid down song.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val songs = listOf(SongUtils.newSongDomain(id = 1), SongUtils.newSongDomain(id = 2))

        val result = validator.validateMovingData(data = songs[1], list = songs, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SONG_NOT_MOVABLE", message = "Song can't be moved down.")))
        }
    }

}
