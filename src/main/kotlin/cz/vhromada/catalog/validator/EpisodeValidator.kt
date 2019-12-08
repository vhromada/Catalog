package cz.vhromada.catalog.validator

import cz.vhromada.catalog.domain.Show
import cz.vhromada.catalog.entity.Episode
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.utils.sorted
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Result
import cz.vhromada.validation.result.Severity
import org.springframework.stereotype.Component

/**
 * A class represents validator for episode.
 *
 * @author Vladimir Hromada
 */
@Component("episodeValidator")
class EpisodeValidator(showService: MovableService<Show>) : AbstractMovableValidator<Episode, Show>("Episode", showService) {

    override fun getData(data: Episode): cz.vhromada.catalog.domain.Episode? {
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

    override fun getList(data: Episode): List<cz.vhromada.catalog.domain.Episode> {
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
        if (data.number == null) {
            result.addEvent(Event(Severity.ERROR, "EPISODE_NUMBER_NULL", "Number of episode mustn't be null."))
        } else if (data.number <= 0) {
            result.addEvent(Event(Severity.ERROR, "EPISODE_NUMBER_NOT_POSITIVE", "Number of episode must be positive number."))
        }
        if (data.name == null) {
            result.addEvent(Event(Severity.ERROR, "EPISODE_NAME_NULL", "Name mustn't be null."))
        } else if (data.name.isBlank()) {
            result.addEvent(Event(Severity.ERROR, "EPISODE_NAME_EMPTY", "Name mustn't be empty string."))
        }
        if (data.length == null) {
            result.addEvent(Event(Severity.ERROR, "EPISODE_LENGTH_NULL", "Length of episode mustn't be null."))
        } else if (data.length < 0) {
            result.addEvent(Event(Severity.ERROR, "EPISODE_LENGTH_NEGATIVE", "Length of episode mustn't be negative number."))
        }
        if (data.note == null) {
            result.addEvent(Event(Severity.ERROR, "EPISODE_NOTE_NULL", "Note mustn't be null."))
        }
    }

}
