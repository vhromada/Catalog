package cz.vhromada.catalog.validator;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.validator.AbstractMovableValidator;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A class represents validator for picture.
 *
 * @author Vladimir Hromada
 */
@Component("pictureValidator")
public class PictureValidator extends AbstractMovableValidator<Picture, cz.vhromada.catalog.domain.Picture> {

    /**
     * Creates a new instance of PictureValidator.
     *
     * @param pictureService service for pictures
     * @throws IllegalArgumentException if service for pictures is null
     */
    @Autowired
    public PictureValidator(final MovableService<cz.vhromada.catalog.domain.Picture> pictureService) {
        super("Picture", pictureService);
    }

    /**
     * Validates picture deeply.
     * <br>
     * Validation errors:
     * <ul>
     * <li>Content is null</li>
     * </ul>
     *
     * @param data   validating picture
     * @param result result with validation errors
     */
    @Override
    protected void validateDataDeep(final Picture data, final Result<Void> result) {
        if (data.getContent() == null) {
            result.addEvent(new Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null."));
        }
    }

}
