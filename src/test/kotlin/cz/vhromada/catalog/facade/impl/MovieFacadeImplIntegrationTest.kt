package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Movie
import cz.vhromada.catalog.facade.MovieFacade
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.catalog.utils.MediumUtils
import cz.vhromada.catalog.utils.MovieUtils
import cz.vhromada.catalog.utils.PictureUtils
import cz.vhromada.common.Language
import cz.vhromada.common.Time
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest
import cz.vhromada.common.test.utils.TestConstants
import cz.vhromada.common.utils.Constants
import cz.vhromada.validation.result.Event
import cz.vhromada.validation.result.Severity
import cz.vhromada.validation.result.Status
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [MovieFacadeImpl].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class MovieFacadeImplIntegrationTest : MovableParentFacadeIntegrationTest<Movie, cz.vhromada.catalog.domain.Movie>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [MovieFacade]
     */
    @Autowired
    private lateinit var facade: MovieFacade

    /**
     * Test method for [MovieFacade.add] with movie with null czech name.
     */
    @Test
    fun add_NullCzechName() {
        val movie = newData(null)
                .copy(czechName = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with empty string as czech name.
     */
    @Test
    fun add_EmptyCzechName() {
        val movie = newData(null)
                .copy(czechName = "")

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null original name.
     */
    @Test
    fun add_NullOriginalName() {
        val movie = newData(null)
                .copy(originalName = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with empty string as original name.
     */
    @Test
    fun add_EmptyOriginalName() {
        val movie = newData(null)
                .copy(originalName = "")

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null year.
     */
    @Test
    fun add_NullYear() {
        val movie = newData(null)
                .copy(year = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_YEAR_NULL", "Year mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad minimum year.
     */
    @Test
    fun add_BadMinimumYear() {
        val movie = newData(null)
                .copy(year = TestConstants.BAD_MIN_YEAR)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_YEAR_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad maximum year.
     */
    @Test
    fun add_BadMaximumYear() {
        val movie = newData(null)
                .copy(year = TestConstants.BAD_MAX_YEAR)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_YEAR_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null language.
     */
    @Test
    fun add_NullLanguage() {
        val movie = newData(null)
                .copy(language = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null subtitles.
     */
    @Test
    fun add_NullSubtitles() {
        val movie = newData(null)
                .copy(subtitles = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with subtitles with null value.
     */
    @Test
    fun add_BadSubtitles() {
        val movie = newData(null)
                .copy(subtitles = listOf(Language.CZ, null))

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null media.
     */
    @Test
    fun add_NullMedia() {
        val movie = newData(null)
                .copy(media = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with media with null value.
     */
    @Test
    fun add_BadMedia() {
        val movie = newData(null)
                .copy(media = listOf(MediumUtils.newMedium(1), null))

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with media with negative value as medium.
     */
    @Test
    fun add_BadMedium() {
        val badMedium = MediumUtils.newMedium(Integer.MAX_VALUE)
                .copy(length = -1)
        val movie = newData(null)
                .copy(media = listOf(MediumUtils.newMedium(1), badMedium))

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null URL to ČSFD page about movie.
     */
    @Test
    fun add_NullCsfd() {
        val movie = newData(null)
                .copy(csfd = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null IMDB code.
     */
    @Test
    fun add_NullImdb() {
        val movie = newData(null)
                .copy(imdbCode = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_IMDB_CODE_NULL", "IMDB code mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad minimal IMDB code.
     */
    @Test
    fun add_BadMinimalImdb() {
        val movie = newData(null)
                .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad divider IMDB code.
     */
    @Test
    fun add_BadDividerImdb() {
        val movie = newData(null)
                .copy(imdbCode = 0)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with bad maximal IMDB code.
     */
    @Test
    fun add_BadMaximalImdb() {
        val movie = newData(null)
                .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null URL to english Wikipedia page about movie.
     */
    @Test
    fun add_NullWikiEn() {
        val movie = newData(null)
                .copy(wikiEn = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                    "URL to english Wikipedia page about movie mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    fun add_NullWikiCz() {
        val movie = newData(null)
                .copy(wikiCz = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about movie mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null note.
     */
    @Test
    fun add_NullNote() {
        val movie = newData(null)
                .copy(note = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with not existing picture.
     */
    @Test
    fun add_NotExistingPicture() {
        val movie = newData(null)
                .copy(picture = Integer.MAX_VALUE)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PICTURE_NOT_EXIST", "Picture doesn't exist.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with null genres.
     */
    @Test
    fun add_NullGenres() {
        val movie = newData(null)
                .copy(genres = null)

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with genres with null value.
     */
    @Test
    fun add_BadGenres() {
        val movie = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), null))

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with genres with genre with null ID.
     */
    @Test
    fun add_NullGenreId() {
        val movie = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), GenreUtils.newGenre(null)))

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with genres with genre with null name.
     */
    @Test
    fun add_NullGenreName() {
        val badGenre = GenreUtils.newGenre(1)
                .copy(name = null)
        val movie = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), badGenre))

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with movie with genres with genre with empty string as name.
     */
    @Test
    fun add_EmptyGenreName() {
        val badGenre = GenreUtils.newGenre(1)
                .copy(name = "")
        val movie = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), badGenre))

        val result = facade.add(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.add] with show with genres with not existing genre.
     */
    @Test
    fun add_NotExistingGenre() {
        val show = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), GenreUtils.newGenre(Integer.MAX_VALUE)))

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NOT_EXIST", "Genre doesn't exist.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null czech name.
     */
    @Test
    fun update_NullCzechName() {
        val movie = newData(1)
                .copy(czechName = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CZECH_NAME_NULL", "Czech name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with empty string as czech name.
     */
    @Test
    fun update_EmptyCzechName() {
        val movie = newData(1)
                .copy(czechName = "")

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null original name.
     */
    @Test
    fun update_NullOriginalName() {
        val movie = newData(1)
                .copy(originalName = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_NULL", "Original name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with empty string as original name.
     */
    @Test
    fun update_EmptyOriginalName() {
        val movie = newData(1)
                .copy(originalName = "")

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null year.
     */
    @Test
    fun update_NullYear() {
        val movie = newData(1)
                .copy(year = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_YEAR_NULL", "Year mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad minimum year.
     */
    @Test
    fun update_BadMinimumYear() {
        val movie = newData(1)
                .copy(year = TestConstants.BAD_MIN_YEAR)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_YEAR_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad maximum year.
     */
    @Test
    fun update_BadMaximumYear() {
        val movie = newData(1)
                .copy(year = TestConstants.BAD_MAX_YEAR)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_YEAR_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null language.
     */
    @Test
    fun update_NullLanguage() {
        val movie = newData(1)
                .copy(language = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_LANGUAGE_NULL", "Language mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null subtitles.
     */
    @Test
    fun update_NullSubtitles() {
        val movie = newData(1)
                .copy(subtitles = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_SUBTITLES_NULL", "Subtitles mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with subtitles with null value.
     */
    @Test
    fun update_BadSubtitles() {
        val movie = newData(1)
                .copy(subtitles = listOf(Language.CZ, null))

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_SUBTITLES_CONTAIN_NULL", "Subtitles mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null media.
     */
    @Test
    fun update_NullMedia() {
        val movie = newData(1)
                .copy(media = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIA_NULL", "Media mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with media with null value.
     */
    @Test
    fun update_BadMedia() {
        val movie = newData(1)
                .copy(media = listOf(MediumUtils.newMedium(1), null))

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIA_CONTAIN_NULL", "Media mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with media with negative value as medium.
     */
    @Test
    fun update_BadMedium() {
        val badMedium = MediumUtils.newMedium(Integer.MAX_VALUE)
                .copy(length = -1)
        val movie = newData(1)
                .copy(media = listOf(MediumUtils.newMedium(1), badMedium))

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_MEDIUM_NOT_POSITIVE", "Length of medium must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null URL to ČSFD page about movie.
     */
    @Test
    fun update_NullCsfd() {
        val movie = newData(1)
                .copy(csfd = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_CSFD_NULL", "URL to ČSFD page about movie mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null IMDB code.
     */
    @Test
    fun update_NullImdb() {
        val movie = newData(1)
                .copy(imdbCode = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_IMDB_CODE_NULL", "IMDB code mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad minimal IMDB code.
     */
    @Test
    fun update_BadMinimalImdb() {
        val movie = newData(1)
                .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad divider IMDB code.
     */
    @Test
    fun update_BadDividerImdb() {
        val movie = newData(1)
                .copy(imdbCode = 0)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with bad maximal IMDB code.
     */
    @Test
    fun update_BadMaximalImdb() {
        val movie = newData(1)
                .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null URL to english Wikipedia page about movie.
     */
    @Test
    fun update_NullWikiEn() {
        val movie = newData(1)
                .copy(wikiEn = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_WIKI_EN_NULL",
                    "URL to english Wikipedia page about movie mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null URL to czech Wikipedia page about movie.
     */
    @Test
    fun update_NullWikiCz() {
        val movie = newData(1)
                .copy(wikiCz = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about movie mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null note.
     */
    @Test
    fun update_NullNote() {
        val movie = newData(1)
                .copy(note = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with not existing picture.
     */
    @Test
    fun update_NotExistingPicture() {
        val movie = newData(1)
                .copy(picture = Integer.MAX_VALUE)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PICTURE_NOT_EXIST", "Picture doesn't exist.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with null genres.
     */
    @Test
    fun update_NullGenres() {
        val movie = newData(1)
                .copy(genres = null)

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_GENRES_NULL", "Genres mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with genres with null value.
     */
    @Test
    fun update_BadGenres() {
        val movie = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), null))

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "MOVIE_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with genres with genre with null ID.
     */
    @Test
    fun update_NullGenreId() {
        val movie = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), GenreUtils.newGenre(null)))

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with genres with genre with null name.
     */
    @Test
    fun update_NullGenreName() {
        val badGenre = GenreUtils.newGenre(1)
                .copy(name = null)
        val movie = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), badGenre))

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with movie with genres with genre with empty string as name.
     */
    @Test
    fun update_EmptyGenreName() {
        val badGenre = GenreUtils.newGenre(1)
                .copy(name = "")
        val movie = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), badGenre))

        val result = facade.update(movie)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.update] with show with genres with not existing genre.
     */
    @Test
    fun update_NotExistingGenre() {
        val show = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), GenreUtils.newGenre(Integer.MAX_VALUE)))

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NOT_EXIST", "Genre doesn't exist.")))
        }

        assertDefaultRepositoryData()
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

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [MovieFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(1000))
            it.assertThat(result.events()).isEmpty()
        }

        assertDefaultRepositoryData()
    }


    override fun getFacade(): MovableParentFacade<Movie> {
        return facade
    }

    override fun getDefaultDataCount(): Int {
        return MovieUtils.MOVIES_COUNT
    }

    override fun getRepositoryDataCount(): Int {
        return MovieUtils.getMoviesCount(entityManager)
    }

    override fun getDataList(): List<cz.vhromada.catalog.domain.Movie> {
        return MovieUtils.getMovies()
    }

    override fun getDomainData(index: Int): cz.vhromada.catalog.domain.Movie {
        return MovieUtils.getMovie(index)
    }

    override fun newData(id: Int?): Movie {
        var movie = MovieUtils.newMovie(id)
        if (id == null || Integer.MAX_VALUE == id) {
            movie = movie.copy(picture = 1, genres = listOf(GenreUtils.newGenre(1)))
        }
        return movie
    }

    override fun newDomainData(id: Int): cz.vhromada.catalog.domain.Movie {
        return MovieUtils.newMovieDomain(id)
    }

    override fun getRepositoryData(id: Int): cz.vhromada.catalog.domain.Movie? {
        return MovieUtils.getMovie(entityManager, id)
    }

    override fun getName(): String {
        return "Movie"
    }

    override fun clearReferencedData() {}

    override fun assertDataListDeepEquals(expected: List<Movie>, actual: List<cz.vhromada.catalog.domain.Movie>) {
        MovieUtils.assertMovieListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Movie, actual: cz.vhromada.catalog.domain.Movie) {
        MovieUtils.assertMovieDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: cz.vhromada.catalog.domain.Movie, actual: cz.vhromada.catalog.domain.Movie) {
        MovieUtils.assertMovieDeepEquals(expected, actual)
    }

    override fun assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData()

        assertReferences()
    }

    override fun assertNewRepositoryData() {
        super.assertNewRepositoryData()

        assertSoftly {
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(0)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun assertAddRepositoryData() {
        super.assertAddRepositoryData()

        assertSoftly {
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 1)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData()

        assertReferences()
    }

    override fun assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData()

        assertSoftly {
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT - MovieUtils.getMovie(1).media.size)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData()

        assertSoftly {
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT + 2)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun getUpdateData(id: Int?): Movie {
        return super.getUpdateData(id)
                .copy(genres = listOf(GenreUtils.getGenre(1)))
    }

    override fun getExpectedAddData(): cz.vhromada.catalog.domain.Movie {
        return super.getExpectedAddData()
                .copy(media = listOf(MediumUtils.newMediumDomain(MediumUtils.MEDIA_COUNT + 1)),
                        picture = 1,
                        genres = listOf(GenreUtils.getGenreDomain(1)))
    }

    override fun getExpectedDuplicatedData(): cz.vhromada.catalog.domain.Movie {
        val medium1 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT - 1)
        medium1.id = MediumUtils.MEDIA_COUNT + 1
        val medium2 = MediumUtils.getMedium(MediumUtils.MEDIA_COUNT)
        medium2.id = MediumUtils.MEDIA_COUNT + 2

        return super.getExpectedDuplicatedData()
                .copy(media = listOf(medium1, medium2),
                        genres = listOf(GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT - 1), GenreUtils.getGenreDomain(GenreUtils.GENRES_COUNT)))
    }

    /**
     * Asserts references.
     */
    private fun assertReferences() {
        assertSoftly {
            it.assertThat(MediumUtils.getMediaCount(entityManager)).isEqualTo(MediumUtils.MEDIA_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
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

    }

}
