package cz.vhromada.catalog.facade.impl

import cz.vhromada.catalog.CatalogTestConfiguration
import cz.vhromada.catalog.entity.Show
import cz.vhromada.catalog.facade.ShowFacade
import cz.vhromada.catalog.utils.EpisodeUtils
import cz.vhromada.catalog.utils.GenreUtils
import cz.vhromada.catalog.utils.PictureUtils
import cz.vhromada.catalog.utils.SeasonUtils
import cz.vhromada.catalog.utils.ShowUtils
import cz.vhromada.common.Time
import cz.vhromada.common.facade.MovableParentFacade
import cz.vhromada.common.result.Event
import cz.vhromada.common.result.Severity
import cz.vhromada.common.result.Status
import cz.vhromada.common.test.facade.MovableParentFacadeIntegrationTest
import cz.vhromada.common.test.utils.TestConstants
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [ShowFacadeImpl].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class ShowFacadeImplIntegrationTest : MovableParentFacadeIntegrationTest<Show, cz.vhromada.catalog.domain.Show>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [ShowFacade]
     */
    @Autowired
    private lateinit var facade: ShowFacade

    /**
     * Test method for [ShowFacade.add] with show with null czech name.
     */
    @Test
    fun addNullCzechName() {
        val show = newData(null)
                .copy(czechName = null)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with empty string as czech name.
     */
    @Test
    fun addEmptyCzechName() {
        val show = newData(null)
                .copy(czechName = "")

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with null original name.
     */
    @Test
    fun addNullOriginalName() {
        val show = newData(null)
                .copy(originalName = null)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with empty string as original name.
     */
    @Test
    fun addEmptyOriginalName() {
        val show = newData(null)
                .copy(originalName = "")

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with null URL to ČSFD page about show.
     */
    @Test
    fun addNullCsfd() {
        val show = newData(null)
                .copy(csfd = null)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with null IMDB code.
     */
    @Test
    fun addNullImdb() {
        val show = newData(null)
                .copy(imdbCode = null)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_IMDB_CODE_NULL", "IMDB code mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with bad minimal IMDB code.
     */
    @Test
    fun addBadMinimalImdb() {
        val show = newData(null)
                .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with bad divider IMDB code.
     */
    @Test
    fun addBadDividerImdb() {
        val show = newData(null)
                .copy(imdbCode = 0)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with bad maximal IMDB code.
     */
    @Test
    fun addBadMaximalImdb() {
        val show = newData(null)
                .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with null URL to english Wikipedia page about show.
     */
    @Test
    fun addNullWikiEn() {
        val show = newData(null)
                .copy(wikiEn = null)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_WIKI_EN_NULL",
                    "URL to english Wikipedia page about show mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with null URL to czech Wikipedia page about show.
     */
    @Test
    fun addNullWikiCz() {
        val show = newData(null)
                .copy(wikiCz = null)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about show mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with null note.
     */
    @Test
    fun addNullNote() {
        val show = newData(null)
                .copy(note = null)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with not existing picture.
     */
    @Test
    fun addNotExistingPicture() {
        val show = newData(null)
                .copy(picture = Integer.MAX_VALUE)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PICTURE_NOT_EXIST", "Picture doesn't exist.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with null genres.
     */
    @Test
    fun addNullGenres() {
        val show = newData(null)
                .copy(genres = null)

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with null value.
     */
    @Test
    fun addBadGenres() {
        val show = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), null))

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with genre with null ID.
     */
    @Test
    fun addNullGenreId() {
        val show = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), GenreUtils.newGenre(null)))

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with genre with null name.
     */
    @Test
    fun addNullGenreName() {
        val badGenre = GenreUtils.newGenre(1)
                .copy(name = null)
        val show = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), badGenre))

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with genre with empty string as name.
     */
    @Test
    fun addEmptyGenreName() {
        val badGenre = GenreUtils.newGenre(1)
                .copy(name = "")
        val show = newData(null)
                .copy(genres = listOf(GenreUtils.newGenre(1), badGenre))

        val result = facade.add(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.add] with show with genres with not existing genre.
     */
    @Test
    fun addNotExistingGenre() {
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
     * Test method for [ShowFacade.update] with show with null czech name.
     */
    @Test
    fun updateNullCzechName() {
        val show = newData(1)
                .copy(czechName = null)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_CZECH_NAME_NULL", "Czech name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with empty string as czech name.
     */
    @Test
    fun updateEmptyCzechName() {
        val show = newData(1)
                .copy(czechName = "")

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_CZECH_NAME_EMPTY", "Czech name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with null original name.
     */
    @Test
    fun updateNullOriginalName() {
        val show = newData(1)
                .copy(originalName = null)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_NULL", "Original name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with empty string as original name.
     */
    @Test
    fun updateEmptyOriginalName() {
        val show = newData(1)
                .copy(originalName = "")

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_ORIGINAL_NAME_EMPTY", "Original name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with null URL to ČSFD page about show.
     */
    @Test
    fun updateNullCsfd() {
        val show = newData(1)
                .copy(csfd = null)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_CSFD_NULL", "URL to ČSFD page about show mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with null IMDB code.
     */
    @Test
    fun updateNullImdb() {
        val show = newData(1)
                .copy(imdbCode = null)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_IMDB_CODE_NULL", "IMDB code mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with bad minimal IMDB code.
     */
    @Test
    fun updateBadMinimalImdb() {
        val show = newData(1)
                .copy(imdbCode = TestConstants.BAD_MIN_IMDB_CODE)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with bad divider IMDB code.
     */
    @Test
    fun updateBadDividerImdb() {
        val show = newData(1)
                .copy(imdbCode = 0)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with bad maximal IMDB code.
     */
    @Test
    fun updateBadMaximalImdb() {
        val show = newData(1)
                .copy(imdbCode = TestConstants.BAD_MAX_IMDB_CODE)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(INVALID_IMDB_CODE_EVENT))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with null URL to english Wikipedia page about show.
     */
    @Test
    fun updateNullWikiEn() {
        val show = newData(1)
                .copy(wikiEn = null)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_WIKI_EN_NULL",
                    "URL to english Wikipedia page about show mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with null URL to czech Wikipedia page about show.
     */
    @Test
    fun updateNullWikiCz() {
        val show = newData(1)
                .copy(wikiCz = null)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_WIKI_CZ_NULL",
                    "URL to czech Wikipedia page about show mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with null note.
     */
    @Test
    fun updateNullNote() {
        val show = newData(1)
                .copy(note = null)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with not existing picture.
     */
    @Test
    fun updateNotExistingPicture() {
        val show = newData(1)
                .copy(picture = Integer.MAX_VALUE)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "PICTURE_NOT_EXIST", "Picture doesn't exist.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with null genres.
     */
    @Test
    fun updateNullGenres() {
        val show = newData(1)
                .copy(genres = null)

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_GENRES_NULL", "Genres mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with null value.
     */
    @Test
    fun updateBadGenres() {
        val show = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), null))

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "SHOW_GENRES_CONTAIN_NULL", "Genres mustn't contain null value.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with genre with null ID.
     */
    @Test
    fun updateNullGenreId() {
        val show = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), GenreUtils.newGenre(null)))

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_ID_NULL", "ID mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with genre with null name.
     */
    @Test
    fun updateNullGenreName() {
        val badGenre = GenreUtils.newGenre(1)
                .copy(name = null)
        val show = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), badGenre))

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with genre with empty string as name.
     */
    @Test
    fun updateEmptyGenreName() {
        val badGenre = GenreUtils.newGenre(1)
                .copy(name = "")
        val show = newData(1)
                .copy(genres = listOf(GenreUtils.newGenre(1), badGenre))

        val result = facade.update(show)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "GENRE_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [ShowFacade.update] with show with genres with not existing genre.
     */
    @Test
    fun updateNotExistingGenre() {
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
     * Test method for [ShowFacade.getTotalLength].
     */
    @Test
    fun getTotalLength() {
        val result = facade.getTotalLength()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.data).isEqualTo(Time(1998))
            it.assertThat(result.events()).isEmpty()
        }

        assertDefaultRepositoryData()
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

        assertDefaultRepositoryData()
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

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableParentFacade<Show> {
        return facade
    }

    override fun getDefaultDataCount(): Int {
        return ShowUtils.SHOWS_COUNT
    }

    override fun getRepositoryDataCount(): Int {
        return ShowUtils.getShowsCount(entityManager)
    }

    override fun getDataList(): List<cz.vhromada.catalog.domain.Show> {
        return ShowUtils.getShows()
    }

    override fun getDomainData(index: Int): cz.vhromada.catalog.domain.Show {
        return ShowUtils.getShow(index)
    }

    override fun newData(id: Int?): Show {
        var show = ShowUtils.newShow(id)
        if (id == null || Integer.MAX_VALUE == id) {
            show = show.copy(picture = 1, genres = listOf(GenreUtils.newGenre(1)))
        }
        return show
    }

    override fun newDomainData(id: Int): cz.vhromada.catalog.domain.Show {
        return ShowUtils.newShowDomain(id)
    }

    override fun getRepositoryData(id: Int): cz.vhromada.catalog.domain.Show? {
        return ShowUtils.getShow(entityManager, id)
    }

    override fun getName(): String {
        return "Show"
    }

    override fun clearReferencedData() {}

    override fun assertDataListDeepEquals(expected: List<Show>, actual: List<cz.vhromada.catalog.domain.Show>) {
        ShowUtils.assertShowListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Show, actual: cz.vhromada.catalog.domain.Show) {
        ShowUtils.assertShowDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: cz.vhromada.catalog.domain.Show, actual: cz.vhromada.catalog.domain.Show) {
        ShowUtils.assertShowDeepEquals(expected, actual)
    }

    override fun assertDefaultRepositoryData() {
        super.assertDefaultRepositoryData()

        assertReferences()
    }

    override fun assertNewRepositoryData() {
        super.assertNewRepositoryData()

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(0)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(0)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun assertAddRepositoryData() {
        super.assertAddRepositoryData()

        assertReferences()
    }

    override fun assertUpdateRepositoryData() {
        super.assertUpdateRepositoryData()

        assertReferences()
    }

    override fun assertRemoveRepositoryData() {
        super.assertRemoveRepositoryData()

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT - SeasonUtils.SEASONS_PER_SHOW_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SHOW_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun assertDuplicateRepositoryData() {
        super.assertDuplicateRepositoryData()

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT + SeasonUtils.SEASONS_PER_SHOW_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SHOW_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    override fun getUpdateData(id: Int?): Show {
        return super.getUpdateData(id)
                .copy(genres = listOf(GenreUtils.getGenre(4)))
    }

    override fun getExpectedAddData(): cz.vhromada.catalog.domain.Show {
        return super.getExpectedAddData()
                .copy(picture = 1, genres = listOf(GenreUtils.getGenreDomain(1)))
    }

    override fun getExpectedDuplicatedData(): cz.vhromada.catalog.domain.Show {
        val show = super.getExpectedDuplicatedData()
        for (season in show.seasons) {
            val index = show.seasons.indexOf(season)
            season.id = SeasonUtils.SEASONS_COUNT + index + 1
            for (episode in season.episodes) {
                episode.id = EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT * index + season.episodes.indexOf(episode) + 1
            }
        }

        return show
    }

    /**
     * Asserts references.
     */
    private fun assertReferences() {
        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(PictureUtils.getPicturesCount(entityManager)).isEqualTo(PictureUtils.PICTURES_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

    companion object {

        /**
         * Event for invalid IMDB code
         */
        private val INVALID_IMDB_CODE_EVENT = Event(Severity.ERROR, "SHOW_IMDB_CODE_NOT_VALID", "IMDB code must be between 1 and 9999999 or -1.")
    }

}
