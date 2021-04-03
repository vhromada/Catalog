package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.PictureUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
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
 * A class represents integration test for class [ShowFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class ShowFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [ShowFacade]
     */
    @Autowired
    private lateinit var facade: ShowFacade

    /**
     * Test method for [ShowFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..ShowUtils.SHOWS_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            ShowUtils.assertShowDeepEquals(expected = ShowUtils.getShowDomain(index = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SHOW_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update].
     */
    @Test
    fun update() {
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = listOf(GenreUtils.getGenre(index = 1)))
        val expectedShow = ShowUtils.newShowDomain(id = 1)
            .copy(genres = listOf(GenreUtils.getGenreDomain(index = 1)), seasons = SeasonUtils.getSeasons(show = 1))
            .fillAudit(audit = AuditUtils.updatedAudit())

        val result = facade.update(data = show)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        ShowUtils.assertShowDeepEquals(expected = expectedShow, actual = ShowUtils.getShow(entityManager = entityManager, id = 1)!!)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null ID.
     */
    @Test
    fun updateNullId() {
        val show = ShowUtils.newShow(id = 1)
            .copy(id = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null position.
     */
    @Test
    fun updateNullPosition() {
        val show = ShowUtils.newShow(id = 1)
            .copy(position = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null czech name.
     */
    @Test
    fun updateNullCzechName() {
        val show = ShowUtils.newShow(id = 1)
            .copy(czechName = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_NULL", message = "Czech name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with empty string as czech name.
     */
    @Test
    fun updateEmptyCzechName() {
        val show = ShowUtils.newShow(id = 1)
            .copy(czechName = "")

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null original name.
     */
    @Test
    fun updateNullOriginalName() {
        val show = ShowUtils.newShow(id = 1)
            .copy(originalName = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_NULL", message = "Original name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with empty string as original name.
     */
    @Test
    fun updateEmptyOriginalName() {
        val show = ShowUtils.newShow(id = 1)
            .copy(originalName = "")

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null URL to ČSFD page about show.
     */
    @Test
    fun updateNullCsfd() {
        val show = ShowUtils.newShow(id = 1)
            .copy(csfd = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CSFD_NULL", message = "URL to ČSFD page about show mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null IMDB code.
     */
    @Test
    fun updateNullImdb() {
        val show = ShowUtils.newShow(id = 1)
            .copy(imdbCode = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_IMDB_CODE_NULL", message = "IMDB code mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with bad minimal IMDB code.
     */
    @Test
    fun updateBadMinimalImdb() {
        val show = ShowUtils.newShow(id = 1)
            .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with bad divider IMDB code.
     */
    @Test
    fun updateBadDividerImdb() {
        val show = ShowUtils.newShow(id = 1)
            .copy(imdbCode = 0)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with bad maximal IMDB code.
     */
    @Test
    fun updateBadMaximalImdb() {
        val show = ShowUtils.newShow(id = 1)
            .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null URL to english Wikipedia page about show.
     */
    @Test
    fun updateNullWikiEn() {
        val show = ShowUtils.newShow(id = 1)
            .copy(wikiEn = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_WIKI_EN_NULL", message = "URL to english Wikipedia page about show mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null URL to czech Wikipedia page about show.
     */
    @Test
    fun updateNullWikiCz() {
        val show = ShowUtils.newShow(id = 1)
            .copy(wikiCz = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about show mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null note.
     */
    @Test
    fun updateNullNote() {
        val show = ShowUtils.newShow(id = 1)
            .copy(note = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with not existing picture.
     */
    @Test
    fun updateNotExistingPicture() {
        val show = ShowUtils.newShow(id = 1)
            .copy(picture = Int.MAX_VALUE)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_EXIST", message = "Picture doesn't exist.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with null genres.
     */
    @Test
    fun updateNullGenres() {
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = null)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_GENRES_NULL", message = "Genres mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with null value.
     */
    @Test
    fun updateBadGenres() {
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), null))

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with genre with null ID.
     */
    @Test
    fun updateNullGenreId() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(id = null)
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with genre with null name.
     */
    @Test
    fun updateNullGenreName() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(name = null)
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with genre with empty string as name.
     */
    @Test
    fun updateEmptyGenreName() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(name = "")
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with not existing genre.
     */
    @Test
    fun updateNotExistingGenre() {
        val show = ShowUtils.newShow(id = 1)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), GenreUtils.newGenre(id = Int.MAX_VALUE)))

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_EXIST", message = "Genre doesn't exist.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.update] with show with bad ID.
     */
    @Test
    fun updateBadId() {
        val show = ShowUtils.newShow(id = 1)
            .copy(id = Int.MAX_VALUE)

        val result = facade.update(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SHOW_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(ShowUtils.getShow(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT - 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT - SeasonUtils.SEASONS_PER_SHOW_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SHOW_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.remove] with show with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SHOW_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        var expectedShow = ShowUtils.getShowDomain(index = ShowUtils.SHOWS_COUNT)
        val expectedSeasons = expectedShow.seasons.mapIndexed { index, season ->
            val expectedEpisodes = season.episodes.mapIndexed { episodeIndex, episode ->
                episode.copy(id = EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT * index + episodeIndex + 1)
                    .fillAudit(audit = AuditUtils.newAudit())
            }.toMutableList()
            season.copy(id = SeasonUtils.SEASONS_COUNT + index + 1, episodes = expectedEpisodes)
                .fillAudit(audit = AuditUtils.newAudit())
        }.toMutableList()
        expectedShow = expectedShow.copy(id = ShowUtils.SHOWS_COUNT + 1, seasons = expectedSeasons)
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.duplicate(id = ShowUtils.SHOWS_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        ShowUtils.assertShowDeepEquals(expected = expectedShow, actual = ShowUtils.getShow(entityManager = entityManager, id = ShowUtils.SHOWS_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT + 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT + SeasonUtils.SEASONS_PER_SHOW_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SHOW_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.duplicate] with show with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SHOW_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val show1 = ShowUtils.getShowDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val show2 = ShowUtils.getShowDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        ShowUtils.assertShowDeepEquals(expected = show1, actual = ShowUtils.getShow(entityManager = entityManager, id = 1)!!)
        ShowUtils.assertShowDeepEquals(expected = show2, actual = ShowUtils.getShow(entityManager = entityManager, id = 2)!!)
        for (i in 3..ShowUtils.SHOWS_COUNT) {
            ShowUtils.assertShowDeepEquals(expected = ShowUtils.getShowDomain(i), actual = ShowUtils.getShow(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.moveUp] with not movable show.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOT_MOVABLE", message = "Show can't be moved up.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.moveUp] with show with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SHOW_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val show1 = ShowUtils.getShowDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val show2 = ShowUtils.getShowDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        ShowUtils.assertShowDeepEquals(expected = show1, actual = ShowUtils.getShow(entityManager = entityManager, id = 1)!!)
        ShowUtils.assertShowDeepEquals(expected = show2, actual = ShowUtils.getShow(entityManager = entityManager, id = 2)!!)
        for (i in 3..ShowUtils.SHOWS_COUNT) {
            ShowUtils.assertShowDeepEquals(expected = ShowUtils.getShowDomain(i), actual = ShowUtils.getShow(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.moveDown] with not movable show.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = ShowUtils.SHOWS_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOT_MOVABLE", message = "Show can't be moved down.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.moveDown] with show with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SHOW_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.newData].
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
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.getAll].
     */
    @Test
    fun getAll() {
        val result = facade.getAll()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isNotNull
            it.assertThat(result.events()).isEmpty()
        }
        ShowUtils.assertShowListDeepEquals(expected = ShowUtils.getShows(), actual = result.data!!)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val show = ShowUtils.newShow(id = null)
            .copy(genres = listOf(GenreUtils.getGenre(index = 1)))
        val expectedShow = ShowUtils.newShowDomain(id = ShowUtils.SHOWS_COUNT + 1)
            .copy(picture = null, genres = listOf(GenreUtils.getGenreDomain(index = 1)))
            .fillAudit(audit = AuditUtils.newAudit())

        val result = facade.add(data = show)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        ShowUtils.assertShowDeepEquals(expected = expectedShow, actual = ShowUtils.getShow(entityManager = entityManager, id = ShowUtils.SHOWS_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT + 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with not null ID.
     */
    @Test
    fun addNotNullId() {
        val show = ShowUtils.newShow(id = null)
            .copy(id = Int.MAX_VALUE, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val show = ShowUtils.newShow(id = null)
            .copy(position = Int.MAX_VALUE, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with null czech name.
     */
    @Test
    fun addNullCzechName() {
        val show = ShowUtils.newShow(id = null)
            .copy(czechName = null, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_NULL", message = "Czech name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with empty string as czech name.
     */
    @Test
    fun addEmptyCzechName() {
        val show = ShowUtils.newShow(id = null)
            .copy(czechName = "", genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CZECH_NAME_EMPTY", message = "Czech name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with null original name.
     */
    @Test
    fun addNullOriginalName() {
        val show = ShowUtils.newShow(id = null)
            .copy(originalName = null, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_NULL", message = "Original name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with empty string as original name.
     */
    @Test
    fun addEmptyOriginalName() {
        val show = ShowUtils.newShow(id = null)
            .copy(originalName = "", genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_ORIGINAL_NAME_EMPTY", message = "Original name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with null URL to ČSFD page about show.
     */
    @Test
    fun addNullCsfd() {
        val show = ShowUtils.newShow(id = null)
            .copy(csfd = null, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_CSFD_NULL", message = "URL to ČSFD page about show mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with null IMDB code.
     */
    @Test
    fun addNullImdb() {
        val show = ShowUtils.newShow(id = null)
            .copy(imdbCode = null, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_IMDB_CODE_NULL", message = "IMDB code mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with bad minimal IMDB code.
     */
    @Test
    fun addBadMinimalImdb() {
        val show = ShowUtils.newShow(id = null)
            .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with bad divider IMDB code.
     */
    @Test
    fun addBadDividerImdb() {
        val show = ShowUtils.newShow(id = null)
            .copy(imdbCode = 0, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with bad maximal IMDB code.
     */
    @Test
    fun addBadMaximalImdb() {
        val show = ShowUtils.newShow(id = null)
            .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_SHOW_IMDB_CODE_EVENT))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with null URL to english Wikipedia page about show.
     */
    @Test
    fun addNullWikiEn() {
        val show = ShowUtils.newShow(id = null)
            .copy(wikiEn = null, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_WIKI_EN_NULL", message = "URL to english Wikipedia page about show mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with null URL to czech Wikipedia page about show.
     */
    @Test
    fun addNullWikiCz() {
        val show = ShowUtils.newShow(id = null)
            .copy(wikiCz = null, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_WIKI_CZ_NULL", message = "URL to czech Wikipedia page about show mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with null note.
     */
    @Test
    fun addNullNote() {
        val show = ShowUtils.newShow(id = null)
            .copy(note = null, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with not existing picture.
     */
    @Test
    fun addNotExistingPicture() {
        val show = ShowUtils.newShow(id = null)
            .copy(picture = Int.MAX_VALUE, genres = listOf(GenreUtils.newGenre(id = 1)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "PICTURE_NOT_EXIST", message = "Picture doesn't exist.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with null genres.
     */
    @Test
    fun addNullGenres() {
        val show = ShowUtils.newShow(id = null)
            .copy(genres = null)

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_GENRES_NULL", message = "Genres mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with null value.
     */
    @Test
    fun addBadGenres() {
        val show = ShowUtils.newShow(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), null))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SHOW_GENRES_CONTAIN_NULL", message = "Genres mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with genre with null ID.
     */
    @Test
    fun addNullGenreId() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(id = null)
        val show = ShowUtils.newShow(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with genre with null name.
     */
    @Test
    fun addNullGenreName() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(name = null)
        val show = ShowUtils.newShow(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with genre with empty string as name.
     */
    @Test
    fun addEmptyGenreName() {
        val badGenre = GenreUtils.newGenre(id = 1)
            .copy(name = "")
        val show = ShowUtils.newShow(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), badGenre))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with not existing genre.
     */
    @Test
    fun addNotExistingGenre() {
        val show = ShowUtils.newShow(id = null)
            .copy(genres = listOf(GenreUtils.newGenre(id = 1), GenreUtils.newGenre(id = Int.MAX_VALUE)))

        val result = facade.add(data = show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "GENRE_NOT_EXIST", message = "Genre doesn't exist.")))
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.updatePositions].
     */
    @Test
    fun updatePositions() {
        val result = facade.updatePositions()
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        for (i in 1..ShowUtils.SHOWS_COUNT) {
            var expectedShow = ShowUtils.getShowDomain(index = i)
            val expectedSeasons = expectedShow.seasons.mapIndexed { index, season ->
                val expectedEpisodes = season.episodes.mapIndexed { episodeIndex, episode ->
                    episode.copy(position = episodeIndex)
                        .fillAudit(audit = AuditUtils.updatedAudit())
                }.toMutableList()
                season.copy(position = index, episodes = expectedEpisodes)
                    .fillAudit(audit = AuditUtils.updatedAudit())
            }.toMutableList()
            expectedShow = expectedShow.copy(position = i - 1, seasons = expectedSeasons)
                .fillAudit(audit = AuditUtils.updatedAudit())
            ShowUtils.assertShowDeepEquals(expected = expectedShow, actual = ShowUtils.getShow(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(length = 1998))
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.getSeasonsCount].
     */
    @Test
    fun getSeasonsCount() {
        val result = facade.getSeasonsCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    /**
     * Test method for [ShowFacade.getEpisodesCount].
     */
    @Test
    fun getEpisodesCount() {
        val result = facade.getEpisodesCount()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(result.events()).isEmpty()
        }

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager = entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager = entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    companion object {

        /**
         * Event for not existing show
         */
        private val SHOW_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "SHOW_NOT_EXIST", message = "Show doesn't exist.")

    }

}
