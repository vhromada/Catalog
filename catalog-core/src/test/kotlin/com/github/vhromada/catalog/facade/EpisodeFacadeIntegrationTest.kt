package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.fillAudit
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
 * A class represents integration test for class [EpisodeFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class EpisodeFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [EpisodeFacade]
     */
    @Autowired
    private lateinit var facade: EpisodeFacade

    /**
     * Test method for [EpisodeFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..EpisodeUtils.EPISODES_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            EpisodeUtils.assertEpisodeDeepEquals(expected = EpisodeUtils.getEpisodeDomain(index = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(EPISODE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update].
     */
    @Test
    fun update() {
        val episode = EpisodeUtils.newEpisode(id = 1)
        val expectedEpisode = EpisodeUtils.newEpisodeDomain(id = 1)
            .fillAudit(audit = AuditUtils.updatedAudit())
        expectedEpisode.season = SeasonUtils.getSeason(entityManager = entityManager, id = 1)

        val result = facade.update(data = episode)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        EpisodeUtils.assertEpisodeDeepEquals(expected = expectedEpisode, actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = 1)!!)

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null ID.
     */
    @Test
    fun updateNullId() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(id = null)

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null position.
     */
    @Test
    fun updateNullPosition() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(position = null)

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null number of episode.
     */
    @Test
    fun updateNullNumber() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(number = null)

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NULL", message = "Number of episode mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with not positive number of episode.
     */
    @Test
    fun updateNotPositiveNumber() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(number = 0)

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NOT_POSITIVE", message = "Number of episode must be positive number.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null name.
     */
    @Test
    fun updateNullName() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(name = null)

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with empty string as name.
     */
    @Test
    fun updateEmptyName() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(name = "")

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null length.
     */
    @Test
    fun updateNullLength() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(length = null)

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NULL", message = "Length of episode mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with negative length.
     */
    @Test
    fun updateNegativeLength() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(length = -1)

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NEGATIVE", message = "Length of episode mustn't be negative number.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with null note.
     */
    @Test
    fun updateNullNote() {
        val episode = EpisodeUtils.newEpisode(id = 1)
            .copy(note = null)

        val result = facade.update(data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.update] with episode with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = EpisodeUtils.newEpisode(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(EPISODE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(EpisodeUtils.getEpisode(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.remove] with episode with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(EPISODE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        val expectedEpisode = EpisodeUtils.getEpisodeDomain(index = EpisodeUtils.EPISODES_COUNT)
            .copy(id = EpisodeUtils.EPISODES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())
        expectedEpisode.season = SeasonUtils.getSeason(entityManager = entityManager, id = SeasonUtils.SEASONS_COUNT)

        val result = facade.duplicate(id = EpisodeUtils.EPISODES_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        EpisodeUtils.assertEpisodeDeepEquals(expected = expectedEpisode, actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = EpisodeUtils.EPISODES_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT + 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.duplicate] with episode with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(EPISODE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val episode1 = EpisodeUtils.getEpisodeDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val episode2 = EpisodeUtils.getEpisodeDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        EpisodeUtils.assertEpisodeDeepEquals(expected = episode1, actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = 1)!!)
        EpisodeUtils.assertEpisodeDeepEquals(expected = episode2, actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = 2)!!)
        for (i in 3..EpisodeUtils.EPISODES_COUNT) {
            EpisodeUtils.assertEpisodeDeepEquals(expected = EpisodeUtils.getEpisodeDomain(i), actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.moveUp] with not movable episode.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOT_MOVABLE", message = "Episode can't be moved up.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.moveUp] with episode with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(EPISODE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val episode1 = EpisodeUtils.getEpisodeDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val episode2 = EpisodeUtils.getEpisodeDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        EpisodeUtils.assertEpisodeDeepEquals(expected = episode1, actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = 1)!!)
        EpisodeUtils.assertEpisodeDeepEquals(expected = episode2, actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = 2)!!)
        for (i in 3..EpisodeUtils.EPISODES_COUNT) {
            EpisodeUtils.assertEpisodeDeepEquals(expected = EpisodeUtils.getEpisodeDomain(i), actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.moveDown] with not movable episode.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = EpisodeUtils.EPISODES_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOT_MOVABLE", message = "Episode can't be moved down.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.moveDown] with episode with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(EPISODE_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedEpisode = EpisodeUtils.newEpisodeDomain(id = EpisodeUtils.EPISODES_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())
        expectedEpisode.season = SeasonUtils.getSeason(entityManager = entityManager, id = 1)

        val result = facade.add(parent = 1, data = EpisodeUtils.newEpisode(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        EpisodeUtils.assertEpisodeDeepEquals(expected = expectedEpisode, actual = EpisodeUtils.getEpisode(entityManager = entityManager, id = EpisodeUtils.EPISODES_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT + 1)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with bad season ID.
     */
    @Test
    fun addBadSeasonId() {
        val result = facade.add(parent = Int.MAX_VALUE, data = EpisodeUtils.newEpisode(id = null))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SEASON_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with not null ID.
     */
    @Test
    fun addNotNullId() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with null number of episode.
     */
    @Test
    fun addNullNumber() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(number = null)

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NULL", message = "Number of episode mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with not positive number of episode.
     */
    @Test
    fun addNotPositiveNumber() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(number = 0)

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NUMBER_NOT_POSITIVE", message = "Number of episode must be positive number.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with null name.
     */
    @Test
    fun addNullName() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(name = null)

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NAME_NULL", message = "Name mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with empty string as name.
     */
    @Test
    fun addEmptyName() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(name = "")

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NAME_EMPTY", message = "Name mustn't be empty string.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with null length.
     */
    @Test
    fun addNullLength() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(length = null)

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NULL", message = "Length of episode mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with negative length.
     */
    @Test
    fun addNegativeLength() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(length = -1)

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_LENGTH_NEGATIVE", message = "Length of episode mustn't be negative number.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.add] with episode with null note.
     */
    @Test
    fun addNullNote() {
        val episode = EpisodeUtils.newEpisode(id = null)
            .copy(note = null)

        val result = facade.add(parent = 1, data = episode)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "EPISODE_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.find].
     */
    @Test
    fun find() {
        for (i in 1..SeasonUtils.SEASONS_COUNT) {
            val result = facade.find(parent = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            EpisodeUtils.assertEpisodeListDeepEquals(expected = EpisodeUtils.getEpisodes(season = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    /**
     * Test method for [EpisodeFacade.find] with bad season ID.
     */
    @Test
    fun findBadSeasonId() {
        val result = facade.find(parent = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SEASON_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
        }
    }

    companion object {

        /**
         * Event for not existing episode
         */
        private val EPISODE_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "EPISODE_NOT_EXIST", message = "Episode doesn't exist.")

        /**
         * Event for not existing season
         */
        private val SEASON_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "SEASON_NOT_EXIST", message = "Season doesn't exist.")

    }

}
