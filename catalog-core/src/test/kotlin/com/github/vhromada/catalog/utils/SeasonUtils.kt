package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Season
import com.github.vhromada.common.entity.Language
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates season fields.
 *
 * @return updated season
 */
fun com.github.vhromada.catalog.domain.Season.updated(): com.github.vhromada.catalog.domain.Season {
    return copy(
        number = 2,
        startYear = SeasonUtils.START_YEAR,
        endYear = SeasonUtils.START_YEAR + 1,
        language = Language.SK,
        subtitles = listOf(Language.CZ),
        note = "Note"
    )
}

/**
 * Updates season fields.
 *
 * @return updated season
 */
fun Season.updated(): Season {
    return copy(
        number = 2,
        startYear = SeasonUtils.START_YEAR,
        endYear = SeasonUtils.START_YEAR + 1,
        language = Language.SK,
        subtitles = listOf(Language.CZ),
        note = "Note"
    )
}

/**
 * A class represents utility class for seasons.
 *
 * @author Vladimir Hromada
 */
object SeasonUtils {

    /**
     * Count of seasons
     */
    const val SEASONS_COUNT = 9

    /**
     * Count of seasons in show
     */
    const val SEASONS_PER_SHOW_COUNT = 3

    /**
     * Start year
     */
    const val START_YEAR = 2000

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Returns seasons for show.
     *
     * @param show show
     * @return seasons for show
     */
    fun getSeasons(show: Int): MutableList<com.github.vhromada.catalog.domain.Season> {
        val seasons = mutableListOf<com.github.vhromada.catalog.domain.Season>()
        for (i in 1..SEASONS_PER_SHOW_COUNT) {
            seasons.add(getSeasonDomain(showIndex = show, seasonIndex = i))
        }

        return seasons
    }

    /**
     * Returns season.
     *
     * @param id ID
     * @return season
     */
    fun newSeasonDomain(id: Int?): com.github.vhromada.catalog.domain.Season {
        return com.github.vhromada.catalog.domain.Season(
            id = id,
            number = 0,
            startYear = 0,
            endYear = 0,
            language = Language.JP,
            subtitles = emptyList(),
            note = null,
            position = if (id == null) null else id - 1,
            episodes = mutableListOf()
        ).updated()
    }

    /**
     * Returns season with show.
     *
     * @param id ID
     * @return season with show
     */
    fun newSeasonDomainWithShow(id: Int): com.github.vhromada.catalog.domain.Season {
        val season = newSeasonDomain(id = id)
        season.show = ShowUtils.newShowDomain(id = id)
        return season
    }

    /**
     * Returns season with episodes.
     *
     * @param id ID
     * @return season with episodes
     */
    fun newSeasonDomainWithEpisodes(id: Int?): com.github.vhromada.catalog.domain.Season {
        return newSeasonDomain(id = id)
            .copy(episodes = mutableListOf(EpisodeUtils.newEpisodeDomain(id = id)))
    }

    /**
     * Returns season.
     *
     * @param id ID
     * @return season
     */
    fun newSeason(id: Int?): Season {
        return Season(
            id = id,
            number = 0,
            startYear = 0,
            endYear = 0,
            language = Language.JP,
            subtitles = emptyList(),
            note = null,
            position = if (id == null) null else id - 1
        ).updated()
    }

    /**
     * Returns season for index.
     *
     * @param index index
     * @return season for index
     */
    fun getSeasonDomain(index: Int): com.github.vhromada.catalog.domain.Season {
        val showNumber = (index - 1) / SEASONS_PER_SHOW_COUNT + 1
        val seasonNumber = (index - 1) % SEASONS_PER_SHOW_COUNT + 1

        return getSeasonDomain(showIndex = showNumber, seasonIndex = seasonNumber)
    }

