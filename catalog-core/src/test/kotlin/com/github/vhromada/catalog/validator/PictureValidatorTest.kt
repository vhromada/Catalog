package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.validator.MovableValidatorTest
import com.github.vhromada.common.validator.AbstractMovableValidator
import com.github.vhromada.common.validator.MovableValidator
import com.github.vhromada.common.validator.ValidationType
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [PictureValidator].
 *
 * @author Vladimir Hromada
 */
class PictureValidatorTest : MovableValidatorTest<Picture, com.github.vhromada.catalog.domain.Picture>() {

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
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_CONTENT_NULL", "Content mustn't be null.")))
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

    override fun getRepositoryData(validatingData: Picture): com.github.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(validatingData.id)
    }

    override fun getItem1(): com.github.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(1)
    }

    override fun getItem2(): com.github.vhromada.catalog.domain.Picture {
        return PictureUtils.newPictureDomain(2)
    }

    override fun getName(): String {
        return "Picture"
    }

}
