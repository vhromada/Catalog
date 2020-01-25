package cz.vhromada.catalog.validator

import cz.vhromada.catalog.domain.Music
import cz.vhromada.catalog.entity.Song
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Result
import cz.vhromada.common.result.Severity
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.utils.sorted
import cz.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for song.
 *
 * @author Vladimir Hromada
 */
@Component("songValidator")
class SongValidator(musicService: MovableService<Music>) : AbstractMovableValidator<Song, Music>("Song", musicService) {

    override fun getData(data: Song): cz.vhromada.catalog.domain.Song? {
        for (music in service.getAll()) {
            for (song in music.songs) {
                if (data.id == song.id) {
                    return song
                }
            }
        }

        return null
    }

    override fun getList(data: Song): List<cz.vhromada.catalog.domain.Song> {
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
