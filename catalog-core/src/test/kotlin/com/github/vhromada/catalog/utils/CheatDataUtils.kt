package com.github.vhromada.catalog.utils

import com.github.vhromada.catalog.entity.CheatData
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import javax.persistence.EntityManager

/**
 * Updates cheat's data fields.
 *
 * @return updated cheat's data
 */
fun com.github.vhromada.catalog.domain.CheatData.updated(): com.github.vhromada.catalog.domain.CheatData {
    return copy(action = "action", description = "description", audit = AuditUtils.newAudit())
}

/**
 * Updates cheat's data fields.
 *
 * @return updated cheat's data
 */
fun CheatData.updated(): CheatData {
    return copy(action = "action", description = "description")
}

/**
 * A class represents utility class for cheat's data.
 *
 * @author Vladimir Hromada
 */
object CheatDataUtils {

    /**
     * Count of cheat's data
     */
    const val CHEAT_DATA_COUNT = 6

    /**
     * Count of cheat's data in cheat
     */
    const val CHEAT_DATA_CHEAT_COUNT = 3

    /**
     * Returns cheat's data.
     *
     * @param id ID
     * @return cheat
     */
    fun newCheatDataDomain(id: Int?): com.github.vhromada.catalog.domain.CheatData {
        return com.github.vhromada.catalog.domain.CheatData(
                id = id,
                action = "Action",
                description = "Description",
                position = if (id == null) null else id - 1,
                audit = null)
                .updated()
    }

    /**
     * Returns cheat's data.
     *
     * @param id ID
     * @return cheat's data
     */
    fun newCheatData(id: Int?): CheatData {
        return CheatData(
                id = id,
                action = "Action",
                description = "Description",
                position = if (id == null) null else id - 1)
                .updated()
    }

    /**
     * Returns cheat's data for cheat.
     *
     * @param cheat cheat
     * @return cheat's data for cheat
     */
    fun getCheatDataList(cheat: Int): List<com.github.vhromada.catalog.domain.CheatData> {
        val cheatData = mutableListOf<com.github.vhromada.catalog.domain.CheatData>()
        for (i in 1..CHEAT_DATA_CHEAT_COUNT) {
            cheatData.add(getCheatData(cheat, i))
        }

        return cheatData
    }

    /**
     * Returns cheat's data for index.
     *
     * @param index index
     * @return cheat's data for index
     */
    fun getCheatData(index: Int): com.github.vhromada.catalog.domain.CheatData {
        val cheatIndex = (index - 1) / CHEAT_DATA_CHEAT_COUNT + 1
        val cheatDataIndex = (index - 1) % CHEAT_DATA_CHEAT_COUNT + 1

        return getCheatData(cheatIndex, cheatDataIndex)
    }

    /**
     * Returns cheat's data for indexes.
     *
     * @param cheatIndex     cheat index
     * @param cheatDataIndex cheat's data index
     * @return cheat's data for indexes
     */
    private fun getCheatData(cheatIndex: Int, cheatDataIndex: Int): com.github.vhromada.catalog.domain.CheatData {
        return com.github.vhromada.catalog.domain.CheatData(
                id = (cheatIndex - 1) * CHEAT_DATA_CHEAT_COUNT + cheatDataIndex,
                action = "Cheat $cheatIndex Data $cheatDataIndex action",
                description = "Cheat $cheatIndex Data $cheatDataIndex description",
                position = cheatDataIndex - 1,
                audit = AuditUtils.getAudit())
    }

    /**
     * Returns cheat's data.
     *
     * @param entityManager entity manager
     * @param id            cheat ID
     * @return cheat's data
     */
    fun getCheatData(entityManager: EntityManager, id: Int): com.github.vhromada.catalog.domain.CheatData? {
        return entityManager.find(com.github.vhromada.catalog.domain.CheatData::class.java, id)
    }

    /**
     * Returns count of cheat's data.
     *
     * @param entityManager entity manager
     * @return count of cheat's data
     */
    fun getCheatDataCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(c.id) FROM CheatData c", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected list of cheat's data
     * @param actual   actual list of cheat's data
     */
    fun assertCheatDataDeepEquals(expected: List<com.github.vhromada.catalog.domain.CheatData?>?, actual: List<com.github.vhromada.catalog.domain.CheatData?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDataDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected cheat's data
     * @param actual   actual cheat's data
     */
    fun assertCheatDataDeepEquals(expected: com.github.vhromada.catalog.domain.CheatData?, actual: com.github.vhromada.catalog.domain.CheatData?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.action).isEqualTo(expected.action)
            it.assertThat(actual.description).isEqualTo(expected.description)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
        AuditUtils.assertAuditDeepEquals(expected!!.audit, actual!!.audit)
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected list of cheat's data
     * @param actual   actual list of cheat's data
     */
    fun assertCheatDataListDeepEquals(expected: List<CheatData?>?, actual: List<com.github.vhromada.catalog.domain.CheatData?>?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertThat(expected!!.size).isEqualTo(actual!!.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDataDeepEquals(expected[i], actual[i])
            }
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected cheat's data
     * @param actual   actual cheat's data
     */
    fun assertCheatDataDeepEquals(expected: CheatData?, actual: com.github.vhromada.catalog.domain.CheatData?) {
        assertSoftly {
            it.assertThat(expected).isNotNull
            it.assertThat(actual).isNotNull
        }
        assertSoftly {
            it.assertThat(actual!!.id).isEqualTo(expected!!.id)
            it.assertThat(actual.action).isEqualTo(expected.action)
            it.assertThat(actual.description).isEqualTo(expected.description)
            it.assertThat(actual.position).isEqualTo(expected.position)
        }
    }

}
