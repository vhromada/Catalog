package cz.vhromada.catalog.validator

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import cz.vhromada.catalog.entity.Genre
import cz.vhromada.catalog.entity.Movie
import cz.vhromada.catalog.entity.Picture
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.catalog.utils.MediumUtils
import cz.vhromada.catalog.utils.MovieUtils
import cz.vhromada.common.entity.Language
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Result
import cz.vhromada.common.result.Severity
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.utils.TestConstants
import cz.vhromada.common.test.validator.MovableValidatorTest
import cz.vhromada.common.utils.Constants
import cz.vhromada.common.validator.AbstractMovableValidator
import cz.vhromada.common.validator.MovableValidator
import cz.vhromada.common.validator.ValidationType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

/**
 * A class represents test for class [MovieValidator].
 *
 * @author Vladimir Hromada
 */
class MovieValidatorTest : MovableValidatorTest<Movie, cz.vhromada.catalog.domain.Movie>() {

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
        val movie = getValidatingData(1)
                .copy(czechName = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with empty string as czech name.
     */
    @Test
    fun validateDeepEmptyCzechName() {
        val movie = getValidatingData(1)
                .copy(czechName = "")

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null original name.
     */
    @Test
    fun validateDeepNullOriginalName() {
        val movie = getValidatingData(1)
                .copy(originalName = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with empty string as original name.
     */
    @Test
    fun validateDeepEmptyOriginalName() {
        val movie = getValidatingData(1)
                .copy(originalName = "")

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null year.
     */
    @Test
    fun validateDeepNullYear() {
        val movie = getValidatingData(1)
                .copy(year = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_YEAR_NULL", "Year mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad minimum year.
     */
    @Test
    fun validateDeepBadMinimumYear() {
        val movie = getValidatingData(1)
                .copy(year = TestConstants.BAD_MIN_YEAR)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_YEAR_EVENT))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad maximum year.
     */
    @Test
    fun validateDeepBadMaximumYear() {
        val movie = getValidatingData(1)
                .copy(year = TestConstants.BAD_MAX_YEAR)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_YEAR_EVENT))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null language.
     */
    @Test
    fun validateDeepNullLanguage() {
        val movie = getValidatingData(1)
                .copy(language = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null subtitles.
     */
    @Test
    fun validateDeepNullSubtitles() {
        val movie = getValidatingData(1)
                .copy(subtitles = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with subtitles with null value.
     */
    @Test
    fun validateDeepBadSubtitles() {
        val movie = getValidatingData(1)
                .copy(subtitles = listOf(Language.CZ, null))

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null media.
     */
    @Test
    fun validateDeepNullMedia() {
        val movie = getValidatingData(1)
                .copy(media = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data media with null value.
     */
    @Test
    fun validateDeepBadMedia() {
        val movie = getValidatingData(1)
                .copy(media = listOf(MediumUtils.newMedium(1), null))

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with media with negative value as medium.
     */
    @Test
    fun validateDeepMediaWithBadMedium() {
        val badMedium = MediumUtils.newMedium(2)
                .copy(length = -1)
        val movie = getValidatingData(1)
                .copy(media = listOf(MediumUtils.newMedium(1), badMedium))

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null URL to ČSFD page about movie.
     */
    @Test
    fun validateDeepNullCsfd() {
        val movie = getValidatingData(1)
                .copy(csfd = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")))
        }

        verifyDeepMock(movie)
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
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_IMDB_CODE_NULL", "IMDB code mustn't be null.")))
        }

        verifyDeepMock(show)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad minimal IMDB code.
     */
    @Test
    fun validateDeepBadMinimalImdb() {
        val movie = getValidatingData(1)
                .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad divider IMDB code.
     */
    @Test
    fun validateDeepBadDividerImdb() {
        val movie = getValidatingData(1)
                .copy(imdbCode = 0)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad maximal IMDB code.
     */
    @Test
    fun validateDeepBadMaximalImdb() {
        val movie = getValidatingData(1)
                .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null URL to english Wikipedia page about movie.
     */
    @Test
    fun validateDeepNullWikiEn() {
        val movie = getValidatingData(1)
                .copy(wikiEn = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                    "URL to english Wikipedia page about movie mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null URL to czech Wikipedia page about movie.
     */
    @Test
    fun validateDeepNullWikiCz() {
        val movie = getValidatingData(1)
                .copy(wikiCz = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about movie mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null note.
     */
    @Test
    fun validateDeepNullNote() {
        val movie = getValidatingData(1)
                .copy(note = null)

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with bad picture.
     */
    @Test
    fun validateDeepBadPicture() {
        val event = Event(Severity.ERROR, "PICTURE_INVALID", "Invalid data")
        val movie = getValidatingData(1)

        whenever(pictureValidator.validate(any(), any())).thenReturn(Result.error(event.key, event.message))
        whenever(genreValidator.validate(any(), any())).thenReturn(Result())

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(event))
        }

        verifyDeepMock(movie)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with null genres.
     */
    @Test
    fun validateDeepNullGenres() {
        val movie = getValidatingData(1)
                .copy(genres = null)

        whenever(pictureValidator.validate(any(), any())).thenReturn(Result())

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")))
        }

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS))
        verifyNoMoreInteractions(pictureValidator)
        verifyZeroInteractions(service, genreValidator)

        validatePicture(movie, pictureArgumentCaptor.lastValue)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with genres with null value.
     */
    @Test
    fun validateDeepBadGenres() {
        val movie = getValidatingData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), null))

        initDeepMock(movie)

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")))
        }

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS))
        verify(genreValidator).validate(movie.genres!![0], ValidationType.EXISTS, ValidationType.DEEP)
        verifyNoMoreInteractions(pictureValidator, genreValidator)
        verifyZeroInteractions(service)

        validatePicture(movie, pictureArgumentCaptor.lastValue)
    }

    /**
     * Test method for [AbstractMovableValidator.validate]} with [ValidationType.DEEP] with data with genres with genre with invalid data.
     */
    @Test
    fun validateDeepGenresWithGenreWithInvalidData() {
        val event = Event(Severity.ERROR, "GENRE_INVALID", "Invalid data")
        val movie = getValidatingData(1)
                .copy(genres = listOf(GenreUtils.newGenre(null)))

        whenever(pictureValidator.validate(any(), any())).thenReturn(Result())
        whenever(genreValidator.validate(any(), any())).thenReturn(Result.error(event.key, event.message))

        val result = getValidator().validate(movie, ValidationType.DEEP)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(event))
        }

        verifyDeepMock(movie)
    }

    override fun initDeepMock(validatingData: Movie) {
        super.initDeepMock(validatingData)

        whenever(pictureValidator.validate(any(), any())).thenReturn(Result())
        whenever(genreValidator.validate(any(), any())).thenReturn(Result())
    }

    override fun verifyDeepMock(validatingData: Movie) {
        super.verifyDeepMock(validatingData)

        verify(pictureValidator).validate(pictureArgumentCaptor.capture(), eq(ValidationType.EXISTS))
        for (genre in validatingData.genres!!) {
            verify(genreValidator).validate(genre, ValidationType.EXISTS, ValidationType.DEEP)
        }
        verifyNoMoreInteractions(pictureValidator, genreValidator)
        verifyZeroInteractions(service)

        validatePicture(validatingData, pictureArgumentCaptor.lastValue)
    }

    override fun getValidator(): MovableValidator<Movie> {
        return MovieValidator(service, pictureValidator, genreValidator)
    }

    override fun getValidatingData(id: Int?): Movie {
        return MovieUtils.newMovie(id)
    }

    override fun getValidatingData(id: Int?, position: Int?): Movie {
        return MovieUtils.newMovie(id)
                .copy(position = position)
    }

    override fun getRepositoryData(validatingData: Movie): cz.vhromada.catalog.domain.Movie {
        return MovieUtils.newMovieDomain(validatingData.id)
    }

    override fun getItem1(): cz.vhromada.catalog.domain.Movie {
        return MovieUtils.newMovieDomain(1)
    }

    override fun getItem2(): cz.vhromada.catalog.domain.Movie {
        return MovieUtils.newMovieDomain(2)
    }

    override fun getName(): String {
        return "Movie"
    }

    companion object {

        /**
         * Event for invalid year
         */
        private val INVALID_YEAR_EVENT = Event(Severity.ERROR, "MOVIE_YEAR_NOT_VALID",
                "Year must be between ${Constants.MIN_YEAR} and ${Constants.CURRENT_YEAR}.")

        /**
         * Event for invalid IMDB code
         */
        private val INVALID_IMDB_CODE_EVENT = Event(Severity.ERROR, "MOVIE_IMDB_CODE_NOT_VALID", "IMDB code must be between 1 and 9999999 or -1.")

        /**
         * Validates picture.
         *
         * @param movie   movie
         * @param picture picture
         */
        private fun validatePicture(movie: Movie, picture: Picture) {
            assertThat(picture).isNotNull
            assertThat(picture.id).isEqualTo(movie.picture)
        }
    }

}
