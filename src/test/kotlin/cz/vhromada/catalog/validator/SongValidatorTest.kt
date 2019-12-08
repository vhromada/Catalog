package cz.vhromada.catalog.validator

import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.domain.Music
import cz.vhromada.catalog.entity.Song
import cz.vhromada.catalog.utils.MusicUtils
import cz.vhromada.catalog.utils.SongUtils
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
 * A class represents test for class [SongValidator].
 *
 * @author Vladimir Hromada
 */
class SongValidatorTest : MovableValidatorTest<Song, Music>() {

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null name.
     */
    @Test
    fun validate_Deep_NullName() {
        val song = getValidatingData(1)
                .copy(name = null)

        val result = getValidator().validate(song, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with empty name.
     */
    @Test
    fun validate_Deep_EmptyName() {
        val song = getValidatingData(1)
                .copy(name = "")

        val result = getValidator().validate(song, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null length of song.
     */
    @Test
    fun validate_Deep_NullLength() {
        val song = getValidatingData(1)
                .copy(length = null)

        val result = getValidator().validate(song, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_LENGTH_NULL", "Length of song mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with negative length of song.
     */
    @Test
    fun validate_Deep_NegativeLength() {
        val song = getValidatingData(1)
                .copy(length = -1)

        val result = getValidator().validate(song, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validate_Deep_NullNote() {
        val song = getValidatingData(1)
                .copy(note = null)

        val result = getValidator().validate(song, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<Song> {
        return SongValidator(service)
    }

    override fun getValidatingData(id: Int?): Song {
        return SongUtils.newSong(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Song {
        return SongUtils.newSong(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Song): Music {
        return MusicUtils.newMusicWithSongs(validatingData.id)
    }

    override fun getItem1(): Music {
        return MusicUtils.newMusicDomain(null)
    }

    override fun getItem2(): Music {
        return MusicUtils.newMusicDomain(null)
    }

    override fun getName(): String {
        return "Song"
    }

    override fun initExistsMock(validatingData: Song, exists: Boolean) {
        val music = if (exists) MusicUtils.newMusicWithSongs(validatingData.id) else MusicUtils.newMusicDomain(Integer.MAX_VALUE)

        whenever(service.getAll()).thenReturn(listOf(music))
    }

    override fun verifyExistsMock(validatingData: Song) {
        verify(service).getAll()
        verifyNoMoreInteractions(service)
    }

    override fun initMovingMock(validatingData: Song, up: Boolean, valid: Boolean) {
        val songs = if (up && valid || !up && !valid) {
            listOf(SongUtils.newSongDomain(1), SongUtils.newSongDomain(validatingData.id))
        } else {
            listOf(SongUtils.newSongDomain(validatingData.id), SongUtils.newSongDomain(Integer.MAX_VALUE))
        }
        val music = MusicUtils.newMusicDomain(1)
                .copy(songs = songs)

        whenever(service.getAll()).thenReturn(listOf(music))
    }

    override fun verifyMovingMock(validatingData: Song) {
        verify(service, times(2)).getAll()
        verifyNoMoreInteractions(service)
    }

}
