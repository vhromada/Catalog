package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.GenreUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * A class represents integration test for class [ShowRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class ShowRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @Autowired
    @Qualifier("containerManagedEntityManager")
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [ShowRepository]
     */
    @Autowired
    private lateinit var showRepository: ShowRepository

    /**
     * Test method for get shows.
     */
    @Test
    fun getShows() {
        val shows = showRepository.findAll(Sort.by("position", "id"))

        ShowUtils.assertShowsDeepEquals(ShowUtils.getShows(), shows)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for get show.
     */
    @Test
    @Suppress("UsePropertyAccessSyntax")
    fun getShow() {
        for (i in 1..ShowUtils.SHOWS_COUNT) {
            val show = showRepository.findById(i).orElse(null)

            ShowUtils.assertShowDeepEquals(ShowUtils.getShow(i), show)
        }

        assertThat(showRepository.findById(Int.MAX_VALUE).isPresent).isFalse()

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for add show.
     */
    @Test
    fun add() {
        val audit = AuditUtils.getAudit()
        val show = ShowUtils.newShowDomain(null)
                .copy(position = ShowUtils.SHOWS_COUNT, genres = listOf(GenreUtils.getGenre(entityManager, 1)!!), audit = audit)

        showRepository.save(show)

        assertThat(show.id).isEqualTo(ShowUtils.SHOWS_COUNT + 1)

        val addedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1)!!
        val expectedAddedShow = ShowUtils.newShowDomain(null)
                .copy(id = ShowUtils.SHOWS_COUNT + 1, position = ShowUtils.SHOWS_COUNT, genres = listOf(GenreUtils.getGenreDomain(1)), audit = audit)
        ShowUtils.assertShowDeepEquals(expectedAddedShow, addedShow)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT + 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for update show with updated data.
     */
    @Test
    fun updateData() {
        val show = ShowUtils.updateShow(entityManager, 1)

        showRepository.save(show)

        val updatedShow = ShowUtils.getShow(entityManager, 1)!!
        val expectedUpdatedShow = ShowUtils.getShow(1)
                .updated()
                .copy(position = ShowUtils.POSITION)
        ShowUtils.assertShowDeepEquals(expectedUpdatedShow, updatedShow)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for update show with added season.
     */
    @Test
    fun updateAddedSeason() {
        val audit = AuditUtils.getAudit()
        var season = SeasonUtils.newSeasonDomain(null)
        season = season.copy(subtitles = season.subtitles.toMutableList(),
                position = SeasonUtils.SEASONS_COUNT, audit = audit)
        entityManager.persist(season)

        var show = ShowUtils.getShow(entityManager, 1)!!
        val seasons = show.seasons.toMutableList()
        seasons.add(season)
        show = show.copy(seasons = seasons)

        showRepository.save(show)

        val updatedShow = ShowUtils.getShow(entityManager, 1)!!
        val expectedSeason = SeasonUtils.newSeasonDomain(null)
                .copy(id = SeasonUtils.SEASONS_COUNT + 1, position = SeasonUtils.SEASONS_COUNT, audit = audit)
        var expectedUpdatedShow = ShowUtils.getShow(1)
        val expectedSeasons = expectedUpdatedShow.seasons.toMutableList()
        expectedSeasons.add(expectedSeason)
        expectedUpdatedShow = expectedUpdatedShow.copy(seasons = seasons)
        ShowUtils.assertShowDeepEquals(expectedUpdatedShow, updatedShow)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT + 1)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for remove show.
     */
    @Test
    fun remove() {
        val seasonsCount = ShowUtils.getShow(1).seasons.size
        val episodesCount = seasonsCount * EpisodeUtils.EPISODES_PER_SEASON_COUNT

        showRepository.delete(ShowUtils.getShow(entityManager, 1)!!)

        assertThat(ShowUtils.getShow(entityManager, 1)).isNull()

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT - 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT - seasonsCount)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - episodesCount)
        }
    }

    /**
     * Test method for remove all shows.
     */
    @Test
    fun removeAll() {
        showRepository.deleteAll()

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(0)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(0)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(0)
        }
    }

    /**
     * Test method for get shows for user.
     */
    @Test
    fun findByAuditCreatedUser() {
        val shows = showRepository.findByAuditCreatedUser(AuditUtils.getAudit().createdUser)

        ShowUtils.assertShowsDeepEquals(ShowUtils.getShows(), shows)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

}
