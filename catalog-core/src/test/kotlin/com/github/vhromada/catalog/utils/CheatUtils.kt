package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Cheat
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates cheat fields.
 *
 * @return updated cheat
 */
fun com.github.vhromada.catalog.domain.Cheat.updated(): com.github.vhromada.catalog.domain.Cheat {
    return copy(gameSetting = "gameSetting", cheatSetting = "cheatSetting")
}

/**
 * Updates cheat fields.
 *
 * @return updated cheat
 */
fun Cheat.updated(): Cheat {
    return copy(gameSetting = "gameSetting", cheatSetting = "cheatSetting")
}

/**
 * A class represents utility class for cheat.
 *
 * @author Vladimir Hromada
 */
object CheatUtils {

    /**
     * Count of cheats
     */
    const val CHEATS_COUNT = 2

    /**
     * Returns cheats for game.
     *
     * @param game game
     * @return cheats  for game
     */
    fun getCheats(game: Int): List<com.github.vhromada.catalog.domain.Cheat> {
        return if (game == 1) emptyList() else listOf(getCheatDomain(index = game - 1))
    }

    /**
     * Returns cheat.
     *
     * @param id ID
     * @return cheat
     */
    fun newCheatDomain(id: Int?): com.github.vhromada.catalog.domain.Cheat {
        return com.github.vhromada.catalog.domain.Cheat(
            id = id,
            gameSetting = "",
            cheatSetting = "",
            data = listOf(CheatDataUtils.newCheatDataDomain(id = id))
        ).updated()
    }

    /**
     * Returns cheat with game.
     *
     * @param id ID
     * @return cheat with game
     */
    fun newCheatDomainWithGame(id: Int): com.github.vhromada.catalog.domain.Cheat {
        val cheat = newCheatDomain(id = id)
        cheat.game = GameUtils.newGameDomain(id = id)
        return cheat
    }

    /**
     * Returns cheat with cheat.
     *
     * @param id ID
     * @return cheat with cheat
     */
    fun newCheatDomainWithData(id: Int?): com.github.vhromada.catalog.domain.Cheat {
        return newCheatDomain(id)
            .copy(data = listOf(CheatDataUtils.newCheatDataDomain(id = id)))
    }

    /**
     * Returns cheat.
     *
     * @param id ID
     * @return cheat
     */
    fun newCheat(id: Int?): Cheat {
        return Cheat(
            id = id,
            gameSetting = "",
            cheatSetting = "",
            data = listOf(CheatDataUtils.newCheatData(id = id))
        ).updated()
    }

    /**
     * Returns cheat for index.
     *
     * @param index index
     * @return cheat for index
     */
    fun getCheatDomain(index: Int): com.github.vhromada.catalog.domain.Cheat {
        return com.github.vhromada.catalog.domain.Cheat(
            id = index,
            gameSetting = "Game $index setting",
            cheatSetting = "Cheat $index setting",
            data = CheatDataUtils.getCheatDataList(cheat = index)
        ).fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns cheat.
     *
     * @param entityManager entity manager
     * @param id            cheat ID
     * @return cheat
     */
    fun getCheat(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Cheat? {
        return entityManager.find(com.github.vhromada.catalog.domain.Cheat::class.java, id)
    }

    /**
     * Returns cheat with updated fields.
     *
     * @param entityManager entity manager
     * @param id            cheat ID
     * @return cheat with updated fields
     */
    fun updateCheat(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.Cheat {
        val storedCheat = getCheat(entityManager = entityManager, id = id)!!
        val cheat = storedCheat
            .updated()
            .fillAudit(audit = storedCheat)
        cheat.game = storedCheat.game
        return cheat
    }

    /**
     * Returns count of cheat.
     *
     * @param entityManager entity manager
     * @return count of cheat
     */
    @Suppress("JpaQlInspection")
    fun getCheatsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(c.id) FROM Cheat c", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts cheats deep equals.
     *
     * @param expected expected list of cheats
     * @param actual   actual list of cheats
     */
    fun assertDomainCheatsDeepEquals(expected: List<com.github.vhromada.catalog.domain.Cheat>, actual: List<com.github.vhromada.catalog.domain.Cheat>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts cheat deep equals.
     *
     * @param expected expected cheat
     * @param actual   actual cheat
     */
    fun assertCheatDeepEquals(expected: com.github.vhromada.catalog.domain.Cheat, actual: com.github.vhromada.catalog.domain.Cheat) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.gameSetting).isEqualTo(expected.gameSetting)
            it.assertThat(actual.cheatSetting).isEqualTo(expected.cheatSetting)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
        CheatDataUtils.assertDomainCheatDataDeepEquals(expected = expected.data, actual = actual.data)
    }

    /**
     * Asserts cheats deep equals.
     *
     * @param expected expected list of cheats
     * @param actual   actual list of cheats
     */
    fun assertCheatsDeepEquals(expected: List<Cheat>, actual: List<com.github.vhromada.catalog.domain.Cheat>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts cheat deep equals.
     *
     * @param expected expected cheat
     * @param actual   actual cheat
     */
    fun assertCheatDeepEquals(expected: Cheat, actual: com.github.vhromada.catalog.domain.Cheat) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.gameSetting).isEqualTo(expected.gameSetting)
            it.assertThat(actual.cheatSetting).isEqualTo(expected.cheatSetting)
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
        }
        CheatDataUtils.assertCheatDataDeepEquals(expected = expected.data!!.filterNotNull(), actual = actual.data)
    }

    /**
     * Asserts cheats deep equals.
     *
     * @param expected expected list of cheats
     * @param actual   actual list of cheats
     */
    fun assertCheatListDeepEquals(expected: List<com.github.vhromada.catalog.domain.Cheat>, actual: List<Cheat>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts cheat deep equals.
     *
     * @param expected expected cheat
     * @param actual   actual cheat
     */
    fun assertCheatDeepEquals(expected: com.github.vhromada.catalog.domain.Cheat, actual: Cheat) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.gameSetting).isEqualTo(expected.gameSetting)
            it.assertThat(actual.cheatSetting).isEqualTo(expected.cheatSetting)
            it.assertThat(actual.data)
                .isNotNull
                .doesNotContainNull()
        }
        CheatDataUtils.assertCheatDataListDeepEquals(expected = expected.data, actual = actual.data!!.filterNotNull())
    }

}
