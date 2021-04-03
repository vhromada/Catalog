package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.validator.AbstractValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for genre.
 *
 * @author Vladimir Hromada
 */
@Component("genreValidator")
class GenreValidator : AbstractValidator<Genre, com.github.vhromada.catalog.domain.Genre>(name = "Genre") {

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
                result.addEvent(event = Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null."))
            }
            data.name.isBlank() -> {
                result.addEvent(event = Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string."))
            }
        }
    }

}
