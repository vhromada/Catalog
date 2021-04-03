package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.catalog.utils.updated
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * A class represents integration test for class [SeasonRepository].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class SeasonRepositoryIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [SeasonRepository]
     */
    @Autowired
    private lateinit var repository: SeasonRepository

    /**
     * Test method for get season.
     */
    @Test
    fun getSeason() {
        for (i in 1..SeasonUtils.SEASONS_COUNT) {
            val season = repository.findById(i).orElse(null)

            SeasonUtils.assertSeasonDeepEquals(expected = SeasonUtils.getSeasonDomain(index = i), actual = season)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for add season.
     */
    @Test
    fun add() {
        val season = SeasonUtils.newSeasonDomain(id = null)
            .copy(position = SeasonUtils.SEASONS_COUNT)
        season.show = ShowUtils.getShow(entityManager = entityManager, id = 1)
        val expectedSeason = SeasonUtils.newSeasonDomain(id = SeasonUtils.SEASONS_COUNT + 1)
            .fillAudit(AuditUtils.newAudit())
        expectedSeason.show = ShowUtils.getShow(entityManager = entityManager, id = 1)

        repository.save(season)

        assertSoftly {
            it.assertThat(season.id).isEqualTo(SeasonUtils.SEASONS_COUNT + 1)
            it.assertThat(season.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(season.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(season.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(season.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedSeason = SeasonUtils.getSeason(entityManager = entityManager, id = SeasonUtils.SEASONS_COUNT + 1)
        assertThat(addedSeason).isNotNull
        SeasonUtils.assertSeasonDeepEquals(expected = expectedSeason, actual = addedSeason!!)

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT + 1)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for update season.
     */
    @Test
    fun update() {
        val season = SeasonUtils.updateSeason(entityManager = entityManager, id = 1)
        val expectedSeason = SeasonUtils.getSeasonDomain(index = 1)
            .updated()
            .copy(position = SeasonUtils.POSITION)
            .fillAudit(AuditUtils.updatedAudit())
        expectedSeason.show = ShowUtils.getShow(entityManager = entityManager, id = 1)

        repository.saveAndFlush(season)

        val updatedSeason = SeasonUtils.getSeason(entityManager = entityManager, id = 1)
        assertThat(updatedSeason).isNotNull
        SeasonUtils.assertSeasonDeepEquals(expectedSeason, updatedSeason!!)

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for get seasons by show.
     */
    @Test
    fun findAllByShowId() {
        for (i in 1..ShowUtils.SHOWS_COUNT) {
            val seasons = repository.findAllByShowId(id = i)

            SeasonUtils.assertDomainSeasonsDeepEquals(expected = SeasonUtils.getSeasons(show = i), actual = seasons)
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for get seasons for user by show.
     */
    @Test
    fun findAllByShowIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..ShowUtils.SHOWS_COUNT) {
            val seasons = repository.findAllByShowIdAndCreatedUser(id = i, user = user)

            SeasonUtils.assertDomainSeasonsDeepEquals(expected = SeasonUtils.getSeasons(show = i), actual = seasons)
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for get season by ID for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..SeasonUtils.SEASONS_COUNT) {
            val season = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            SeasonUtils.assertSeasonDeepEquals(expected = SeasonUtils.getSeasonDomain(index = i), actual = season)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

}
