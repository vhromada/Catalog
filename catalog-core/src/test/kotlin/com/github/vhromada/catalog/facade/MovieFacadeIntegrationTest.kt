package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.MediumUtils
import com.github.vhromada.catalog.utils.MovieUtils
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.common.entity.Language
import com.github.vhromada.common.entity.Time
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * A class represents integration test for class [MovieFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class MovieFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [MovieFacade]
     */
    @Autowired
    private lateinit var facade: MovieFacade

    /**
     * Test method for [MovieFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..MovieUtils.MOVIES_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            MovieUtils.assertMovieDeepEquals(expected = MovieUtils.getMovieDomain(index = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MOVIE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update].
     */
    @Test
    fun update() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = listOf(GenreUtils.getGenre(index = 1)))
        val expectedMovie = MovieUtils.newMovieDomain(id = 1)
            .copy(genres = listOf(GenreUtils.getGenreDomain(index = 1)))
            .fillAudit(audit = AuditUtils.updatedAudit())
        expectedMovie.media.forEach { it.fillAudit(audit = AuditUtils.updatedAudit()) }

        val result = facade.update(data = movie)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = MovieUtils.getMovie(entityManager = entityManager, id = 1)!!)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null ID.
     */
    @Test
    fun updateNullId() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(id = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null position.
     */
    @Test
    fun updateNullPosition() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(position = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null czech name.
     */
    @Test
    fun updateNullCzechName() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(czechName = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_NULL", message = "Czech name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with empty string as czech name.
     */
    @Test
    fun updateEmptyCzechName() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(czechName = "")

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null original name.
     */
    @Test
    fun updateNullOriginalName() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(originalName = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_NULL", message = "Original name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with empty string as original name.
     */
    @Test
    fun updateEmptyOriginalName() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(originalName = "")

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null year.
     */
    @Test
    fun updateNullYear() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(year = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_YEAR_NULL", message = "Year mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad minimum year.
     */
    @Test
    fun updateBadMinimumYear() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(year = TestConstants.BAD_MIN_YEAR)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_YEAR_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad maximum year.
     */
    @Test
    fun updateBadMaximumYear() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(year = TestConstants.BAD_MAX_YEAR)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_YEAR_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null language.
     */
    @Test
    fun updateNullLanguage() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(language = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_LANGUAGE_NULL", message = "Language mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null subtitles.
     */
    @Test
    fun updateNullSubtitles() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(subtitles = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_NULL", message = "Subtitles mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with subtitles with null value.
     */
    @Test
    fun updateBadSubtitles() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(subtitles = listOf(Language.CZ, null))

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null media.
     */
    @Test
    fun updateNullMedia() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(media = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_NULL", message = "Media mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with media with null value.
     */
    @Test
    fun updateBadMedia() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(media = listOf(MediumUtils.newMedium(1), null))

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_CONTAIN_NULL", message = "Media mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with media with negative value as medium.
     */
    @Test
    fun updateBadMedium() {
        val badMedium = MediumUtils.newMedium(Int.MAX_VALUE)
            .copy(length = -1)
        val movie = MovieUtils.newMovie(id = 1)
            .copy(media = listOf(MediumUtils.newMedium(1), badMedium))

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIUM_NOT_POSITIVE", message = "Length of medium must be positive number.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null URL to ČSFD page about movie.
     */
    @Test
    fun updateNullCsfd() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(csfd = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CSFD_NULL", message = "URL to ČSFD page about movie mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null IMDB code.
     */
    @Test
    fun updateNullImdb() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(imdbCode = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_IMDB_CODE_NULL", message = "IMDB code mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad minimal IMDB code.
     */
    @Test
    fun updateBadMinimalImdb() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad divider IMDB code.
     */
    @Test
    fun updateBadDividerImdb() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(imdbCode = 0)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad maximal IMDB code.
     */
    @Test
    fun updateBadMaximalImdb() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null URL to english Wikipedia page about movie.
     */
    @Test
    fun updateNullWikiEn() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(wikiEn = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_WIKI_EN_NULL", message = "URL to english Wikipedia page about movie mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    fun updateNullWikiCz() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(wikiCz = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about movie mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null note.
     */
    @Test
    fun updateNullNote() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(note = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with not existing picture.
     */
    @Test
    fun updateNotExistingPicture() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(picture = Int.MAX_VALUE)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_EXIST", message = "Picture doesn't exist.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with null genres.
     */
    @Test
    fun updateNullGenres() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = null)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_GENRES_NULL", message = "Genres mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with genres with null value.
     */
    @Test
    fun updateBadGenres() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), null))

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with genres with genre with null ID.
     */
    @Test
    fun updateNullGenreId() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(id = null)
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with genres with genre with null name.
     */
    @Test
    fun updateNullGenreName() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(name = null)
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with genres with genre with empty string as name.
     */
    @Test
    fun updateEmptyGenreName() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(name = "")
        val movie = MovieUtils.newMovie(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with show with genres with not existing genre.
     */
    @Test
    fun updateNotExistingGenre() {
        val show = MovieUtils.newMovie(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), GenreUtils.newGenre(id = Int.MAX_VALUE)))

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_EXIST", message = "Genre doesn't exist.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad ID.
     */
    @Test
    fun updateBadId() {
        val movie = MovieUtils.newMovie(id = 1)
            .copy(id = Int.MAX_VALUE)

        val result = facade.update(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MOVIE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(MovieUtils.getMovie(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT - 1)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - MovieUtils.getMovieDomain(index = 1).media.size)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.remove] with movie with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MOVIE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        var expectedMovie = MovieUtils.getMovieDomain(index = MovieUtils.MOVIES_COUNT)
        val expectedMedia = expectedMovie.media.mapIndexed { index, medium ->
            medium.copy(id = MediumUtils.MEDIA_COUNT + index + 1)
                .fillAudit(audit = AuditUtils.newAudit())
        }
        expectedMovie = expectedMovie.copy(id = MovieUtils.MOVIES_COUNT + 1, media = expectedMedia)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.duplicate(id = MovieUtils.MOVIES_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = MovieUtils.getMovie(entityManager = entityManager, id = MovieUtils.MOVIES_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT + 1)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + MovieUtils.getMovieDomain(index = MovieUtils.MOVIES_COUNT).media.size)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.duplicate] with movie with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MOVIE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val movie1 = MovieUtils.getMovieDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val movie2 = MovieUtils.getMovieDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        MovieUtils.assertMovieDeepEquals(expected = movie1, actual = MovieUtils.getMovie(entityManager = entityManager, id = 1)!!)
        MovieUtils.assertMovieDeepEquals(expected = movie2, actual = MovieUtils.getMovie(entityManager = entityManager, id = 2)!!)
        for (i in 3..MovieUtils.MOVIES_COUNT) {
            MovieUtils.assertMovieDeepEquals(expected = MovieUtils.getMovieDomain(i), actual = MovieUtils.getMovie(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.moveUp] with not movable movie.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOT_MOVABLE", message = "Movie can't be moved up.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.moveUp] with movie with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MOVIE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val movie1 = MovieUtils.getMovieDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val movie2 = MovieUtils.getMovieDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        MovieUtils.assertMovieDeepEquals(expected = movie1, actual = MovieUtils.getMovie(entityManager = entityManager, id = 1)!!)
        MovieUtils.assertMovieDeepEquals(expected = movie2, actual = MovieUtils.getMovie(entityManager = entityManager, id = 2)!!)
        for (i in 3..MovieUtils.MOVIES_COUNT) {
            MovieUtils.assertMovieDeepEquals(expected = MovieUtils.getMovieDomain(i), actual = MovieUtils.getMovie(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.moveDown] with not movable movie.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = MovieUtils.MOVIES_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOT_MOVABLE", message = "Movie can't be moved down.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.moveDown] with movie with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(MOVIE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.newData].
     */
    @Test
    fun newData() {
        val result = facade.newData()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.getAll].
     */
    @Test
    fun getAll() {
        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isNotNull
            it.assertThat(result.events()).isEmpty()
        }
        MovieUtils.assertMovieListDeepEquals(expected = MovieUtils.getMovies(), actual = result.data!!)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = listOf(GenreUtils.getGenre(index = 1)))
        val expectedMovie = MovieUtils.newMovieDomain(id = MovieUtils.MOVIES_COUNT + 1)
            .copy(media = listOf(MediumUtils.newMediumDomain(id = MediumUtils.MEDIA_COUNT + 1)), picture = null, genres = listOf(GenreUtils.getGenreDomain(index = 1)))
            .fillAudit(audit = AuditUtils.newAudit())
        expectedMovie.media.forEach { it.fillAudit(audit = AuditUtils.newAudit()) }

        val result = facade.add(data = movie)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = MovieUtils.getMovie(entityManager = entityManager, id = MovieUtils.MOVIES_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT + 1)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 1)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with not null ID.
     */
    @Test
    fun addNotNullId() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(id = Int.MAX_VALUE, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(position = Int.MAX_VALUE, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null czech name.
     */
    @Test
    fun addNullCzechName() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(czechName = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_NULL", message = "Czech name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with empty string as czech name.
     */
    @Test
    fun addEmptyCzechName() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(czechName = "", genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null original name.
     */
    @Test
    fun addNullOriginalName() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(originalName = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_NULL", message = "Original name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with empty string as original name.
     */
    @Test
    fun addEmptyOriginalName() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(originalName = "", genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null year.
     */
    @Test
    fun addNullYear() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(year = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_YEAR_NULL", message = "Year mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad minimum year.
     */
    @Test
    fun addBadMinimumYear() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(year = TestConstants.BAD_MIN_YEAR, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_YEAR_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad maximum year.
     */
    @Test
    fun addBadMaximumYear() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(year = TestConstants.BAD_MAX_YEAR, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_YEAR_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null language.
     */
    @Test
    fun addNullLanguage() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(language = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_LANGUAGE_NULL", message = "Language mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null subtitles.
     */
    @Test
    fun addNullSubtitles() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(subtitles = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_NULL", message = "Subtitles mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with subtitles with null value.
     */
    @Test
    fun addBadSubtitles() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(subtitles = listOf(Language.CZ, null), genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null media.
     */
    @Test
    fun addNullMedia() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(media = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_NULL", message = "Media mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with media with null value.
     */
    @Test
    fun addBadMedia() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(media = listOf(MediumUtils.newMedium(1), null), genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIA_CONTAIN_NULL", message = "Media mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with media with negative value as medium.
     */
    @Test
    fun addBadMedium() {
        val badMedium = MediumUtils.newMedium(Int.MAX_VALUE)
            .copy(length = -1)
        val movie = MovieUtils.newMovie(id = null)
            .copy(media = listOf(MediumUtils.newMedium(1), badMedium), genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_MEDIUM_NOT_POSITIVE", message = "Length of medium must be positive number.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null URL to ČSFD page about movie.
     */
    @Test
    fun addNullCsfd() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(csfd = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_CSFD_NULL", message = "URL to ČSFD page about movie mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null IMDB code.
     */
    @Test
    fun addNullImdb() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(imdbCode = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_IMDB_CODE_NULL", message = "IMDB code mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad minimal IMDB code.
     */
    @Test
    fun addBadMinimalImdb() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad divider IMDB code.
     */
    @Test
    fun addBadDividerImdb() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(imdbCode = 0, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad maximal IMDB code.
     */
    @Test
    fun addBadMaximalImdb() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_MOVIE_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null URL to english Wikipedia page about movie.
     */
    @Test
    fun addNullWikiEn() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(wikiEn = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_WIKI_EN_NULL", message = "URL to english Wikipedia page about movie mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    fun addNullWikiCz() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(wikiCz = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about movie mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null note.
     */
    @Test
    fun addNullNote() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(note = null, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with not existing picture.
     */
    @Test
    fun addNotExistingPicture() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(picture = Int.MAX_VALUE, genres = listOf(GenreUtils.getGenre(index = 1)))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_EXIST", message = "Picture doesn't exist.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with null genres.
     */
    @Test
    fun addNullGenres() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = null)

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_GENRES_NULL", message = "Genres mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with genres with null value.
     */
    @Test
    fun addBadGenres() {
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), null))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "MOVIE_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with genres with genre with null ID.
     */
    @Test
    fun addNullGenreId() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(id = null)
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with genres with genre with null name.
     */
    @Test
    fun addNullGenreName() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(name = null)
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with movie with genres with genre with empty string as name.
     */
    @Test
    fun addEmptyGenreName() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(name = "")
        val movie = MovieUtils.newMovie(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.add(data = movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.add] with show with genres with not existing genre.
     */
    @Test
    fun addNotExistingGenre() {
        val show = MovieUtils.newMovie(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), GenreUtils.newGenre(id = Int.MAX_VALUE)))

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_EXIST", message = "Genre doesn't exist.")))
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        for (i in 1..MovieUtils.MOVIES_COUNT) {
            val expectedMovie = MovieUtils.getMovieDomain(index = i)
                .copy(position = i - 1)
                .fillAudit(audit = AuditUtils.updatedAudit())
            MovieUtils.assertMovieDeepEquals(expected = expectedMovie, actual = MovieUtils.getMovie(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.getTotalMediaCount].
     */
    @Test
    fun getTotalMediaCount() {
        val result = facade.getTotalMediaCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [MovieFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(length = 1000))
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(MovieUtils.getMoviesCount(entityManager = entityManager)).isEqualTo(MovieUtils.MOVIES_COUNT)
            it.assertThat(MediumUtils.getMediaCount(entityManager = entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    companion object {

        /**
         * Event for not existing movie
         */
        private val MOVIE_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "MOVIE_NOT_EXIST", message = "Movie doesn't exist.")

    }

}
