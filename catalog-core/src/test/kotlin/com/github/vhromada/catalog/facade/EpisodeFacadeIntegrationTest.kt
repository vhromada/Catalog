package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.entity.Episode
import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.common.facade.MovableChildFacade
import com.github.vhromada.common.result.Event
import com.github.vhromada.common.result.Severity
import com.github.vhromada.common.result.Status
import com.github.vhromada.common.test.facade.MovableChildFacadeIntegrationTest
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.test.context.ContextConfiguration
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [EpisodeFacade].
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
class EpisodeFacadeIntegrationTest : MovableChildFacadeIntegrationTest<Episode, com.github.vhromada.catalog.domain.Episode, Season>() {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [EpisodeFacade]
     */
    @Autowired
    private lateinit var facade: EpisodeFacade

    /**
     * Test method for [EpisodeFacade.add] with episode with null number of episode.
     */
    @Test
    fun addNullNumber() {
        val episode = newChildData(null)
                .copy(number = null)

        val result = facade.add(SeasonUtils.newSeason(1), episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NUMBER_NULL", "Number of episode mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with not positive number of episode.
     */
    @Test
    fun addNotPositiveNumber() {
        val episode = newChildData(null)
                .copy(number = 0)

        val result = facade.add(SeasonUtils.newSeason(1), episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NUMBER_NOT_POSITIVE", "Number of episode must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with null name.
     */
    @Test
    fun addNullName() {
        val episode = newChildData(null)
                .copy(name = null)

        val result = facade.add(SeasonUtils.newSeason(1), episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val episode = newChildData(null)
                .copy(name = "")

        val result = facade.add(SeasonUtils.newSeason(1), episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with null length.
     */
    @Test
    fun addNullLength() {
        val episode = newChildData(null)
                .copy(length = null)

        val result = facade.add(SeasonUtils.newSeason(1), episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_LENGTH_NULL", "Length of episode mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with negative length.
     */
    @Test
    fun addNegativeLength() {
        val episode = newChildData(null)
                .copy(length = -1)

        val result = facade.add(SeasonUtils.newSeason(1), episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_LENGTH_NEGATIVE", "Length of episode mustn't be negative number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with null note.
     */
    @Test
    fun addNullNote() {
        val episode = newChildData(null)
                .copy(note = null)

        val result = facade.add(SeasonUtils.newSeason(1), episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null number of episode.
     */
    @Test
    fun updateNullNumber() {
        val episode = newChildData(1)
                .copy(number = null)

        val result = facade.update(episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NUMBER_NULL", "Number of episode mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with not positive number of episode.
     */
    @Test
    fun updateNotPositiveNumber() {
        val episode = newChildData(1)
                .copy(number = 0)

        val result = facade.update(episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NUMBER_NOT_POSITIVE", "Number of episode must be positive number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null name.
     */
    @Test
    fun updateNullName() {
        val episode = newChildData(1)
                .copy(name = null)

        val result = facade.update(episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NAME_NULL", "Name mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val episode = newChildData(1)
                .copy(name = "")

        val result = facade.update(episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NAME_EMPTY", "Name mustn't be empty string.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null length.
     */
    @Test
    fun updateNullLength() {
        val episode = newChildData(1)
                .copy(length = null)

        val result = facade.update(episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_LENGTH_NULL", "Length of episode mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with negative length.
     */
    @Test
    fun updateNegativeLength() {
        val episode = newChildData(1)
                .copy(length = -1)

        val result = facade.update(episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_LENGTH_NEGATIVE", "Length of episode mustn't be negative number.")))
        }

        assertDefaultRepositoryData()
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null note.
     */
    @Test
    fun updateNullNote() {
        val episode = newChildData(1)
                .copy(note = null)

        val result = facade.update(episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(Severity.ERROR, "${getChildPrefix()}_NOTE_NULL", "Note mustn't be null.")))
        }

        assertDefaultRepositoryData()
    }

    override fun getFacade(): MovableChildFacade<Episode, Season> {
        return facade
    }

    override fun getDefaultParentDataCount(): Int {
        return SeasonUtils.SEASONS_COUNT
    }

    override fun getDefaultChildDataCount(): Int {
        return EpisodeUtils.EPISODES_COUNT
    }

    override fun getRepositoryParentDataCount(): Int {
        return SeasonUtils.getSeasonsCount(entityManager)
    }

    override fun getRepositoryChildDataCount(): Int {
        return EpisodeUtils.getEpisodesCount(entityManager)
    }

    override fun getDataList(parentId: Int): List<com.github.vhromada.catalog.domain.Episode> {
        val showNumber = (parentId - 1) / SeasonUtils.SEASONS_PER_SHOW_COUNT + 1
        val seasonNumber = (parentId - 1) % SeasonUtils.SEASONS_PER_SHOW_COUNT + 1

        return EpisodeUtils.getEpisodes(showNumber, seasonNumber)
    }

    override fun getDomainData(index: Int): com.github.vhromada.catalog.domain.Episode {
        return EpisodeUtils.getEpisode(index)
    }

    override fun newParentData(id: Int?): Season {
        return SeasonUtils.newSeason(id)
    }

    override fun newChildData(id: Int?): Episode {
        return EpisodeUtils.newEpisode(id)
    }

    override fun newDomainData(id: Int): com.github.vhromada.catalog.domain.Episode {
        return EpisodeUtils.newEpisodeDomain(id)
    }

    override fun getRepositoryData(id: Int): com.github.vhromada.catalog.domain.Episode? {
        return EpisodeUtils.getEpisode(entityManager, id)
    }

    override fun getParentName(): String {
        return "Season"
    }

    override fun getChildName(): String {
        return "Episode"
    }

    override fun assertDataListDeepEquals(expected: List<Episode>, actual: List<com.github.vhromada.catalog.domain.Episode>) {
        EpisodeUtils.assertEpisodeListDeepEquals(expected, actual)
    }

    override fun assertDataDeepEquals(expected: Episode, actual: com.github.vhromada.catalog.domain.Episode) {
        EpisodeUtils.assertEpisodeDeepEquals(expected, actual)
    }

    override fun assertDataDomainDeepEquals(expected: com.github.vhromada.catalog.domain.Episode, actual: com.github.vhromada.catalog.domain.Episode) {
        EpisodeUtils.assertEpisodeDeepEquals(expected, actual)
    }

    override fun assertReferences() {
        super.assertReferences()

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(GenreUtils.getGenresCount(entityManager)).isEqualTo(GenreUtils.GENRES_COUNT)
        }
    }

}
