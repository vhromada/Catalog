package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.validator.MovableValidatorTest
import com.github.vhromada.common.validator.MovableValidator
import com.github.vhromada.common.validator.ValidationType
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test

/**
 * A class represents test for class [GenreValidator].
 *
 * @author Vladimir Hromada
 */
class GenreValidatorTest : MovableValidatorTest<Genre, com.github.vhromada.catalog.domain.Genre>() {

    /**
     * Test method for [GenreValidator.validate] with [ValidationType.DEEP] with data with null name.
     */
    @Test
    fun validateDeepNullName() {
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
    fun validateDeepEmptyName() {
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

    override fun getRepositoryData(validatingData: Genre): com.github.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(validatingData.id)
    }

    override fun getItem1(): com.github.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(1)
    }

    override fun getItem2(): com.github.vhromada.catalog.domain.Genre {
        return GenreUtils.newGenreDomain(2)
    }

    override fun getName(): String {
        return "Genre"
    }

}
