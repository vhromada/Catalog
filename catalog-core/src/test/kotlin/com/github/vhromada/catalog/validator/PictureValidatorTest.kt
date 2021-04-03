package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [PictureValidator].
 *
 * @author Vladimir Hromada
 */
class PictureValidatorTest {

    /**
     * Instance of [PictureValidator]
     */
    private lateinit var validator: PictureValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = PictureValidator()
    }

    /**
     * Test method for [PictureValidator.validate] with correct new picture.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = PictureUtils.newPicture(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [PictureValidator.validate] with null new picture.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NULL", message = "Picture mustn't be null.")))
        }
    }

    /**
     * Test method for [PictureValidator.validate] with new picture with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val picture = PictureUtils.newPicture(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = picture, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [PictureValidator.validate] with new picture with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val picture = PictureUtils.newPicture(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = validator.validate(data = picture, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_POSITION_NOT_NULL", message = "Position must be null.")))
        }
    }

    /**
     * Test method for [PictureValidator.validate] with new picture with null content.
     */
    @Test
    fun validateNewNullContent() {
        val picture = PictureUtils.newPicture(id = null)
            .copy(content = null)

        val result = validator.validate(data = picture, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_CONTENT_NULL", message = "Content mustn't be null.")))
        }
    }

    /**
     * Test method for [PictureValidator.validate] with with update correct picture.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = PictureUtils.newPicture(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [PictureValidator.validate] with null update picture.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NULL", message = "Picture mustn't be null.")))
        }
    }

    /**
     * Test method for [PictureValidator.validate] with update picture with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val picture = PictureUtils.newPicture(id = 1)
            .copy(id = null)

        val result = validator.validate(data = picture, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [PictureValidator.validate] with update picture with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val picture = PictureUtils.newPicture(id = 1)
            .copy(position = null)

        val result = validator.validate(data = picture, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_POSITION_NULL", message = "Position mustn't be null.")))
        }
    }

    /**
     * Test method for [PictureValidator.validate] with update picture with null content.
     */
    @Test
    fun validateUpdateNullContent() {
        val picture = PictureUtils.newPicture(id = 1)
            .copy(content = null)

        val result = validator.validate(data = picture, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_CONTENT_NULL", message = "Content mustn't be null.")))
        }
    }

    /**
     * Test method for [PictureValidator.validateExists] with correct picture.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(PictureUtils.newPictureDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [PictureValidator.validateExists] with invalid picture.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_EXIST", message = "Picture doesn't exist.")))
        }
    }

    /**
     * Test method for [PictureValidator.validateMovingData] with correct up picture.
     */
    @Test
    fun validateMovingDataUp() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        val result = validator.validateMovingData(data = pictures[1], list = pictures, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [PictureValidator.validateMovingData] with with invalid up picture.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        val result = validator.validateMovingData(data = pictures[0], list = pictures, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_MOVABLE", message = "Picture can't be moved up.")))
        }
    }

    /**
     * Test method for [PictureValidator.validateMovingData] with correct down picture.
     */
    @Test
    fun validateMovingDataDown() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        val result = validator.validateMovingData(data = pictures[0], list = pictures, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [PictureValidator.validateMovingData] with with invalid down picture.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val pictures = listOf(PictureUtils.newPictureDomain(id = 1), PictureUtils.newPictureDomain(id = 2))

        val result = validator.validateMovingData(data = pictures[1], list = pictures, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_MOVABLE", message = "Picture can't be moved down.")))
        }
    }

}
