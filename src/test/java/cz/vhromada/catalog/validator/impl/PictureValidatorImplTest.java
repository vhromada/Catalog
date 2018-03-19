package cz.vhromada.catalog.validator.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Collections;

import cz.vhromada.catalog.common.Movable;
import cz.vhromada.catalog.entity.Picture;
import cz.vhromada.catalog.service.CatalogService;
import cz.vhromada.catalog.utils.PictureUtils;
import cz.vhromada.catalog.validator.CatalogValidator;
import cz.vhromada.catalog.validator.common.ValidationType;
import cz.vhromada.result.Event;
import cz.vhromada.result.Result;
import cz.vhromada.result.Severity;
import cz.vhromada.result.Status;

import org.junit.jupiter.api.Test;

/**
 * A class represents test for class {@link PictureValidatorImpl}.
 *
 * @author Vladimir Hromada
 */
class PictureValidatorImplTest extends AbstractValidatorTest<Picture, cz.vhromada.catalog.domain.Picture> {

    /**
     * Test method for {@link PictureValidatorImpl#PictureValidatorImpl(CatalogService)} with null service for pictures.
     */
    @Test
    void constructor_NullPictureService() {
        assertThatThrownBy(() -> new PictureValidatorImpl(null)).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test method for {@link PictureValidatorImpl#validate(Movable, ValidationType...)} with {@link ValidationType#DEEP} with data with null name.
     */
    @Test
    void validate_Deep_NullContent() {
        final Picture picture = getValidatingData(1);
        picture.setContent(null);

        final Result<Void> result = getCatalogValidator().validate(picture, ValidationType.DEEP);

        assertSoftly(softly -> {
            softly.assertThat(result.getStatus()).isEqualTo(Status.ERROR);
            softly.assertThat(result.getEvents())
                .isEqualTo(Collections.singletonList(new Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null.")));
        });

        verifyZeroInteractions(getCatalogService());
    }

    @Override
    protected CatalogValidator<Picture> getCatalogValidator() {
        return new PictureValidatorImpl(getCatalogService());
    }

    @Override
    protected Picture getValidatingData(final Integer id) {
        return PictureUtils.newPicture(id);
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
