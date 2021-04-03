package com.github.vhromada.catalog.repository

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.GenreUtils
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
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [ShowRepository]
     */
    @Autowired
    private lateinit var repository: ShowRepository

    /**
     * Test method for get shows.
     */
    @Test
    fun getShows() {
        val shows = repository.findAll()

        ShowUtils.assertDomainShowsDeepEquals(expected = ShowUtils.getShows(), actual = shows)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for get show.
     */
    @Test
    fun getShow() {
        for (i in 1..ShowUtils.SHOWS_COUNT) {
            val show = repository.findById(i).orElse(null)

            ShowUtils.assertShowDeepEquals(expected = ShowUtils.getShowDomain(index = i), actual = show)
        }

        assertThat(repository.findById(Int.MAX_VALUE)).isNotPresent

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for add show.
     */
    @Test
    fun add() {
        val show = ShowUtils.newShowDomain(id = null)
            .copy(position = ShowUtils.SHOWS_COUNT, genres = listOf(GenreUtils.getGenre(entityManager = entityManager, id = 1)!!))
        val expectedShow = ShowUtils.newShowDomain(id = ShowUtils.SHOWS_COUNT + 1)
            .copy(picture = null, genres = listOf(GenreUtils.getGenre(entityManager = entityManager, id = 1)!!))
            .fillAudit(audit = AuditUtils.newAudit())

        repository.save(show)

        assertSoftly {
            it.assertThat(show.id).isEqualTo(ShowUtils.SHOWS_COUNT + 1)
            it.assertThat(show.createdUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(show.createdTime).isEqualTo(TestConstants.TIME)
            it.assertThat(show.updatedUser).isEqualTo(TestConstants.ACCOUNT.uuid!!)
            it.assertThat(show.updatedTime).isEqualTo(TestConstants.TIME)
        }

        val addedShow = ShowUtils.getShow(entityManager, ShowUtils.SHOWS_COUNT + 1)!!
        assertThat(addedShow).isNotNull
        ShowUtils.assertShowDeepEquals(expected = expectedShow, actual = addedShow)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT + 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for update show.
     */
    @Test
    fun update() {
        val show = ShowUtils.updateShow(entityManager = entityManager, id = 1)
        val expectedShow = ShowUtils.getShowDomain(index = 1)
            .updated()
            .copy(position = ShowUtils.POSITION)
            .fillAudit(audit = AuditUtils.updatedAudit())

        repository.saveAndFlush(show)

        val updatedShow = ShowUtils.getShow(entityManager = entityManager, id = 1)
        assertThat(updatedShow).isNotNull
        ShowUtils.assertShowDeepEquals(expected = expectedShow, actual = updatedShow!!)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for remove show.
     */
    @Test
    fun remove() {
        repository.delete(ShowUtils.getShow(entityManager = entityManager, id = 1)!!)

        assertThat(ShowUtils.getShow(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT - 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT - SeasonUtils.SEASONS_PER_SHOW_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SHOW_COUNT)
        }
    }

    /**
     * Test method for remove all shows.
     */
    @Test
    fun removeAll() {
        repository.deleteAll()

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(0)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(0)
        }
    }

    /**
     * Test method for get shows for user.
     */
    @Test
    fun findByCreatedUser() {
        val shows = repository.findByCreatedUser(user = AuditUtils.getAudit().createdUser!!)

        ShowUtils.assertDomainShowsDeepEquals(expected = ShowUtils.getShows(), actual = shows)

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for get show by id for user.
     */
    @Test
    fun findByIdAndCreatedUser() {
        val user = AuditUtils.getAudit().createdUser!!
        for (i in 1..ShowUtils.SHOWS_COUNT) {
            val author = repository.findByIdAndCreatedUser(id = i, user = user).orElse(null)

            ShowUtils.assertShowDeepEquals(expected = ShowUtils.getShowDomain(index = i), actual = author)
        }

        assertThat(repository.findByIdAndCreatedUser(id = Int.MAX_VALUE, user = user)).isNotPresent

        assertSoftly {
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

}
