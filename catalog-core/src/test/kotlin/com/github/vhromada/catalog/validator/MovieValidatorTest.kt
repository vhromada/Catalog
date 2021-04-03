package com.github.vhromada.catalog.validator

import com.github.vhromada.catalog.entity.Genre
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.MediumUtils
import com.github.vhromada.catalog.utils.MovieUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.common.entity.Language
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
 * A class represents test for class [MovieValidator].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(MockitoExtension::class)
class MovieValidatorTest {

    /**
     * Instance of [Validator] for genres
     */
    @Mock
    private lateinit var genreValidator: Validator<Genre, com.github.vhromada.catalog.domain.Genre>

    /**
     * Instance of [MovieValidator]
     */
    private lateinit var validator: MovieValidator

    /**
     * Initializes validator.
     */
    @BeforeEach
    fun setUp() {
        validator = MovieValidator(genreValidator = genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with correct new movie.
     */
    @Test
    fun validateNew() {
        val movie = MovieUtils.newMovie(id = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with null new movie.
     */
    @Test
    fun validateNewNull() {
        val result = validator.validate(data = null, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NULL", message = "Movie mustn't be null.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with not null ID.
     */
    @Test
    fun validateNewNotNullId() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(id = Int.MAX_VALUE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ID_NOT_NULL", message = "ID must be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with not null position.
     */
    @Test
    fun validateNewNotNullPosition() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(position = Int.MAX_VALUE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null czech name.
     */
    @Test
    fun validateNewNullCzechName() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(czechName = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_NULL", message = "Czech name mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with empty string as czech name.
     */
    @Test
    fun validateNewEmptyCzechName() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(czechName = "")

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null original name.
     */
    @Test
    fun validateNewNullOriginalName() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(originalName = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_NULL", message = "Original name mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with empty string as original name.
     */
    @Test
    fun validateNewEmptyOriginalName() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(originalName = "")

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null year.
     */
    @Test
    fun validateNewNullYear() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(year = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_YEAR_NULL", message = "Year mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with bad minimum year.
     */
    @Test
    fun validateNewBadMinimumYear() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(year = TestConstants.BAD_MIN_YEAR)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_YEAR_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with bad maximum year.
     */
    @Test
    fun validateNewBadMaximumYear() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(year = TestConstants.BAD_MAX_YEAR)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_YEAR_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null language.
     */
    @Test
    fun validateNewNullLanguage() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(language = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_LANGUAGE_NULL", message = "Language mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null subtitles.
     */
    @Test
    fun validateNewNullSubtitles() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(subtitles = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_NULL", message = "Subtitles mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with subtitles with null value.
     */
    @Test
    fun validateNewBadSubtitles() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(subtitles = listOf(Language.CZ, null))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null media.
     */
    @Test
    fun validateNewNullMedia() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(media = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_NULL", message = "Media mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with media with null value.
     */
    @Test
    fun validateNewBadMedia() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(media = listOf(MediumUtils.newMedium(id = 1), null))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_CONTAIN_NULL", message = "Media mustn't contain null value.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with media with negative value as medium.
     */
    @Test
    fun validateNewMediaWithBadMedium() {
        val badMedium = MediumUtils.newMedium(id = 2)
            .copy(length = -1)
        val movie = MovieUtils.newMovie(id = null)
            .copy(media = listOf(MediumUtils.newMedium(id = 1), badMedium))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIUM_NOT_POSITIVE", message = "Length of medium must be positive number.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null URL to ČSFD page about movie.
     */
    @Test
    fun validateNewNullCsfd() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(csfd = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CSFD_NULL", message = "URL to ČSFD page about movie mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null IMDB code.
     */
    @Test
    fun validateNewNullImdb() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(imdbCode = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_IMDB_CODE_NULL", message = "IMDB code mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with bad minimal IMDB code.
     */
    @Test
    fun validateNewBadMinimalImdb() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with bad divider IMDB code.
     */
    @Test
    fun validateNewBadDividerImdb() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(imdbCode = 0)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with bad maximal IMDB code.
     */
    @Test
    fun validateNewBadMaximalImdb() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null URL to english Wikipedia page about movie.
     */
    @Test
    fun validateNewNullWikiEn() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(wikiEn = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_WIKI_EN_NULL", message = "URL to english Wikipedia page about movie mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    fun validateNewNullWikiCz() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(wikiCz = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about movie mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null note.
     */
    @Test
    fun validateNewNullNote() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(note = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOTE_NULL", message = "Note mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with null genres.
     */
    @Test
    fun validateNewNullGenres() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = null)

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_GENRES_NULL", message = "Genres mustn't be null.")))
        }

        verifyZeroInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with genres with null value.
     */
    @Test
    fun validateNewBadGenres() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), null))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with new movie with genres with genre with invalid data.
     */
    @Test
    fun validateNewGenresWithGenreWithInvalidData() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = Int.MAX_VALUE)))

        whenever(genreValidator.validate(any(), any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = validator.validate(data = movie, update = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with with update correct movie.
     */
    @Test
    fun validateUpdate() {
        val movie = MovieUtils.newMovie(id = 1)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with null update movie.
     */
    @Test
    fun validateUpdateNull() {
        val result = validator.validate(data = null, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NULL", message = "Movie mustn't be null.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null ID.
     */
    @Test
    fun validateUpdateNullId() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(id = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ID_NULL", message = "ID mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null position.
     */
    @Test
    fun validateUpdateNullPosition() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(position = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_POSITION_NULL", message = "Position mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null czech name.
     */
    @Test
    fun validateUpdateNullCzechName() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(czechName = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_NULL", message = "Czech name mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with empty string as czech name.
     */
    @Test
    fun validateUpdateEmptyCzechName() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(czechName = "")

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null original name.
     */
    @Test
    fun validateUpdateNullOriginalName() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(originalName = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_NULL", message = "Original name mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with empty string as original name.
     */
    @Test
    fun validateUpdateEmptyOriginalName() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(originalName = "")

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null year.
     */
    @Test
    fun validateUpdateNullYear() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(year = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_YEAR_NULL", message = "Year mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with bad minimum year.
     */
    @Test
    fun validateUpdateBadMinimumYear() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(year = TestConstants.BAD_MIN_YEAR)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_YEAR_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with bad maximum year.
     */
    @Test
    fun validateUpdateBadMaximumYear() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(year = TestConstants.BAD_MAX_YEAR)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_YEAR_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null language.
     */
    @Test
    fun validateUpdateNullLanguage() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(language = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_LANGUAGE_NULL", message = "Language mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null subtitles.
     */
    @Test
    fun validateUpdateNullSubtitles() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(subtitles = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_NULL", message = "Subtitles mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with subtitles with null value.
     */
    @Test
    fun validateUpdateBadSubtitles() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(subtitles = listOf(Language.CZ, null))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null media.
     */
    @Test
    fun validateUpdateNullMedia() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(media = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_NULL", message = "Media mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with media with null value.
     */
    @Test
    fun validateUpdateBadMedia() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(media = listOf(MediumUtils.newMedium(id = 1), null))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_CONTAIN_NULL", message = "Media mustn't contain null value.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with media with negative value as medium.
     */
    @Test
    fun validateUpdateMediaWithBadMedium() {
        val badMedium = MediumUtils.newMedium(id = 2)
            .copy(length = -1)
        val movie = MovieUtils.newMovie(id = 1)
            .copy(media = listOf(MediumUtils.newMedium(id = 1), badMedium))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIUM_NOT_POSITIVE", message = "Length of medium must be positive number.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null URL to ČSFD page about movie.
     */
    @Test
    fun validateUpdateNullCsfd() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(csfd = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CSFD_NULL", message = "URL to ČSFD page about movie mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null IMDB code.
     */
    @Test
    fun validateUpdateNullImdb() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(imdbCode = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_IMDB_CODE_NULL", message = "IMDB code mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with bad minimal IMDB code.
     */
    @Test
    fun validateUpdateBadMinimalImdb() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with bad divider IMDB code.
     */
    @Test
    fun validateUpdateBadDividerImdb() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(imdbCode = 0)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with bad maximal IMDB code.
     */
    @Test
    fun validateUpdateBadMaximalImdb() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null URL to english Wikipedia page about movie.
     */
    @Test
    fun validateUpdateNullWikiEn() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(wikiEn = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_WIKI_EN_NULL", message = "URL to english Wikipedia page about movie mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    fun validateUpdateNullWikiCz() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(wikiCz = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about movie mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null note.
     */
    @Test
    fun validateUpdateNullNote() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(note = null)

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOTE_NULL", message = "Note mustn't be null.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with null genres.
     */
    @Test
    fun validateUpdateNullGenres() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = null)

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_GENRES_NULL", message = "Genres mustn't be null.")))
        }

        verifyZeroInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with genres with null value.
     */
    @Test
    fun validateUpdateBadGenres() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), null))

        whenever(genreValidator.validate(data = any(), update = any())).thenReturn(Result())

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value.")))
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validate] with update movie with genres with genre with invalid data.
     */
    @Test
    fun validateUpdateGenresWithGenreWithInvalidData() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = Int.MAX_VALUE)))

        whenever(genreValidator.validate(any(), any())).thenReturn(TestConstants.INVALID_DATA_RESULT)

        val result = validator.validate(data = movie, update = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(TestConstants.INVALID_DATA_RESULT.events())
        }

        movie.genres!!.filterNotNull().forEach { verify(genreValidator).validate(data = it, update = true) }
        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validateExists] with correct movie.
     */
    @Test
    fun validateExists() {
        val result = validator.validateExists(data = Optional.of(MovieUtils.newMovieDomain(id = 1)))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validateExists] with invalid movie.
     */
    @Test
    fun validateExistsInvalid() {
        val result = validator.validateExists(data = Optional.empty())

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOT_EXIST", message = "Movie doesn't exist.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validateMovingData] with correct up movie.
     */
    @Test
    fun validateMovingDataUp() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        val result = validator.validateMovingData(data = movies[1], list = movies, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validateMovingData] with with invalid up movie.
     */
    @Test
    fun validateMovingDataUpInvalid() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        val result = validator.validateMovingData(data = movies[0], list = movies, up = true)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOT_MOVABLE", message = "Movie can't be moved up.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validateMovingData] with correct down movie.
     */
    @Test
    fun validateMovingDataDown() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        val result = validator.validateMovingData(data = movies[0], list = movies, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        verifyNoMoreInteractions(genreValidator)
    }

    /**
     * Test method for [MovieValidator.validateMovingData] with with invalid down movie.
     */
    @Test
    fun validateMovingDataDownInvalid() {
        val movies = listOf(MovieUtils.newMovieDomain(id = 1), MovieUtils.newMovieDomain(id = 2))

        val result = validator.validateMovingData(data = movies[1], list = movies, up = false)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOT_MOVABLE", message = "Movie can't be moved down.")))
        }

        verifyNoMoreInteractions(genreValidator)
    }

}
