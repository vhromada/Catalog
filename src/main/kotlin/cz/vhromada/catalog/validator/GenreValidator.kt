package cz.vhromada.catalog.validator

import cz.vhromada.catalog.entity.Genre
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Result
import cz.vhromada.common.result.Severity
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreValidator")
class GenreValidator(genreService: MovableService<cz.vhromada.catalog.domain.Genre>) : AbstractMovableValidator<Genre, cz.vhromada.catalog.domain.Genre>("Genre", genreService) {

    /**
     * Validates genre deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Name is null
     *  * Name is empty string
     *
     * @param data   validating genre
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Genre, result: Result<Unit>) {
        when {
            data.name == null -> {
                result.addEvent(Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string."))
            }
        }
    }

}
