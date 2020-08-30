package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.domain.Music
import com.github.vhromada.catalog.entity.Song
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.sorted
import com.github.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for song.
 *
 * @author Vladimir Hromada
 */
@Component("songValidator")
class SongValidator(musicService: MovableService<Music>) : AbstractMovableValidator<Song, Music>("Song", musicService) {

    override fun getData(data: Song): com.github.vhromada.catalog.domain.Song? {
        for (music in service.getAll()) {
            for (song in music.songs) {
                if (data.id == song.id) {
                    return song
                }
            }
        }

        return null
    }

    override fun getList(data: Song): List<com.github.vhromada.catalog.domain.Song> {
        for (music in service.getAll()) {
            for (song in music.songs) {
                if (data.id == song.id) {
                    return music.songs
                            .sorted()
                }
            }
        }

        return emptyList()
    }

    /**
     * Validates song deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Name is null
     *  * Name is empty string
     *  * Length of song is null
     *  * Length of song is negative value
     *  * Note is null
     *
     * @param data   validating song
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Song, result: Result<Unit>) {
        when {
            data.name == null -> {
                result.addEvent(Event(Severity.ERROR, "SONG_NAME_NULL", "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "SONG_NAME_EMPTY", "Name mustn't be empty string."))
            }
        }
        when {
            data.length == null -> {
                result.addEvent(Event(Severity.ERROR, "SONG_LENGTH_NULL", "Length of song mustn't be null."))
            }
            data.length < 0 -> {
                result.addEvent(Event(Severity.ERROR, "SONG_LENGTH_NEGATIVE", "Length of song mustn't be negative number."))
            }
        }
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "SONG_NOTE_NULL", "Note mustn't be null."))
        }
    }

}
