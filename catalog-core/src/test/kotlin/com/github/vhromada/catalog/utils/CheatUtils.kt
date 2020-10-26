package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.Cheat
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates cheat fields.
 *
 * @return updated cheat
 */
fun com.github.vhromada.catalog.domain.Cheat.updated(): com.github.vhromada.catalog.domain.Cheat {
    return copy(gameSetting = "gameSetting", cheatSetting = "cheatSetting", audit = AuditUtils.newAudit())
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
                data = emptyList(),
                position = null,
                audit = null)
                .updated()
    }

    /**
     * Returns cheat with cheat.
     *
     * @param id ID
     * @return cheat with cheat
     */
    fun newCheatWithData(id: Int?): com.github.vhromada.catalog.domain.Cheat {
        return newCheatDomain(id)
                .copy(data = listOf(CheatDataUtils.newCheatDataDomain(id)))
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
                data = emptyList(),
                position = null)
                .updated()
    }

    /**
     * Returns cheats.
     *
     * @return cheats
     */
    fun getCheats(): List<com.github.vhromada.catalog.domain.Cheat> {
        val cheats = mutableListOf<com.github.vhromada.catalog.domain.Cheat>()
        for (i in 0 until CHEATS_COUNT) {
            cheats.add(getCheat(i + 1))
        }

        return cheats
    }

    /**
     * Returns cheat for index.
     *
     * @param index index
     * @return cheat for index
     */
    fun getCheat(index: Int): com.github.vhromada.catalog.domain.Cheat {
        return com.github.vhromada.catalog.domain.Cheat(
                id = index,
                gameSetting = "Game $index setting",
                cheatSetting = "Cheat $index setting",
                data = CheatDataUtils.getCheatDataList(index),
                position = null,
                audit = AuditUtils.getAudit())
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
     * Returns count of cheat.
     *
     * @param entityManager entity manager
     * @return count of cheat
     */
    fun getCheatsCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(c.id) FROM Cheat c", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts cheats deep equals.
     *
     * @param expected expected list of cheats
     * @param actual   actual list of cheats
     */
    fun assertCheatsDeepEquals(expected: List<com.github.vhromada.catalog.domain.Cheat?>?, actual: List<com.github.vhromada.catalog.domain.Cheat?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        Assertions.assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts cheat deep equals.
     *
     * @param expected expected cheat
     * @param actual   actual cheat
     */
    fun assertCheatDeepEquals(expected: com.github.vhromada.catalog.domain.Cheat?, actual: com.github.vhromada.catalog.domain.Cheat?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.gameSetting).isEqualTo(expected.gameSetting)
            it.assertThat(actual.cheatSetting).isEqualTo(expected.cheatSetting)
        }
        CheatDataUtils.assertCheatDataDeepEquals(expected!!.data, actual!!.data)
        AuditUtils.assertAuditDeepEquals(expected.audit, actual.audit)
    }

    /**
     * Asserts cheats deep equals.
     *
     * @param expected expected list of cheats
     * @param actual   actual list of cheats
     */
    fun assertCheatListDeepEquals(expected: List<Cheat?>?, actual: List<com.github.vhromada.catalog.domain.Cheat?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        Assertions.assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts cheat deep equals.
     *
     * @param expected expected cheat
     * @param actual   actual cheat
     */
    fun assertCheatDeepEquals(expected: Cheat?, actual: com.github.vhromada.catalog.domain.Cheat?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.gameSetting).isEqualTo(expected.gameSetting)
            it.assertThat(actual.cheatSetting).isEqualTo(expected.cheatSetting)
        }
        CheatDataUtils.assertCheatDataListDeepEquals(expected!!.data, actual!!.data)
    }

}
