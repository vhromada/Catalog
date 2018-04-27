package cz.vhromada.catalog.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collections;

import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.utils.PictureUtils;
import cz.vhromada.common.Movable;
import cz.vhromada.common.service.MovableService;
import cz.vhromada.common.test.validator.MovableValidatorTest;
import cz.vhromada.common.validator.MovableValidator;
import cz.vhromada.common.validator.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link PictureValidator}.
 *
 * @author Vladimir Hromada
 */
class PictureValidatorTest extends MovableValidatorTest<Picture, cz.vhromada.catalog.domain.Picture> {

    /**
     * Test method for {@link PictureValidator#PictureValidator(MovableService)} with null service for pictures.
     */
    @Test
    void constructor_NullPictureService() {
        assertThatThrownBy(() -> new PictureValidator(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link PictureValidator#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    void validate_Deep_NullContent() {
        final Picture picture = getValidatingData(1);
        picture.setContent(null);

        final Result<Void> result = getMovableValidator().validate(picture, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null.")));
        });

        verifyZeroInteractions(getMovableService());
    }

    @Override
    protected MovableValidator<Picture> getMovableValidator() {
        return new PictureValidator(getMovableService());
    }

    @Override
    protected Picture getValidatingData(final Integer id) {
        return PictureUtils.newPicture(id);
    }

    @Override
    protected Picture getValidatingData(final Integer id, final Integer position) {
        final Picture picture = PictureUtils.newPicture(id);
        picture.setPosition(position);

        return picture;
    }

    @Override
    protected cz.vhromada.catalog.domain.Picture getRepositoryData(final Picture validatingData) {
        return PictureUtils.newPictureDomain(validatingData.getId());
    }

    @Override
    protected cz.vhromada.catalog.domain.Picture getItem1() {
        return PictureUtils.newPictureDomain(1);
    }

    @Override
    protected cz.vhromada.catalog.domain.Picture getItem2() {
        return PictureUtils.newPictureDomain(2);
    }

    @Override
    protected String getName() {
        return "Picture";
    }

}
