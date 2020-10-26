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
    return copy(number = 2,
            startYear = SeasonUtils.START_YEAR,
            endYear = SeasonUtils.START_YEAR + 1,
            language = Language.SK,
            subtitles = listOf(Language.CZ),
            note = "Note",
            audit = AuditUtils.newAudit())
}

/**
 * Updates season fields.
 *
 * @return updated season
 */
fun Season.updated(): Season {
    return copy(number = 2,
            startYear = SeasonUtils.START_YEAR,
            endYear = SeasonUtils.START_YEAR + 1,
            language = Language.SK,
            subtitles = listOf(Language.CZ),
            note = "Note")
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
                episodes = emptyList(),
                audit = null)
                .updated()
    }

    /**
     * Returns season with episodes.
     *
     * @param id ID
     * @return season with episodes
     */
    fun newSeasonWithEpisodes(id: Int?): com.github.vhromada.catalog.domain.Season {
        return newSeasonDomain(id)
                .copy(episodes = listOf(EpisodeUtils.newEpisodeDomain(id)))
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
                position = if (id == null) null else id - 1)
                .updated()
    }

    /**
     * Returns seasons for show.
     *
     * @param show show
     * @return seasons for show
     */
    fun getSeasons(show: Int): List<com.github.vhromada.catalog.domain.Season> {
        val seasons = mutableListOf<com.github.vhromada.catalog.domain.Season>()
        for (i in 1..SEASONS_PER_SHOW_COUNT) {
            seasons.add(getSeason(show, i))
        }

        return seasons
    }

    /**
     * Returns season for index.
     *
     * @param index index
     * @return season for index
     */
    fun getSeason(index: Int): com.github.vhromada.catalog.domain.Season {
        val showNumber = (index - 1) / SEASONS_PER_SHOW_COUNT + 1
        val seasonNumber = (index - 1) % SEASONS_PER_SHOW_COUNT + 1

        return getSeason(showNumber, seasonNumber)
    }

    /**
     * Returns season for indexes.
     *
     * @param showIndex   show index
     * @param seasonIndex season index
     * @return season for indexes
     */
    private fun getSeason(showIndex: Int, seasonIndex: Int): com.github.vhromada.catalog.domain.Season {
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
                position = seasonIndex - 1,
                episodes = EpisodeUtils.getEpisodes(showIndex, seasonIndex),
                audit = AuditUtils.getAudit())
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
     * Returns count of seasons.
     *
     * @param entityManager entity manager
     * @return count of seasons
     */
    fun getSeasonsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(s.id) FROM Season s", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts seasons deep equals.
     *
     * @param expected expected list of seasons
     * @param actual   actual list of seasons
     */
    fun assertSeasonsDeepEquals(expected: List<com.github.vhromada.catalog.domain.Season?>?, actual: List<com.github.vhromada.catalog.domain.Season?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSeasonDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts season deep equals.
     *
     * @param expected expected season
     * @param actual   actual season
     */
    fun assertSeasonDeepEquals(expected: com.github.vhromada.catalog.domain.Season?, actual: com.github.vhromada.catalog.domain.Season?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
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
        EpisodeUtils.assertEpisodesDeepEquals(expected!!.episodes, actual!!.episodes)
        AuditUtils.assertAuditDeepEquals(expected.audit, actual.audit)
    }

    /**
     * Asserts seasons deep equals.
     *
     * @param expected expected list of seasons
     * @param actual   actual list of seasons
     */
    fun assertSeasonListDeepEquals(expected: List<Season?>?, actual: List<com.github.vhromada.catalog.domain.Season?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertSeasonDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts season deep equals.
     *
     * @param expected expected season
     * @param actual   actual season
     */
    fun assertSeasonDeepEquals(expected: Season?, actual: com.github.vhromada.catalog.domain.Season?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
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