    /**
     * Returns season for indexes.
     *
     * @param showIndex   show index
     * @param seasonIndex season index
     * @return season for indexes
     */
    private fun getSeasonDomain(showIndex: Int, seasonIndex: Int): com.github.vhromada.catalog.domain.Season {
        val year = 1980
        val subtitles = mutableListOf<Language>()
        val language: Language
        when (seasonIndex) {
            1 -> {
                language = Language.EN
                subtitles.add(Language.CZ)
                subtitles.add(Language.EN)
            }
            2 -> language = Language.FR
            3 -> {
                language = Language.JP
                subtitles.add(Language.EN)
            }
            else -> throw IllegalArgumentException("Bad season index")
        }

        return com.github.vhromada.catalog.domain.Season(
            id = (showIndex - 1) * SEASONS_PER_SHOW_COUNT + seasonIndex,
            number = seasonIndex,
            startYear = year + seasonIndex,
            endYear = year + if (seasonIndex == 3) 4 else 2,
            language = language,
            subtitles = subtitles,
            note = if (seasonIndex == 2) "Show $showIndex Season 2 note" else "",
            position = seasonIndex + 9,
            episodes = EpisodeUtils.getEpisodes(show = showIndex, season = seasonIndex)
        ).fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns season.
     *
     * @param entityManager entity manager
     * @param id            season ID
     * @return season
     */
    fun getSeason(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Season? {
        return entityManager.find(com.github.vhromada.catalog.domain.Season::class.java, id)
    }

    /**
     * Returns season with updated fields.
     *
     * @param entityManager entity manager
     * @param id            season ID
     * @return season with updated fields
     */
    fun updateSeason(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Season {
        val storedSeason = getSeason(entityManager = entityManager, id = id)!!
        val season = storedSeason
            .updated()
            .copy(position = POSITION)
            .fillAudit(audit = storedSeason)
        season.show = storedSeason.show
        return season
    }

    /**
     * Returns count of seasons.
     *
     * @param entityManager entity manager
     * @return count of seasons
     */
    @Suppress("JpaQlInspection")
    fun getSeasonsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(s.id) FROM Season s", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts seasons deep equals.
     *
     * @param expected expected list of seasons
     * @param actual   actual list of seasons
     */
    fun assertDomainSeasonsDeepEquals(expected: List<com.github.vhromada.catalog.domain.Season>, actual: List<com.github.vhromada.catalog.domain.Season>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSeasonDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts season deep equals.
     *
     * @param expected      expected season
     * @param actual        actual season
     * @param checkEpisodes true if episodes should be checked
     */
    fun assertSeasonDeepEquals(expected: com.github.vhromada.catalog.domain.Season, actual: com.github.vhromada.catalog.domain.Season, checkEpisodes: Boolean = true) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.startYear).isEqualTo(expected.startYear)
            it.assertThat(actual.endYear).isEqualTo(expected.endYear)
            it.assertThat(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles)
                .hasSameSizeAs(expected.subtitles)
                .hasSameElementsAs(expected.subtitles)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
        if (checkEpisodes) {
            EpisodeUtils.assertDomainEpisodesDeepEquals(expected = expected.episodes, actual = actual.episodes)
        }
        if (expected.show != null) {
            assertThat(actual.show).isNotNull
            assertThat(actual.show!!.seasons).hasSameSizeAs(expected.show!!.seasons)
            ShowUtils.assertShowDeepEquals(expected = expected.show!!, actual = actual.show!!, checkSeasons = false)
        }
    }

    /**
     * Asserts seasons deep equals.
     *
     * @param expected expected list of seasons
     * @param actual   actual list of seasons
     */
    fun assertSeasonsDeepEquals(expected: List<Season>, actual: List<com.github.vhromada.catalog.domain.Season>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSeasonDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts season deep equals.
     *
     * @param expected expected season
     * @param actual   actual season
     */
    fun assertSeasonDeepEquals(expected: Season, actual: com.github.vhromada.catalog.domain.Season) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.startYear).isEqualTo(expected.startYear)
            it.assertThat(actual.endYear).isEqualTo(expected.endYear)
            it.assertThat(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles)
                .hasSameSizeAs(expected.subtitles)
                .hasSameElementsAs(expected.subtitles)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            it.assertThat(actual.episodes).isEmpty()
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
            it.assertThat(actual.show).isNull()
        }
    }

    /**
     * Asserts seasons deep equals.
     *
     * @param expected expected list of seasons
     * @param actual   actual list of seasons
     */
    fun assertSeasonListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Season>, actual: List<Season>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSeasonDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts season deep equals.
     *
     * @param expected expected season
     * @param actual   actual season
     */
    fun assertSeasonDeepEquals(expected: com.github.vhromada.catalog.domain.Season, actual: Season) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.startYear).isEqualTo(expected.startYear)
            it.assertThat(actual.endYear).isEqualTo(expected.endYear)
            it.assertThat(actual.language).isEqualTo(expected.language)
            it.assertThat(actual.subtitles)
                .hasSameSizeAs(expected.subtitles)
                .hasSameElementsAs(expected.subtitles)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
