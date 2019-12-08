package cz.vhromada.catalog.validator

import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import cz.vhromada.catalog.entity.Genre
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.common.test.validator.MovableValidatorTest
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.common.validator.ValidationType
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Severity
import cz.vhromada.validation.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [GenreValidator].
 *
 * @author Vladimir Hromada
 */
class GenreValidatorTest : MovableValidatorTest<Genre, cz.vhromada.catalog.domain.Genre>() {

    /**
     * Test method for [GenreValidator.validate] with [ValidationType.DEEP] with data with null name.
     */
    @Test
    fun validate_Deep_NullName() {
        val genre = getValidatingData(1)
                .copy(name = null)

        val result = getValidator().validate(genre, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")))
        }

        verifyZeroInteractions(service)
    }

    /**
     * Test method for [GenreValidator.validate] with [ValidationType.DEEP] with data with empty name.
     */
    @Test
    fun validate_Deep_EmptyName() {
        val genre = getValidatingData(1)
                .copy(name = "")

        val result = getValidator().validate(genre, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        verifyZeroInteractions(service)
    }

    override fun getValidator(): MovableValidator<Genre> {
        return GenreValidator(service)
    }

    override fun getValidatingData(id: Int?): Genre {
        return GenreUtils.newGenre(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Genre {
        return GenreUtils.newGenre(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Genre): cz.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(validatingData.id)
    }

    override fun getItem1(): cz.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(1)
    }

    override fun getItem2(): cz.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(2)
    }

    override fun getName(): String {
        return "Genre"
    }

}
