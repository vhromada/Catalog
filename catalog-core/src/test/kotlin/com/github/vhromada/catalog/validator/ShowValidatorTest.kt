package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.entity.Picture
import com.github.vhromada.catalog.entity.Show
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Result
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.utils.TestConstants
import com.github.vhromada.common.test.validator.MovableValidatorTest
import com.github.vhromada.common.validator.AbstractMovableValidator
import com.github.vhromada.common.validator.MovableValidator
import com.github.vhromada.common.validator.ValidationType
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

/**
 * A class represents test for class [ShowValidator].
 *
 * @author Vladimir Hromada
 */
class ShowValidatorTest : MovableValidatorTest<Show, com.github.vhromada.catalog.domain.Show>() {

    /**
     * Validator for picture
     */
    @Mock
    private lateinit var pictureValidator: MovableValidator<Picture>

    /**
     * Validator for genre
     */
    @Mock
    private lateinit var genreValidator: MovableValidator<Genre>

    /**
     * Argument captor for picture
     */
    private lateinit var pictureArgumentCaptor: KArgumentCaptor<Picture>

    /**
     * Initializes data.
     */
    @BeforeEach
    override fun setUp() {
        super.setUp()

        pictureArgumentCaptor = argumentCaptor()
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null czech name.
     */
    @Test
    fun validateDeepNullCzechName() {
        val show = getValidatingData(1)
                .copy(czechName = null)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_CZECH_NAME_NULL", "Czech name mustn't be null.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with empty string as czech name.
     */
    @Test
    fun validateDeepEmptyCzechName() {
        val show = getValidatingData(1)
                .copy(czechName = "")

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null original name.
     */
    @Test
    fun validateDeepNullOriginalName() {
        val show = getValidatingData(1)
                .copy(originalName = null)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_ORIGINAL_NAME_NULL", "Original name mustn't be null.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with empty string as original name.
     */
    @Test
    fun validateDeepEmptyOriginalName() {
        val show = getValidatingData(1)
                .copy(originalName = "")

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null URL to ČSFD page about show.
     */
    @Test
    fun validateDeepNullCsfd() {
        val show = getValidatingData(1)
                .copy(csfd = null)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null IMDB code.
     */
    @Test
    fun validateDeepNullImdb() {
        val show = getValidatingData(1)
                .copy(imdbCode = null)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_IMDB_CODE_NULL", "IMDB code mustn't be null.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad minimal IMDB code.
     */
    @Test
    fun validateDeepBadMinimalImdb() {
        val show = getValidatingData(1)
                .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad divider IMDB code.
     */
    @Test
    fun validateDeepBadDividerImdb() {
        val show = getValidatingData(1)
                .copy(imdbCode = 0)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad maximal IMDB code.
     */
    @Test
    fun validateDeepBadMaximalImdb() {
        val show = getValidatingData(1)
                .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null URL to english Wikipedia page about show.
     */
    @Test
    fun validateDeepNullWikiEn() {
        val show = getValidatingData(1)
                .copy(wikiEn = null)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_WIKI_EN_NULL",
                    "URL to english Wikipedia page about show mustn't be null.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null URL to czech Wikipedia page about show.
     */
    @Test
    fun validateDeepNullWikiCz() {
        val show = getValidatingData(1)
                .copy(wikiCz = null)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about show mustn't be null.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validateDeepNullNote() {
        val show = getValidatingData(1)
                .copy(note = null)

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_NOTE_NULL", "Note mustn't be null.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad picture.
     */
    @Test
    fun validateDeepBadPicture() {
        val event = Event(Severity.ERROR, "PICTURE_INVALID", "Invalid data")
        val show = getValidatingData(1)

        whenever(pictureValidator.validate(any(), any())).thenReturn(Result.error(event.key, event.message))
        whenever(genreValidator.validate(any(), any())).thenReturn(Result())

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(event))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null genres.
     */
    @Test
    fun validateDeepNullGenres() {
        val show = getValidatingData(1)
                .copy(genres = null)

        whenever(pictureValidator.validate(any(), any())).thenReturn(Result())

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_GENRES_NULL", "Genres mustn't be null.")))
        }

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS))
        verifyNoMoreInteractions(pictureValidator)
        verifyZeroInteractions(service, genreValidator)

        validatePicture(show, pictureArgumentCaptor.lastValue)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with genres with null value.
     */
    @Test
    fun validateDeepBadGenres() {
        val show = getValidatingData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), null))

        initDeepMock(show)

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getPrefix()}_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")))
        }

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS))
        verify(genreValidator).validate(show.genres!![0], ValidationType.EXISTS, ValidationType.DEEP)
        verifyNoMoreInteractions(pictureValidator, genreValidator)
        verifyZeroInteractions(service)

        validatePicture(show, pictureArgumentCaptor.lastValue)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with genres with genre with invalid data.
     */
    @Test
    fun validateDeepGenresWithGenreWithInvalidData() {
        val event = Event(Severity.ERROR, "GENRE_INVALID", "Invalid data")
        val show = getValidatingData(1)
                .copy(genres = listOf(GenreUtils.newGenre(null)))

        whenever(pictureValidator.validate(any(), any())).thenReturn(Result())
        whenever(genreValidator.validate(any(), any())).thenReturn(Result.error(event.key, event.message))

        val result = getValidator().validate(show, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(event))
        }

        verifyDeepMock(show)
    }

    override fun initDeepMock(validatingData: Show) {
        super.initDeepMock(validatingData)

        whenever(pictureValidator.validate(any(), any())).thenReturn(Result())
        whenever(genreValidator.validate(any(), any())).thenReturn(Result())
    }

    override fun verifyDeepMock(validatingData: Show) {
        super.verifyDeepMock(validatingData)

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS))
        for (genre in validatingData.genres!!) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP)
        }
        verifyNoMoreInteractions(pictureValidator, genreValidator)
        verifyZeroInteractions(service)

        validatePicture(validatingData, pictureArgumentCaptor.lastValue)
    }

    override fun getValidator(): MovableValidator<Show> {
        return ShowValidator(service, pictureValidator, genreValidator)
    }

    override fun getValidatingData(id: Int?): Show {
        return ShowUtils.newShow(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Show {
        return ShowUtils.newShow(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Show): com.github.vhromada.catalog.domain.Show {
        return ShowUtils.newShowDomain(validatingData.id)
    }

    override fun getItem1(): com.github.vhromada.catalog.domain.Show {
        return ShowUtils.newShowDomain(1)
    }

    override fun getItem2(): com.github.vhromada.catalog.domain.Show {
        return ShowUtils.newShowDomain(2)
    }

    override fun getName(): String {
        return "Show"
    }

    companion object {

        /**
         * Event for invalid IMDB code
         */
        private val INVALID_IMDB_CODE_EVENT = Event(Severity.ERROR, "SHOW_IMDB_CODE_NOT_VALID", "IMDB code must be between 1 and 9999999 or -1.")

        /**
         * Validates picture.
         *
         * @param show    show
         * @param picture picture
         */
        private fun validatePicture(show: Show, picture: Picture) {
            assertThat(picture).isNotNull
            assertThat(picture.id).isEqualTo(show.picture)
        }
    }

}
