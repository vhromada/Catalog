package com.github.vhromada.catalog.facade

import com.github.vhromada.catalog.CatalogTestConfiguration
import com.github.vhromada.catalog.utils.AuditUtils
import com.github.vhromada.catalog.utils.EpisodeUtils
import com.github.vhromada.catalog.utils.SeasonUtils
import com.github.vhromada.catalog.utils.ShowUtils
import com.github.vhromada.catalog.utils.TestConstants
import com.github.vhromada.catalog.utils.fillAudit
import com.github.vhromada.common.entity.Language
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
 * A class represents integration test for class [SeasonFacade].
 *
 * @author Vladimir Hromada
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CatalogTestConfiguration::class])
@Transactional
@Rollback
class SeasonFacadeIntegrationTest {

    /**
     * Instance of [EntityManager]
     */
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Instance of [SeasonFacade]
     */
    @Autowired
    private lateinit var facade: SeasonFacade

    /**
     * Test method for [SeasonFacade.get].
     */
    @Test
    fun get() {
        for (i in 1..SeasonUtils.SEASONS_COUNT) {
            val result = facade.get(id = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            SeasonUtils.assertSeasonDeepEquals(expected = SeasonUtils.getSeasonDomain(index = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.get] with bad ID.
     */
    @Test
    fun getBadId() {
        val result = facade.get(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SEASON_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update].
     */
    @Test
    fun update() {
        val season = SeasonUtils.newSeason(id = 1)
        val expectedSeason = SeasonUtils.newSeasonDomain(id = 1)
            .copy(episodes = EpisodeUtils.getEpisodes(season = 1))
            .fillAudit(audit = AuditUtils.updatedAudit())
        expectedSeason.show = ShowUtils.getShow(entityManager = entityManager, id = 1)

        val result = facade.update(data = season)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        SeasonUtils.assertSeasonDeepEquals(expected = expectedSeason, actual = SeasonUtils.getSeason(entityManager = entityManager, id = 1)!!)

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with null ID.
     */
    @Test
    fun updateNullId() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(id = null)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_ID_NULL", message = "ID mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with null position.
     */
    @Test
    fun updateNullPosition() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(position = null)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_POSITION_NULL", message = "Position mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with null number of season.
     */
    @Test
    fun updateNullNumber() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(number = null)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NULL", message = "Number of season mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with not positive number of season.
     */
    @Test
    fun updateNotPositiveNumber() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(number = 0)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NOT_POSITIVE", message = "Number of season must be positive number.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with null starting year.
     */
    @Test
    fun updateNullStartYear() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(startYear = null)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_START_YEAR_NULL", message = "Starting year mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with null ending year.
     */
    @Test
    fun updateNullEndYear() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(endYear = null)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_END_YEAR_NULL", message = "Ending year mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    fun updateBadMinimumYears() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(startYear = TestConstants.BAD_MIN_YEAR, endYear = TestConstants.BAD_MIN_YEAR)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_STARTING_YEAR_EVENT, TestConstants.INVALID_ENDING_YEAR_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    fun updateBadMaximumYears() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(startYear = TestConstants.BAD_MAX_YEAR, endYear = TestConstants.BAD_MAX_YEAR)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_STARTING_YEAR_EVENT, TestConstants.INVALID_ENDING_YEAR_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with starting year greater than ending year.
     */
    @Test
    fun updateBadYears() {
        var season = SeasonUtils.newSeason(id = 4)
        season = season.copy(startYear = season.endYear!! + 1)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with null language.
     */
    @Test
    fun updateNullLanguage() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(language = null)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_LANGUAGE_NULL", message = "Language mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with null subtitles.
     */
    @Test
    fun updateNullSubtitles() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(subtitles = null)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_NULL", message = "Subtitles mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with subtitles with null value.
     */
    @Test
    fun updateBadSubtitles() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(subtitles = listOf(Language.CZ, null))

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with null note.
     */
    @Test
    fun updateNullNote() {
        val season = SeasonUtils.newSeason(id = 1)
            .copy(note = null)

        val result = facade.update(data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.update] with season with bad ID.
     */
    @Test
    fun updateBadId() {
        val result = facade.update(data = SeasonUtils.newSeason(id = Int.MAX_VALUE))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SEASON_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.remove].
     */
    @Test
    fun remove() {
        val result = facade.remove(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        assertThat(SeasonUtils.getSeason(entityManager = entityManager, id = 1)).isNull()

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT - 1)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT - EpisodeUtils.EPISODES_PER_SEASON_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.remove] with season with bad ID.
     */
    @Test
    fun removeBadId() {
        val result = facade.remove(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SEASON_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.duplicate].
     */
    @Test
    @DirtiesContext
    fun duplicate() {
        var expectedSeason = SeasonUtils.getSeasonDomain(index = SeasonUtils.SEASONS_COUNT)
        val expectedEpisodes = expectedSeason.episodes.mapIndexed { index, episode ->
            episode.copy(id = EpisodeUtils.EPISODES_COUNT + index + 1)
                .fillAudit(audit = AuditUtils.newAudit())
        }.toMutableList()
        expectedSeason = expectedSeason.copy(id = SeasonUtils.SEASONS_COUNT + 1, episodes = expectedEpisodes)
            .fillAudit(audit = AuditUtils.newAudit())
        expectedSeason.show = ShowUtils.getShow(entityManager = entityManager, id = ShowUtils.SHOWS_COUNT)

        val result = facade.duplicate(id = SeasonUtils.SEASONS_COUNT)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        SeasonUtils.assertSeasonDeepEquals(expected = expectedSeason, actual = SeasonUtils.getSeason(entityManager = entityManager, id = SeasonUtils.SEASONS_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT + 1)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT + EpisodeUtils.EPISODES_PER_SEASON_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.duplicate] with season with bad ID.
     */
    @Test
    fun duplicateBadId() {
        val result = facade.duplicate(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SEASON_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.moveUp].
     */
    @Test
    fun moveUp() {
        val result = facade.moveUp(id = 2)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val season1 = SeasonUtils.getSeasonDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val season2 = SeasonUtils.getSeasonDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        SeasonUtils.assertSeasonDeepEquals(expected = season1, actual = SeasonUtils.getSeason(entityManager = entityManager, id = 1)!!)
        SeasonUtils.assertSeasonDeepEquals(expected = season2, actual = SeasonUtils.getSeason(entityManager = entityManager, id = 2)!!)
        for (i in 3..SeasonUtils.SEASONS_COUNT) {
            SeasonUtils.assertSeasonDeepEquals(expected = SeasonUtils.getSeasonDomain(i), actual = SeasonUtils.getSeason(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.moveUp] with not movable season.
     */
    @Test
    fun moveUpNotMovable() {
        val result = facade.moveUp(id = 1)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOT_MOVABLE", message = "Season can't be moved up.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.moveUp] with season with bad ID.
     */
    @Test
    fun moveUpBadId() {
        val result = facade.moveUp(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SEASON_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.moveDown].
     */
    @Test
    fun moveDown() {
        val result = facade.moveDown(id = 1)
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        val season1 = SeasonUtils.getSeasonDomain(index = 1)
            .copy(position = 11)
            .fillAudit(audit = AuditUtils.updatedAudit())
        val season2 = SeasonUtils.getSeasonDomain(index = 2)
            .copy(position = 10)
            .fillAudit(audit = AuditUtils.updatedAudit())
        SeasonUtils.assertSeasonDeepEquals(expected = season1, actual = SeasonUtils.getSeason(entityManager = entityManager, id = 1)!!)
        SeasonUtils.assertSeasonDeepEquals(expected = season2, actual = SeasonUtils.getSeason(entityManager = entityManager, id = 2)!!)
        for (i in 3..SeasonUtils.SEASONS_COUNT) {
            SeasonUtils.assertSeasonDeepEquals(expected = SeasonUtils.getSeasonDomain(i), actual = SeasonUtils.getSeason(entityManager = entityManager, id = i)!!)
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.moveDown] with not movable season.
     */
    @Test
    fun moveDownNotMovable() {
        val result = facade.moveDown(id = SeasonUtils.SEASONS_COUNT)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOT_MOVABLE", message = "Season can't be moved down.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.moveDown] with season with bad ID.
     */
    @Test
    fun moveDownBadId() {
        val result = facade.moveDown(id = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SEASON_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add].
     */
    @Test
    @DirtiesContext
    fun add() {
        val expectedSeason = SeasonUtils.newSeasonDomain(id = SeasonUtils.SEASONS_COUNT + 1)
            .fillAudit(audit = AuditUtils.newAudit())
        expectedSeason.show = ShowUtils.getShow(entityManager = entityManager, id = 1)

        val result = facade.add(parent = 1, data = SeasonUtils.newSeason(id = null))
        entityManager.flush()

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.OK)
            it.assertThat(result.events()).isEmpty()
        }

        SeasonUtils.assertSeasonDeepEquals(expected = expectedSeason, actual = SeasonUtils.getSeason(entityManager = entityManager, id = SeasonUtils.SEASONS_COUNT + 1)!!)

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT + 1)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with bad show ID.
     */
    @Test
    fun addBadShowId() {
        val result = facade.add(parent = Int.MAX_VALUE, data = SeasonUtils.newSeason(id = null))

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SHOW_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with not null ID.
     */
    @Test
    fun addNotNullId() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(id = Int.MAX_VALUE)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_ID_NOT_NULL", message = "ID must be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with not null position.
     */
    @Test
    fun addNotNullPosition() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(position = Int.MAX_VALUE)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_POSITION_NOT_NULL", message = "Position must be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with null number of season.
     */
    @Test
    fun addNullNumber() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(number = null)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NULL", message = "Number of season mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with not positive number of season.
     */
    @Test
    fun addNotPositiveNumber() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(number = 0)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NUMBER_NOT_POSITIVE", message = "Number of season must be positive number.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with null starting year.
     */
    @Test
    fun addNullStartYear() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(startYear = null)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_START_YEAR_NULL", message = "Starting year mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with null ending year.
     */
    @Test
    fun addNullEndYear() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(endYear = null)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_END_YEAR_NULL", message = "Ending year mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with bad minimum starting year and bad minimum ending year.
     */
    @Test
    fun addBadMinimumYears() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(startYear = TestConstants.BAD_MIN_YEAR, endYear = TestConstants.BAD_MIN_YEAR)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_STARTING_YEAR_EVENT, TestConstants.INVALID_ENDING_YEAR_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with bad maximum starting year and bad maximum ending year.
     */
    @Test
    fun addBadMaximumYears() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(startYear = TestConstants.BAD_MAX_YEAR, endYear = TestConstants.BAD_MAX_YEAR)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(TestConstants.INVALID_STARTING_YEAR_EVENT, TestConstants.INVALID_ENDING_YEAR_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with starting year greater than ending year.
     */
    @Test
    fun addBadYears() {
        var season = SeasonUtils.newSeason(id = null)
        season = season.copy(startYear = season.endYear!! + 1)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_YEARS_NOT_VALID", "Starting year mustn't be greater than ending year.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with null language.
     */
    @Test
    fun addNullLanguage() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(language = null)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_LANGUAGE_NULL", message = "Language mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with null subtitles.
     */
    @Test
    fun addNullSubtitles() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(subtitles = null)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_NULL", message = "Subtitles mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with subtitles with null value.
     */
    @Test
    fun addBadSubtitles() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(subtitles = listOf(Language.CZ, null))

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_SUBTITLES_CONTAIN_NULL", message = "Subtitles mustn't contain null value.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.add] with season with null note.
     */
    @Test
    fun addNullNote() {
        val season = SeasonUtils.newSeason(id = null)
            .copy(note = null)

        val result = facade.add(parent = 1, data = season)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(Event(severity = Severity.ERROR, key = "SEASON_NOTE_NULL", message = "Note mustn't be null.")))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.find].
     */
    @Test
    fun find() {
        for (i in 1..ShowUtils.SHOWS_COUNT) {
            val result = facade.find(parent = i)

            assertSoftly {
                it.assertThat(result.status).isEqualTo(Status.OK)
                it.assertThat(result.data).isNotNull
                it.assertThat(result.events()).isEmpty()
            }
            SeasonUtils.assertSeasonListDeepEquals(expected = SeasonUtils.getSeasons(show = i), actual = result.data!!)
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    /**
     * Test method for [SeasonFacade.find] with bad show ID.
     */
    @Test
    fun findBadShowId() {
        val result = facade.find(parent = Int.MAX_VALUE)

        assertSoftly {
            it.assertThat(result.status).isEqualTo(Status.ERROR)
            it.assertThat(result.events()).isEqualTo(listOf(SHOW_NOT_EXIST_EVENT))
        }

        assertSoftly {
            it.assertThat(SeasonUtils.getSeasonsCount(entityManager = entityManager)).isEqualTo(SeasonUtils.SEASONS_COUNT)
            it.assertThat(ShowUtils.getShowsCount(entityManager = entityManager)).isEqualTo(ShowUtils.SHOWS_COUNT)
            it.assertThat(EpisodeUtils.getEpisodesCount(entityManager = entityManager)).isEqualTo(EpisodeUtils.EPISODES_COUNT)
        }
    }

    companion object {

        /**
         * Event for not existing season
         */
        private val SEASON_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "SEASON_NOT_EXIST", message = "Season doesn't exist.")

        /**
         * Event for not existing show
         */
        private val SHOW_NOT_EXIST_EVENT = Event(severity = Severity.ERROR, key = "SHOW_NOT_EXIST", message = "Show doesn't exist.")

    }

}
