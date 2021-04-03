package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Episode
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates episode fields.
 *
 * @return updated episode
 */
fun com.github.vhromada.catalog.domain.Episode.updated(): com.github.vhromada.catalog.domain.Episode {
    return copy(number = 2, name = "Name", length = 5, note = "Note")
}

/**
 * Updates episode fields.
 *
 * @return updated episode
 */
fun Episode.updated(): Episode {
    return copy(number = 2, name = "Name", length = 5, note = "Note")
}

/**
 * A class represents utility class for episodes.
 *
 * @author Vladimir Hromada
 */
object EpisodeUtils {

    /**
     * Count of episodes
     */
    const val EPISODES_COUNT = 27

    /**
     * Count of episodes in season
     */
    const val EPISODES_PER_SEASON_COUNT = 3

    /**
     * Count of episodes in show
     */
    const val EPISODES_PER_SHOW_COUNT = 9

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Multipliers for length
     */
    private val LENGTH_MULTIPLIERS = intArrayOf(1, 10, 100)

    /**
     * Returns episodes for show and season.
     *
     * @param show   show
     * @param season season
     * @return episodes for show and season
     */
    fun getEpisodes(show: Int, season: Int): MutableList<com.github.vhromada.catalog.domain.Episode> {
        val episodes = mutableListOf<com.github.vhromada.catalog.domain.Episode>()
        for (i in 1..EPISODES_PER_SEASON_COUNT) {
            episodes.add(getEpisodeDomain(showIndex = show, seasonIndex = season, episodeIndex = i))
        }

        return episodes
    }

    /**
     * Returns episodes for season.
     *
     * @param season season
     * @return episodes for season
     */
    fun getEpisodes(season: Int): MutableList<com.github.vhromada.catalog.domain.Episode> {
        val showNumber = (season - 1) / SeasonUtils.SEASONS_PER_SHOW_COUNT + 1
        val seasonNumber = (season - 1) % SeasonUtils.SEASONS_PER_SHOW_COUNT + 1

        val episodes = mutableListOf<com.github.vhromada.catalog.domain.Episode>()
        for (i in 1..EPISODES_PER_SEASON_COUNT) {
            episodes.add(getEpisodeDomain(showIndex = showNumber, seasonIndex = seasonNumber, episodeIndex = i))
        }

        return episodes
    }

    /**
     * Returns episode.
     *
     * @param id ID
     * @return episode
     */
    fun newEpisodeDomain(id: Int?): com.github.vhromada.catalog.domain.Episode {
        return com.github.vhromada.catalog.domain.Episode(
            id = id,
            number = 0,
            name = "",
            length = 0,
            note = null,
            position = if (id == null) null else id - 1
        ).updated()
    }

    /**
     * Returns episode with season.
     *
     * @param id ID
     * @return episode with season
     */
    fun newEpisodeDomainWithSeason(id: Int): com.github.vhromada.catalog.domain.Episode {
        val episode = newEpisodeDomain(id = id)
        episode.season = SeasonUtils.newSeasonDomainWithShow(id = id)
        return episode
    }

    /**
     * Returns episode.
     *
     * @param id ID
     * @return episode
     */
    fun newEpisode(id: Int?): Episode {
        return Episode(
            id = id,
            number = 0,
            name = "",
            length = 0,
            note = null,
            position = if (id == null) null else id - 1
        ).updated()
    }

    /**
     * Returns episode for index.
     *
     * @param index index
     * @return episode for index
     */
    fun getEpisodeDomain(index: Int): com.github.vhromada.catalog.domain.Episode {
        val showNumber = (index - 1) / EPISODES_PER_SHOW_COUNT + 1
        val seasonNumber = (index - 1) % EPISODES_PER_SHOW_COUNT / EPISODES_PER_SEASON_COUNT + 1
        val episodeNumber = (index - 1) % EPISODES_PER_SEASON_COUNT + 1

        return getEpisodeDomain(showIndex = showNumber, seasonIndex = seasonNumber, episodeIndex = episodeNumber)
    }

