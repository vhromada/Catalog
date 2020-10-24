package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.common.Format
import com.github.vhromada.catalog.entity.Game
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates game fields.
 *
 * @return updated game
 */
fun com.github.vhromada.catalog.domain.Game.updated(): com.github.vhromada.catalog.domain.Game {
    return copy(name = "Name",
            wikiEn = "enWiki",
            wikiCz = "czWiki",
            mediaCount = 1,
            format = Format.STEAM,
            crack = true,
            serialKey = true,
            patch = true,
            trainer = true,
            trainerData = true,
            editor = true,
            saves = true,
            otherData = "Other data",
            note = "Note",
            audit = AuditUtils.newAudit())
}

/**
 * Updates game fields.
 *
 * @return updated game
 */
fun Game.updated(): Game {
    return copy(name = "Name",
            wikiEn = "enWiki",
            wikiCz = "czWiki",
            mediaCount = 1,
            format = Format.STEAM,
            crack = true,
            serialKey = true,
            patch = true,
            trainer = true,
            trainerData = true,
            editor = true,
            saves = true,
            otherData = "Other data",
            note = "Note")
}

/**
 * A class represents utility class for games.
 *
 * @author Vladimir Hromada
 */
object GameUtils {

    /**
     * Count of games
     */
    const val GAMES_COUNT = 3

    /**
     * Position
     */
    const val POSITION = 10

    /**
     * Game name
     */
    private const val GAME = "Game "

    /**
     * Returns games.
     *
     * @return games
     */
    fun getGames(): List<com.github.vhromada.catalog.domain.Game> {
        val games = mutableListOf<com.github.vhromada.catalog.domain.Game>()
        for (i in 0 until GAMES_COUNT) {
            games.add(getGame(i + 1))
        }

        return games
    }

    /**
     * Returns game.
     *
     * @param id ID
     * @return game
     */
    fun newGameDomain(id: Int?): com.github.vhromada.catalog.domain.Game {
        return com.github.vhromada.catalog.domain.Game(
                id = id,
                name = "",
                wikiEn = null,
                wikiCz = null,
                mediaCount = 0,
                format = Format.STEAM,
                crack = false,
                serialKey = false,
                patch = false,
                trainer = false,
                trainerData = false,
                editor = false,
                saves = false,
                otherData = null,
                note = null,
                position = if (id == null) null else id - 1,
                audit = null)
                .updated()
    }

    /**
     * Returns game.
     *
     * @param id ID
     * @return game
     */
    fun newGame(id: Int?): Game {
        return Game(
                id = id,
                name = "",
                wikiEn = null,
                wikiCz = null,
                mediaCount = 0,
                format = Format.STEAM,
                crack = false,
                serialKey = false,
                patch = false,
                trainer = false,
                trainerData = false,
                editor = false,
                saves = false,
                otherData = null,
                note = null,
                position = if (id == null) null else id - 1)
                .updated()
    }

    /**
     * Returns game for index.
     *
     * @param index index
     * @return game for index
     */
    fun getGame(index: Int): com.github.vhromada.catalog.domain.Game {
        return com.github.vhromada.catalog.domain.Game(
                id = index,
                name = "$GAME$index name",
                wikiEn = "$GAME$index English Wikipedia",
                wikiCz = "$GAME$index Czech Wikipedia",
                mediaCount = index,
                format = getFormat(index),
                crack = index != 1,
                serialKey = index != 1,
                patch = index != 1,
                trainer = index != 1,
                trainerData = index == 3,
                editor = index == 3,
                saves = index == 3,
                otherData = if (index == 3) GAME + "3 other data" else "",
                note = if (index == 3) GAME + "3 note" else "",
                position = index - 1,
                audit = AuditUtils.getAudit())
    }

    /**
     * Returns format for index.
     *
     * @param index index
     * @return format for index
     */
    private fun getFormat(index: Int): Format {
        return when (index) {
            1 -> Format.ISO
            2 -> Format.STEAM
            3 -> Format.BINARY
            else -> throw IllegalArgumentException("Bad index")
        }
    }

