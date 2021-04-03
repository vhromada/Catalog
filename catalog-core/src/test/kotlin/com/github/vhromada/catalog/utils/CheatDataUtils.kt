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
    return copy(action = "action", description = "description")
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
     * Returns cheat's data for cheat.
     *
     * @param cheat cheat
     * @return cheat's data for cheat
     */
    fun getCheatDataList(cheat: Int): List<com.github.vhromada.catalog.domain.CheatData> {
        val cheatData = mutableListOf<com.github.vhromada.catalog.domain.CheatData>()
        for (i in 1..CHEAT_DATA_CHEAT_COUNT) {
            cheatData.add(getCheatDataDomain(cheatIndex = cheat, cheatDataIndex = i))
        }

        return cheatData
    }

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
            description = "Description"
        ).updated()
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
            description = "Description"
        ).updated()
    }

    /**
     * Returns cheat's data for indexes.
     *
     * @param cheatIndex     cheat index
     * @param cheatDataIndex cheat's data index
     * @return cheat's data for indexes
     */
    private fun getCheatDataDomain(cheatIndex: Int, cheatDataIndex: Int): com.github.vhromada.catalog.domain.CheatData {
        return com.github.vhromada.catalog.domain.CheatData(
            id = (cheatIndex - 1) * CHEAT_DATA_CHEAT_COUNT + cheatDataIndex,
            action = "Cheat $cheatIndex Data $cheatDataIndex action",
            description = "Cheat $cheatIndex Data $cheatDataIndex description"
        ).fillAudit(audit = AuditUtils.getAudit())
    }

    /**
     * Returns count of cheat's data.
     *
     * @param entityManager entity manager
     * @return count of cheat's data
     */
    @Suppress("JpaQlInspection")
    fun getCheatDataCount(entityManager: EntityManager): Int {
        return entityManager.createQuery("SELECT COUNT(c.id) FROM CheatData c", java.lang.Long::class.java).singleResult.toInt()
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected list of cheat's data
     * @param actual   actual list of cheat's data
     */
    fun assertDomainCheatDataDeepEquals(expected: List<com.github.vhromada.catalog.domain.CheatData>, actual: List<com.github.vhromada.catalog.domain.CheatData>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDataDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected cheat's data
     * @param actual   actual cheat's data
     */
    private fun assertCheatDataDeepEquals(expected: com.github.vhromada.catalog.domain.CheatData, actual: com.github.vhromada.catalog.domain.CheatData) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.action).isEqualTo(expected.action)
            it.assertThat(actual.description).isEqualTo(expected.description)
            AuditUtils.assertAuditDeepEquals(softly = it, expected = expected, actual = actual)
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected list of cheat's data
     * @param actual   actual list of cheat's data
     */
    fun assertCheatDataDeepEquals(expected: List<CheatData>, actual: List<com.github.vhromada.catalog.domain.CheatData>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDataDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected cheat's data
     * @param actual   actual cheat's data
     */
    fun assertCheatDataDeepEquals(expected: CheatData, actual: com.github.vhromada.catalog.domain.CheatData) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.action).isEqualTo(expected.action)
            it.assertThat(actual.description).isEqualTo(expected.description)
            it.assertThat(actual.createdUser).isNull()
            it.assertThat(actual.createdTime).isNull()
            it.assertThat(actual.updatedUser).isNull()
            it.assertThat(actual.updatedTime).isNull()
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected list of cheat's data
     * @param actual   actual list of cheat's data
     */
    fun assertCheatDataListDeepEquals(expected: List<com.github.vhromada.catalog.domain.CheatData>, actual: List<CheatData>) {
        assertThat(expected.size).isEqualTo(actual.size)
        if (expected.isNotEmpty()) {
            for (i in expected.indices) {
                assertCheatDataDeepEquals(expected = expected[i], actual = actual[i])
            }
        }
    }

    /**
     * Asserts cheat's data deep equals.
     *
     * @param expected expected cheat's data
     * @param actual   actual cheat's data
     */
    fun assertCheatDataDeepEquals(expected: com.github.vhromada.catalog.domain.CheatData, actual: CheatData) {
        assertSoftly {
            it.assertThat(actual.id).isEqualTo(expected.id)
            it.assertThat(actual.action).isEqualTo(expected.action)
            it.assertThat(actual.description).isEqualTo(expected.description)
        }
    }

}
