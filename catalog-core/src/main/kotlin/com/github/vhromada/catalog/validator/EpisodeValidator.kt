package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.domain.Show
import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.utils.sorted
import com.github.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeValidator")
class EpisodeValidator(showService: MovableService<Show>) : AbstractMovableValidator<Episode, Show>("Episode", showService) {

    override fun getData(data: Episode): com.github.vhromada.catalog.domain.Episode? {
        for (show in service.getAll()) {
            for (season in show.seasons) {
                for (episode in season.episodes) {
                    if (data.id == episode.id) {
                        return episode
                    }
                }
            }
        }

        return null
    }

    override fun getList(data: Episode): List<com.github.vhromada.catalog.domain.Episode> {
        for (show in service.getAll()) {
            for (season in show.seasons) {
                for (episode in season.episodes) {
                    if (data.id == episode.id) {
                        return season.episodes
                                .sorted()
                    }
                }
            }
        }

        return emptyList()
    }

    /**
     * Validates episode deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Number of episode is null
     *  * Number of episode isn't positive number
     *  * Name is null
     *  * Name is empty string
     *  * Length of episode is null
     *  * Length of episode is negative value
     *  * Note is null
     *
     * @param data   validating episode
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Episode, result: Result<Unit>) {
        when {
            data.number == null -> {
                result.addEvent(Event(Severity.ERROR, "EPISODE_NUMBER_NULL", "Number of episode mustn't be null."))
            }
            data.number <= 0 -> {
                result.addEvent(Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number."))
            }
        }
        when {
            data.name == null -> {
                result.addEvent(Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string."))
            }
        }
        when {
            data.length == null -> {
                result.addEvent(Event(Severity.ERROR, "EPISODE_LENGTH_NULL", "Length of episode mustn't be null."))
            }
            data.length < 0 -> {
                result.addEvent(Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number."))
            }
        }
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null."))
        }
    }

}