    /**
     * Returns game.
     *
     * @param entityManager entity manager
     * @param id            game ID
     * @return game
     */
    fun getGame(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Game? {
        return entityManager.find(com.github.vhromada.catalog.domain.Game::class.java, id)
    }

    /**
     * Returns game with updated fields.
     *
     * @param entityManager entity manager
     * @param id            game ID
     * @return game with updated fields
     */
    fun updateGame(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Game {
        return getGame(entityManager, id)!!
                .updated()
                .copy(position = POSITION)
    }

    /**
     * Returns count of games.
     *
     * @param entityManager entity manager
     * @return count of games
     */
    @Suppress("CheckStyle")
    fun getGamesCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(g.id) FROM Game g", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts games deep equals.
     *
     * @param expected expected games
     * @param actual   actual games
     */
    fun assertGamesDeepEquals(expected: List<com.github.vhromada.catalog.domain.Game?>?, actual: List<com.github.vhromada.catalog.domain.Game?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertGameDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts game deep equals.
     *
     * @param expected expected game
     * @param actual   actual game
     */
    fun assertGameDeepEquals(expected: com.github.vhromada.catalog.domain.Game?, actual: com.github.vhromada.catalog.domain.Game?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(expected!!.id).isEqualTo(actual!!.id)
            it.assertThat(expected.name).isEqualTo(actual.name)
            it.assertThat(expected.wikiEn).isEqualTo(actual.wikiEn)
            it.assertThat(expected.wikiCz).isEqualTo(actual.wikiCz)
            it.assertThat(expected.mediaCount).isEqualTo(actual.mediaCount)
            it.assertThat(expected.format).isEqualTo(actual.format)
            it.assertThat(expected.crack).isEqualTo(actual.crack)
            it.assertThat(expected.serialKey).isEqualTo(actual.serialKey)
            it.assertThat(expected.patch).isEqualTo(actual.patch)
            it.assertThat(expected.trainer).isEqualTo(actual.trainer)
            it.assertThat(expected.trainerData).isEqualTo(actual.trainerData)
            it.assertThat(expected.editor).isEqualTo(actual.editor)
            it.assertThat(expected.saves).isEqualTo(actual.saves)
            it.assertThat(expected.otherData).isEqualTo(actual.otherData)
            it.assertThat(expected.note).isEqualTo(actual.note)
            it.assertThat(expected.position).isEqualTo(actual.position)
            AuditUtils.assertAuditDeepEquals(expected.audit, actual.audit)
        }
    }

    /**
     * Asserts games deep equals.
     *
     * @param expected expected list of game
     * @param actual   actual games
     */
    fun assertGameListDeepEquals(expected: List<Game?>?, actual: List<com.github.vhromada.catalog.domain.Game?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertGameDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts game deep equals.
     *
     * @param expected expected game
     * @param actual   actual game
     */
    fun assertGameDeepEquals(expected: Game?, actual: com.github.vhromada.catalog.domain.Game?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.name).isEqualTo(expected.name)
            it.assertThat(actual.wikiEn).isEqualTo(expected.wikiEn)
            it.assertThat(actual.wikiCz).isEqualTo(expected.wikiCz)
            it.assertThat(actual.mediaCount).isEqualTo(expected.mediaCount)
            it.assertThat(actual.format).isEqualTo(expected.format)
            it.assertThat(actual.crack).isEqualTo(expected.crack)
            it.assertThat(actual.serialKey).isEqualTo(expected.serialKey)
            it.assertThat(actual.patch).isEqualTo(expected.patch)
            it.assertThat(actual.trainer).isEqualTo(expected.trainer)
            it.assertThat(actual.trainerData).isEqualTo(expected.trainerData)
            it.assertThat(actual.editor).isEqualTo(expected.editor)
            it.assertThat(actual.saves).isEqualTo(expected.saves)
            it.assertThat(actual.otherData).isEqualTo(expected.otherData)
            it.assertThat(actual.note).isEqualTo(expected.note)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
