package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

/**
 * A class represents test for class [GenreValidator].
 *
 * @author Vladimir Hromada
 */
class GenreValidatorTest {

    /**
     * Instance of [GenreValidator]
     */
    private lateinit var validator: GenreValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = GenreValidator()
    }

    /**
     * Test method for [GenreValidator.validate] with correct new genre.
     */
    @Test
    fun validateNew() {
        val result = validator.validate(data = GenreUtils.newGenre(id = null), update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GenreValidator.validate] with null new genre.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NULL", message = "Genre mustn't be null.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with new genre with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val genre = GenreUtils.newGenre(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = validator.validate(data = genre, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_ID_NOT_NULL", message = "ID must be null.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with new genre with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val genre = GenreUtils.newGenre(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = validator.validate(data = genre, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_POSITION_NOT_NULL", message = "Position must be null.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with new genre with null name.
     */
    @Test
    fun validateNewNullName() {
        val genre = GenreUtils.newGenre(id = null)
            .copy(name = null)

        val result = validator.validate(data = genre, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with new genre with empty name.
     */
    @Test
    fun validateNewEmptyName() {
        val genre = GenreUtils.newGenre(id = null)
            .copy(name = "")

        val result = validator.validate(data = genre, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with with update correct genre.
     */
    @Test
    fun validateUpdate() {
        val result = validator.validate(data = GenreUtils.newGenre(id = 1), update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GenreValidator.validate] with null update genre.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NULL", message = "Genre mustn't be null.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with update genre with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val genre = GenreUtils.newGenre(id = 1)
            .copy(id = null)

        val result = validator.validate(data = genre, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_ID_NULL", message = "ID mustn't be null.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with update genre with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val genre = GenreUtils.newGenre(id = 1)
            .copy(position = null)

        val result = validator.validate(data = genre, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_POSITION_NULL", message = "Position mustn't be null.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with update genre with null name.
     */
    @Test
    fun validateUpdateNullName() {
        val genre = GenreUtils.newGenre(id = 1)
            .copy(name = null)

        val result = validator.validate(data = genre, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null.")))
        }
    }

    /**
     * Test method for [GenreValidator.validate] with update genre with empty name.
     */
    @Test
    fun validateUpdateEmptyName() {
        val genre = GenreUtils.newGenre(id = 1)
            .copy(name = "")

        val result = validator.validate(data = genre, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }
    }

    /**
     * Test method for [GenreValidator.validateExists] with correct genre.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(GenreUtils.newGenreDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GenreValidator.validateExists] with invalid genre.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_EXIST", message = "Genre doesn't exist.")))
        }
    }

    /**
     * Test method for [GenreValidator.validateMovingData] with correct up genre.
     */
    @Test
    fun validateMovingDataUp() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        val result = validator.validateMovingData(data = genres[1], list = genres, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GenreValidator.validateMovingData] with with invalid up genre.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        val result = validator.validateMovingData(data = genres[0], list = genres, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_MOVABLE", message = "Genre can't be moved up.")))
        }
    }

    /**
     * Test method for [GenreValidator.validateMovingData] with correct down genre.
     */
    @Test
    fun validateMovingDataDown() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        val result = validator.validateMovingData(data = genres[0], list = genres, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }
    }

    /**
     * Test method for [GenreValidator.validateMovingData] with with invalid down genre.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val genres = listOf(GenreUtils.newGenreDomain(id = 1), GenreUtils.newGenreDomain(id = 2))

        val result = validator.validateMovingData(data = genres[1], list = genres, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_MOVABLE", message = "Genre can't be moved down.")))
        }
    }

}
