package cz.vhromada.catalog.validator

import cz.vhromada.catalog.entity.Picture
import cz.vhromada.common.service.MovableService
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Result
import cz.vhromada.validation.result.Severity
import org.springframework.stereotype.Component

/**
 * A class represents validator for picture.
 *
 * @author Vladimir Hromada
 */
@Component("pictureValidator")
class PictureValidator(pictureService: MovableService<cz.vhromada.catalog.domain.Picture>) : AbstractMovableValidator<Picture, cz.vhromada.catalog.domain.Picture>("Picture", pictureService) {

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