    /**
     * Returns episode for indexes.
     *
     * @param showIndex    show index
     * @param seasonIndex  season index
     * @param episodeIndex episode index
     * @return episode for indexes
     */
    private fun getEpisodeDomain(showIndex: Int, seasonIndex: Int, episodeIndex: Int): com.github.vhromada.catalog.domain.Episode {
        return com.github.vhromada.catalog.domain.Episode(
            id = (showIndex - 1) * EPISODES_PER_SHOW_COUNT + (seasonIndex - 1) * EPISODES_PER_SEASON_COUNT + episodeIndex,
            number = episodeIndex,
            name = "Show $showIndex Season $seasonIndex Episode $episodeIndex",
            length = episodeIndex * LENGTH_MULTIPLIERS[seasonIndex - 1],
            note = if (episodeIndex == 2) "Show $showIndex Season $seasonIndex Episode 2 note" else "",
            position = episodeIndex + 9
        ).fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns episode.
     *
     * @param entityManager entity manager
     * @param id            episode ID
     * @return episode
     */
    fun getEpisode(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Episode? {
        return entityManager.find(com.github.vhromada.catalog.domain.Episode::class.java, id)
    }

    /**
     * Returns episode with updated fields.
     *
     * @param entityManager entity manager
     * @param id            episode ID
     * @return episode with updated fields
     */
    fun updateEpisode(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Episode {
        val storedEpisode = getEpisode(entityManager = entityManager, id = id)!!
        val episode = storedEpisode
            .updated()
            .copy(position = POSITION)
            .fillAudit(audit = storedEpisode)
        episode.season = storedEpisode.season
        return episode
    }

    /**
     * Returns count of episodes.
     *
     * @param entityManager entity manager
     * @return count of episodes
     */
    @Suppress("JpaQlInspection")
    fun getEpisodesCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(e.id) FROM Episode e", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts episodes deep equals.
     *
     * @param expected expected list of episodes
     * @param actual   actual list of episodes
     */
    fun assertDomainEpisodesDeepEquals(expected: List<com.github.vhromada.catalog.domain.Episode>, actual: List<com.github.vhromada.catalog.domain.Episode>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertEpisodeDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts episode deep equals.
     *
     * @param expected expected episode
     * @param actual   actual episode
     */
    fun assertEpisodeDeepEquals(expected: com.github.vhromada.catalog.domain.Episode, actual: com.github.vhromada.catalog.domain.Episode) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.length).isEqualTo(expected.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
        if (expected.season != null) {
            assertThat(actual.season).isNotNull
            assertThat(actual.season!!.episodes).hasSameSizeAs(expected.season!!.episodes)
            SeasonUtils.assertSeasonDeepEquals(expected = expected.season!!, actual = actual.season!!, checkEpisodes = false)
        }
    }

    /**
     * Asserts episodes deep equals.
     *
     * @param expected expected list of episodes
     * @param actual   actual list of episodes
     */
    fun assertEpisodesDeepEquals(expected: List<Episode>, actual: List<com.github.vhromada.catalog.domain.Episode>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertEpisodeDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts episode deep equals.
     *
     * @param expected expected episode
     * @param actual   actual episode
     */
    fun assertEpisodeDeepEquals(expected: Episode, actual: com.github.vhromada.catalog.domain.Episode) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.length).isEqualTo(expected.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
            it.assertThat(actual.season).isNull()
        }
    }

    /**
     * Asserts episodes deep equals.
     *
     * @param expected expected list of episodes
     * @param actual   actual list of episodes
     */
    fun assertEpisodeListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Episode>, actual: List<Episode>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertEpisodeDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts episode deep equals.
     *
     * @param expected expected episode
     * @param actual   actual episode
     */
    fun assertEpisodeDeepEquals(expected: com.github.vhromada.catalog.domain.Episode, actual: Episode) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.number).isEqualTo(expected.number)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.length).isEqualTo(expected.length)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
