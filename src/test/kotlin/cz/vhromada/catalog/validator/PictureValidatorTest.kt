package cz.vhromada.catalog.validator

import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import cz.vhromada.catalog.entity.Picture
import cz.vhromada.catalog.utils.PictureUtils
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Severity
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.validator.MovableValidatorTest
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.common.validator.ValidationType
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [PictureValidator].
 *
 * @author Vladimir Hromada
 */
class PictureValidatorTest : MovableValidatorTest<Picture, cz.vhromada.catalog.domain.Picture>() {

    /**
     * Test method for [AbstractMovableValidator.validate] with [ValidationType.DEEP] with data with null content.
     */
    @Test
    fun validateDeepNullContent() {
        val picture = getValidatingData(1)
                .copy(content = null)

        val result = getValidator().validate(picture, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PICTURE_CONTENT_NULL", "Content mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<Picture> {
        return PictureValidator(service)
    }

    override fun getValidatingData(id: Int?): Picture {
        return PictureUtils.newPicture(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Picture {
        return PictureUtils.newPicture(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Picture): cz.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(validatingData.id)
    }

    override fun getItem1(): cz.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(1)
    }

    override fun getItem2(): cz.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(2)
    }

    override fun getName(): String {
        return "Picture"
    }

}
