package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.service.MovableService
import com.github.vhromada.common.validator.AbstractMovableValidator
import org.springframework.stereotype.Component

/**
 * A class represents validator for picture.
 *
 * @author Vladimir Hromada
 */
@Component("pictureValidator")
class PictureValidator(pictureService: MovableService<com.github.vhromada.catalog.domain.Picture>) : AbstractMovableValidator<Picture, com.github.vhromada.catalog.domain.Picture>("Picture", pictureService) {

    /**
     * Validates picture deeply.
     * <br></br>
     * Validation errors:
     *
     *  * Content is null
     *
     * @param data   validating picture
     * @param result result with validation errors
     */
    override fun validateDataDeep(data: Picture, result: Result<Unit>) {
        if (data.content == null) {
            result.addEvent(Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null."))
        }
    }

}
