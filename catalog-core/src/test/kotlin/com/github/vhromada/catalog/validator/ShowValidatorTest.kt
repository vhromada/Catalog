package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.validator.Validator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

/**
 * A class represents test for class [ShowValidator].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class ShowValidatorTest {

    /**
     * Instance of [Validator] for genres
     */
    @Mock
    private lateinit var genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [ShowValidator]
     */
    private lateinit var validator: ShowValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = ShowValidator(genreValidator = genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with correct new show.
     */
    @Test
    fun validateNew() {
        val show = ShowUtils.newShow(id = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with null new show.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NULL", message = "Show mustn't be null.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val show = ShowUtils.newShow(id = null)
            .copy(id = Int.MAX_VALUE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ID_NOT_NULL", message = "ID must be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val show = ShowUtils.newShow(id = null)
            .copy(position = Int.MAX_VALUE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with null czech name.
     */
    @Test
    fun validateNewNullCzechName() {
        val show = ShowUtils.newShow(id = null)
            .copy(czechName = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_NULL", message = "Czech name mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with empty string as czech name.
     */
    @Test
    fun validateNewEmptyCzechName() {
        val show = ShowUtils.newShow(id = null)
            .copy(czechName = "")

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with null original name.
     */
    @Test
    fun validateNewNullOriginalName() {
        val show = ShowUtils.newShow(id = null)
            .copy(originalName = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_NULL", message = "Original name mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with empty string as original name.
     */
    @Test
    fun validateNewEmptyOriginalName() {
        val show = ShowUtils.newShow(id = null)
            .copy(originalName = "")

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with null URL to ČSFD page about show.
     */
    @Test
    fun validateNewNullCsfd() {
        val show = ShowUtils.newShow(id = null)
            .copy(csfd = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CSFD_NULL", message = "URL to ČSFD page about show mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with null IMDB code.
     */
    @Test
    fun validateNewNullImdb() {
        val show = ShowUtils.newShow(id = null)
            .copy(imdbCode = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_IMDB_CODE_NULL", message = "IMDB code mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with bad minimal IMDB code.
     */
    @Test
    fun validateNewBadMinimalImdb() {
        val show = ShowUtils.newShow(id = null)
            .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with bad divider IMDB code.
     */
    @Test
    fun validateNewBadDividerImdb() {
        val show = ShowUtils.newShow(id = null)
            .copy(imdbCode = 0)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with bad maximal IMDB code.
     */
    @Test
    fun validateNewBadMaximalImdb() {
        val show = ShowUtils.newShow(id = null)
            .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with null URL to english Wikipedia page about show.
     */
    @Test
    fun validateNewNullWikiEn() {
        val show = ShowUtils.newShow(id = null)
            .copy(wikiEn = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_WIKI_EN_NULL", message = "URL to english Wikipedia page about show mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with null URL to czech Wikipedia page about show.
     */
    @Test
    fun validateNewNullWikiCz() {
        val show = ShowUtils.newShow(id = null)
            .copy(wikiCz = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about show mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with null note.
     */
    @Test
    fun validateNewNullNote() {
        val show = ShowUtils.newShow(id = null)
            .copy(note = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOTE_NULL", message = "Note mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with null genres.
     */
    @Test
    fun validateNewNullGenres() {
        val show = ShowUtils.newShow(id = null)
            .copy(genres = null)

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_GENRES_NULL", message = "Genres mustn't be null.")))
        }

        verifyZeroInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with genres with null value.
     */
    @Test
    fun validateNewBadGenres() {
        val show = ShowUtils.newShow(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), null))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with new show with genres with genre with invalid data.
     */
    @Test
    fun validateNewGenresWithGenreWithInvalidData() {
        val show = ShowUtils.newShow(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = Int.MAX_VALUE)))

        whenever(genreValidator.validate(any(), any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = validator.validate(data = show, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with with update correct show.
     */
    @Test
    fun validateUpdate() {
        val show = ShowUtils.newShow(id = 1)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with null update show.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NULL", message = "Show mustn't be null.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val show = ShowUtils.newShow(id = 1)
            .copy(id = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ID_NULL", message = "ID mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val show = ShowUtils.newShow(id = 1)
            .copy(position = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_POSITION_NULL", message = "Position mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null czech name.
     */
    @Test
    fun validateUpdateNullCzechName() {
        val show = ShowUtils.newShow(id = 1)
            .copy(czechName = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_NULL", message = "Czech name mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with empty string as czech name.
     */
    @Test
    fun validateUpdateEmptyCzechName() {
        val show = ShowUtils.newShow(id = 1)
            .copy(czechName = "")

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null original name.
     */
    @Test
    fun validateUpdateNullOriginalName() {
        val show = ShowUtils.newShow(id = 1)
            .copy(originalName = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_NULL", message = "Original name mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with empty string as original name.
     */
    @Test
    fun validateUpdateEmptyOriginalName() {
        val show = ShowUtils.newShow(id = 1)
            .copy(originalName = "")

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null URL to ČSFD page about show.
     */
    @Test
    fun validateUpdateNullCsfd() {
        val show = ShowUtils.newShow(id = 1)
            .copy(csfd = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CSFD_NULL", message = "URL to ČSFD page about show mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null IMDB code.
     */
    @Test
    fun validateUpdateNullImdb() {
        val show = ShowUtils.newShow(id = 1)
            .copy(imdbCode = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_IMDB_CODE_NULL", message = "IMDB code mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with bad minimal IMDB code.
     */
    @Test
    fun validateUpdateBadMinimalImdb() {
        val show = ShowUtils.newShow(id = 1)
            .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with bad divider IMDB code.
     */
    @Test
    fun validateUpdateBadDividerImdb() {
        val show = ShowUtils.newShow(id = 1)
            .copy(imdbCode = 0)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with bad maximal IMDB code.
     */
    @Test
    fun validateUpdateBadMaximalImdb() {
        val show = ShowUtils.newShow(id = 1)
            .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null URL to english Wikipedia page about show.
     */
    @Test
    fun validateUpdateNullWikiEn() {
        val show = ShowUtils.newShow(id = 1)
            .copy(wikiEn = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_WIKI_EN_NULL", message = "URL to english Wikipedia page about show mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null URL to czech Wikipedia page about show.
     */
    @Test
    fun validateUpdateNullWikiCz() {
        val show = ShowUtils.newShow(id = 1)
            .copy(wikiCz = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about show mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null note.
     */
    @Test
    fun validateUpdateNullNote() {
        val show = ShowUtils.newShow(id = 1)
            .copy(note = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOTE_NULL", message = "Note mustn't be null.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with null genres.
     */
    @Test
    fun validateUpdateNullGenres() {
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = null)

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_GENRES_NULL", message = "Genres mustn't be null.")))
        }

        verifyZeroInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with genres with null value.
     */
    @Test
    fun validateUpdateBadGenres() {
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), null))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value.")))
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validate] with update show with genres with genre with invalid data.
     */
    @Test
    fun validateUpdateGenresWithGenreWithInvalidData() {
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = Int.MAX_VALUE)))

        whenever(genreValidator.validate(any(), any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = validator.validate(data = show, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        show.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validateExists] with correct show.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(ShowUtils.newShowDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validateExists] with invalid show.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOT_EXIST", message = "Show doesn't exist.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validateMovingData] with correct up show.
     */
    @Test
    fun validateMovingDataUp() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        val result = validator.validateMovingData(data = shows[1], list = shows, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validateMovingData] with with invalid up show.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        val result = validator.validateMovingData(data = shows[0], list = shows, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOT_MOVABLE", message = "Show can't be moved up.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validateMovingData] with correct down show.
     */
    @Test
    fun validateMovingDataDown() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        val result = validator.validateMovingData(data = shows[0], list = shows, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [ShowValidator.validateMovingData] with with invalid down show.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val shows = listOf(ShowUtils.newShowDomain(id = 1), ShowUtils.newShowDomain(id = 2))

        val result = validator.validateMovingData(data = shows[1], list = shows, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOT_MOVABLE", message = "Show can't be moved down.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

}
